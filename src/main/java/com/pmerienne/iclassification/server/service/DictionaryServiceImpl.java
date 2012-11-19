package com.pmerienne.iclassification.server.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.pmerienne.iclassification.server.core.KMeans;
import com.pmerienne.iclassification.server.repository.DictionaryRepository;
import com.pmerienne.iclassification.server.util.FeatureUtils;
import com.pmerienne.iclassification.shared.model.Dictionary;
import com.pmerienne.iclassification.shared.model.Feature;
import com.pmerienne.iclassification.shared.model.FeatureConfiguration;
import com.pmerienne.iclassification.shared.model.ImageLabel;
import com.pmerienne.iclassification.shared.model.ImageMetadata;

@Service
public class DictionaryServiceImpl implements DictionaryService {

	private final static Logger LOGGER = Logger.getLogger(DictionaryServiceImpl.class);

	@Autowired
	private DictionaryRepository dictionaryRepository;

	@Autowired
	private FeatureService featureService;

	@Override
	public Dictionary createDictionary(FeatureConfiguration featureConfiguration, ImageLabel imageLabel, List<ImageMetadata> imageMetadatas) {
		// Check image metadata
		for (ImageMetadata imageMetadata : imageMetadatas) {
			if (!imageLabel.equals(imageMetadata.getLabel())) {
				throw new IllegalArgumentException(imageMetadata + " isn't a " + imageLabel);
			}
		}

		// Create vocabulary
		LOGGER.info("Extracting vocabulary (" + featureConfiguration + ") from " + imageMetadatas.size() + " images.");
		double[][] vocabulary = this.createVocabulary(featureConfiguration, imageMetadatas);

		// Cluster vocabulary to make dictionary
		int dictionarySize = featureConfiguration.getDictionarySize();
		LOGGER.info("Clustering " + vocabulary.length + " features into " + dictionarySize + " words");

		KMeans kMeans = new KMeans(vocabulary, dictionarySize);
		double[][] rawCentroids = kMeans.cluster();

		// Create and save dictionary
		List<Feature> centroids = FeatureUtils.toFeatureList(rawCentroids);
		Dictionary dictionary = new Dictionary(imageLabel, featureConfiguration, centroids);
		this.dictionaryRepository.save(dictionary);

		return dictionary;
	}

	@Override
	public Map<Dictionary, Double> getDictionaryResponses(List<Dictionary> dictionaries, ImageMetadata imageMetadata) {
		Map<Dictionary, Double> dictionaryResponses = new HashMap<Dictionary, Double>();

		Multimap<FeatureConfiguration, Dictionary> mappedDictionaries = this.mapDictionariesByFeatureConfiguration(dictionaries);

		// Fetch dictionary responses for each feature configuration
		for (FeatureConfiguration fc : mappedDictionaries.keySet()) {
			Collection<Dictionary> featureDictionaries = mappedDictionaries.get(fc);
			Map<Dictionary, Double> featureResponses = this.getFeatureResponses(featureDictionaries, imageMetadata);
			dictionaryResponses.putAll(featureResponses);
		}

		return dictionaryResponses;
	}

	protected Map<Dictionary, Double> getFeatureResponses(Collection<Dictionary> dictionaries, ImageMetadata imageMetadata) {
		FeatureConfiguration featureConfiguration = dictionaries.iterator().next().getFeatureConfiguration();

		// Init response
		Map<Dictionary, Double> bowHistogram = new HashMap<Dictionary, Double>();
		for (Dictionary dictionary : dictionaries) {
			bowHistogram.put(dictionary, 0.0);
		}

		// Get image features
		List<Feature> imageFeatures = this.featureService.getFeatures(imageMetadata, featureConfiguration);
		double[][] features = FeatureUtils.toArray(imageFeatures);

		// Load all cluster centroids
		List<Feature> allCentroids = new ArrayList<Feature>();
		Map<Integer, Dictionary> clusterIndex = new HashMap<Integer, Dictionary>();
		for (Dictionary dictionary : dictionaries) {
			for (Feature centroid : dictionary.getCentroids()) {
				clusterIndex.put(allCentroids.size(), dictionary);
				allCentroids.add(centroid);
			}
		}
		double[][] centroids = FeatureUtils.toArray(allCentroids);

		// Assign features to centroids
		KMeans kMeans = new KMeans(centroids);
		int[] assignedClusterIndexes = kMeans.assign(features);

		// Count number of assigned feature by dictionary
		for (int assignedClusterIndex : assignedClusterIndexes) {
			Dictionary assignedDictionary = clusterIndex.get(assignedClusterIndex);
			bowHistogram.put(assignedDictionary, bowHistogram.get(assignedDictionary) + 1);
		}

		// Normalize
		for (Dictionary dictionary : bowHistogram.keySet()) {
			bowHistogram.put(dictionary, bowHistogram.get(dictionary) / assignedClusterIndexes.length);
		}

		return bowHistogram;
	}

	protected double[][] createVocabulary(FeatureConfiguration featureConfiguration, List<ImageMetadata> imageMetadatas) {
		double[][] vocabulary = null;
		List<Feature> allFeatures = new ArrayList<Feature>();
		List<Feature> imageFeatures = null;

		// Fetch all features
		for (ImageMetadata imageMetadata : imageMetadatas) {
			imageFeatures = this.featureService.getFeatures(imageMetadata, featureConfiguration);
			if (imageFeatures != null && !imageFeatures.isEmpty()) {
				allFeatures.addAll(imageFeatures);
			}
		}

		// Convert to byte 2d array
		vocabulary = FeatureUtils.toArray(allFeatures);
		return vocabulary;
	}

	protected Multimap<FeatureConfiguration, Dictionary> mapDictionariesByFeatureConfiguration(List<Dictionary> dictionaries) {
		Multimap<FeatureConfiguration, Dictionary> mappedDictionaries = ArrayListMultimap.create();
		for (Dictionary dictionary : dictionaries) {
			mappedDictionaries.put(dictionary.getFeatureConfiguration(), dictionary);
		}
		return mappedDictionaries;
	}
}
