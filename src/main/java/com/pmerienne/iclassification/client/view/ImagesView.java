package com.pmerienne.iclassification.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.pmerienne.iclassification.shared.model.CropZone;
import com.pmerienne.iclassification.shared.model.ImageLabel;
import com.pmerienne.iclassification.shared.model.ImageMetadata;
import com.pmerienne.iclassification.shared.model.Workspace;

public interface ImagesView extends IsWidget {

	void setImages(List<ImageMetadata> imagesMetadatas);

	void setWorkspace(Workspace workspace);

	void setAvailableLabels(List<ImageLabel> labels);

	ImageLabel getSelectedLabel();

	void clear();

	void setPresenter(Presenter presenter);

	interface Presenter {

		void remove(Workspace workspace, ImageMetadata imageMetadata);

		void setCropZone(Workspace workspace, ImageMetadata imageMetadata, CropZone cropZone);

		void searchImages(ImageLabel label, Workspace workspace);
	}
}
