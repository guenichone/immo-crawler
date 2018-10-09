package org.barrak.immocrawler.batch.crawler;

import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;

import java.util.List;

public interface ICrawler {

    List<SearchResultDocument> search(SearchCriteria criteria);

    ProviderEnum getInternalProvider();
}
