package org.barrak.immocrawler.database.repository;

import org.barrak.immocrawler.database.config.TestConfig;
import org.barrak.immocrawler.database.model.ArticleDocument;
import org.barrak.immocrawler.utils.ArticleDocumentFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ArticleRepository.class, TestConfig.class})
@EnableAutoConfiguration
@DataMongoTest
@ExtendWith(SpringExtension.class)
class ArticleRepositoryIT {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleDocumentFactory articleDocumentFactory;

    @Test
    void findByLandSurfaceGreaterThan() {
        List<ArticleDocument> results = articleRepository.findByLandSurfaceGreaterThan(9);
        assertThat(results).hasSize(2);

        results = articleRepository.findByLandSurfaceGreaterThan(10);
        assertThat(results).hasSize(1);
    }

    @Test
    void findByHomeSurfaceGreaterThan() {
        List<ArticleDocument> results = articleRepository.findByHomeSurfaceGreaterThan(80);
        assertThat(results).hasSize(2);

        results = articleRepository.findByHomeSurfaceGreaterThan(129);
        assertThat(results).hasSize(1);
    }

    @Test
    void findByError() {
        List<ArticleDocument> results = articleRepository.findByError(false);
        assertThat(results).hasSize(3);
        results = articleRepository.findByError(true);
        assertThat(results).hasSize(1);
    }

    @BeforeAll
    public void createArticleDocumentData() {
        // String id, String url, ProviderEnum provider, RealEstateTypeEnum realEstateTypeEnum, String city, int price
        ArticleDocument articleDocument1 = articleDocumentFactory.createArticleDocumentWithLandAndHomeSurface("1", 5, 80);
        ArticleDocument articleDocument2 = articleDocumentFactory.createArticleDocumentWithLandAndHomeSurface("2", 10, 100);
        ArticleDocument articleDocument3 = articleDocumentFactory.createArticleDocumentWithLandAndHomeSurface("3", 15, 130);
        ArticleDocument articleDocument4 = articleDocumentFactory.createArticleDocument("4");
        articleDocument4.setError(true);

        articleRepository.saveAll(Arrays.asList(articleDocument1, articleDocument2, articleDocument3, articleDocument4));
    }
}