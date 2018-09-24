package org.barrak.immocrawler.batch;

import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.crawler.IPagedCrawler;
import org.barrak.immocrawler.batch.crawler.criterias.SearchCriteria;
import org.barrak.crawler.database.repository.SearchResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collection;
import java.util.Map;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = SearchResultRepository.class)
public class ImmoCrawlerBatch {

    @Autowired
    private SearchResultRepository searchResultRepository;
	@Autowired
    private Map<String, SearchResultDocument> cache;

	public static void main(String args[]) {
		SpringApplication.run(ImmoCrawlerBatch.class, args);
	}

//	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			SearchCriteria searchCriteria = new SearchCriteria("crusnes ", 15);
            searchCriteria.setMinPrice(100000);
            searchCriteria.setMaxPrice(500000);

			Collection<IPagedCrawler> crawlers = ctx.getBeansOfType(IPagedCrawler.class).values();

			// Clean repo for fresh import
			searchResultRepository.deleteAll();
			cache.clear();

			for (IPagedCrawler crawler : crawlers) {
                crawler.search(searchCriteria, searchResults -> searchResultRepository.insert(searchResults));
			}
		};
	}
}
