package org.barrak.immocrawler.batch.crawler.impl.immoregion;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.IPagedCrawler;
import org.barrak.immocrawler.batch.crawler.ProviderEnum;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.batch.utils.DriverUtils;
import org.barrak.immocrawler.batch.utils.URLUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ImmoRegionSeleniumCrawler implements IPagedCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImmoRegionSeleniumCrawler.class);

    @Value("${provider.website.immoregion}")
    private String immoregionUrl;

    @Value("${webdriver.gecko.driver}")
    private String geckodriverPath;

    private WebDriver driver;

    @Autowired
    private Map<String, SearchResultDocument> cache;

    @Override
    public List<SearchResultDocument> search(SearchCriteria criteria) {
        return search(criteria, null);
    }

    @Override
    public List<SearchResultDocument> search(SearchCriteria criteria, Consumer<List<SearchResultDocument>> consumer) {
        List<SearchResultDocument> results = new ArrayList<>();

        open();

        fillCriteria(criteria);

        search();

        do {
            List<SearchResultDocument> resultsPage = parseResultsPage(criteria);
            if (consumer != null) {
                consumer.accept(resultsPage);
            }
            results.addAll(resultsPage);
        } while(loadNextResultsPage(criteria));

        close();

        LOGGER.info("results {}", results);

        return results;
    }

    private boolean loadNextResultsPage(SearchCriteria criteria) {

        try {
            LOGGER.info("From : {}", driver.getCurrentUrl());

            Function<NameValuePair, NameValuePair> pageProcess = kvp -> {
                int page = Integer.parseInt(kvp.getValue());
                page++;
                return new BasicNameValuePair(kvp.getName(), "" + page);
            };

            List<NameValuePair> params = URLEncodedUtils.parse(new URI(driver.getCurrentUrl()), Charset.forName("UTF-8"));
            URLUtils.updateUrlParam(params, "page", pageProcess, "2");
//            URLUtils.updateUrlParam(params, "price_min", "" + criteria.getMinPrice());
//            URLUtils.updateUrlParam(params, "price_max", "" + criteria.getMaxPrice());

            String nextUrl ="https://www.immoregion.fr/srp/?" + URLEncodedUtils.format(params, Charset.forName("UTF-8"));
            LOGGER.info("To : {}", nextUrl);
            driver.get(nextUrl);

            try {
                driver.findElement(By.xpath(".//p[@class='no_results']"));
                return false;
            } catch (WebDriverException ex) {
                // More results continue ...
            }

            // There is a bug for this, we are losing the url parameters after redirection
//            String nextPage = DriverUtils.waitUntil(driver, By.linkText("Suivant"), 10).getAttribute("href");
//            driver.get(nextPage);
        } catch (NoSuchElementException ex) {
            LOGGER.info("End");
            return false;
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Incorrect URL");
        }
        LOGGER.info("Next ...");
        return true;
    }

    private void open() {
        System.setProperty("webdriver.gecko.driver", geckodriverPath);

        driver = new FirefoxDriver();
        driver.get(immoregionUrl);
    }

    private void close() {
        driver.close();
    }

    private void fillCriteria(SearchCriteria criteria) {
        WebElement qs = driver.findElement(By.name("qs"));
        qs.click();
        qs.clear();

        DriverUtils.sendHumanKeys(qs, criteria.getCity());

        driver.findElement(By.linkText("Crusnes (54680)")).click();

        DriverUtils.findByNormalizedText(driver, "15").click();
        DriverUtils.findByNormalizedText(driver, "OK").click();

        driver.findElement(By.linkText("Tout type de bien")).click();
        driver.findElement(By.linkText("Maison")).click();
        driver.findElement(By.linkText("Terrain")).click();

        driver.findElement(By.linkText("Budget")).click();

        driver.findElement(By.linkText(amountToEuro(criteria.getMinPrice()))).click();
        driver.findElement(By.linkText(amountToEuro(criteria.getMaxPrice()))).click();
    }

    private void search() {
        driver.findElement(By.xpath("//input[@value='Chercher']")).click();
    }

    private String amountToEuro(int amount) {
        return String.format("%,d €", amount).replace(String.valueOf((char) 160)," ");
    }

    private List<SearchResultDocument> parseResultsPage(SearchCriteria criteria) {

        DriverUtils.scrollDown(driver); // Because of lazy load in page
        DriverUtils.sleep(3000);

        LOGGER.info("Parsing page {}", driver.getCurrentUrl());

//        String nbAnnonces = DriverUtils.waitUntil(driver, By.xpath("//span[contains(., 'annonces')]"), 20).getText();
//        LOGGER.info(nbAnnonces);

        List<WebElement> articles = driver.findElements(By.xpath("//article"));

        return articles.stream()
                .map(article -> parseArticle(criteria, article))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private SearchResultDocument parseArticle(SearchCriteria criteria, WebElement article) {
        WebElement pageLink = article.findElement(By.xpath(".//a"));
        String href = pageLink.getAttribute("href");
        String title = pageLink.getText();

        LOGGER.info("{};{};", title, href);

        int price = getPrice(article, href);

        if (cache.containsKey(href)) {
            SearchResultDocument oldSearchResult = cache.get(href);
            if (oldSearchResult.getPrice() != price) {
                LOGGER.info("New price for {}, previous {}, new {}", href, oldSearchResult.getPrice(), price);
            } else {
                LOGGER.info("Already in cache, registered the {}", oldSearchResult.getCreated());
                return null;
            }
        }

        SearchResultDocument searchResult = new SearchResultDocument(href, ProviderEnum.IMMOREGION.toString(), criteria.getCity(), price);
        searchResult.setTitle(title);
        searchResult.setImageUrl(getImgUrl(article, href));

        return searchResult;
    }

    private int getPrice(WebElement article, String url) {
        WebElement priceLink = article.findElement(By.xpath(".//ul[@class='mainInfos']//a"));
        String priceAndCurrency = priceLink.getText();
        String strPrice = priceAndCurrency.replaceAll("[^\\d]", "");
        try {
            return Integer.parseInt(strPrice);
        } catch (NumberFormatException ex) {
            LOGGER.error("Error when parsing price for {} with value {}", url, strPrice);
            return -1;
        }
    }

    private String getImgUrl(WebElement article, String url) {
        try {
            WebElement img = article.findElement(By.xpath(".//img"));
            return img.getAttribute("src");
        } catch (WebDriverException ex) {
            // Fallback on no script node
            try {
                return article.findElement(By.xpath(".//noscript")).getText().replaceAll("\"", "");
            } catch (WebDriverException ex2) {
                LOGGER.error("Error when retrieving image for {}", url);
                return null;
            }
        }
    }
}
