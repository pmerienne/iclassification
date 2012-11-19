package com.pmerienne.iclassification.server.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.pmerienne.iclassification.shared.model.Feature;
import com.pmerienne.iclassification.shared.model.FeatureConfiguration;
import com.pmerienne.iclassification.shared.model.FeatureType;
import com.pmerienne.iclassification.shared.model.ImageMetadata;

public interface FeatureService {

	List<Feature> getFeatures(ImageMetadata imageMetadata, FeatureConfiguration featureConfiguration);

	List<Feature> computeFeatures(File image, FeatureConfiguration featureConfiguration);

	File getFeatureFile(ImageMetadata imageMetadata, FeatureType featureType, boolean useCropZone) throws IOException;

	void clearFeatures(ImageMetadata imageMetadata);

}
