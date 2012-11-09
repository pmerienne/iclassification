package com.pmerienne.iclassification.shared.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pmerienne.iclassification.shared.model.ClassificationEvaluation;
import com.pmerienne.iclassification.shared.model.ImageLabel;

public class ClassificationEvaluationTest {

	@Test
	public void testUpdate() {
		ImageLabel egret = new ImageLabel("egret", "egret");
		ImageLabel owl = new ImageLabel("owl", "owl");
		ImageLabel mandarin = new ImageLabel("mandarin", "mandarin");

		ClassificationEvaluation classificationEvaluation = new ClassificationEvaluation();

		// 6 good classifications
		classificationEvaluation.update(egret, egret);
		classificationEvaluation.update(egret, egret);
		classificationEvaluation.update(owl, owl);
		classificationEvaluation.update(owl, owl);
		classificationEvaluation.update(mandarin, mandarin);
		classificationEvaluation.update(mandarin, mandarin);

		// 3 bad classification
		classificationEvaluation.update(owl, mandarin);
		classificationEvaluation.update(owl, mandarin);
		classificationEvaluation.update(owl, mandarin);

		assertEquals(Integer.valueOf(3), classificationEvaluation.getErrorCount());
		assertEquals(Integer.valueOf(6), classificationEvaluation.getSucessCount());
	}

}
