package com.pmerienne.iclassification.server.core;

import java.util.ArrayList;
import java.util.List;

import com.pmerienne.iclassification.shared.model.ImageMetadata;

public class Dataset {

	private List<ImageMetadata> trainingImages = new ArrayList<ImageMetadata>();

	private List<ImageMetadata> evalImages = new ArrayList<ImageMetadata>();

	public Dataset() {
		super();
	}

	public Dataset(List<ImageMetadata> trainingImages, List<ImageMetadata> evalImages) {
		super();
		this.trainingImages = trainingImages;
		this.evalImages = evalImages;
	}

	public List<ImageMetadata> getAllImages() {
		List<ImageMetadata> imageMetadatas = new ArrayList<ImageMetadata>();
		imageMetadatas.addAll(this.trainingImages);
		imageMetadatas.addAll(this.evalImages);
		return imageMetadatas;
	}

	public List<ImageMetadata> getTrainingImages() {
		return trainingImages;
	}

	public void setTrainingImages(List<ImageMetadata> trainingImages) {
		this.trainingImages = trainingImages;
	}

	public List<ImageMetadata> getEvalImages() {
		return evalImages;
	}

	public void setEvalImages(List<ImageMetadata> evalImages) {
		this.evalImages = evalImages;
	}

}
