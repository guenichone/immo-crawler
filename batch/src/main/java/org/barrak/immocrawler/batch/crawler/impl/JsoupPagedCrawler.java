package org.barrak.immocrawler.batch.crawler.impl;

import org.barrak.immocrawler.batch.crawler.IPagedCrawler;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.database.model.ArticleDocument;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class JsoupPagedCrawler implements IPagedCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsoupPagedCrawler.class);

    @Override
    public void search(SearchCriteria criteria, Consumer<List<ArticleDocument>> consumer) {
        try {
            UriComponentsBuilder urlBuilder = getSearchUrlBuilder(criteria);
            Document document = getDocumentPage(urlBuilder, 1);

            int total = getTotal(document);
            if (total == 0) {
                throw new NoSuchElementException("No articles found for " + buildSearchUrl(urlBuilder, 1));
            } else if (total >= 1000) {
                throw new IllegalArgumentException("There is too many results (" + total + "), please restrict your search.");
            }

            Elements articles = getArticles(document);
            int nbByPage = articles.size();

            int numberOfPages = total  / nbByPage;
            if (total % nbByPage != 0) {
                numberOfPages++;
            }

            AtomicInteger counter = new AtomicInteger(0);
            consumer.accept(countResults(counter, parseArticles(criteria, articles)));

            LOGGER.info("{} : Found {} results in {} pages of results", getInternalProvider(), total, numberOfPages);

            if (numberOfPages > 1) {
                ForkJoinPool pool = new ForkJoinPool(8);
                IntStream.rangeClosed(2, numberOfPages).forEach(page -> pool.submit(() -> {
                    try {
                        Document doc = getDocumentPage(urlBuilder, page);
                        consumer.accept(countResults(counter, parseResultPage(criteria, doc)));
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }));

                pool.shutdown();
                pool.awaitTermination(2, TimeUnit.MINUTES);
            }

            LOGGER.info("{} : Added {} new results", getInternalProvider(), counter.get());
        } catch (Exception e) {
            LOGGER.error(getInternalProvider() + " " + e.getMessage(), e);
        }
    }

    private List<ArticleDocument> countResults(AtomicInteger counter, List<ArticleDocument> results) {
        counter.addAndGet(results.size());
        return results;
    }

    protected Document getDocumentPage(UriComponentsBuilder builder, int pageNumber) throws IOException {
        String url = buildSearchUrl(builder, pageNumber);

        return getDocument(addConnectionParams(Jsoup.connect(url)));
    }

    protected Document getDocument(Connection connection) throws IOException {
        return connection.get();
    }

    protected Connection addConnectionParams(Connection connection) {
        return connection;
    }

    protected List<ArticleDocument> parseResultPage(SearchCriteria criteria, Document document) {
        return parseArticles(criteria, getArticles(document));
    }

    protected List<ArticleDocument> parseArticles(SearchCriteria criteria, Elements articles) {
        return articles.parallelStream()
                .map(article -> parseArticle(criteria, article))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    protected abstract int getTotal(Document document);

    protected abstract Elements getArticles(Document document);

    protected abstract ArticleDocument parseArticle(SearchCriteria criteria, Element article);

    protected abstract UriComponentsBuilder getSearchUrlBuilder(SearchCriteria criteria);

    protected abstract String buildSearchUrl(UriComponentsBuilder builder, int page);
}
