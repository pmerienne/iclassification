package com.pmerienne.iclassification.server.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import weka.classifiers.meta.MultiClassClassifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import com.pmerienne.iclassification.shared.model.Dictionary;
import com.pmerienne.iclassification.shared.model.FeatureConfiguration;
import com.pmerienne.iclassification.shared.model.ImageLabel;

public class DictionaryResponsesClassifier {

	private final static Logger LOGGER = Logger.getLogger(DictionaryResponsesClassifier.class);

	private Instances trainingDataset;
	private MultiClassClassifier classifier;

	private Set<ImageLabel> imageLabels = new HashSet<ImageLabel>();
	private Set<String> classes = new TreeSet<String>();
	private Set<String> features = new TreeSet<String>();

	public DictionaryResponsesClassifier(List<Dictionary> dictionaries) {
		// Fill features and classes
		FastVector classValues = new FastVector();
		FastVector attributes = new FastVector();
		for (Dictionary dictionary : dictionaries) {
			ImageLabel imageLabel = dictionary.getImageLabel();
			this.imageLabels.add(imageLabel);
			String className = imageLabel.getName();
			this.classes.add(className);
			classValues.addElement(className);

			String featureName = this.getFeatureName(dictionary);
			this.features.add(featureName);
			attributes.addElement(new Attribute(featureName));
		}
		attributes.addElement(new Attribute("Class", classValues));

		// Init datasets
		String trainingDatasetName = "training_dataset_" + Long.toString(System.currentTimeMillis());
		this.trainingDataset = new Instances(trainingDatasetName, attributes, 100);
		this.trainingDataset.setClassIndex(this.trainingDataset.numAttributes() - 1);

	}

	public void addToTrainingSet(ImageLabel imageLabel, Map<Dictionary, Double> dictionaryResponses) {
		Instance instance = makeInstance(dictionaryResponses);
		String className = imageLabel.getName();
		instance.setClassValue(className);

		// Add instance to training data.
		this.trainingDataset.add(instance);
	}

	public void train() throws ClassificationException {
		try {
			// Init classifier
			this.classifier = new MultiClassClassifier();
			this.classifier.buildClassifier(this.trainingDataset);
		} catch (Exception e) {
			String errorMsg = "Error while training classifier";
			LOGGER.error(errorMsg, e);
			throw new ClassificationException(errorMsg, e);
		}
	}

	public ImageLabel classify(Map<Dictionary, Double> dictionaryResponses) throws ClassificationException {
		ImageLabel predictedLabel = null;

		try {
			Instance instance = makeInstance(dictionaryResponses);

			double predicted = this.classifier.classifyInstance(instance);
			String clazz = this.trainingDataset.classAttribute().value((int) predicted);

			predictedLabel = this.findImageLabel(clazz);
		} catch (Exception e) {
			String errorMsg = "Error while classifying instance";
			LOGGER.error(errorMsg, e);
			throw new ClassificationException(errorMsg, e);
		}

		return predictedLabel;
	}

	public Map<ImageLabel, Double> classifyWithDistribution(Map<Dictionary, Double> dictionaryResponse) throws ClassificationException {
		Map<ImageLabel, Double> distribution = new HashMap<ImageLabel, Double>();

		try {
			Instance instance = makeInstance(dictionaryResponse);
			double[] distributionForInstance = this.classifier.distributionForInstance(instance);

			for (int predicted = 0; predicted < distributionForInstance.length; predicted++) {
				double probability = distributionForInstance[predicted];
				String clazz = this.trainingDataset.classAttribute().value((int) predicted);
				ImageLabel predictedLabel = this.findImageLabel(clazz);
				distribution.put(predictedLabel, probability);
			}

		} catch (Exception e) {
			String errorMsg = "Error while classifying instance";
			LOGGER.error(errorMsg, e);
			throw new ClassificationException(errorMsg, e);
		}

		return distribution;
	}

	private Instance makeInstance(Map<Dictionary, Double> dictionaryResponses) {
		Instance instance = new Instance(this.features.size() + 1);
		instance.setDataset(this.trainingDataset);

		// Set dictionary responses
		for (Dictionary dictionary : dictionaryResponses.keySet()) {
			String featureName = this.getFeatureName(dictionary);
			Integer featureIndex = new ArrayList<String>(this.features).indexOf(featureName);
			instance.setValue(featureIndex, dictionaryResponses.get(dictionary));
		}

		return instance;
	}

	private String getFeatureName(Dictionary dictionary) {
		FeatureConfiguration fc = dictionary.getFeatureConfiguration();
		ImageLabel imageLabel = dictionary.getImageLabel();

		String className = imageLabel.getName();
		String featureTypeName = fc.getType().name();
		String featureCrop = fc.isUseCropZone() ? "CROP" : "NOTCROPPED";

		return className + "_" + featureTypeName + "_" + featureCrop;
	}

	protected ImageLabel findImageLabel(String className) {
		ImageLabel label = null;

		Iterator<ImageLabel> it = this.imageLabels.iterator();
		while (label == null && it.hasNext()) {
			ImageLabel candidate = it.next();
			if (candidate.getName().equals(className)) {
				label = candidate;
			}
		}
		return label;
	}
}
