package org.barrak.crawler.database.repository;

import org.barrak.crawler.database.document.SearchResultDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SearchResultRepository extends MongoRepository<SearchResultDocument, String> {

}
