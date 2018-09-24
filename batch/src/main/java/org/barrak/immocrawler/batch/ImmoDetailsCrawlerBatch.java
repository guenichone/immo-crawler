package org.barrak.immocrawler.batch;

import org.barrak.crawler.database.document.SearchResultDetailsDocument;
import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.crawler.database.repository.SearchResultDetailsRepository;
import org.barrak.crawler.database.repository.SearchResultRepository;
import org.barrak.immocrawler.batch.crawler.IDetailsCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = SearchResultRepository.class)
public class ImmoDetailsCrawlerBatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImmoDetailsCrawlerBatch.class);

    @Autowired
    private Map<String, SearchResultDocument> searchResultCache;

    @Autowired
    private Map<String, SearchResultDetailsDocument> searchResultDetailsCache;

    @Autowired
    private SearchResultRepository searchResultRepository;

    @Autowired
    private SearchResultDetailsRepository searchResultDetailsRepository;

	public static void main(String args[]) {
		SpringApplication.run(ImmoDetailsCrawlerBatch.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			Collection<IDetailsCrawler> crawlers = ctx.getBeansOfType(IDetailsCrawler.class).values();
			IDetailsCrawler crawler = crawlers.iterator().next();

			List<SearchResultDetailsDocument> detailsDocumentList = searchResultCache.values().parallelStream()
                    .filter(article -> !searchResultDetailsCache.containsKey(article.getUrl()))
                    .filter(article -> !article.isMoved() && !article.isError())
                    .map(article -> {
                        try {
                            return crawler.getDetails(article);
                        } catch (NoSuchElementException ex) {
                            LOGGER.warn("Article moved permanently : " + article.getUrl());
                            article.setMoved(true);
                            searchResultRepository.save(article);
                        } catch (Exception ex) {
                            LOGGER.error(ex.getMessage(), ex);
                            LOGGER.warn("Saving parsing error in article : " + article.getUrl());
                            article.setError(true);
                            searchResultRepository.save(article);
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

			searchResultDetailsRepository.saveAll(detailsDocumentList);
		};
	}
}
