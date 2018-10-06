package org.barrak.immocrawler.batch;

import org.barrak.crawler.database.document.ProviderEnum;
import org.barrak.crawler.database.document.SearchResultDetailsDocument;
import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.crawler.database.repository.SearchResultDetailsRepository;
import org.barrak.immocrawler.batch.crawler.IDetailsCrawler;
import org.barrak.immocrawler.batch.crawler.IPagedCrawler;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.crawler.database.repository.SearchResultRepository;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = SearchResultRepository.class)
public class ImmoCrawlerBatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImmoCrawlerBatch.class);

    @Autowired
    private SearchResultRepository searchResultRepository;

    @Autowired
    private SearchResultDetailsRepository searchResultDetailsRepository;

    @Autowired
    private Map<String, SearchResultDocument> searchResultCache;

    @Autowired
    private Map<String, SearchResultDetailsDocument> searchResultDetailsCache;

	public static void main(String args[]) {
		SpringApplication.run(ImmoCrawlerBatch.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

            // Clean repo for fresh import
//			searchResultRepository.deleteAll();
//            searchResultCache.clear();
//
//            searchResultDetailsRepository.deleteAll();
//            searchResultDetailsCache.clear();

			SearchCriteria searchCriteria = new SearchCriteria("crusnes ", 15);
            searchCriteria.setMinPrice(100000);
            searchCriteria.setMaxPrice(500000);

			Collection<IPagedCrawler> crawlers = ctx.getBeansOfType(IPagedCrawler.class).values();
            crawlers.stream().parallel()
                    .forEach(crawler -> crawler.search(searchCriteria, searchResults -> searchResultRepository.saveAll(searchResults)));

			findDetails(ctx);
		};
	}

    private void findDetails(ApplicationContext ctx) {
        Collection<IDetailsCrawler> crawlers = ctx.getBeansOfType(IDetailsCrawler.class).values();
        Map<ProviderEnum, IDetailsCrawler> crawlerMap = crawlers.stream()
                .collect(Collectors.toMap(IDetailsCrawler::getInternalProvider, Function.identity()));

        searchResultRepository.findAll().parallelStream()
                .filter(article -> !searchResultDetailsCache.containsKey(article.getUrl()) && article.getInternalProvider() != ProviderEnum.PANETTA_IMMO)
                .filter(article -> !article.isMoved() && !article.isError())
                .map(article -> {
                    try {
                        IDetailsCrawler crawler = crawlerMap.get(article.getInternalProvider());
                        if (crawler != null) {
                            LOGGER.info("Getting details for {}", article.getUrl());
                            return crawler.getDetails(article);
                        } else {
                            LOGGER.warn("No crawler details found for {} and url {}", article.getInternalProvider(), article.getUrl());
                            return null;
                        }
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
                .forEach(result -> searchResultDetailsRepository.save(result));
    }
}
