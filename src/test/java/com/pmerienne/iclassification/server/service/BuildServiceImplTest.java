package com.pmerienne.iclassification.server.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.pmerienne.iclassification.server.core.ClassificationException;
import com.pmerienne.iclassification.server.core.Dataset;
import com.pmerienne.iclassification.server.repository.BuildRepository;
import com.pmerienne.iclassification.shared.model.Build;
import com.pmerienne.iclassification.shared.model.Dictionary;
import com.pmerienne.iclassification.shared.model.Build.State;
import com.pmerienne.iclassification.shared.model.ClassificationEvaluation;

public class BuildServiceImplTest {

	private BuildServiceImpl buildServiceImpl;

	private BuildRepository buildRepository;
	private DatasetService datasetService;
	private DictionaryService dictionaryService;
	private ClassificationService classificationService;
	private WorkspaceService workspaceService;
	private ImageLabelService imageLabelService;

	@Before
	public void setup() {
		buildServiceImpl = new BuildServiceImpl();

		buildRepository = mock(BuildRepository.class);
		buildServiceImpl.setBuildRepository(buildRepository);

		datasetService = mock(DatasetService.class);
		buildServiceImpl.setDatasetService(datasetService);

		dictionaryService = mock(DictionaryService.class);
		buildServiceImpl.setDictionaryService(dictionaryService);

		classificationService = mock(ClassificationService.class);
		buildServiceImpl.setClassificationService(classificationService);

		workspaceService = mock(WorkspaceService.class);
		buildServiceImpl.setWorkspaceService(workspaceService);

		imageLabelService = mock(ImageLabelService.class);
		buildServiceImpl.setImageLabelService(imageLabelService);
	}

	@Test
	public void testEvaluateClassifier() throws ClassificationException {
		// Test data
		int errorCount = 12;
		int sucessCount = 54;
		ClassificationEvaluation expectedEvaluation = new ClassificationEvaluation(errorCount, sucessCount);
		Dataset dataset = new Dataset();

		List<Dictionary> dictionaries = new ArrayList<Dictionary>();
		Build build = new Build(null, null);
		build.setDictionaries(dictionaries);

		// Mock call
		when(this.buildRepository.save(build)).thenReturn(build);
		when(this.classificationService.evaluate(dataset, dictionaries)).thenReturn(expectedEvaluation);
		when(this.buildRepository.save(build)).thenReturn(build);

		// Test
		this.buildServiceImpl.evaluateClassifier(build, dataset);
		assertNotNull(build.getClassificationEvaluation());
		assertEquals(expectedEvaluation, build.getClassificationEvaluation());
		assertEquals(State.FINISH, build.getState());
	}

	@Test
	public void testEvaluateClassifierWithClassificationException() throws ClassificationException {
		// Test data
		Dataset dataset = new Dataset();

		List<Dictionary> dictionaries = new ArrayList<Dictionary>();
		Build build = new Build(null, null);
		build.setDictionaries(dictionaries);

		// Mock call
		when(this.buildRepository.save(build)).thenReturn(build);
		when(this.classificationService.evaluate(dataset, dictionaries)).thenThrow(new ClassificationException());

		// Test
		this.buildServiceImpl.evaluateClassifier(build, dataset);
		assertNull(build.getClassificationEvaluation());
		assertEquals(State.FAILED, build.getState());
	}
}
