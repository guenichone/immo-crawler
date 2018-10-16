package org.barrak.immocrawler.batch.crawler.impl.immoregion;

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
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

 @Component
public class ImmoRegionCrawler implements IPagedCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImmoRegionCrawler.class);

    @Value("${provider.website.immoregion}")
    private String immoregionUrl;

    @Autowired
    private Map<SearchResultDocumentKey, SearchResultDocument> cache;

    @Override
    public void search(SearchCriteria criteria, Consumer<List<SearchResultDocument>> consumer) {
        try {
            String url = buildSearchUrl(criteria, 1);

            Document document = Jsoup.connect(url).followRedirects(false).get();

            Element header = document.getElementsByClass("intro").first();
            String resultText = header.getElementsByTag("h2").first().text();
            int total = (int) ParserUtils.getNumericOnly(resultText);

            Elements articles = document.getElementsByTag("article");
            int nbByPage = articles.size();

            int numberOfPages = total  / nbByPage;
            if (total % nbByPage != 0) {
                numberOfPages++;
            }

            consumer.accept(parseArticles(criteria, articles));

            LOGGER.info("{} : Found {} results in {} pages of results", ProviderEnum.IMMOREGION, total, numberOfPages);

            if (numberOfPages > 1) {
                IntStream.rangeClosed(2, numberOfPages).parallel().forEach(page -> {
                    try {
                        consumer.accept(parseResultPage(criteria, page));
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                });
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<SearchResultDocument> parseResultPage(SearchCriteria criteria, int pageNumber) throws IOException {
        String url = buildSearchUrl(criteria, pageNumber);

        Document document = Jsoup.connect(url).followRedirects(false).get();

        return parseArticles(criteria, document.getElementsByTag("article"));
    }

    public List<SearchResultDocument> parseArticles(SearchCriteria criteria, Elements articles) {
        return articles.stream()
                .map(article -> parseArticle(criteria, article))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public SearchResultDocument parseArticle(SearchCriteria criteria, Element article) {

        Element a = article.getElementsByClass("mainInfos").first().getElementsByTag("a").first();
        String href = a.attr("href");
        String url = immoregionUrl + href;
        String id = ParserUtils.getLastPart(href, "/")
                .replaceAll("id-", "")
                .replaceAll(".html", "");

        int price = (int) ParserUtils.getNumericOnly(a.text());

        SearchResultDocumentKey cacheKey = new SearchResultDocumentKey(this.getInternalProvider(), id);
        if (cache.containsKey(cacheKey)) {
            SearchResultDocument oldSearchResult = cache.get(cacheKey);
            if (oldSearchResult.getPrice() != price) {
                LOGGER.info("New price for {}, previous {}, new {}", id, oldSearchResult.getPrice(), price);
            } else {
                return null;
            }
        } else {
            LOGGER.info("Add new result id {} : {}", id, url);
        }

        String title = getTitle(article);
        String city = getCity(article);
        RealEstateType type = title.startsWith("Maison individuelle") ? RealEstateType.HOUSE : RealEstateType.LAND;

        SearchResultDocument searchResult = new SearchResultDocument(id, url, ProviderEnum.IMMOREGION, type, city, price);
        searchResult.setTitle(title);
        searchResult.setImageUrl(getImgUrl(article, href));
        searchResult.setHomeSurface(getCharacteristic(article, "icon-surface"));
        searchResult.setNbRooms(getCharacteristic(article, "icon-room"));

        return searchResult;
    }

     private int getCharacteristic(Element article, String clazz) {
         Element characteristic = article.getElementsByClass(clazz).first();
         if (characteristic != null) {
             return (int) ParserUtils.getNumericOnly(characteristic.text());
         } else {
             return -1;
         }
     }

     private String getTitle(Element article) {
        return article.getElementsByTag("a").first().attr("title");
    }

    private String getCity(Element article) {
        return article.getElementsByAttributeValue("itemprop", "addressLocality").first().text().trim().toLowerCase();
    }

    private String getImgUrl(Element article, String url) {
        try {
            Element img = article.getElementsByTag("img").first();
            return img.attr("src");
        } catch (Exception ex) {
            // Fallback on no script node
            try {
                return article.getElementsByTag("noscript").first().text().replaceAll("\"", "");
            } catch (Exception ex2) {
                LOGGER.error("Error when retrieving image for {}", url);
                return null;
            }
        }
    }

    private String buildSearchUrl(SearchCriteria criteria, int pageNumber) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(immoregionUrl + "/srp/")
                .queryParam("distance", getDistance(criteria))
                .queryParam("tr", "buy");

        if (criteria.getMinPrice() > 0) {
            builder.queryParam("price_min", criteria.getMinPrice());
        }

        if (criteria.getMaxPrice() > 0) {
            builder.queryParam("price_max", criteria.getMaxPrice());
        }

        builder.queryParam("ptypes", "ground,house")
                .queryParam("page", pageNumber);

        return builder.toUriString();
    }

    private String getDistance(SearchCriteria criteria) {
        if (criteria.getLat() == null) {
            throw new IllegalArgumentException("Missing 'lat' in criteria");
        }
        if (criteria.getLng() == null) {
            throw new IllegalArgumentException("Missing 'lng' in criteria");
        }

        return String.format(Locale.ROOT,"%f,%f,%d", criteria.getLat(), criteria.getLng(), criteria.getAround());
    }

     @Override
     public ProviderEnum getInternalProvider() {
         return ProviderEnum.IMMOREGION;
     }
}
