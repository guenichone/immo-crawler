package org.barrak.immocrawler.batch.crawler.impl.panetta;

import org.barrak.immocrawler.database.document.RealEstateType;
import org.barrak.immocrawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.IPagedCrawler;
import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.batch.utils.ParserUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class PanettaCrawler implements IPagedCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PanettaCrawler.class);

    @Value("${provider.website.panetta}")
    private String panettaImmoUrl;

    @Autowired
    private Map<String, SearchResultDocument> cache;

    @Override
    public void search(SearchCriteria criteria, Consumer<List<SearchResultDocument>> consumer) {
        try {
            String url = buildSearchUrl(criteria, 1);

            Document document = Jsoup.connect(url).followRedirects(false).get();

            String announces = document.getElementsByClass("resume").first().text();
            announces = announces.substring(announces.indexOf("sur "));
            int total = (int) ParserUtils.getNumericOnly(announces);

            Elements articles = document.getElementsByTag("article");
            int nbByPage = articles.size();

            int numberOfPages = total  / nbByPage;
            if (total % nbByPage != 0) {
                numberOfPages++;
            }

            consumer.accept(parseArticles(criteria, articles));

            LOGGER.info("{} : Found {} results in {} pages of results", ProviderEnum.PANETTA_IMMO, total, numberOfPages);

            if (numberOfPages > 1) {
                IntStream.rangeClosed(2, numberOfPages).parallel().forEach(page -> {
                    try {
                        consumer.accept(parseResultPage(criteria, page));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private List<SearchResultDocument> parseResultPage(SearchCriteria criteria, int pageNumber) throws IOException {
        String url = buildSearchUrl(criteria, pageNumber);

        Document document = Jsoup.connect(url).followRedirects(false).get();

        return parseArticles(criteria, document.getElementsByTag("article"));
    }

    private List<SearchResultDocument> parseArticles(SearchCriteria criteria, Elements articles) {
        return articles.stream()
                .map(article -> parseArticle(criteria, article))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private SearchResultDocument parseArticle(SearchCriteria criteria, Element article) {
        Element link = article.getElementsByTag("a").first();
        String title = link.text();
        String href = panettaImmoUrl + link.attr("href");
        String priceStr = getValueByKey(article, "price");
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

        String city = link.getElementsByClass("city").first().text();
        RealEstateType type = link.getElementsByClass("design-name").first().text().equals("Maison individuelle") ?
                RealEstateType.HOUSE : RealEstateType.LAND;

        SearchResultDocument searchResult = new SearchResultDocument(href, ProviderEnum.PANETTA_IMMO, type, city, price);
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


    private String getImgUrl(Element article) {
        return article.getElementsByTag("img").first().attr("src");
    }

    private String buildSearchUrl(SearchCriteria criteria, int i) {
        // http://www.panetta-immobilier.fr/offer/search/transaction/by/property_type/h/currentPage/1
        return panettaImmoUrl + "/offer/search/transaction/by/property_type/h/currentPage/" + i;
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
