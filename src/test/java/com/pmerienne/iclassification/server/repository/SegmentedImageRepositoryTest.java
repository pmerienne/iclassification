package com.pmerienne.iclassification.server.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pmerienne.iclassification.server.IntegrationTest;
import com.pmerienne.iclassification.server.core.SegmentedImage;
import com.pmerienne.iclassification.shared.model.CropZone;
import com.pmerienne.iclassification.shared.model.ImageMetadata;

public class SegmentedImageRepositoryTest extends IntegrationTest {

	@Autowired
	private SegmentedImageRepository segmentedImageRepository;

	@Autowired
	private ImageRepository imageRepository;

	@Test
	public void testFindByOriginalImageAndCropZone() {
		// Test data
		CropZone cropZone = new CropZone(10, 5, 150, 20);
		ImageMetadata originalImage = new ImageMetadata("test.jpg");
		this.imageRepository.save(originalImage);

		SegmentedImage segmentedImage = new SegmentedImage(originalImage, cropZone);
		this.segmentedImageRepository.save(segmentedImage);

		SegmentedImage actualImage = this.segmentedImageRepository.findByOriginalImageAndCropZone(originalImage, cropZone);
		assertEquals(segmentedImage, actualImage);

		actualImage = this.segmentedImageRepository.findByOriginalImageAndCropZone(originalImage, new CropZone(12, 56, 231, 45));
		assertNull(actualImage);
	}
}
