package org.barrak.immocrawler.batch.crawler.impl.optimhome;

import org.barrak.immocrawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OptimHomeCrawlerTest {

    private OptimHomeCrawler crawler = new OptimHomeCrawler("D:/Telechargement/geckodriver-v0.22.0-win64/geckodriver.exe");

    @Test
    public void search() {
        Whitebox.setInternalState(crawler, "optimhomeUrl", "https://www.optimhome.com/");
        Whitebox.setInternalState(crawler, "cache", new HashMap<String, SearchResultDocument>());

        SearchCriteria criteria = new SearchCriteria("crusnes");
        criteria.setMinPrice(100000);
        criteria.setMaxPrice(500000);

        List<SearchResultDocument> resultDocumentList = crawler.search(criteria);

        assertThat(resultDocumentList).hasSize(41);
    }
}