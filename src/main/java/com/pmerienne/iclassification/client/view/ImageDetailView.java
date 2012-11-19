package com.pmerienne.iclassification.client.view;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.pmerienne.iclassification.shared.model.CropZone;
import com.pmerienne.iclassification.shared.model.ImageMetadata;
import com.pmerienne.iclassification.shared.model.Workspace;

public interface ImageDetailView extends IsWidget {

	void setImage(ImageMetadata image);

	void setWorkspace(Workspace workspace);

	void setPresenter(Presenter presenter);

	interface Presenter {

		void goTo(Place newPlace);

		void remove(Workspace workspace, ImageMetadata imageMetadata);

		void setCropZone(Workspace workspace, ImageMetadata imageMetadata, CropZone cropZone);
	}

}
