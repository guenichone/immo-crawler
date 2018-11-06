package org.barrak.immocrawler.batch;

import org.barrak.immocrawler.batch.crawler.IDetailsCrawler;
import org.barrak.immocrawler.batch.crawler.IPagedCrawler;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.model.ArticleDocument;
import org.barrak.immocrawler.database.model.ArticleDocumentKey;
import org.barrak.immocrawler.database.repository.ArticleRepository;
import org.barrak.immocrawler.geoloc.IGeoLocService;
import org.barrak.immocrawler.geoloc.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = ArticleRepository.class)
public class ImmoCrawlerBatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImmoCrawlerBatch.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private Map<ArticleDocumentKey, ArticleDocument> cache;

    @Autowired
    private IGeoLocService geoLocService;

	public static void main(String args[]) {
		SpringApplication.run(ImmoCrawlerBatch.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

            // Clean repo for fresh import
//			  articleRepository.deleteAll();
//            cache.clear();

            Location location = geoLocService.getLocation("crusnes");

			SearchCriteria searchCriteria = new SearchCriteria(location.getLat(), location.getLng(), 15);
            searchCriteria.setMinPrice(150000);
            searchCriteria.setMaxPrice(450000);
            searchCriteria.setPostalCode(location.getPostalCode());

			Collection<IPagedCrawler> crawlers = ctx.getBeansOfType(IPagedCrawler.class).values();

            ForkJoinPool pool = new ForkJoinPool(8);
            crawlers.stream().forEach(crawler -> pool.submit(() ->
                crawler.search(searchCriteria, searchResults -> articleRepository.saveAll(searchResults))
            ));

            pool.shutdown();
            pool.awaitTermination(5, TimeUnit.MINUTES);

			findDetails(ctx);
		};
	}

    private void findDetails(ApplicationContext ctx) throws InterruptedException {
        Collection<IDetailsCrawler> crawlers = ctx.getBeansOfType(IDetailsCrawler.class).values();
        Map<ProviderEnum, IDetailsCrawler> crawlerMap = crawlers.stream()
                .collect(Collectors.toMap(IDetailsCrawler::getInternalProvider, Function.identity()));

        ForkJoinPool pool = new ForkJoinPool(64);
        articleRepository.findAll().stream()
            .filter(article -> !article.isMoved() && !article.isError() && !article.isDetailsParsed())
            .forEach(article -> pool.submit(() -> {
                IDetailsCrawler crawler = crawlerMap.get(article.getInternalProvider());
                updateArticle(crawler, article);
            }));

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.MINUTES);
    }

    private void updateArticle(IDetailsCrawler crawler, ArticleDocument article) {
        try {
            if (crawler != null) {
                LOGGER.info("Getting details for {} {}", crawler.getInternalProvider(), article);
                crawler.updateDetails(article);
            } else {
                LOGGER.warn("No crawler details found for {} {}", article.getInternalProvider(), article.getUrl());
                return;
            }
        } catch (NoSuchElementException ex) {
            LOGGER.warn("Article moved permanently : " + article.getUrl());
            article.setMoved(true);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            LOGGER.warn("Saving parsing error in article : " + article.getUrl());
            article.setError(true);
        }
        articleRepository.save(article);
    }
}
