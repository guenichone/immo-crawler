package org.barrak.immocrawler.batch.crawler;

import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;

import java.util.List;

public interface ICrawler {

    List<SearchResultDocument> search(SearchCriteria criteria);
}
