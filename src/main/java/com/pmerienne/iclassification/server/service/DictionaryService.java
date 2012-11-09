package com.pmerienne.iclassification.server.service;

import java.util.List;
import java.util.Map;

import com.pmerienne.iclassification.shared.model.Dictionary;
import com.pmerienne.iclassification.shared.model.FeatureConfiguration;
import com.pmerienne.iclassification.shared.model.ImageLabel;
import com.pmerienne.iclassification.shared.model.ImageMetadata;

public interface DictionaryService {

	/**
	 * Creates (and saves) a {@link Dictionary} for a
	 * {@link FeatureConfiguration} and an {@link ImageLabel}.
	 * 
	 * @param featureConfiguration
	 *            {@link FeatureConfiguration} used to create "words"
	 * @param imageLabel
	 *            {@link Dictionary} class
	 * @param imageMetadatas
	 *            images from which "words" are extracted
	 * @return
	 */
	Dictionary createDictionary(FeatureConfiguration featureConfiguration, ImageLabel imageLabel, List<ImageMetadata> imageMetadatas);

	/**
	 * Calculates an image histogram response for a list of dictionaries.
	 * 
	 * @param dictionaries
	 * @param imageMetadata
	 * @return
	 */
	Map<Dictionary, Double> getDictionaryResponses(List<Dictionary> dictionaries, ImageMetadata imageMetadata);
}
