package com.pmerienne.iclassification.client.widget;

import java.util.Arrays;

import com.pmerienne.iclassification.shared.model.FeatureType;

public class FeatureTypeListBox extends ItemListBox<FeatureType> {

	public FeatureTypeListBox() {
		super();
		this.setItems(Arrays.asList(FeatureType.values()));
	}

	@Override
	protected String getLabel(FeatureType item) {
		return item.name();
	}
}
