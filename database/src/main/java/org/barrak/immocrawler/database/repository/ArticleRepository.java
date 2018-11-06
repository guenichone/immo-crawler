package org.barrak.immocrawler.database.repository;

import org.barrak.immocrawler.database.model.ArticleDocument;
import org.barrak.immocrawler.database.model.ArticleDocumentKey;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArticleRepository extends MongoRepository<ArticleDocument, ArticleDocumentKey> {

    List<ArticleDocument> findByLandSurfaceGreaterThan(int surface);

    List<ArticleDocument> findByHomeSurfaceGreaterThan(int surface);

    List<ArticleDocument> findByError(boolean error);
}
