package com.pmerienne.iclassification.server.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.engine.DoGSIFTEngineOptions;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.springframework.stereotype.Component;

import com.pmerienne.iclassification.server.util.FeatureUtils;
import com.pmerienne.iclassification.shared.model.Feature;

@Component
public class SiftFeatureExtractor implements FeatureExtractor {

	@Override
	public List<Feature> getFeatures(File file) {
		List<Feature> features = null;
		try {
			InputStream is = new FileInputStream(file);

			LocalFeatureList<Keypoint> siftKeypoints = this.getSiftFeatures(is);
			features = FeatureUtils.keypointsToFeatures(siftKeypoints);
		} catch (IOException ioe) {
			throw new IllegalArgumentException("Unable to get sift features.", ioe);
		}
		return features;
	}

	private LocalFeatureList<Keypoint> getSiftFeatures(InputStream is) throws IOException {
		// Read image in OpenIMAJ format
		MBFImage image = ImageUtilities.readMBF(is);

		// Configure sift engine
		DoGSIFTEngineOptions<FImage> options = new DoGSIFTEngineOptions<FImage>();
		DoGSIFTEngine siftEngine = new DoGSIFTEngine(options);

		// Extract keypoints
		LocalFeatureList<Keypoint> imageKeypoints = siftEngine.findFeatures(Transforms.calculateIntensityNTSC(image));
		return imageKeypoints;
	}
}
