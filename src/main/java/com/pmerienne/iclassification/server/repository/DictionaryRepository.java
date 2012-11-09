package com.pmerienne.iclassification.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pmerienne.iclassification.shared.model.Dictionary;

public interface DictionaryRepository extends MongoRepository<Dictionary, String> {

}
