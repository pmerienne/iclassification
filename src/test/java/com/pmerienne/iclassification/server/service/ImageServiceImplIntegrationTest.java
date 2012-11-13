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

	private final static File ZIP_FILE = new File("src/test/resources/data/dataset-flowers.min.zip");

	@Autowired
	private ImageService imageService;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private FileRepository fileRepository;

	@Test
	public void testImportFromZip() throws FileNotFoundException {
		ImageMetadata expectedCrocus1 = new ImageMetadata("3473334861.jpg", this.crocus);
		ImageMetadata expectedCrocus2 = new ImageMetadata("3787308169.jpg", this.crocus);
		ImageMetadata expectedCrocus3 = new ImageMetadata("1432183464.jpg", this.crocus);
		ImageMetadata expectedCrocus4 = new ImageMetadata("1829452279.jpg", this.crocus);

		ImageMetadata expectedBluebell1 = new ImageMetadata("1696632932.jpg", this.bluebell);
		ImageMetadata expectedBluebell2 = new ImageMetadata("3128781090.jpg", this.bluebell);
		ImageMetadata expectedBluebell3 = new ImageMetadata("2856072647.jpg", this.bluebell);
		ImageMetadata expectedBluebell4 = new ImageMetadata("3430063913.jpg", this.bluebell);

		ImageMetadata expectedSnowdrop1 = new ImageMetadata("1630243156.jpg", this.snowdrop);
		ImageMetadata expectedSnowdrop2 = new ImageMetadata("285473913.jpg", this.snowdrop);
		ImageMetadata expectedSnowdrop3 = new ImageMetadata("1622193758.jpg", this.snowdrop);
		ImageMetadata expectedSnowdrop4 = new ImageMetadata("649904515.jpg", this.snowdrop);

		// Test
		List<ImageMetadata> importedFiles = this.imageService.importFromZip(this.testWorkspace, new FileInputStream(ZIP_FILE));

		// Assert that dataset contains all files
		List<ImageMetadata> allImages = this.imageRepository.findByWorkspaceId(this.testWorkspace.getId());
		assertNotNull(allImages);
		assertEquals(12, allImages.size());
		assertTrue(allImages.contains(expectedCrocus1));
		assertTrue(allImages.contains(expectedCrocus2));
		assertTrue(allImages.contains(expectedCrocus3));
		assertTrue(allImages.contains(expectedCrocus4));
		assertTrue(allImages.contains(expectedBluebell1));
		assertTrue(allImages.contains(expectedBluebell2));
		assertTrue(allImages.contains(expectedBluebell3));
		assertTrue(allImages.contains(expectedBluebell4));
		assertTrue(allImages.contains(expectedSnowdrop1));
		assertTrue(allImages.contains(expectedSnowdrop2));
		assertTrue(allImages.contains(expectedSnowdrop3));
		assertTrue(allImages.contains(expectedSnowdrop4));

		List<ImageMetadata> bluebellImages = this.imageRepository.findByWorkspaceIdAndLabel(this.testWorkspace.getId(), this.bluebell);
		assertTrue(bluebellImages.contains(expectedBluebell1));
		assertTrue(bluebellImages.contains(expectedBluebell2));
		assertTrue(bluebellImages.contains(expectedBluebell3));
		assertTrue(bluebellImages.contains(expectedBluebell4));

		// Assert that all files are imported
		assertNotNull(importedFiles);
		assertEquals(12, importedFiles.size());
		assertTrue(importedFiles.contains(expectedCrocus1));
		assertTrue(importedFiles.contains(expectedCrocus2));
		assertTrue(importedFiles.contains(expectedCrocus3));
		assertTrue(importedFiles.contains(expectedCrocus4));
		assertTrue(importedFiles.contains(expectedBluebell1));
		assertTrue(importedFiles.contains(expectedBluebell2));
		assertTrue(importedFiles.contains(expectedBluebell3));
		assertTrue(importedFiles.contains(expectedBluebell4));
		assertTrue(importedFiles.contains(expectedSnowdrop1));
		assertTrue(importedFiles.contains(expectedSnowdrop2));
		assertTrue(importedFiles.contains(expectedSnowdrop3));
		assertTrue(importedFiles.contains(expectedSnowdrop4));

		// Assert that all imagefiles are stored
		List<ImageMetadata> allFiles = this.imageRepository.findAll();
		assertNotNull(allFiles);

		// Assert that all files are stored
		for (ImageMetadata imageFile : allFiles) {
			assertTrue(this.fileRepository.contains(imageFile.getFilename()));
		}
	}

}
