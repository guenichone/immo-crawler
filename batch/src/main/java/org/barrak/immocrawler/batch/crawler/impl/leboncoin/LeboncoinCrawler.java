package org.barrak.immocrawler.batch.crawler.impl.leboncoin;

import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.batch.crawler.impl.JsoupPagedCrawler;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

// @Component
public class LeboncoinCrawler extends JsoupPagedCrawler {

    @Value("${leboncoin.url}")
    private String leboncoinUrl;

    @Override
    public void search(SearchCriteria criteria, Consumer<List<SearchResultDocument>> consumer) {

    }

    @Override
    public ProviderEnum getInternalProvider() {
        return ProviderEnum.LEBONCOIN_PARTICULIER;
    }

    @Override
    protected int getTotal(Document document) {
        return 0;
    }

    @Override
    protected Elements getArticles(Document document) {
        return null;
    }

    @Override
    protected SearchResultDocument parseArticle(SearchCriteria criteria, Element article) {
        return null;
    }

    @Override
    protected String buildSearchUrl(SearchCriteria criteria, int page) {
        // https://www.leboncoin.fr/recherche/
        // ?category=9
        // &lat=49.3155509
        // &lng=6.113929499999999
        // &radius=20000
        // &owner_type=private
        // &price=min-400000


        return null;
    }
}
