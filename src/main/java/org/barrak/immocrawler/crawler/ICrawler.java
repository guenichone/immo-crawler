package org.barrak.immocrawler.crawler;

import org.barrak.immocrawler.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.crawler.results.SearchResult;

import java.util.List;

public interface ICrawler {

    List<SearchResult> search(SearchCriteria criteria);
}
