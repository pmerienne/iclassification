package com.pmerienne.iclassification.client.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pmerienne.iclassification.client.place.ImageDetailPlace;
import com.pmerienne.iclassification.client.widget.ImageLabelListBox;
import com.pmerienne.iclassification.client.widget.ImageThumbnail;
import com.pmerienne.iclassification.client.widget.ImportFromZipModal;
import com.pmerienne.iclassification.shared.model.ImageLabel;
import com.pmerienne.iclassification.shared.model.ImageMetadata;
import com.pmerienne.iclassification.shared.model.Workspace;

public class ImagesViewImpl extends Composite implements ImagesView {

	private static ImagesViewImplUiBinder uiBinder = GWT.create(ImagesViewImplUiBinder.class);

	interface ImagesViewImplUiBinder extends UiBinder<Widget, ImagesViewImpl> {
	}

	private final static String API_BASE_URL = GWT.getHostPageBaseURL() + "api/";

	@UiField
	Button exportButton;

	@UiField
	ImageLabelListBox imageLabel;

	@UiField
	ScrollPanel scrollPanel;

	@UiField
	HTMLPanel imageContainer;

	@UiField
	ImportFromZipModal imporFromZipModal;

	private Workspace workspace;

	private Presenter presenter;

	private List<ImageMetadata> availableImages = new ArrayList<ImageMetadata>();
	private Map<ImageMetadata, ImageThumbnail> visibleImages = new HashMap<ImageMetadata, ImageThumbnail>();

	private int currentIndex = 0;
	private final static int STEP = 20;

	public ImagesViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

		// Infinite scroll handler
		this.scrollPanel.addScrollHandler(new ScrollHandler() {
			@Override
			public void onScroll(ScrollEvent event) {
				boolean mustShowMoreImages = ImagesViewImpl.this.isScrollAtMax();
				if (mustShowMoreImages) {
					ImagesViewImpl.this.showMoreImages();
				}
			}
		});
	}

	protected void showMoreImages() {
		if (this.currentIndex < this.availableImages.size()) {
			int endIndex = this.currentIndex + STEP;
			if (endIndex >= this.availableImages.size()) {
				endIndex = this.availableImages.size() - 1;
			}

			List<ImageMetadata> images = this.availableImages.subList(this.currentIndex, endIndex);
			for (ImageMetadata image : images) {
				this.addImage(image);
			}

			this.currentIndex += STEP;

//			boolean mustShowMoreImages = this.isScrollAtMax();
//			if (mustShowMoreImages) {
//				this.showMoreImages();
//			}
		}
	}
	
	protected boolean isScrollAtMax() {
		this.scrollPanel.onResize();
		int verticalScrollPosition = this.scrollPanel.getVerticalScrollPosition();
		int maxVerticalScrollPosition = this.scrollPanel.getMaximumVerticalScrollPosition();

		return verticalScrollPosition == maxVerticalScrollPosition;
	}

	@UiHandler("importFromZipButton")
	protected void onImportFromZipClicked(ClickEvent event) {
		this.imporFromZipModal.show();
	}

	@UiHandler("exportButton")
	protected void onExportClicked(ClickEvent event) {
		String url = API_BASE_URL + "workspaces/" + this.workspace.getId() + "/exports";
		Window.Location.replace(url);
	}

	@UiHandler("imageLabel")
	protected void onImageLabelChange(ChangeEvent event) {
		ImageLabel selectedLabel = this.imageLabel.getSelected();
		this.presenter.searchImages(selectedLabel, this.workspace);
	}

	@Override
	public void setAvailableLabels(List<ImageLabel> labels) {
		this.imageLabel.setItems(labels);
	}

	@Override
	public ImageLabel getSelectedLabel() {
		return this.imageLabel.getSelected();
	}

	@Override
	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
		this.imporFromZipModal.setWorkspace(workspace);
	}

	@Override
	public void clear() {
		this.currentIndex = 0;
		this.visibleImages.clear();
		this.imageContainer.clear();
	}

	@Override
	public void setImages(List<ImageMetadata> imageMetadatas) {
		this.clear();
		this.availableImages = imageMetadatas;
		this.showMoreImages();
	}

	private void addImage(final ImageMetadata imageMetadata) {
		final ImageThumbnail thumbnail;
		thumbnail = new ImageThumbnail(imageMetadata);

		thumbnail.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String workspaceId = ImagesViewImpl.this.workspace.getId();
				String imageId = imageMetadata.getFilename();
				ImagesViewImpl.this.presenter.goTo(new ImageDetailPlace(workspaceId, imageId));
			}
		});

		this.visibleImages.put(imageMetadata, thumbnail);
		this.imageContainer.add(thumbnail);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
