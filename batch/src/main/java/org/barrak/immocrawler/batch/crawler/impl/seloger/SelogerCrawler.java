package org.barrak.immocrawler.batch.crawler.impl.seloger;

import org.barrak.crawler.database.document.ProviderEnum;
import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.IPagedCrawler;
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
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class SelogerCrawler implements IPagedCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelogerCrawler.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${provider.seloger.api}")
    private String selogerUrl;
    @Value("${provider.seloger.cityApi}")
    private String cityApiUrl;

    @Override
    public void search(SearchCriteria criteria, Consumer<List<SearchResultDocument>> consumer) {
        try {
            String url = buildSearchUrl(criteria, 1);

            Document document = Jsoup.connect(url).followRedirects(false).get();

            String strTotal = document.getElementsByClass("pagination-title").first()
                    .getElementsByClass("u-500").text();
            int total = (int) ParserUtils.getNumericOnly(strTotal);

            Elements articles = document.getElementsByClass("c-pa-list");
            int nbByPage = articles.size();

            int numberOfPages = total  / nbByPage;
            if (total % nbByPage != 0) {
                numberOfPages++;
            }

            consumer.accept(parseArticles(criteria, articles));

            LOGGER.info("Found {} results in {} pages of results", total, numberOfPages);

            if (numberOfPages > 1) {
                IntStream.rangeClosed(2, numberOfPages).parallel().forEach(page -> {
                    try {
                        consumer.accept(parseResultPage(criteria, page));
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                });
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
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
        String href = article.getElementsByTag("a").first().attr("href");
        String priceStr = article.getElementsByClass("c-pa-cprice").text();
        int price = (int) ParserUtils.getNumericOnly(priceStr);

        SearchResultDocument searchResult = new SearchResultDocument(href, ProviderEnum.SELOGER, criteria.getCity(), price);

        return searchResult;
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
                .queryParam("places", "{ci:570041}") // TODO city mapping in array ?
                .queryParam("price", criteria.getMinPrice() + "/" + criteria.getMaxPrice())
                .queryParam("projects", "2") // bind projects ?
                .queryParam("proximity","0,10") // TODO around
                .queryParam("qsversion", "1.0")
                .queryParam("types", "2,4")
                .queryParam("LISTING-LISTpg", pageNumber);

        return builder.toUriString();
    }

    private String getCityParam(List<String> cities) {
        List<String> ciList = new ArrayList<>();
        for (String city : cities) {
            String cityUrl = cityApiUrl + "text=" + city.toLowerCase();

            LOGGER.info("Calling cityApi : {}", cityUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.add("origin","https://www.seloger.com");
            // headers.add("Referer", "https://www.seloger.com/list.htm?types=2%2C4&projects=2&enterprise=0&natures=1%2C2%2C4&proximity=0%2C10&places=%5B%7Bci%3A570041%7D%5D&qsVersion=1.0");
            HttpEntity<String> entity = new HttpEntity<>("", headers);

            ResponseEntity<String> response = restTemplate.exchange(cityUrl, HttpMethod.GET, entity, String.class);

            JsonParser jsonParser = new BasicJsonParser();
            List<Object> jsonObjects = jsonParser.parseList(response.getBody());

            LOGGER.info("jsonobjects {}", jsonObjects);
        }

        // [{ci:570041}<,...>]
        String cityParam = "[" + ciList.stream().collect(Collectors.joining(",")) + "]";

        LOGGER.info("CityParam", cityParam);

        return cityParam;
    }
}
