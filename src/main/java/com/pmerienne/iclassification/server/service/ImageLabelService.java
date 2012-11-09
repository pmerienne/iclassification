package com.pmerienne.iclassification.server.service;

import java.util.List;

import com.pmerienne.iclassification.shared.model.ImageLabel;

public interface ImageLabelService {

	List<ImageLabel> findAll();

	ImageLabel findByName(String name);

	ImageLabel findById(String id);

	ImageLabel save(ImageLabel imageLabel);
	
	void delete(ImageLabel imageLabel);
}
