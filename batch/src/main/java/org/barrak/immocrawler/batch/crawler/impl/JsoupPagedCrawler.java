package org.barrak.immocrawler.batch.crawler.impl;

import org.barrak.immocrawler.batch.crawler.IPagedCrawler;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.database.document.SearchResultDocument;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class JsoupPagedCrawler implements IPagedCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsoupPagedCrawler.class);

    @Override
    public void search(SearchCriteria criteria, Consumer<List<SearchResultDocument>> consumer) {
        try {
            Document document = getDocumentPage(criteria, 1);

            int total = getTotal(document);
            if (total == 0) {
                throw new NoSuchElementException("No articles found for " + buildSearchUrl(criteria, 1));
            }

            Elements articles = getArticles(document);
            int nbByPage = articles.size();

            int numberOfPages = total  / nbByPage;
            if (total % nbByPage != 0) {
                numberOfPages++;
            }

            consumer.accept(parseArticles(criteria, articles));

            LOGGER.info("{} : Found {} results in {} pages of results", getInternalProvider(), total, numberOfPages);

            if (numberOfPages > 1) {
                IntStream.rangeClosed(2, numberOfPages).parallel().forEach(page -> {
                    try {
                        Document doc = getDocumentPage(criteria, page);
                        consumer.accept(parseResultPage(criteria, doc));
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                });
            }
        } catch (Exception e) {
            LOGGER.error(getInternalProvider() + " " + e.getMessage(), e);
        }
    }

    protected Document getDocumentPage(SearchCriteria criteria, int pageNumber) throws IOException {
        String url = buildSearchUrl(criteria, pageNumber);

        return addConnectionParams(Jsoup.connect(url)).get();
    }

    protected Connection addConnectionParams(Connection connection) {
        return connection;
    }

    protected List<SearchResultDocument> parseResultPage(SearchCriteria criteria, Document document) {
        return parseArticles(criteria, getArticles(document));
    }

    protected List<SearchResultDocument> parseArticles(SearchCriteria criteria, Elements articles) {
        return articles.parallelStream()
                .map(article -> parseArticle(criteria, article))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    protected abstract int getTotal(Document document);

    protected abstract Elements getArticles(Document document);

    protected abstract SearchResultDocument parseArticle(SearchCriteria criteria, Element article);

    protected abstract String buildSearchUrl(SearchCriteria criteria, int page);
}
