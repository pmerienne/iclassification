package com.pmerienne.iclassification.server.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class ImageCropperTest {

	private final static File TEST_IMAGE = new File("src/test/resources/data/egret.jpg");

	private ImageCropper imageCropper = new ImageCropper();

	@Test
	public void testCrop() throws IOException {
		// Test
		File actualFile = this.imageCropper.crop(TEST_IMAGE, 120, 100, 100, 100);

		// Assert
		assertNotNull(actualFile);
		assertTrue(actualFile.exists());
	}

}
