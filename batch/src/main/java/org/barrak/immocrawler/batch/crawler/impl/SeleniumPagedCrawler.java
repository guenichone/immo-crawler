package org.barrak.immocrawler.batch.crawler.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

public abstract class SeleniumPagedCrawler extends JsoupPagedCrawler {

    @Value("${webdriver.gecko.driver}")
    private String geckodriverPath;

    @Override
    protected Document getDocumentPage(UriComponentsBuilder builder, int pageNumber) throws IOException {
        String url = buildSearchUrl(builder, pageNumber);

        WebDriver driver = new FirefoxDriver();
        driver.get(url);

        return Jsoup.parse(driver.getPageSource());
    }
}
