package org.barrak.immocrawler.database.repository;

import org.barrak.immocrawler.database.model.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserDocument, String> {

    UserDocument findByEmail(String email);

}
