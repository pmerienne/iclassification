package com.pmerienne.iclassification.server.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pmerienne.iclassification.server.core.Dataset;
import com.pmerienne.iclassification.server.repository.ImageRepository;
import com.pmerienne.iclassification.shared.model.ImageMetadata;
import com.pmerienne.iclassification.shared.model.Workspace;

@Service
public class DatasetServiceImpl implements DatasetService {

	private final static Logger LOGGER = Logger.getLogger(DatasetServiceImpl.class);

	@Autowired
	private ImageRepository imageRepository;

	@Override
	public Dataset createDataset(Workspace workspace, int percent) {
		List<ImageMetadata> allFiles = this.imageRepository.findByWorkspaceId(workspace.getId());
		Collections.shuffle(allFiles);

		List<ImageMetadata> trainingImages = new ArrayList<ImageMetadata>();
		List<ImageMetadata> evaluationsImages = new ArrayList<ImageMetadata>();
		this.splitDataSet(percent, allFiles, trainingImages, evaluationsImages);

		Dataset dataset = new Dataset(trainingImages, evaluationsImages);
		LOGGER.info("Dataset created (training : " + trainingImages.size() + ", evaluation : " + evaluationsImages.size() + ")");

		return dataset;
	}

	private void splitDataSet(double trainingPercent, List<ImageMetadata> files, List<ImageMetadata> trainingFiles, List<ImageMetadata> evalFiles) {
		Long splitIndex = Math.round(files.size() * trainingPercent);
		trainingFiles.addAll(files.subList(0, splitIndex.intValue()));
		evalFiles.addAll(files.subList(splitIndex.intValue(), files.size()));
	}
}
