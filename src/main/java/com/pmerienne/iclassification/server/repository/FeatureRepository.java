package com.pmerienne.iclassification.server.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pmerienne.iclassification.shared.model.Feature;
import com.pmerienne.iclassification.shared.model.FeatureType;
import com.pmerienne.iclassification.shared.model.ImageMetadata;

public interface FeatureRepository extends MongoRepository<Feature, String> {

	List<Feature> findByImageMetadata(ImageMetadata imageMetadata);

	List<Feature> findByImageMetadataAndTypeAndUseCropZone(ImageMetadata imageMetadata, FeatureType type, boolean useCropZone);
}
