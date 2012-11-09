package com.pmerienne.iclassification.client.factory;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.pmerienne.iclassification.client.view.ImageLabelsView;
import com.pmerienne.iclassification.client.view.ImageLabelsViewImpl;
import com.pmerienne.iclassification.client.view.ImagesView;
import com.pmerienne.iclassification.client.view.ImagesViewImpl;
import com.pmerienne.iclassification.client.view.LoginView;
import com.pmerienne.iclassification.client.view.LoginViewImpl;
import com.pmerienne.iclassification.client.view.WorkspaceConfigurationView;
import com.pmerienne.iclassification.client.view.WorkspaceConfigurationViewImpl;

public class ClientFactoryImpl implements ClientFactory {

	private final EventBus eventBus = new SimpleEventBus();

	private final PlaceController placeController = new PlaceController(this.eventBus);
	private final LoginView loginView = new LoginViewImpl();
	private final WorkspaceConfigurationView workspaceConfigurationView = new WorkspaceConfigurationViewImpl();
	private final ImagesView imagesView = new ImagesViewImpl();
	private final ImageLabelsView imageLabelsView = new ImageLabelsViewImpl();

	@Override
	public EventBus getEventBus() {
		return this.eventBus;
	}

	@Override
	public PlaceController getPlaceController() {
		return this.placeController;
	}

	@Override
	public LoginView getLoginView() {
		return this.loginView;
	}

	@Override
	public WorkspaceConfigurationView getWorkspaceConfigurationView() {
		return workspaceConfigurationView;
	}

	@Override
	public ImagesView getImagesView() {
		return imagesView;
	}

	@Override
	public ImageLabelsView getImageLabelsView() {
		return this.imageLabelsView;
	}
}
