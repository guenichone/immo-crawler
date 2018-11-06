package org.barrak.immocrawler.backend;

import org.barrak.immocrawler.database.repository.ArticleRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = ArticleRepository.class)
public class ImmoCrawlerBackendApp {

    public static void main(String args[]) {
        SpringApplication.run(ImmoCrawlerBackendApp.class, args);
    }
}
