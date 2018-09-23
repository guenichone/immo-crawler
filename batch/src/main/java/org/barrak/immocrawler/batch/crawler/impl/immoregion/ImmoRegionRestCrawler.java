package org.barrak.immocrawler.batch.crawler.impl.immoregion;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class ImmoRegionRestCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImmoRegionSeleniumCrawler.class);

    @Value("${provider.website.immoregion}")
    private String immoregionUrl;

    @Autowired
    private WebDriver driver;

    @Autowired
    private RestTemplate restTemplate;

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
}
