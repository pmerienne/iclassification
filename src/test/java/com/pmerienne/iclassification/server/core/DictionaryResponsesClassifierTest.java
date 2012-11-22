package com.pmerienne.iclassification.server.core;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;

import com.pmerienne.iclassification.shared.model.ClassificationEvaluation;
import com.pmerienne.iclassification.shared.model.Dictionary;
import com.pmerienne.iclassification.shared.model.FeatureConfiguration;
import com.pmerienne.iclassification.shared.model.FeatureType;
import com.pmerienne.iclassification.shared.model.ImageLabel;

public class DictionaryResponsesClassifierTest {

	@Test
	public void testFull() throws ClassificationException {
		// Dictionaries data
		ImageLabel label1 = new ImageLabel("label1", "label1");
		ImageLabel label2 = new ImageLabel("label2", "label2");
		ImageLabel label3 = new ImageLabel("label3", "label3");
		ImageLabel label4 = new ImageLabel("label4", "label4");
		ImageLabel label5 = new ImageLabel("label5", "label5");
		ImageLabel label6 = new ImageLabel("label6", "label6");
		ImageLabel label7 = new ImageLabel("label7", "label7");
		List<ImageLabel> labels = Arrays.asList(label1, label2, label3, label4, label5, label6, label7);

		FeatureConfiguration sift = new FeatureConfiguration(FeatureType.SIFT, true, 700);
		FeatureConfiguration hsv = new FeatureConfiguration(FeatureType.HSV_COLOR, true, 20);
		FeatureConfiguration rgb = new FeatureConfiguration(FeatureType.RGB_COLOR, true, 40);
		List<FeatureConfiguration> fcs = Arrays.asList(sift, hsv, rgb);

		List<Dictionary> dictionaries = new ArrayList<Dictionary>();
		for (ImageLabel label : labels) {
			for (FeatureConfiguration fc : fcs) {
				Dictionary dictionary = new Dictionary(label, fc, null);
				dictionaries.add(dictionary);
			}
		}

		// Create classifier
		DictionaryResponsesClassifier classifier = new DictionaryResponsesClassifier(dictionaries);

		// Add training data
		for (ImageLabel label : labels) {
			for (int i = 0; i < 10; i++) {
				Map<Dictionary, Double> responses = this.getResponses(label, dictionaries, 1);
				classifier.addToTrainingSet(label, responses);
			}
		}

		// Train classifier
		classifier.train();

		// Evaluate
		ClassificationEvaluation eval = new ClassificationEvaluation();
		for (ImageLabel label : labels) {
			for (int i = 0; i < 7; i++) {
				Map<Dictionary, Double> responses = this.getResponses(label, dictionaries, 1.6);
				ImageLabel actualLabel = classifier.classify(responses);
				eval.update(actualLabel, label);
			}
		}

		// Assert success rate > 80%
		System.out.println(eval.successRate());
		assertTrue(eval.successRate() > 80);
	}

	protected Map<Dictionary, Double> getResponses(ImageLabel label, List<Dictionary> dictionaries, double noise) {
		Map<Dictionary, Double> responses = new HashMap<Dictionary, Double>();
		Random random = new Random();

		for (Dictionary dictionary : dictionaries) {
			if (label.equals(dictionary.getImageLabel())) {
				responses.put(dictionary, 1 + random.nextDouble() * noise);
			} else {
				responses.put(dictionary, 0 + random.nextDouble() * noise);
			}
		}

		return responses;
	}
}
