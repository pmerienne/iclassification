package com.pmerienne.iclassification.server.core;

import java.io.File;
import java.util.List;

import com.pmerienne.iclassification.shared.model.Feature;

public interface FeatureExtractor {

	List<Feature> getFeatures(File file);
}
