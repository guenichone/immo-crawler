package org.barrak.immocrawler.batch.crawler.impl.panetta;

import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.batch.crawler.impl.JsoupPagedCrawler;
import org.barrak.immocrawler.batch.utils.ParserUtils;
import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.document.RealEstateTypeEnum;
import org.barrak.immocrawler.database.model.ArticleDocument;
import org.barrak.immocrawler.database.model.ArticleDocumentKey;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PanettaCrawler extends JsoupPagedCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PanettaCrawler.class);

    @Value("${provider.website.panetta}")
    private String panettaImmoUrl;

    @Autowired
    private Map<ArticleDocumentKey, ArticleDocument> cache;

    @Override
    protected int getTotal(Document document) {
        String announces = document.getElementsByClass("resume").first().text();
        announces = announces.substring(announces.indexOf("sur "));
        return (int) ParserUtils.getNumericOnly(announces);
    }

    @Override
    protected Elements getArticles(Document document) {
        return document.getElementsByTag("article");
    }

    @Override
    protected List<ArticleDocument> parseArticles(SearchCriteria criteria, Elements articles) {
        return articles.stream()
                .map(article -> parseArticle(criteria, article))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    protected ArticleDocument parseArticle(SearchCriteria criteria, Element article) {
        Element link = article.getElementsByTag("a").first();
        String id = getId(link);
        int price = getPrice(article);

        String href = panettaImmoUrl + link.attr("href");

        ArticleDocumentKey cacheKey = new ArticleDocumentKey(this.getInternalProvider(), id);
        if (cache.containsKey(cacheKey)) {
            ArticleDocument oldSearchResult = cache.get(cacheKey);
            if (oldSearchResult.getPrice() != price) {
                LOGGER.info("New price for {}, previous {}, new {}", id, oldSearchResult.getPrice(), price);
            } else {
                return null;
            }
        } else {
            LOGGER.info("Add new result id {} : {}", id, href);
        }

        String city = link.getElementsByClass("city").first().text();
        String title = link.text();
        RealEstateTypeEnum type = link.getElementsByClass("design-name").first().text().equals("Maison individuelle") ?
                RealEstateTypeEnum.HOUSE : RealEstateTypeEnum.LAND;

        ArticleDocument searchResult = new ArticleDocument(id, href, ProviderEnum.PANETTA_IMMO, type, city, price);
        searchResult.setTitle(title);
        searchResult.setImageUrl(getImgUrl(article));

        String surface = getValueByKey(article, "surface");
        searchResult.setHomeSurface((int) ParserUtils.getNumericOnly(surface));

        String landSurface = getValueByKey(article, "ground");
        if (landSurface != null) {
            landSurface = landSurface.substring(0, landSurface.indexOf("."));
            searchResult.setLandSurface((int) ParserUtils.getNumericOnly(landSurface));
        }
        searchResult.setInternalReference(getValueByKey(article, "reference"));

        String nbRooms = getValueByKey(article, "room");
        searchResult.setNbRooms((int) ParserUtils.getNumericOnly(nbRooms));

        return searchResult;
    }

    private String getId(Element link) {
        String lastUrlPart = ParserUtils.getLastPart(link.attr("href"), "/");
        return lastUrlPart.replaceAll("[^\\d]", "");
    }

    private int getPrice(Element article) {
        String priceStr = getValueByKey(article, "price");
        return (int) ParserUtils.getNumericOnly(priceStr);
    }

    private String getImgUrl(Element article) {
        return article.getElementsByTag("img").first().attr("src");
    }

    @Override
    protected UriComponentsBuilder getSearchUrlBuilder(SearchCriteria criteria) {
        return null;
    }

    @Override
    protected String buildSearchUrl(UriComponentsBuilder builder, int page) {
        // http://www.panetta-immobilier.fr/offer/search/transaction/by/property_type/h/currentPage/1
        // There is no way to check around a location by default, a solution would be to check all the available cities if they match ...

        return panettaImmoUrl + "/offer/search/transaction/by/property_type/h/currentPage/" + page;
    }

    private String getValueByKey(Element article, String key) {
        try {
            return article.getElementsByClass("__" + key).first()
                    .getElementsByClass("value").first()
                    .text();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public ProviderEnum getInternalProvider() {
        return ProviderEnum.PANETTA_IMMO;
    }
}
