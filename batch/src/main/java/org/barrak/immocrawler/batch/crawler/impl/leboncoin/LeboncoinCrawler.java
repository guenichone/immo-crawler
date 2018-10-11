package org.barrak.immocrawler.batch.crawler.impl.leboncoin;

import org.barrak.immocrawler.batch.utils.ParserUtils;
import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.document.RealEstateType;
import org.barrak.immocrawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.batch.crawler.impl.JsoupPagedCrawler;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class LeboncoinCrawler extends JsoupPagedCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeboncoinCrawler.class);

    @Value("${provider.website.leboncoin}")
    private String leboncoinUrl;

    @Autowired
    private Map<String, SearchResultDocument> cache;

    @Override
    protected Connection addConnectionParams(Connection connection) {
        return LeboncoinJsoupConnectionUpdater.addConnectionParams(connection);
    }

    @Override
    protected int getTotal(Document document) {
        String total = document.getElementsByAttributeValue("data-qa-id", "result_part").first()
                .getElementsByTag("span").text();
        return (int) ParserUtils.getNumericOnly(total);
    }

    @Override
    protected Elements getArticles(Document document) {
        return document.getElementsByAttributeValue("itemtype", "http://schema.org/Offer");
    }

    @Override
    protected SearchResultDocument parseArticle(SearchCriteria criteria, Element article) {
        String href = leboncoinUrl + article.getElementsByTag("a").first().attr("href");
        String priceStr = article.getElementsByAttributeValue("itemprop", "price").text();
        int price = (int) ParserUtils.getNumericOnly(priceStr);

        if (cache.containsKey(href)) {
            SearchResultDocument oldSearchResult = cache.get(href);
            if (oldSearchResult.getPrice() != price) {
                LOGGER.info("New price for {}, previous {}, new {}", href, oldSearchResult.getPrice(), price);
            } else {
                return null;
            }
        } else {
            LOGGER.info("Add new result {}", href);
        }

        String city = article.getElementsByAttributeValue("data-qa-id", "aditem_location").text();

        SearchResultDocument result = new SearchResultDocument(href, getInternalProvider(), RealEstateType.UNKNOW, city, price);

        String title = article.getElementsByAttributeValue("data-qa-id", "aditem_title").text();
        result.setTitle(title);
        String imageUrl = article.getElementsByTag("img").attr("src");
        result.setImageUrl(imageUrl);

        return result;
    }

    @Override
    protected String buildSearchUrl(SearchCriteria criteria, int page) {
        // https://www.leboncoin.fr/recherche/
        // ?category=9
        // &lat=49.434418
        // &lng=5.921767,15
        // &radius=20000
        // &owner_type=private
        // &price=min-400000
        // &real_estate_type=1,3

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(leboncoinUrl + "/recherche/")
                .queryParam("lat", "49.434418")
                .queryParam("lng", "5.921767")
                .queryParam("owner_type", "private")
                .queryParam("price", getPriceCriteria(criteria))
                .queryParam("real_estate_type", "1,3")
                .queryParam("category", "9")
                .queryParam("page", page);

        if (criteria.getAround() > 0) {
            builder.queryParam("radius", criteria.getAround() * 1000);
        }

        return builder.toUriString();
    }

    private String getPriceCriteria(SearchCriteria criteria) {
        return "" + (criteria.getMinPrice() > 0 ? criteria.getMinPrice() : "min")
                + "-"
                + (criteria.getMaxPrice() > 0 ? criteria.getMaxPrice() : "max");
    }

    @Override
    public ProviderEnum getInternalProvider() {
        return ProviderEnum.LEBONCOIN_PARTICULIER;
    }
}
