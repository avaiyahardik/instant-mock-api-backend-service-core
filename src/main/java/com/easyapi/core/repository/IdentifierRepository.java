package com.easyapi.core.repository;

import com.easyapi.core.model.Identifier;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IdentifierRepository extends MongoRepository<Identifier, String> {
}
