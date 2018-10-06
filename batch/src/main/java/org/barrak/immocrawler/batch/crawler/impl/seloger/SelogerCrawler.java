package org.barrak.immocrawler.batch.crawler.impl.seloger;

import org.barrak.crawler.database.document.ProviderEnum;
import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.IPagedCrawler;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.batch.utils.ParserUtils;
import org.jsoup.Connection;
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
    private Map<String, SearchResultDocument> cache;

    @Value("${provider.seloger.api}")
    private String selogerUrl;
    @Value("${provider.seloger.cityApi}")
    private String cityApiUrl;

    @Override
    public void search(SearchCriteria criteria, Consumer<List<SearchResultDocument>> consumer) {
        try {
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
        String href = article.getElementsByTag("a").first().attr("href");
        String priceStr = article.getElementsByClass("c-pa-cprice").text();
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

        String city = article.getElementsByClass("c-pa-city").text();

        SearchResultDocument searchResult = new SearchResultDocument(href, ProviderEnum.SELOGER, city.toLowerCase(), price);

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
        String json = article.getElementsByAttribute("data-lazy").first().attr("data-lazy");
        JsonParser jsonParser = new BasicJsonParser();
        Map<String, Object> jsonMap = jsonParser.parseMap(json);
        return (String) jsonMap.get("url");
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
                .queryParam("places", "{ci:570041}") // TODO city mapping in array ?
                .queryParam("price", criteria.getMinPrice() + "/" + criteria.getMaxPrice())
                .queryParam("projects", "2") // bind projects ?
                .queryParam("proximity","0,10") // TODO around
                .queryParam("qsversion", "1.0")
                .queryParam("types", "2,4");
        if (pageNumber > 1) {
            builder.queryParam("LISTING-LISTpg", pageNumber);
        }

        String uri = builder.toUriString();

        LOGGER.info("Builded url {}", uri);

        return uri;
    }

//    private String getCityParam(List<String> cities) {
//        List<String> ciList = new ArrayList<>();
//        for (String city : cities) {
//            String cityUrl = cityApiUrl + "text=" + city.toLowerCase();
//
//            LOGGER.info("Calling cityApi : {}", cityUrl);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("origin","https://www.seloger.com");
//            // headers.add("Referer", "https://www.seloger.com/list.htm?types=2%2C4&projects=2&enterprise=0&natures=1%2C2%2C4&proximity=0%2C10&places=%5B%7Bci%3A570041%7D%5D&qsVersion=1.0");
//            HttpEntity<String> entity = new HttpEntity<>("", headers);
//
//            ResponseEntity<String> response = restTemplate.exchange(cityUrl, HttpMethod.GET, entity, String.class);
//
//            JsonParser jsonParser = new BasicJsonParser();
//            List<Object> jsonObjects = jsonParser.parseList(response.getBody());
//
//            LOGGER.info("jsonobjects {}", jsonObjects);
//        }
//
//        // [{ci:570041}<,...>]
//        String cityParam = "[" + ciList.stream().collect(Collectors.joining(",")) + "]";
//
//        LOGGER.info("CityParam", cityParam);
//
//        return cityParam;
//    }

    @Override
    public ProviderEnum getInternalProvider() {
        return ProviderEnum.SELOGER;
    }
}
