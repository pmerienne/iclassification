package com.pmerienne.iclassification.client.activity;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.pmerienne.iclassification.client.event.WorkspaceLoadedEvent;
import com.pmerienne.iclassification.client.factory.ClientFactory;
import com.pmerienne.iclassification.client.service.Services;
import com.pmerienne.iclassification.client.utils.Notifications;
import com.pmerienne.iclassification.client.view.ImageDetailView;
import com.pmerienne.iclassification.shared.model.CropZone;
import com.pmerienne.iclassification.shared.model.ImageMetadata;
import com.pmerienne.iclassification.shared.model.Workspace;

public class ImageDetailActivity extends AbstractActivity implements ImageDetailView.Presenter {

	private ClientFactory clientFactory;
	private String workspaceId;
	private String imageId;

	public ImageDetailActivity(ClientFactory clientFactory, String workspaceId, String imageId) {
		this.clientFactory = clientFactory;
		this.workspaceId = workspaceId;
		this.imageId = imageId;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		final ImageDetailView view = this.clientFactory.getImageDetailView();
		view.setPresenter(this);
		panel.setWidget(view);

		loadImage(this.imageId);
		loadWorkspace(this.workspaceId);
	}

	@Override
	public void goTo(Place newPlace) {
		this.clientFactory.getPlaceController().goTo(newPlace);
	}

	@Override
	public void remove(final Workspace workspace, final ImageMetadata imageMetadata) {
		Services.getImageService().remove(workspace.getId(), imageMetadata.getFilename(), new MethodCallback<Void>() {
			@Override
			public void onSuccess(Method arg0, Void arg1) {
				Notifications.info("Image removed from workspace");
			}

			@Override
			public void onFailure(Method arg0, Throwable t) {
				Notifications.error("Unable to remove image. Cause : " + t.getMessage());
			}
		});
	}

	@Override
	public void setCropZone(final Workspace workspace, final ImageMetadata imageMetadata, final CropZone cropZone) {
		Services.getImageService().setCropZone(workspace.getId(), imageMetadata.getFilename(), cropZone, new MethodCallback<ImageMetadata>() {
			@Override
			public void onSuccess(Method arg0, ImageMetadata newImage) {
				Notifications.info("Crop zone saved");

				ImageDetailView view = clientFactory.getImageDetailView();
				view.setImage(newImage);
			}

			@Override
			public void onFailure(Method arg0, Throwable t) {
				Notifications.error("Unable to crop image. Cause : " + t.getMessage());
			}
		});
	}

	private void loadWorkspace(String workspaceId) {
		Services.getWorkspaceService().find(workspaceId, new MethodCallback<Workspace>() {
			@Override
			public void onSuccess(Method arg0, Workspace workspace) {
				clientFactory.getEventBus().fireEvent(new WorkspaceLoadedEvent(workspace));

				ImageDetailView view = clientFactory.getImageDetailView();
				view.setWorkspace(workspace);
			}

			@Override
			public void onFailure(Method arg0, Throwable ex) {
				Notifications.error("Workspace cannot be loaded. Cause : " + ex.getMessage());
			}
		});
	}

	private void loadImage(String imageId) {
		Services.getImageService().find(imageId, new MethodCallback<ImageMetadata>() {
			@Override
			public void onSuccess(Method method, ImageMetadata image) {
				if (image != null) {
					ImageDetailView view = clientFactory.getImageDetailView();
					view.setImage(image);
				} else {
					Notifications.error("Image not found");
				}
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				Notifications.error("Image cannot be loaded. Cause : " + exception.getMessage());
			}
		});
	}

}
