package com.pmerienne.iclassification.server.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pmerienne.iclassification.server.core.FeatureImage;
import com.pmerienne.iclassification.shared.model.FeatureType;
import com.pmerienne.iclassification.shared.model.ImageMetadata;

public interface FeatureImageRepository extends MongoRepository<FeatureImage, String> {

	FeatureImage findByOriginalImageAndFeatureTypeAndUseCropZone(ImageMetadata originalImage, FeatureType featureType, boolean useCropZone);
	
	List<FeatureImage> findByOriginalImage(ImageMetadata originalImage);
}
