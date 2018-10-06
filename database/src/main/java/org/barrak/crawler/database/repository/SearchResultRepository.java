package org.barrak.crawler.database.repository;

import org.barrak.crawler.database.document.SearchResultDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SearchResultRepository extends MongoRepository<SearchResultDocument, String> {

    List<SearchResultDocument> findByLandSurfaceGreaterThan(int surface);

    List<SearchResultDocument> findByHomeSurfaceGreaterThan(int surface);

}
