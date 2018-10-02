package org.barrak.immocrawler.batch.crawler;

import org.barrak.crawler.database.document.ProviderEnum;
import org.barrak.crawler.database.document.SearchResultDetailsDocument;
import org.barrak.crawler.database.document.SearchResultDocument;

/**
 * Classes that implements this interface must be able to retrieve the full data of a detailed article.
 * Ex:
 * - http://www.panetta-immobilier.fr/neuf/maison/meurthe-et-moselle/briey/mont-saint-martin/6036985
 * - https://www.immoregion.fr/vente/terrain/mexy/id-6041306.html
 */
public interface IDetailsCrawler {

    SearchResultDetailsDocument getDetails(SearchResultDocument article);

    ProviderEnum getInternalProvider();
}
