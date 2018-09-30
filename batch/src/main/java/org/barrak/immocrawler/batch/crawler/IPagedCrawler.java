package org.barrak.immocrawler.batch.crawler;

import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface IPagedCrawler extends ICrawler {

    @Override
    default List<SearchResultDocument> search(SearchCriteria criteria) {
        List<SearchResultDocument> results = new ArrayList<>();
        search(criteria, searchResultDocuments -> results.addAll(searchResultDocuments));
        return results;
    }

    void search(SearchCriteria criteria, Consumer<List<SearchResultDocument>> consumer);
}
