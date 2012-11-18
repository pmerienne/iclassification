package com.pmerienne.iclassification.server.core;

import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ImageProcessor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import de.lmu.ifi.dbs.jfeaturelib.LibProperties;
import de.lmu.ifi.dbs.jfeaturelib.features.AbstractFeatureDescriptor;

public class AbstractJFeatureExtractor {

	private final static Logger LOGGER = Logger.getLogger(AbstractJFeatureExtractor.class);

	protected static LibProperties LIB_PROPERTIES;
	static {
		try {
			LIB_PROPERTIES = LibProperties.get();
		} catch (IOException ioe) {
			LOGGER.warn("Unable to init lib properties", ioe);
		}
	}

	protected List<double[]> getFeatures(AbstractFeatureDescriptor featureDescriptor, File file) throws IOException {
		ImageProcessor processor = this.getProcessor(file);
		featureDescriptor.setProperties(LIB_PROPERTIES);
		featureDescriptor.run(processor);
		List<double[]> features = featureDescriptor.getFeatures();
		return features;

	}

	protected ImageProcessor getProcessor(File file) {
		ImageProcessor ip = null;
		if (file != null) {
			ImagePlus iplus = new Opener().openImage(file.getAbsolutePath());
			ip = iplus.getProcessor();
		}
		return ip;
	}
}
