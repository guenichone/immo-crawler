package org.barrak.immocrawler.backend;

import org.barrak.crawler.database.repository.SearchResultRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = SearchResultRepository.class)
public class ImmoCrawlerBackendApp {

    public static void main(String args[]) {
        SpringApplication.run(ImmoCrawlerBackendApp.class, args);
    }
}
