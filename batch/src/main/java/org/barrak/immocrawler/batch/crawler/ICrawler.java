package org.barrak.immocrawler.batch.crawler;

import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.model.ArticleDocument;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;

import java.util.List;

public interface ICrawler {

    List<ArticleDocument> search(SearchCriteria criteria);

    ProviderEnum getInternalProvider();
}
