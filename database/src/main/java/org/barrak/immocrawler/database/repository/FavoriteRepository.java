package org.barrak.immocrawler.database.repository;

import org.barrak.immocrawler.database.model.FavoriteDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FavoriteRepository extends MongoRepository<FavoriteDocument, String> {
}
