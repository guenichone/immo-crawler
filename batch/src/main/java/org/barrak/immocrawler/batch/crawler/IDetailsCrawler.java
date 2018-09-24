package org.barrak.immocrawler.batch.crawler;

import org.barrak.crawler.database.document.SearchResultDetailsDocument;
import org.barrak.crawler.database.document.SearchResultDocument;

public interface IDetailsCrawler {

    SearchResultDetailsDocument getDetails(SearchResultDocument article);
}
