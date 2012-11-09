package com.pmerienne.iclassification.client.widget;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.pmerienne.iclassification.shared.model.BuildConfiguration;
import com.pmerienne.iclassification.shared.model.FeatureConfiguration;
import com.pmerienne.iclassification.shared.model.FeatureType;

public class CreateBuildModal extends Modal {

	private static CreateBuildModalUiBinder uiBinder = GWT.create(CreateBuildModalUiBinder.class);

	interface CreateBuildModalUiBinder extends UiBinder<Widget, CreateBuildModal> {
	}

	@UiField
	FeatureTypeListBox type;

	@UiField
	CheckBox useCropZone;

	@UiField
	TextBox dictionarySize;

	@UiField
	Button saveButton;

	public CreateBuildModal() {
		super();
		this.setTitle("Create new build");
		this.setAnimation(true);
		this.add(uiBinder.createAndBindUi(this));
	}

	public void addSaveHandler(ClickHandler handler) {
		this.saveButton.addClickHandler(handler);
	}

	public BuildConfiguration getBuildConfiguration() {
		// TODO check
		BuildConfiguration buildConfiguration = new BuildConfiguration();

		FeatureType featureType = this.type.getSelected();
		Boolean useCropZone = this.useCropZone.getValue();
		Integer dictionarySize = Integer.parseInt(this.dictionarySize.getValue());
		FeatureConfiguration fc = new FeatureConfiguration(featureType, useCropZone, dictionarySize);
		buildConfiguration.getFeatureConfigurations().add(fc);

		return buildConfiguration;
	}

	public void clear() {
		this.type.setSelectedIndex(0);
		this.useCropZone.setValue(false);
		this.dictionarySize.setValue("");
	}
}
