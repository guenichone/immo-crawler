package org.barrak.crawler.database.repository;

import org.barrak.crawler.database.document.SearchResultDetailsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SearchResultDetailsRepository extends MongoRepository<SearchResultDetailsDocument, String> {

    List<SearchResultDetailsDocument> findByLandSurfaceGreaterThan(int surface);

}
