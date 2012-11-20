package com.pmerienne.iclassification.server.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.pmerienne.iclassification.server.core.Dataset;
import com.pmerienne.iclassification.server.core.SameLabelPredicate;
import com.pmerienne.iclassification.server.repository.BuildRepository;
import com.pmerienne.iclassification.server.repository.ImageRepository;
import com.pmerienne.iclassification.shared.model.Build;
import com.pmerienne.iclassification.shared.model.Build.State;
import com.pmerienne.iclassification.shared.model.ClassificationEvaluation;
import com.pmerienne.iclassification.shared.model.Dictionary;
import com.pmerienne.iclassification.shared.model.FeatureConfiguration;
import com.pmerienne.iclassification.shared.model.ImageLabel;
import com.pmerienne.iclassification.shared.model.ImageMetadata;
import com.pmerienne.iclassification.shared.model.Workspace;

@Service
public class BuildServiceImpl implements BuildService {

	private final static Integer MAX_PARALLEL_BUILD = 1;

	private ExecutorService executorService = Executors.newFixedThreadPool(MAX_PARALLEL_BUILD);

	private final static Logger LOGGER = Logger.getLogger(BuildServiceImpl.class);

	@Autowired
	private BuildRepository buildRepository;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private DictionaryService dictionaryService;

	@Autowired
	private ClassificationService classificationService;

	@Autowired
	private WorkspaceService workspaceService;

	@Autowired
	private ImageLabelService imageLabelService;

	@Override
	public List<Build> findAll() {
		return this.buildRepository.findAll();
	}

	@Override
	public Build findById(String id) {
		return this.buildRepository.findOne(id);
	}

	@Override
	public List<Build> findByWorkspace(Workspace workspace) {
		String workspaceId = workspace.getId();
		List<Build> builds = this.buildRepository.findByWorkspaceId(workspaceId);
		return builds;
	}

	@Override
	public void delete(Build build) {
		this.buildRepository.delete(build);
	}

	@Override
	public Build createBuild(final Workspace workspace, final List<FeatureConfiguration> featureConfigurations) {
		final Build build = new Build(workspace, featureConfigurations);
		this.buildRepository.save(build);

		this.executorService.execute(new Runnable() {
			@Override
			public void run() {
				LOGGER.info("Start building " + build);
				Dataset dataset = BuildServiceImpl.this.createDataset(workspace, 0.8);

				LOGGER.info("Creating dictionaries");
				createDictionaries(build, dataset);

				LOGGER.info("Training and evaluating classifier");
				evaluateClassifier(build, dataset);
			}
		});

		return build;
	}

	protected void createDictionaries(Build build, Dataset dataset) {
		build.setState(State.CREATING_DICTIONARIES);
		this.buildRepository.save(build);

		List<ImageLabel> availableLabels = this.imageLabelService.findAll();
		List<ImageMetadata> training = dataset.getTrainingImages();
		List<Dictionary> dictionaries = new ArrayList<Dictionary>();

		// Build a dictionary for each label/feature pair
		for (FeatureConfiguration fc : build.getFeatureConfigurations()) {
			for (ImageLabel Label : availableLabels) {
				List<ImageMetadata> imagesWithLabel = Lists.newArrayList(Iterables.filter(training,
						new SameLabelPredicate(Label)));
				if (imagesWithLabel != null && !imagesWithLabel.isEmpty()) {
					Dictionary dictionary = this.dictionaryService.createDictionary(fc, Label, imagesWithLabel);
					dictionaries.add(dictionary);
				}
			}
		}

		build.setDictionaries(dictionaries);
		build.setState(State.WAITING);
		this.buildRepository.save(build);
	}

	protected void evaluateClassifier(Build build, Dataset dataset) {
		List<Dictionary> dictionaries = build.getDictionaries();

		try {
			build.setState(State.EVALUATING);
			this.buildRepository.save(build);

			ClassificationEvaluation evaluation = this.classificationService.evaluate(dataset, dictionaries);
			build.setClassificationEvaluation(evaluation);

			build.setState(State.FINISH);
			this.buildRepository.save(build);

		} catch (Exception e) {
			LOGGER.error("An error occured while evaluating classifier : " + e.getMessage(), e);
			build.setState(State.FAILED);
			this.buildRepository.save(build);
		}
	}

	protected Dataset createDataset(Workspace workspace, double percent) {
		List<ImageMetadata> allFiles = this.imageRepository.findByWorkspaceId(workspace.getId());
		Collections.shuffle(allFiles);

		List<ImageMetadata> trainingImages = new ArrayList<ImageMetadata>();
		List<ImageMetadata> evaluationsImages = new ArrayList<ImageMetadata>();

		Long splitIndex = Math.round(allFiles.size() * percent);
		trainingImages.addAll(allFiles.subList(0, splitIndex.intValue()));
		evaluationsImages.addAll(allFiles.subList(splitIndex.intValue(), allFiles.size()));

		Dataset dataset = new Dataset(trainingImages, evaluationsImages);
		LOGGER.info("Dataset created (training : " + trainingImages.size() + ", evaluation : "
				+ evaluationsImages.size() + ")");

		return dataset;
	}

	public void setBuildRepository(BuildRepository buildRepository) {
		this.buildRepository = buildRepository;
	}

	public void setDictionaryService(DictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}

	public void setClassificationService(ClassificationService classificationService) {
		this.classificationService = classificationService;
	}

	public void setWorkspaceService(WorkspaceService workspaceService) {
		this.workspaceService = workspaceService;
	}

	public void setImageLabelService(ImageLabelService imageLabelService) {
		this.imageLabelService = imageLabelService;
	}

}
