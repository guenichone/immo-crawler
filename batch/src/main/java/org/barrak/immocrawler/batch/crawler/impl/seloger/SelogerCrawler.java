package org.barrak.immocrawler.batch.crawler.impl.seloger;

import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.batch.crawler.impl.JsoupPagedCrawler;
import org.barrak.immocrawler.batch.crawler.impl.leboncoin.FakeBrowserConnectionUpdater;
import org.barrak.immocrawler.batch.utils.ParserUtils;
import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.document.RealEstateType;
import org.barrak.immocrawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.database.document.SearchResultDocumentKey;
import org.barrak.immocrawler.geoloc.impl.OpendataSoftService;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Component
public class SelogerCrawler extends JsoupPagedCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelogerCrawler.class);

    @Autowired
    private Map<SearchResultDocumentKey, SearchResultDocument> cache;

    @Autowired
    private OpendataSoftService opendataSoftService;

    @Value("${provider.seloger.api}")
    private String selogerUrl;

    @Override
    protected Connection addConnectionParams(Connection connection) {
        return FakeBrowserConnectionUpdater.addConnectionParams(connection)
                .referrer("www.seloger.com")
                .followRedirects(false);
    }

    @Override
    protected Document getDocument(Connection connection) throws IOException {
        Connection.Response response = connection.execute();
        if (response.statusCode() == HttpStatus.TEMPORARY_REDIRECT.value()) {
            throw new  IllegalStateException("Temporary redirect.");
        }
        return response.parse();
    }

    @Override
    protected int getTotal(Document document) {
        String strTotal = document.getElementsByClass("title_nbresult").first().text();
        return (int) ParserUtils.getNumericOnly(strTotal);
    }

    @Override
    protected Elements getArticles(Document document) {
        return document.getElementsByClass("c-pa-list");
    }

    @Override
    protected SearchResultDocument parseArticle(SearchCriteria criteria, Element article) {
        String priceStr = article.getElementsByClass("c-pa-cprice").text();
        int price = (int) ParserUtils.getNumericOnly(priceStr);
        String href = article.getElementsByTag("a").first().attr("href");
        String endHref = ParserUtils.getLastPart(href, "/");
        String id = endHref.substring(0, endHref.indexOf("."));

        SearchResultDocumentKey cacheKey = new SearchResultDocumentKey(this.getInternalProvider(), id);
        if (cache.containsKey(cacheKey)) {
            SearchResultDocument oldSearchResult = cache.get(cacheKey);
            if (oldSearchResult.getPrice() != price) {
                LOGGER.info("New price for {}, previous {}, new {}", id, oldSearchResult.getPrice(), price);
            } else {
                return null;
            }
        } else {
            LOGGER.info("Add new result id {} : {}", id, href);
        }

        String city = article.getElementsByClass("c-pa-city").text();
        RealEstateType type = article.getElementsByClass("c-pa-info").text().equals("Maison / Villa") ?
                RealEstateType.HOUSE : RealEstateType.LAND;

        SearchResultDocument searchResult = new SearchResultDocument(
                id, href, ProviderEnum.SELOGER, type, city.toLowerCase(), price);

        searchResult.setTitle(getTitle(article));
        searchResult.setNbRooms(getCriterionValue(article, "[0-9]+ p"));
        searchResult.setHomeSurface(getCriterionValue(article, "[0-9]+ m²"));
        searchResult.setImageUrl(getImageUrl(article));

        return searchResult;
    }

    private String getTitle(Element article) {
        return article.getElementsByClass("c-pa-link").first().text();
    }

    private String getImageUrl(Element article) {
        // Lazy load image
        Element dataLazy = article.getElementsByAttribute("data-lazy").first();
        if (dataLazy != null) {
            String json = dataLazy.attr("data-lazy");
            JsonParser jsonParser = new BasicJsonParser();
            Map<String, Object> jsonMap = jsonParser.parseMap(json);
            return (String) jsonMap.get("url");
        } else {
            return null;
        }
    }

    private int getCriterionValue(Element article, String regex) {
        String criterion = article.getElementsByClass("c-pa-criterion").text();
        String valueStr = ParserUtils.matchByRegex(criterion, regex);
        return valueStr != null ? (int) ParserUtils.getNumericOnly(valueStr) : -1;
    }

    @Override
    protected UriComponentsBuilder getSearchUrlBuilder(SearchCriteria criteria) {
        // https://www.seloger.com/list.htm
        // ?types=2,4
        // &projects=2
        // &enterprise=0
        // &natures=1,2,4
        // &price=100000/500000
        // &proximity=0,10
        // &places=[{ci:570041}]
        // &qsVersion=1.0
        // &LISTING-LISTpg=1

        return UriComponentsBuilder.fromHttpUrl(selogerUrl + "/list.htm")
                .queryParam("enterprise", "0")
                .queryParam("natures", "1,2,4") // bind natures ?
                .queryParam("places", getPlaces(criteria))
                .queryParam("price", criteria.getMinPrice() + "/" + criteria.getMaxPrice())
                .queryParam("projects", "2") // bind projects ?
                .queryParam("proximity","0," + criteria.getAround())
                .queryParam("qsversion", "1.0")
                .queryParam("types", "2,4");
    }

    @Override
    protected String buildSearchUrl(UriComponentsBuilder builder, int page) {
        if (page > 1) {
            builder.queryParam("LISTING-LISTpg", page);
        }
        return builder.toUriString();
    }

    private String getPlaces(SearchCriteria criteria) {
        String communalCode = opendataSoftService.getCommunalCodeFromPostalCode(criteria.getPostalCode());
        return String.format("{ci:%s0%s}", communalCode.substring(0, 2), communalCode.substring(2, 5));
    }

    @Override
    public ProviderEnum getInternalProvider() {
        return ProviderEnum.SELOGER;
    }
}
