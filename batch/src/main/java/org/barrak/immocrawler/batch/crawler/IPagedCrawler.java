package org.barrak.immocrawler.batch.crawler;

import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;

import java.util.List;
import java.util.function.Consumer;

public interface IPagedCrawler extends ICrawler {

    List<SearchResultDocument> search(SearchCriteria criteria, Consumer<List<SearchResultDocument>> consumer);
}
