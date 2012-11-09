package com.pmerienne.iclassification.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pmerienne.iclassification.shared.model.ImageLabel;

public interface ImageLabelRepository extends MongoRepository<ImageLabel, String> {

	ImageLabel findByName(String name);
}
