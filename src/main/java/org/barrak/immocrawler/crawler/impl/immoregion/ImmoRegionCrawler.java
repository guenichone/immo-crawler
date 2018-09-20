package org.barrak.immocrawler.crawler.impl.immoregion;

import org.barrak.immocrawler.crawler.ICrawler;
import org.barrak.immocrawler.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.crawler.results.SearchResult;
import org.barrak.immocrawler.utils.DriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ImmoRegionCrawler implements ICrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImmoRegionCrawler.class);

    @Value("${provider.website.immoregion}")
    private String immoregionUrl;

    @Autowired
    private WebDriver driver;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<SearchResult> search(SearchCriteria criteria) {

        List<SearchResult> results = new ArrayList<>();

        openWebsite();
//        openWebsiteWithCriterias();

        fillCriteria();

        parseResultsPage();

        // driver.close();

        return results;
    }

    private void openWebsiteWithCriterias() {
        driver.get("https://www.immoregion.fr/srp/?distance=49.434418,5.921767,15&tr=buy&price_min=100000&price_max=500000&q=13bfe2b9&loc=L9-crusnes&ptypes=house,ground&page=30");

        // https://www.immoregion.fr/srp/
        // ?distance=49.434418,5.921767,15
        // &tr=buy
        // &price_min=100000
        // &price_max=500000
        // &q=13bfe2b9
        // &loc=L9-crusnes
        // &ptypes=house,ground
        // &page=30
    }

    private void openResultPage(String resultLink) {
        // https://www.immoregion.fr/vente/maison/crusnes/id-5995352.html

        // String result = restTemplate.getForObject(resultLink, String.class);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(resultLink);

        } catch (ParserConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (SAXException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void openWebsite() {
        driver.get(immoregionUrl);
    }

    private void fillCriteria() {
        WebElement qs = driver.findElement(By.name("qs"));
        qs.click();
        qs.clear();

        DriverUtils.sendHumanKeys(qs, "crusnes");

        driver.findElement(By.linkText("Crusnes (54680)")).click();

        driver.findElement(By.xpath(".//*[normalize-space(text()) and normalize-space(.)='15']")).click();
        driver.findElement(By.xpath(".//*[normalize-space(text()) and normalize-space(.)='OK']")).click();

        driver.findElement(By.linkText("Tout type de bien")).click();
        driver.findElement(By.linkText("Maison")).click();
        driver.findElement(By.linkText("Terrain")).click();

        driver.findElement(By.linkText("Budget")).click();
        // driver.findElement(By.xpath(".//*[normalize-space(text()) and normalize-space(.)='Min.']")).click();
        driver.findElement(By.linkText("100 000 €")).click();
        // driver.findElement(By.xpath(".//*[normalize-space(text()) and normalize-space(.)='Max.']")).click();
        driver.findElement(By.linkText("500 000 €")).click();

        driver.findElement(By.xpath("//form")).submit();
    }

    private void parseResultsPage() {

        DriverUtils.sleep(2000);
        DriverUtils.scrollDown(driver); // Because of lazy load in page

        String nbAnnonces = DriverUtils.waitUntil(driver, By.xpath("//span[contains(., 'annonces')]"), 20).getText();
//        LOGGER.info(nbAnnonces);

        List<WebElement> articles = driver.findElements(By.xpath("//article"));
        parseResultList(articles);

        String nextPage = DriverUtils.waitUntil(driver, By.linkText("Suivant"), 10).getAttribute("href");
        driver.get(nextPage);

        parseResultsPage();
    }

    private int parseNbPages(String nbAnnonces) {
        return 0; // TODO
    }

    private void parseResultList(List<WebElement> articleList) {
        LOGGER.info("Processing {} article(s)", articleList.size());
        for (WebElement article : articleList) {
            parseArticle(article);
        }
    }

    private void parseArticle(WebElement article) {
        WebElement pageLink = article.findElement(By.xpath(".//a"));
        LOGGER.info("{};{};", pageLink.getText(), pageLink.getAttribute("href"));
    }
}
