package org.barrak.immocrawler.batch.crawler.impl.panetta;

import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PanettaCrawlerTest {

    private PanettaCrawler crawler = new PanettaCrawler();

    @Test
    public void search() {
        Whitebox.setInternalState(crawler, "panettaImmoUrl", "http://www.panetta-immobilier.fr");

        SearchCriteria criteria = new SearchCriteria(null);
        criteria.setMinPrice(100000);
        criteria.setMaxPrice(500000);

        List<SearchResultDocument> resultDocumentList = crawler.search(criteria);

        assertThat(resultDocumentList).isNotEmpty();
    }
}