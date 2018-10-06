package org.barrak.immocrawler.batch.config;

import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.crawler.database.repository.SearchResultRepository;
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
    public Map<String, SearchResultDocument> getSearchResultCache(
            SearchResultRepository searchResultRepository) {

        Map<String, SearchResultDocument> result = searchResultRepository.findAll().stream()
                .collect(Collectors.toMap(SearchResultDocument::getUrl, Function.identity()));
        LOGGER.info("Cache SearchResult loading with {} entries", result.size());
        return result;
    }

}
