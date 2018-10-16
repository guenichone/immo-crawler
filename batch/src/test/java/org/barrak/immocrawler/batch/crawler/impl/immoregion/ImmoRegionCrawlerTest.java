package org.barrak.immocrawler.batch.crawler.impl.immoregion;

import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.batch.utils.Whitebox;
import org.barrak.immocrawler.database.document.SearchResultDocument;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ImmoRegionCrawlerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImmoRegionCrawlerTest.class);

    private ImmoRegionCrawler crawler = new ImmoRegionCrawler();

    @Test
    public void search() {
        Whitebox.setInternalState(crawler, "immoregionUrl", "https://www.immoregion.fr/srp/");
        Whitebox.setInternalState(crawler, "cache", new HashMap<String, SearchResultDocument>());

        SearchCriteria criteria = new SearchCriteria(49.434418, 5.921767, 15);
        criteria.setMinPrice(100000);
        criteria.setMaxPrice(500000);

        List<SearchResultDocument> resultDocumentList = crawler.search(criteria);

        assertThat(resultDocumentList).isNotEmpty();
    }
}