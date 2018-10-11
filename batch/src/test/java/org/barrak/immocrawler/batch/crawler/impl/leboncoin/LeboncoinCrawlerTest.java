package org.barrak.immocrawler.batch.crawler.impl.leboncoin;

import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.batch.crawler.impl.optimhome.OptimHomeCrawler;
import org.barrak.immocrawler.database.document.SearchResultDocument;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LeboncoinCrawlerTest {

    private LeboncoinCrawler crawler = new LeboncoinCrawler();

    @Test
    public void search() {
        Whitebox.setInternalState(crawler, "leboncoinUrl", "https://www.leboncoin.fr");
         Whitebox.setInternalState(crawler, "cache", new HashMap<String, SearchResultDocument>());

        SearchCriteria criteria = new SearchCriteria("crusnes");
        criteria.setMinPrice(100000);
        criteria.setMaxPrice(500000);

        List<SearchResultDocument> resultDocumentList = crawler.search(criteria);

        assertThat(resultDocumentList).hasSize(95);
    }
}