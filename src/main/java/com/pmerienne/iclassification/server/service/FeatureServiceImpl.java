package com.pmerienne.iclassification.server.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pmerienne.iclassification.server.core.FeatureExtractor;
import com.pmerienne.iclassification.server.core.ImageSegmenter;
import com.pmerienne.iclassification.server.core.SiftFeatureExtractor;
import com.pmerienne.iclassification.server.repository.FeatureRepository;
import com.pmerienne.iclassification.server.repository.FileRepository;
import com.pmerienne.iclassification.server.repository.ImageRepository;
import com.pmerienne.iclassification.shared.model.CropZone;
import com.pmerienne.iclassification.shared.model.Feature;
import com.pmerienne.iclassification.shared.model.FeatureConfiguration;
import com.pmerienne.iclassification.shared.model.ImageMetadata;

@Service
public class FeatureServiceImpl implements FeatureService {

	private final static Logger LOGGER = Logger.getLogger(FeatureServiceImpl.class);

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private FeatureRepository featureRepository;

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private SiftFeatureExtractor siftFeatureExtractor;

	@Autowired
	private ImageSegmenter imageSegmenter;

	@Override
	public List<Feature> getFeatures(ImageMetadata imageMetadata, FeatureConfiguration fc) {
		List<Feature> features = null;

		try {
			// Load image from database
			imageMetadata = this.imageRepository.findOne(imageMetadata.getFilename());
			File file = this.fileRepository.get(imageMetadata.getFilename());

			features = this.featureRepository.findByImageMetadataAndTypeAndUseCropZone(imageMetadata, fc.getType(), fc.isUseCropZone());

			// If the features doesn't exists, we compute it!
			if (features == null) {
				LOGGER.info("Computing features (" + fc + ") for " + imageMetadata);

				// Segment image if needed
				if (fc.isUseCropZone()) {
					CropZone cropZone = imageMetadata.getCropZone();
					file = this.imageSegmenter.segment(file, cropZone);
				}

				features = this.computeFeatures(file, fc);

				// Save image features
				for (Feature feature : features) {
					feature.setImageMetadata(imageMetadata);
					feature.setType(fc.getType());
					feature.setUseCropZone(fc.isUseCropZone());
				}
				this.featureRepository.save(features);
			}
		} catch (IOException ioe) {
			throw new RuntimeException("Unable to get image features", ioe);
		}

		return features;
	}

	@Override
	public List<Feature> computeFeatures(File image, FeatureConfiguration featureConfiguration) {
		List<Feature> features = null;

		// Get extractor according to feature type
		FeatureExtractor extractor = null;
		switch (featureConfiguration.getType()) {
		case SIFT:
			extractor = this.siftFeatureExtractor;
			break;
		}

		// Extract and retur features
		features = extractor.getFeatures(image);
		return features;
	}

	@Override
	public void clearFeatures(ImageMetadata imageMetadata) {
		List<Feature> features = this.featureRepository.findByImageMetadata(imageMetadata);
		this.featureRepository.delete(features);
	}
}
