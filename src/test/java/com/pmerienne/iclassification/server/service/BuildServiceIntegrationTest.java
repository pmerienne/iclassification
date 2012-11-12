package com.pmerienne.iclassification.server.service;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.pmerienne.iclassification.server.IntegrationTest;
import com.pmerienne.iclassification.server.repository.ImageLabelRepository;
import com.pmerienne.iclassification.shared.model.Build;
import com.pmerienne.iclassification.shared.model.FeatureConfiguration;
import com.pmerienne.iclassification.shared.model.FeatureType;
import com.pmerienne.iclassification.shared.model.ImageLabel;

public class BuildServiceIntegrationTest extends IntegrationTest {

	private final static File ZIP_FILE = new File("src/test/resources/data/dataset-flowers.min.zip");
	// private final static File ZIP_FILE = new
	// File("src/test/resources/data/dataset-flowers.zip");
	// private final static File ZIP_FILE = new
	// File("src/test/resources/data/dataset-flowers.segmented.zip");

	@Autowired
	private BuildService buildService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private ImageLabelRepository imageLabelRepository;

	@Before
	public void setupDataset() throws Exception {
		this.imageLabelRepository.deleteAll();

		// Add image label
		ImageLabel bluebell = new ImageLabel("bluebell", "bluebell");
		ImageLabel crocus = new ImageLabel("crocus", "crocus");
		ImageLabel daffodil = new ImageLabel("daffodil", "daffodil");
		ImageLabel iris = new ImageLabel("iris", "iris");
		ImageLabel lilyvalley = new ImageLabel("lilyvalley", "lilyvalley");
		ImageLabel snowdrop = new ImageLabel("snowdrop", "snowdrop");
		ImageLabel tiggerlily = new ImageLabel("tiggerlily", "tiggerlily");
		this.imageLabelRepository.save(Arrays
				.asList(bluebell, crocus, daffodil, iris, lilyvalley, snowdrop, tiggerlily));

		// Load images
		this.imageService.importFromZip(this.testWorkspace, new FileInputStream(ZIP_FILE));
	}

	@Test
	public void bigTest() throws InterruptedException {
		List<FeatureConfiguration> featureConfigurations = new ArrayList<FeatureConfiguration>();
		featureConfigurations.add(new FeatureConfiguration(FeatureType.SIFT, true, 80));

		Build build = this.buildService.createBuild(this.testWorkspace, featureConfigurations);
		assertNotNull(build);

		// Wait 3 minutes
		 Thread.sleep(3 * 60 * 1000);
	}
}
