package com.pmerienne.iclassification.server.service;

import java.io.File;
import java.util.List;

import com.pmerienne.iclassification.shared.model.Feature;
import com.pmerienne.iclassification.shared.model.FeatureConfiguration;
import com.pmerienne.iclassification.shared.model.ImageMetadata;

public interface FeatureService {

	List<Feature> getFeatures(ImageMetadata imageMetadata, FeatureConfiguration featureConfiguration);

	List<Feature> computeFeatures(File image, FeatureConfiguration featureConfiguration);
	
	void clearFeatures(ImageMetadata imageMetadata);

}
