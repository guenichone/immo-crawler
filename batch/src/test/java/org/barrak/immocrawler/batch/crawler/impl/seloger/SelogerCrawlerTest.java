package org.barrak.immocrawler.batch.crawler.impl.seloger;

import org.barrak.immocrawler.batch.crawler.ICrawler;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.batch.utils.Whitebox;
import org.barrak.immocrawler.database.model.ArticleDocument;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SelogerCrawlerTest {

    private ICrawler crawler = new SelogerCrawler();
//    private ICrawler crawler = new SelogerSeleniumCrawler();

    // https://www.seloger.com/list.htm?enterprise=0&natures=1,2,4&places={ci:570041}&price=100000/500000&proximity=0/15&projects=2&types=2&LISTING-LISTpg=1&qsversion=1.0
    // https://www.seloger.com/list.htm?enterprise=0&natures=1,2,4&places={ci:570041}&price=100000/500000&projects=2&proximity=0,10&qsversion=1.0&types=2,4&bd=DetailToList_SL

    @Test
    public void search() {
        Whitebox.setInternalState(crawler, "selogerUrl", "https://www.seloger.com");
        Whitebox.setInternalState(crawler, "cache", new HashMap<String, ArticleDocument>());
//        Whitebox.setInternalState(crawler, "geckodriverPath", "D:/Telechargement/geckodriver-v0.22.0-win64/geckodriver.exe");

        SearchCriteria criteria = new SearchCriteria(49.434418, 5.921767, 15);
        criteria.setMinPrice(100000);
        criteria.setMaxPrice(500000);
        criteria.setPostalCode("57041");

        List<ArticleDocument> resultDocumentList = crawler.search(criteria);

        assertThat(resultDocumentList).isNotEmpty();
    }
}
