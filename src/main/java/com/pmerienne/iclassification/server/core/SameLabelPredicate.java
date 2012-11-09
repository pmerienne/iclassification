package com.pmerienne.iclassification.server.core;

import com.google.common.base.Predicate;
import com.pmerienne.iclassification.shared.model.ImageLabel;
import com.pmerienne.iclassification.shared.model.ImageMetadata;

public class SameLabelPredicate implements Predicate<ImageMetadata> {

	private ImageLabel imageLabel;

	public SameLabelPredicate(ImageLabel imageLabel) {
		this.imageLabel = imageLabel;
	}

	@Override
	public boolean apply(ImageMetadata imageMetadata) {
		if (imageMetadata == null || imageMetadata.getLabel() == null) {
			throw new NullPointerException("imageMetadata and its label can't be null");
		}

		return this.imageLabel.equals(imageMetadata.getLabel());
	}
}
