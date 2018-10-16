package org.barrak.immocrawler.batch.crawler.impl.seloger;

import org.barrak.immocrawler.batch.crawler.IPagedCrawler;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.batch.utils.ParserUtils;
import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.document.RealEstateType;
import org.barrak.immocrawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.database.document.SearchResultDocumentKey;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class SelogerCrawler implements IPagedCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelogerCrawler.class);

    @Autowired
    private Map<SearchResultDocumentKey, SearchResultDocument> cache;

    @Value("${provider.seloger.api}")
    private String selogerUrl;

    @Override
    public void search(SearchCriteria criteria, Consumer<List<SearchResultDocument>> consumer) {
        try {
            if (criteria.getPostalCode() == null) {
                throw new IllegalArgumentException("Missing 'postalCode' in criteria");
            }

            String url = buildSearchUrl(criteria, 1);

            Document document = Jsoup.connect(url)
                    .header("Host", "www.seloger.com")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
                    .get();

            String strTotal = document.getElementsByClass("title_nbresult").first().text();
            int total = (int) ParserUtils.getNumericOnly(strTotal);

            Elements articles = document.getElementsByClass("c-pa-list");
            int nbByPage = articles.size();

            int numberOfPages = total  / nbByPage;
            if (total % nbByPage != 0) {
                numberOfPages++;
            }

            consumer.accept(parseArticles(criteria, articles));

            LOGGER.info("{} : Found {} results in {} pages of results", ProviderEnum.SELOGER, total, numberOfPages);

            if (numberOfPages > 1) {
                IntStream.rangeClosed(2, numberOfPages).parallel().forEach(page -> {
                    try {
                        consumer.accept(parseResultPage(criteria, page));
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                });
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private List<SearchResultDocument> parseResultPage(SearchCriteria criteria, int pageNumber) throws IOException {
        String url = buildSearchUrl(criteria, pageNumber);

        Document document = Jsoup.connect(url).followRedirects(false).get();

        return parseArticles(criteria, document.getElementsByClass("c-pa-list"));
    }

    public List<SearchResultDocument> parseArticles(SearchCriteria criteria, Elements articles) {
        return articles.stream()
                .map(article -> parseArticle(criteria, article))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private SearchResultDocument parseArticle(SearchCriteria criteria, Element article) {
        String id = article.getElementsByAttribute("data-publication-id").first()
                .attr("data-publication-id");
        String priceStr = article.getElementsByClass("c-pa-cprice").text();
        int price = (int) ParserUtils.getNumericOnly(priceStr);
        String href = article.getElementsByTag("a").first().attr("href");

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

    private String buildSearchUrl(SearchCriteria criteria, int pageNumber) {
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

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(selogerUrl + "/list.htm")
                .queryParam("enterprise", "0")
                .queryParam("natures", "1,2,4") // bind natures ?
                .queryParam("places", getPlaces(criteria))
                .queryParam("price", criteria.getMinPrice() + "/" + criteria.getMaxPrice())
                .queryParam("projects", "2") // bind projects ?
                .queryParam("proximity","0," + criteria.getAround())
                .queryParam("qsversion", "1.0")
                .queryParam("types", "2,4");
        if (pageNumber > 1) {
            builder.queryParam("LISTING-LISTpg", pageNumber);
        }

        return builder.toUriString();
    }

    private String getPlaces(SearchCriteria criteria) {
        return String.format("{ci:%s0%s}",
                criteria.getPostalCode().substring(0, 2),
                criteria.getPostalCode().substring(2, 5));
    }

    @Override
    public ProviderEnum getInternalProvider() {
        return ProviderEnum.SELOGER;
    }
}
