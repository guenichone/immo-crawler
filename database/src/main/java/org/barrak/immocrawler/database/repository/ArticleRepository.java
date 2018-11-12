package org.barrak.immocrawler.database.repository;

import org.barrak.immocrawler.database.model.ArticleDocument;
import org.barrak.immocrawler.database.model.ArticleDocumentKey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ArticleRepository extends MongoRepository<ArticleDocument, ArticleDocumentKey> {

    @Query("{'landSurface': {$gt: ?0}, 'sold': false}")
    List<ArticleDocument> findByLandSurfaceGreaterThanAndNotSold(int surface);

    @Query("{'homeSurface': {$gt: ?0}, 'sold': false}")
    List<ArticleDocument> findByHomeSurfaceGreaterThanAndNotSold(int surface);

    List<ArticleDocument> findByError(boolean error);
}
