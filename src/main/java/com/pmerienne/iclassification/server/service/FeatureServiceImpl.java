package com.pmerienne.iclassification.server.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pmerienne.iclassification.server.core.FeatureExtractor;
import com.pmerienne.iclassification.server.core.FeatureImage;
import com.pmerienne.iclassification.server.core.HSVColorFeatureExtractor;
import com.pmerienne.iclassification.server.core.RGBColorFeatureExtractor;
import com.pmerienne.iclassification.server.core.SiftFeatureExtractor;
import com.pmerienne.iclassification.server.core.SurfFeatureExtractor;
import com.pmerienne.iclassification.server.repository.FeatureImageRepository;
import com.pmerienne.iclassification.server.repository.FeatureRepository;
import com.pmerienne.iclassification.server.repository.FileRepository;
import com.pmerienne.iclassification.server.repository.ImageRepository;
import com.pmerienne.iclassification.shared.model.Feature;
import com.pmerienne.iclassification.shared.model.FeatureConfiguration;
import com.pmerienne.iclassification.shared.model.FeatureType;
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
	private SurfFeatureExtractor sufFeatureExtractor;

	@Autowired
	private RGBColorFeatureExtractor rgbColorFeatureExtractor;

	@Autowired
	private HSVColorFeatureExtractor hsvColorFeatureExtractor;

	@Autowired
	private ImageService imageService;

	@Autowired
	private FeatureImageRepository featureImageRepository;

	@Override
	public List<Feature> getFeatures(ImageMetadata imageMetadata, FeatureConfiguration fc) {
		List<Feature> features = null;

		try {
			// Load image from database
			imageMetadata = this.imageRepository.findOne(imageMetadata.getFilename());
			File file = this.fileRepository.get(imageMetadata.getFilename());
			features = this.featureRepository.findByFilenameAndTypeAndUseCropZone(imageMetadata.getFilename(), fc.getType(), fc.isUseCropZone());

			// If the features doesn't exists, we compute it!
			if (features == null || features.isEmpty()) {
				LOGGER.info("Computing features (" + fc + ") for " + imageMetadata);

				// Segment image if needed
				if (fc.isUseCropZone()) {
					file = this.imageService.getSegmentedFile(imageMetadata);
				}

				features = this.computeFeatures(file, fc);

				// Save image features
				for (Feature feature : features) {
					feature.setFilename(imageMetadata.getFilename());
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
		// Get extractor according to feature type
		FeatureExtractor extractor = this.getFeatureExtractor(featureConfiguration.getType());

		// Extract and retur features
		List<Feature> features = extractor.getFeatures(image);
		return features;
	}

	@Override
	public File getFeatureFile(ImageMetadata imageMetadata, FeatureType featureType, boolean useCropZone) throws IOException {
		File featureFile = null;

		FeatureImage featureImage = this.featureImageRepository.findByOriginalImageAndFeatureTypeAndUseCropZone(imageMetadata, featureType, useCropZone);
		if (featureFile == null) {
			File inputFile = this.fileRepository.get(imageMetadata.getFilename());

			// Create feature file
			FeatureExtractor extractor = this.getFeatureExtractor(featureType);
			featureFile = extractor.createFeatureImage(inputFile);

			String filename = this.fileRepository.save(featureFile);
			featureImage = new FeatureImage(filename, imageMetadata, featureType, useCropZone);
			this.featureImageRepository.save(featureImage);
		}

		featureFile = this.fileRepository.get(featureImage.getFilename());

		return featureFile;
	}

	@Override
	public void clearFeatures(ImageMetadata imageMetadata) {
		String filename = imageMetadata.getFilename();

		// Remove calculated features
		List<Feature> features = this.featureRepository.findByFilename(filename);
		if (features != null && !features.isEmpty()) {
			this.featureRepository.delete(features);
		}

		// Remove calculated feature images
		List<FeatureImage> featureImages = this.featureImageRepository.findByOriginalImage(imageMetadata);
		for (FeatureImage featureImage : featureImages) {
			this.fileRepository.delete(featureImage.getFilename());
			this.featureImageRepository.delete(featureImage);
		}
	}

	protected FeatureExtractor getFeatureExtractor(FeatureType featureType) {
		FeatureExtractor extractor = null;
		switch (featureType) {
		case SIFT:
			extractor = this.siftFeatureExtractor;
			break;
		case SURF:
			extractor = this.sufFeatureExtractor;
			break;
		case RGB_COLOR:
			extractor = this.rgbColorFeatureExtractor;
			break;
		case HSV_COLOR:
			extractor = this.hsvColorFeatureExtractor;
			break;
		}

		return extractor;
	}
}
