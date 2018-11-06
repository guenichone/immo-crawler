package org.barrak.immocrawler.batch.config;

import org.barrak.immocrawler.database.model.ArticleDocument;
import org.barrak.immocrawler.database.model.ArticleDocumentKey;
import org.barrak.immocrawler.database.repository.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class CacheConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheConfig.class);

    @Bean
    public Map<ArticleDocumentKey, ArticleDocument> getSearchResultCache(
            ArticleRepository articleRepository) {

        Map<ArticleDocumentKey, ArticleDocument> result = articleRepository.findAll().stream()
                .collect(Collectors.toMap(ArticleDocument::getId, Function.identity()));
        LOGGER.info("Cache SearchResult loading with {} entries", result.size());
        return result;
    }

}
