package com.pmerienne.iclassification.server.util;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Test;

public class ZipUtilsTest {

	private final static File ZIP_FILE = new File("src/test/resources/data/dataset-min.zip");
	private final static File TEST_DIRECTORY = new File("src/test/resources/");

	@Test
	public void testUnzip() throws FileNotFoundException, Exception {
		File outputFolder = ZipUtils.unzip(new FileInputStream(ZIP_FILE));
		assertTrue(outputFolder.exists());
		System.out.println(outputFolder);
	}

	@Test
	public void testZip() throws FileNotFoundException, Exception {
		File outputZip = ZipUtils.zip(TEST_DIRECTORY);
		assertTrue(outputZip.exists());
		System.out.println(outputZip);
	}

}
