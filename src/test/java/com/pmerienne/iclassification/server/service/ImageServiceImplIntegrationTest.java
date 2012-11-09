package com.pmerienne.iclassification.server.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pmerienne.iclassification.server.IntegrationTest;
import com.pmerienne.iclassification.server.repository.FileRepository;
import com.pmerienne.iclassification.server.repository.ImageRepository;
import com.pmerienne.iclassification.shared.model.ImageMetadata;

public class ImageServiceImplIntegrationTest extends IntegrationTest {

	private final static File ZIP_FILE = new File("src/test/resources/data/dataset-min.zip");

	@Autowired
	private ImageService imageService;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private FileRepository fileRepository;

	@Test
	public void testImportFromZip() throws FileNotFoundException {
		ImageMetadata expectedEgret1 = new ImageMetadata("3777402756.jpg", egret);
		ImageMetadata expectedEgret2 = new ImageMetadata("4239281284.jpg", egret);
		ImageMetadata expectedOwl1 = new ImageMetadata("791682407.jpg", owl);
		ImageMetadata expectedOwl2 = new ImageMetadata("3743525590.jpg", owl);
		ImageMetadata expectedMandarin1 = new ImageMetadata("540609095.jpg", mandarin);
		ImageMetadata expectedMandarin2 = new ImageMetadata("3655119658.jpg", mandarin);

		// Test
		List<ImageMetadata> importedFiles = this.imageService.importFromZip(this.testWorkspace, new FileInputStream(ZIP_FILE));

		// Assert that dataset contains all files
		List<ImageMetadata> allImages = this.imageRepository.findByWorkspace(this.testWorkspace);
		assertNotNull(allImages);
		assertEquals(6, allImages.size());
		assertTrue(allImages.contains(expectedEgret1));
		assertTrue(allImages.contains(expectedEgret2));
		assertTrue(allImages.contains(expectedOwl1));
		assertTrue(allImages.contains(expectedOwl2));
		assertTrue(allImages.contains(expectedMandarin1));
		assertTrue(allImages.contains(expectedMandarin2));
		
		List<ImageMetadata> egretImages = this.imageRepository.findByWorkspaceAndLabel(this.testWorkspace, this.egret);
		assertTrue(egretImages.contains(expectedEgret1));
		assertTrue(egretImages.contains(expectedEgret2));

		// Assert that all files are imported
		assertNotNull(importedFiles);
		assertEquals(6, importedFiles.size());
		assertTrue(importedFiles.contains(expectedEgret1));
		assertTrue(importedFiles.contains(expectedEgret2));
		assertTrue(importedFiles.contains(expectedOwl1));
		assertTrue(importedFiles.contains(expectedOwl2));
		assertTrue(importedFiles.contains(expectedMandarin1));
		assertTrue(importedFiles.contains(expectedMandarin2));

		// Assert that all imagefiles are stored
		List<ImageMetadata> allFiles = this.imageRepository.findAll();
		assertNotNull(allFiles);
		assertEquals(6, allFiles.size());
		assertTrue(allFiles.contains(expectedEgret1));
		assertTrue(allFiles.contains(expectedEgret2));
		assertTrue(allFiles.contains(expectedOwl1));
		assertTrue(allFiles.contains(expectedOwl2));
		assertTrue(allFiles.contains(expectedMandarin1));
		assertTrue(allFiles.contains(expectedMandarin2));

		// Assert that all files are stored
		for (ImageMetadata imageFile : allFiles) {
			assertTrue(this.fileRepository.contains(imageFile.getFilename()));
		}
	}

}
