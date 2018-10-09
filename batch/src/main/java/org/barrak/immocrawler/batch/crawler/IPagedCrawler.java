package org.barrak.immocrawler.batch.crawler;

import org.barrak.immocrawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public interface IPagedCrawler extends ICrawler {

    @Override
    default List<SearchResultDocument> search(SearchCriteria criteria) {
        List<SearchResultDocument> results = new ArrayList<>();
        search(criteria, searchResultDocuments -> results.addAll(searchResultDocuments));
        return results.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    void search(SearchCriteria criteria, Consumer<List<SearchResultDocument>> consumer);
}
