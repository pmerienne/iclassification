package com.pmerienne.iclassification.server.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pmerienne.iclassification.shared.model.Feature;
import com.pmerienne.iclassification.shared.model.FeatureType;

public interface FeatureRepository extends MongoRepository<Feature, String> {

	List<Feature> findByFilename(String filename);

	List<Feature> findByFilenameAndTypeAndUseCropZone(String filename, FeatureType type, boolean useCropZone);
}
