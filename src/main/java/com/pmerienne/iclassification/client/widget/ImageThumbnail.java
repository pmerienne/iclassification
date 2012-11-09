package com.pmerienne.iclassification.client.widget;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.pmerienne.iclassification.client.utils.ImageUtils;
import com.pmerienne.iclassification.shared.model.ImageMetadata;

public class ImageThumbnail extends Composite {

	private AbsolutePanel container;

	private Image image;

	private Button deleteButton;

	private ImageMetadata imageMetadata;

	public ImageThumbnail(ImageMetadata imageMetadata) {
		this.imageMetadata = imageMetadata;
		this.container = new AbsolutePanel();

		// Set image
		String url = null;
		url = ImageUtils.getImageUrl(imageMetadata);
		this.image = new Image(url);
		this.container.add(this.image);

		// Set delete button
		this.deleteButton = new Button("x");
		this.deleteButton.addStyleName("close");
		this.container.add(this.deleteButton, 0, 0);

		this.initWidget(this.container);
		this.addStyleName("span2");
		this.getElement().getStyle().setCursor(Cursor.POINTER);
	}

	public ImageMetadata getImageMetadata() {
		return imageMetadata;
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return this.image.addClickHandler(handler);
	}

	public HandlerRegistration addRemoveHandler(ClickHandler handler) {
		return this.deleteButton.addClickHandler(handler);
	}
}
