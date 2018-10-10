package org.barrak.immocrawler.database.repository;

import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.document.SearchResultDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SearchResultRepository extends MongoRepository<SearchResultDocument, String> {

    List<SearchResultDocument> findByLandSurfaceGreaterThan(int surface);

    List<SearchResultDocument> findByHomeSurfaceGreaterThan(int surface);

    List<SearchResultDocument> findByInternalProvider(ProviderEnum provider);

    void deleteByInternalProvider(ProviderEnum provider);

}
