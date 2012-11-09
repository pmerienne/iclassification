package com.pmerienne.iclassification.server.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.pmerienne.iclassification.shared.model.CropZone;

public class ImageSegmenterTest {

	private final static File TEST_IMAGE = new File("src/test/resources/data/flower.jpg");

	private ImageSegmenter imageSegmenter = new ImageSegmenter();

	@Test
	public void testSegment() throws IOException {
		// Test
		CropZone cropZone = new CropZone(100, 70, 140, 180);
		File actualFile = this.imageSegmenter.segment(TEST_IMAGE, cropZone);
		System.out.println(actualFile);

		// Assert
		assertNotNull(actualFile);
		assertTrue(actualFile.exists());
	}

	@Test
	public void testSegmentWithoutCropZone() throws IOException {
		// Test
		File actualFile = this.imageSegmenter.segment(TEST_IMAGE, null);
		System.out.println(actualFile);

		// Assert
		assertNotNull(actualFile);
		assertTrue(actualFile.exists());
	}
}
