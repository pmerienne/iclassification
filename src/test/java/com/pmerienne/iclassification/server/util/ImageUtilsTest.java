package com.pmerienne.iclassification.server.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class ImageUtilsTest {

	private final static File PNG_FILE = new File("src/test/resources/data/segmented.png");

	@Test
	public void testConvertToJpg() throws IOException {
		File file = ImageUtils.convertToJpg(PNG_FILE);
		assertNotNull(file);
		assertTrue(file.exists());
		System.out.println(file);
	}

}
