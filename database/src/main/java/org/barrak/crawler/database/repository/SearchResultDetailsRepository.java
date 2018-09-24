package org.barrak.crawler.database.repository;

import org.barrak.crawler.database.document.SearchResultDetailsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SearchResultDetailsRepository extends MongoRepository<SearchResultDetailsDocument, String> {

}
