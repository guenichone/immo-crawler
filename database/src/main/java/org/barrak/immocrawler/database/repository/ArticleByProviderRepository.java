package org.barrak.immocrawler.database.repository;

import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.model.ArticleDocument;
import org.barrak.immocrawler.database.model.ArticleDocumentKey;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArticleByProviderRepository extends MongoRepository<ArticleDocument, ArticleDocumentKey> {

    List<ArticleDocument> findByInternalProvider(ProviderEnum provider);

    void deleteByInternalProvider(ProviderEnum provider);
}
