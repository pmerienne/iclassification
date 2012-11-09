package com.pmerienne.iclassification.client.widget;

import java.util.List;

import com.pmerienne.iclassification.shared.model.ImageLabel;

public class ImageLabelListBox extends ItemListBox<ImageLabel> {

	public ImageLabelListBox() {
		super();
	}

	public ImageLabelListBox(List<ImageLabel> imageLabels) {
		super();
		this.setItems(imageLabels);
	}

	@Override
	protected String getLabel(ImageLabel item) {
		return item.getName();
	}
}
