package org.barrak.immocrawler.batch.crawler.impl.immoregion;

import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;

public class ImmoRegionRestCrawlerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImmoRegionRestCrawlerTest.class);

    private ImmoRegionRestCrawler restCrawler = new ImmoRegionRestCrawler();

    @Test
    public void search() throws IOException {
        RestTemplate restTemplate = new RestTemplateBuilder().build();

        Whitebox.setInternalState(restCrawler, "restTemplate", restTemplate);
        Whitebox.setInternalState(restCrawler, "immoregionUrl", "https://www.immoregion.fr/srp/");
        Whitebox.setInternalState(restCrawler, "cache", new HashMap<String, SearchResultDocument>());

        SearchCriteria criteria = new SearchCriteria("crusnes");
        criteria.setMinPrice(100000);
        criteria.setMaxPrice(500000);

        restCrawler.search(criteria, searchResultDocuments -> LOGGER.info(
                "Found {} results : {}", searchResultDocuments.size(), searchResultDocuments));
    }
}