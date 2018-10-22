package org.barrak.immocrawler.batch.crawler.impl.seloger;

import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;

// @Component
public class SelogerSeleniumCrawler extends SelogerCrawler {

    @Value("${webdriver.gecko.driver}")
    private String geckodriverPath;

    protected Document getDocumentPage(SearchCriteria criteria, int pageNumber) {
        UriComponentsBuilder builder = getSearchUrlBuilder(criteria);
        String url = buildSearchUrl(builder, pageNumber);

        WebDriver driver = new FirefoxDriver();
        driver.get(url);

        return Jsoup.parse(driver.getPageSource());
    }
}
