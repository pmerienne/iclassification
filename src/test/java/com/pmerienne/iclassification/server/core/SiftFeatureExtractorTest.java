package com.pmerienne.iclassification.server.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.pmerienne.iclassification.shared.model.Feature;


public class SiftFeatureExtractorTest {

	@Test
	public void testGetFeatures() {
		File image = new File("src/test/resources/data/flower.jpg");
		SiftFeatureExtractor extractor = new SiftFeatureExtractor();
		List<Feature> features = extractor.getFeatures(image);
		assertNotNull(features);
		assertFalse(features.isEmpty());
	}

}
