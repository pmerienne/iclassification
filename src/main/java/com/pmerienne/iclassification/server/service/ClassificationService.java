package com.pmerienne.iclassification.server.service;

import java.util.List;

import com.pmerienne.iclassification.server.core.ClassificationException;
import com.pmerienne.iclassification.server.core.Dataset;
import com.pmerienne.iclassification.shared.model.ClassificationEvaluation;
import com.pmerienne.iclassification.shared.model.Dictionary;

public interface ClassificationService {

	ClassificationEvaluation evaluate(Dataset dataset, List<Dictionary> dictionaries) throws ClassificationException;

	// ImageLabel predict(File file, Dictionary dictionary,
	// Decider<DictionaryResponse, ImageLabel> decider);
}
