package com.pmerienne.iclassification.server.repository;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pmerienne.iclassification.server.IntegrationTest;
import com.pmerienne.iclassification.shared.model.Feature;
import com.pmerienne.iclassification.shared.model.FeatureType;

public class FeatureRepositoryTest extends IntegrationTest {

	@Autowired
	private FeatureRepository featureRepository;

	@Test
	public void testSave() {
		String filename = "321654.jpg";
		Feature feature = new Feature(filename, FeatureType.HSV_COLOR, false, new double[] { 123, 21, 321 });
		this.featureRepository.save(feature);

		List<Feature> actualFeatures = this.featureRepository.findByFilename(filename);
		assertNotNull(actualFeatures);
		assertTrue(actualFeatures.contains(feature));
		assertEquals(1, actualFeatures.size());
	}

	@Test
	public void testDeleteMultiple() {
		String filename = "321654.jpg";
		Feature feature1 = new Feature(filename, FeatureType.HSV_COLOR, false, new double[] { 123, 21, 321 });
		Feature feature2 = new Feature(filename, FeatureType.RGB_COLOR, false, new double[] { 123, 21, 321 });
		Feature feature3 = new Feature(filename, FeatureType.SIFT, false, new double[] { 123, 21, 321 });
		Feature feature4 = new Feature(filename, FeatureType.SURF, false, new double[] { 123, 21, 321 });
		List<Feature> features = Arrays.asList(feature1, feature2, feature3, feature4);

		this.featureRepository.save(features);

		List<Feature> actualFeatures = this.featureRepository.findByFilename(filename);
		this.featureRepository.delete(actualFeatures);

		actualFeatures = this.featureRepository.findByFilename(filename);
		assertTrue(actualFeatures.isEmpty());
	}

}
