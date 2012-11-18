package com.pmerienne.iclassification.server.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.pmerienne.iclassification.shared.model.Feature;

public class HSVColorFeatureExtractorTest {

	private final static File FILE = new File("src/test/resources/data/flower.jpg");

	private HSVColorFeatureExtractor extractor = new HSVColorFeatureExtractor();

	@Test
	public void testGetFeatures() {
		List<Feature> features = extractor.getFeatures(FILE);
		assertNotNull(features);
		assertFalse(features.isEmpty());
	}

	@Test
	public void testGetFeatureImage() {
		File output = this.extractor.createFeatureImage(FILE);
		assertNotNull(output);
		assertTrue(output.exists());
		System.out.println(output);
	}
}
