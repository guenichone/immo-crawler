package org.barrak.immocrawler.batch.crawler.impl.optimhome;

import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.batch.utils.Whitebox;
import org.barrak.immocrawler.database.model.ArticleDocument;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OptimHomeCrawlerTest {

    private OptimHomeCrawler crawler = new OptimHomeCrawler("D:/Telechargement/geckodriver-v0.22.0-win64/geckodriver.exe");

    @Test
    public void search() {
        Whitebox.setInternalState(crawler, "optimhomeUrl", "https://www.optimhome.com/");
        Whitebox.setInternalState(crawler, "cache", new HashMap<String, ArticleDocument>());

        SearchCriteria criteria = new SearchCriteria(49.434418, 5.921767, 15);
        criteria.setMinPrice(100000);
        criteria.setMaxPrice(500000);

        List<ArticleDocument> resultDocumentList = crawler.search(criteria);

        assertThat(resultDocumentList).isNotEmpty();
    }
}