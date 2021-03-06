package com.pmerienne.iclassification.client.view;

import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.Image;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pmerienne.iclassification.client.place.ImagesPlace;
import com.pmerienne.iclassification.client.utils.ImageUtils;
import com.pmerienne.iclassification.client.widget.CroppedImage;
import com.pmerienne.iclassification.client.widget.FeatureTypeListBox;
import com.pmerienne.iclassification.shared.model.CropZone;
import com.pmerienne.iclassification.shared.model.FeatureType;
import com.pmerienne.iclassification.shared.model.ImageMetadata;
import com.pmerienne.iclassification.shared.model.Workspace;

public class ImageDetailViewImpl extends Composite implements ImageDetailView {

	private static ImageDetailViewImplUiBinder uiBinder = GWT.create(ImageDetailViewImplUiBinder.class);

	interface ImageDetailViewImplUiBinder extends UiBinder<Widget, ImageDetailViewImpl> {
	}

	@UiField
	HTMLPanel imageContainer;

	private CroppedImage originalImage;

	@UiField
	HeadingElement heading;

	@UiField
	Image segmentedImage;

	@UiField
	Image featureImage;

	@UiField
	FeatureTypeListBox featureTypeListBox;

	@UiField
	CheckBox useCropZone;

	private Presenter presenter;

	private Workspace workspace;

	private ImageMetadata image;

	public ImageDetailViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("cancelButton")
	protected void onCancelClicked(ClickEvent event) {
		String workspaceId = this.workspace.getId();
		this.presenter.goTo(new ImagesPlace(workspaceId));
	}

	@UiHandler("applyButton")
	protected void onApplyClicked(ClickEvent event) {
		CropZone cropZone = this.originalImage.getCropZone();
		this.presenter.setCropZone(this.workspace, this.image, cropZone);
	}

	@UiHandler("deleteButton")
	protected void onDeleteClicked(ClickEvent event) {
		this.presenter.remove(workspace, this.image);

		String workspaceId = this.workspace.getId();
		this.presenter.goTo(new ImagesPlace(workspaceId));
	}

	@UiHandler("saveButton")
	protected void onSaveClicked(ClickEvent event) {
		CropZone cropZone = this.originalImage.getCropZone();
		this.presenter.setCropZone(this.workspace, this.image, cropZone);

		String workspaceId = this.workspace.getId();
		this.presenter.goTo(new ImagesPlace(workspaceId));
	}

	@Override
	public void setImage(ImageMetadata image) {
		this.image = image;
		this.heading.setInnerHTML(image.getFilename() + " (" + image.getLabel().getName() + ")");
		this.reloadImage();
	}

	protected void reloadImage() {
		this.imageContainer.clear();

		String originalUrl = ImageUtils.getImageUrl(this.image);
		this.originalImage = new CroppedImage(originalUrl);
		this.originalImage.setCropZone(this.image.getCropZone());
		this.imageContainer.add(this.originalImage);

		String segmentedUrl = ImageUtils.getSegmentedImageUrl(this.image);
		this.segmentedImage.setUrl(segmentedUrl);

		FeatureType featureType = this.featureTypeListBox.getSelected();
		boolean useCropZone = this.useCropZone.getValue();
		String featureUrl = ImageUtils.getFeatureImageUrl(this.image, featureType, useCropZone);
		this.featureImage.setUrl(featureUrl);
	}

	@Override
	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
