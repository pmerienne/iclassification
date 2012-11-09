package com.pmerienne.iclassification.client.factory;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.pmerienne.iclassification.client.view.ImageLabelsView;
import com.pmerienne.iclassification.client.view.ImagesView;
import com.pmerienne.iclassification.client.view.LoginView;
import com.pmerienne.iclassification.client.view.WorkspaceConfigurationView;

public interface ClientFactory {

	EventBus getEventBus();

	PlaceController getPlaceController();

	LoginView getLoginView();

	WorkspaceConfigurationView getWorkspaceConfigurationView();
	
	ImagesView getImagesView();
	
	ImageLabelsView getImageLabelsView();
}
