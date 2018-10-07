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

public class ImmoRegionCrawlerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImmoRegionCrawlerTest.class);

    private ImmoRegionCrawler crawler = new ImmoRegionCrawler();

    @Test
    public void search() {
        Whitebox.setInternalState(crawler, "immoregionUrl", "https://www.immoregion.fr/srp/");
        Whitebox.setInternalState(crawler, "cache", new HashMap<String, SearchResultDocument>());

        SearchCriteria criteria = new SearchCriteria("crusnes");
        criteria.setMinPrice(100000);
        criteria.setMaxPrice(500000);

        crawler.search(criteria, searchResultDocuments -> LOGGER.info(
                "Found {} results : {}", searchResultDocuments.size(), searchResultDocuments));
    }
}