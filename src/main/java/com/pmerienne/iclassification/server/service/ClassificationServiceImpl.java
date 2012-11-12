package com.pmerienne.iclassification.server.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pmerienne.iclassification.server.core.ClassificationException;
import com.pmerienne.iclassification.server.core.Dataset;
import com.pmerienne.iclassification.server.core.DictionaryResponsesClassifier;
import com.pmerienne.iclassification.shared.model.ClassificationEvaluation;
import com.pmerienne.iclassification.shared.model.Dictionary;
import com.pmerienne.iclassification.shared.model.ImageLabel;
import com.pmerienne.iclassification.shared.model.ImageMetadata;

@Service
public class ClassificationServiceImpl implements ClassificationService {

	private final static Logger LOGGER = Logger.getLogger(ClassificationServiceImpl.class);

	@Autowired
	private DictionaryService dictionaryService;

	@Override
	public ClassificationEvaluation evaluate(Dataset dataset, List<Dictionary> dictionaries) throws ClassificationException {
		LOGGER.info("Evaluating classification with " + dictionaries.size() + " dictionaries");

		// Separate training and eval
		List<ImageMetadata> training = dataset.getTrainingImages();
		List<ImageMetadata> eval = dataset.getEvalImages();

		// Init classifier
		DictionaryResponsesClassifier classifier = new DictionaryResponsesClassifier(dictionaries);

		// Train classifier
		LOGGER.info("Training classifier with " + training.size() + " images");
		Map<Dictionary, Double> dictionaryResponses;
		for (ImageMetadata trainingMetadata : training) {
			dictionaryResponses = this.dictionaryService.getDictionaryResponses(dictionaries, trainingMetadata);
			classifier.addToTrainingSet(trainingMetadata.getLabel(), dictionaryResponses);
		}
		classifier.train();

		// Eval classifier
		LOGGER.info("Evaluating classifier with " + eval.size() + " images");
		ClassificationEvaluation evaluation = new ClassificationEvaluation();
		for (ImageMetadata evalMetadata : eval) {
			dictionaryResponses = this.dictionaryService.getDictionaryResponses(dictionaries, evalMetadata);

			ImageLabel expectedLabel = evalMetadata.getLabel();
			ImageLabel actualLabel = classifier.classify(dictionaryResponses);
			evaluation.update(actualLabel, expectedLabel);
		}

		LOGGER.info("Evaluation done : " + evaluation);
		return evaluation;
	}
}
