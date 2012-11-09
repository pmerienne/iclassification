package com.pmerienne.iclassification.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pmerienne.iclassification.server.repository.ImageLabelRepository;
import com.pmerienne.iclassification.shared.model.ImageLabel;

@Service
public class ImageLabelServiceImpl implements ImageLabelService {

	@Autowired
	private ImageLabelRepository imageLabelRepository;

	@Override
	public List<ImageLabel> findAll() {
		return this.imageLabelRepository.findAll();
	}

	@Override
	public ImageLabel findByName(String name) {
		return this.imageLabelRepository.findByName(name);
	}

	@Override
	public ImageLabel findById(String id) {
		return this.imageLabelRepository.findOne(id);
	}

	@Override
	public ImageLabel save(ImageLabel imageLabel) {
		return this.imageLabelRepository.save(imageLabel);
	}

	@Override
	public void delete(ImageLabel imageLabel) {
		this.imageLabelRepository.delete(imageLabel);
	}
}
