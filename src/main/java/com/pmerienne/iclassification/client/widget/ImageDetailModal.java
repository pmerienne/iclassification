package com.pmerienne.iclassification.client.widget;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pmerienne.iclassification.client.utils.ImageUtils;
import com.pmerienne.iclassification.shared.model.CropZone;
import com.pmerienne.iclassification.shared.model.ImageMetadata;

public class ImageDetailModal extends Modal {

	private static ImageDetailModalUiBinder uiBinder = GWT.create(ImageDetailModalUiBinder.class);

	interface ImageDetailModalUiBinder extends UiBinder<Widget, ImageDetailModal> {
	}

	@UiField
	ListBox imageTypeListBox;

	@UiField
	HTMLPanel imageContainer;

	@UiField
	Button applyButton;

	@UiField
	Button saveButton;

	private ImageMetadata imageMetadata;

	private CroppedImage croppedImage;

	public ImageDetailModal() {
		// Init modal
		this.setTitle("Image detail");
		this.setAnimation(true);
		this.add(uiBinder.createAndBindUi(this));

		// Init image type
		this.imageTypeListBox.addItem("Original image");
		this.imageTypeListBox.addItem("Segmented image");
		// this.imageTypeListBox.addItem("Image with features");

		this.imageTypeListBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				ImageDetailModal.this.loadImage();
			}
		});
	}

	@UiHandler("applyButton")
	protected void onApplyClicked(ClickEvent event) {
		this.imageTypeListBox.setSelectedIndex(0);
		this.loadImage();
	}

	public void setImageMetadata(ImageMetadata imageMetadata) {
		this.imageMetadata = imageMetadata;
		this.setTitle(imageMetadata.getFilename() + " (" + imageMetadata.getLabel().getName() + ") details");
		this.loadImage();
	}

	protected void loadImage() {
		this.imageContainer.clear();

		String url = null;
		int index = this.imageTypeListBox.getSelectedIndex();
		switch (index) {
		case 0:
			url = ImageUtils.getImageUrl(this.imageMetadata);
			break;
		case 1:
			url = ImageUtils.getSegmentedImageUrl(this.imageMetadata);
			break;
		}

		// Create cropped image
		this.croppedImage = new CroppedImage(url);
		CropZone cropZone = this.imageMetadata.getCropZone();
		if (cropZone == null) {
			cropZone = new CropZone(1, 1, 10, 10);
		}
		this.croppedImage.setCropZone(cropZone);
		this.imageContainer.add(this.croppedImage);
	}

	public ImageMetadata getImageMetadata() {
		return imageMetadata;
	}

	public CropZone getSelectedCropZone() {
		return this.croppedImage.getCropZone();
	}

	public HandlerRegistration addSaveHandler(ClickHandler handler) {
		return this.saveButton.addClickHandler(handler);
	}

	public HandlerRegistration addApplyHandler(ClickHandler handler) {
		return this.applyButton.addClickHandler(handler);
	}

}
