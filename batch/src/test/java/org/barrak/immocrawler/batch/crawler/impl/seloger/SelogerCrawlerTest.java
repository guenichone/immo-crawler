package org.barrak.immocrawler.batch.crawler.impl.seloger;

import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SelogerCrawlerTest {

    private SelogerCrawler crawler = new SelogerCrawler();

    // https://www.seloger.com/list.htm?enterprise=0&natures=1,2,4&places={ci:570041}&price=100000/500000&proximity=0/15&projects=2&types=2&LISTING-LISTpg=1&qsversion=1.0
    // https://www.seloger.com/list.htm?enterprise=0&natures=1,2,4&places={ci:570041}&price=100000/500000&projects=2&proximity=0,10&qsversion=1.0&types=2,4&bd=DetailToList_SL

    @Test
    public void search() {
        Whitebox.setInternalState(crawler, "selogerUrl", "https://www.seloger.com");

        SearchCriteria criteria = new SearchCriteria(null);
        criteria.setMinPrice(100000);
        criteria.setMaxPrice(500000);

        List<SearchResultDocument> resultDocumentList = crawler.search(criteria);

        assertThat(resultDocumentList).isNotEmpty();
    }
}
