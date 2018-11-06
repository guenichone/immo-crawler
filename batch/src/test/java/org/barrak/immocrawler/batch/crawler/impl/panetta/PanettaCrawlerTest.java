package org.barrak.immocrawler.batch.crawler.impl.panetta;

import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.batch.utils.Whitebox;
import org.barrak.immocrawler.database.model.ArticleDocument;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PanettaCrawlerTest {

    private PanettaCrawler crawler = new PanettaCrawler();

    @Test
    public void search() {
        Whitebox.setInternalState(crawler, "panettaImmoUrl", "http://www.panetta-immobilier.fr");
        Whitebox.setInternalState(crawler, "cache", new HashMap<String, ArticleDocument>());

        SearchCriteria criteria = new SearchCriteria(49.434418, 5.921767, 15);
        criteria.setMinPrice(100000);
        criteria.setMaxPrice(500000);

        List<ArticleDocument> resultDocumentList = crawler.search(criteria);

        assertThat(resultDocumentList).isNotEmpty();
    }
}