package org.barrak.immocrawler.batch.crawler;

import org.barrak.immocrawler.database.model.ArticleDocument;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public interface IPagedCrawler extends ICrawler {

    @Override
    default List<ArticleDocument> search(SearchCriteria criteria) {
        List<ArticleDocument> results = new ArrayList<>();
        search(criteria, searchResultDocuments -> results.addAll(searchResultDocuments));
        return results.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    void search(SearchCriteria criteria, Consumer<List<ArticleDocument>> consumer);
}
