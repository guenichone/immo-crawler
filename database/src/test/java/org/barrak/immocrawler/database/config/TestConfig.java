package org.barrak.immocrawler.database.config;

import org.barrak.immocrawler.utils.ArticleDocumentFactory;
import org.barrak.immocrawler.utils.UserDocumentFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class TestConfig {

    @Bean
    public ArticleDocumentFactory articleDocumentFactory() {
        return new ArticleDocumentFactory(10);
    }

    @Bean
    public UserDocumentFactory userDocumentFactory() {
        return new UserDocumentFactory(20);
    }
}
