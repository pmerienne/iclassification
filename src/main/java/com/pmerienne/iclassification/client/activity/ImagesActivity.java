package com.pmerienne.iclassification.client.activity;

import java.util.List;

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
import com.pmerienne.iclassification.client.view.ImagesView;
import com.pmerienne.iclassification.shared.model.ImageLabel;
import com.pmerienne.iclassification.shared.model.ImageMetadata;
import com.pmerienne.iclassification.shared.model.Workspace;

public class ImagesActivity extends AbstractActivity implements ImagesView.Presenter {

	private ClientFactory clientFactory;
	private String workspaceId;

	public ImagesActivity(ClientFactory clientFactory, String workspaceId) {
		this.clientFactory = clientFactory;
		this.workspaceId = workspaceId;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		final ImagesView view = this.clientFactory.getImagesView();
		view.setPresenter(this);
		panel.setWidget(view);

		Services.getImageLabelService().findAll(new MethodCallback<List<ImageLabel>>() {
			@Override
			public void onFailure(Method arg0, Throwable arg1) {
				Notifications.error("Unable to load image labels : " + arg1.getMessage());

				if (workspaceId != null && !workspaceId.isEmpty()) {
					loadWorkspace(workspaceId);
				}
			}

			@Override
			public void onSuccess(Method arg0, List<ImageLabel> labels) {
				view.setAvailableLabels(labels);

				if (workspaceId != null && !workspaceId.isEmpty()) {
					loadWorkspace(workspaceId);
				}
			}
		});

	}

	@Override
	public void searchImages(ImageLabel label, Workspace workspace) {
		final ImagesView view = clientFactory.getImagesView();

		String labelName = label == null ? null : label.getName();
		Services.getImageService().find(labelName, workspace.getId(), new MethodCallback<List<ImageMetadata>>() {
			@Override
			public void onSuccess(Method arg0, List<ImageMetadata> images) {
				view.setImages(images);
			}

			@Override
			public void onFailure(Method arg0, Throwable t) {
				Notifications.error("Unable to load images . Cause : " + t.getMessage());
			}
		});
	}

	private void loadWorkspace(String workspaceId) {
		Services.getWorkspaceService().find(workspaceId, new MethodCallback<Workspace>() {
			@Override
			public void onSuccess(Method arg0, Workspace workspace) {
				clientFactory.getEventBus().fireEvent(new WorkspaceLoadedEvent(workspace));

				ImagesView view = clientFactory.getImagesView();
				view.setWorkspace(workspace);

				ImageLabel label = view.getSelectedLabel();
				searchImages(label, workspace);
			}

			@Override
			public void onFailure(Method arg0, Throwable ex) {
				Notifications.error("Workspace cannot be loaded. Cause : " + ex.getMessage());
			}
		});
	}

	@Override
	public void goTo(Place newPlace) {
		this.clientFactory.getPlaceController().goTo(newPlace);
	}
}
