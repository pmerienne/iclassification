package com.pmerienne.iclassification.client.activity;

import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.pmerienne.iclassification.client.event.WorkspaceLoadedEvent;
import com.pmerienne.iclassification.client.factory.ClientFactory;
import com.pmerienne.iclassification.client.service.Services;
import com.pmerienne.iclassification.client.utils.Notifications;
import com.pmerienne.iclassification.client.view.WorkspaceConfigurationView;
import com.pmerienne.iclassification.shared.model.Build;
import com.pmerienne.iclassification.shared.model.BuildConfiguration;
import com.pmerienne.iclassification.shared.model.Workspace;

public class WorkspaceConfigurationActivity extends AbstractActivity implements WorkspaceConfigurationView.Presenter {

	private ClientFactory clientFactory;
	private String workspaceId;

	public WorkspaceConfigurationActivity(ClientFactory clientFactory, String workspaceId) {
		this.clientFactory = clientFactory;
		this.workspaceId = workspaceId;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		WorkspaceConfigurationView view = this.clientFactory.getWorkspaceConfigurationView();
		view.setPresenter(this);
		panel.setWidget(view);

		if (this.workspaceId != null && !this.workspaceId.isEmpty()) {
			loadWorkspace(this.workspaceId);
		}
	}

	private void loadWorkspace(final String workspaceId) {
		Services.getWorkspaceService().find(workspaceId, new MethodCallback<Workspace>() {
			@Override
			public void onSuccess(Method arg0, Workspace workspace) {
				clientFactory.getEventBus().fireEvent(new WorkspaceLoadedEvent(workspace));
				loadBuilds(workspaceId);
			}

			@Override
			public void onFailure(Method arg0, Throwable ex) {
				Notifications.error("Workspace cannot be loaded. Cause : " + ex.getMessage());
			}
		});
	}

	private void loadBuilds(final String workspaceId) {
		Services.getWorkspaceService().getBuilds(workspaceId, new MethodCallback<List<Build>>() {
			@Override
			public void onFailure(Method arg0, Throwable arg1) {
				Notifications.error("Unable to load builds : " + arg1.getMessage());
			}

			@Override
			public void onSuccess(Method arg0, List<Build> builds) {
				WorkspaceConfigurationView view = clientFactory.getWorkspaceConfigurationView();
				view.setBuilds(builds);
			}
		});
	}

	@Override
	public void createBuild(BuildConfiguration buildConfiguration) {
		buildConfiguration.setWorkspaceId(this.workspaceId);
		Services.getWorkspaceService().createBuild(buildConfiguration, new MethodCallback<Build>() {
			@Override
			public void onFailure(Method arg0, Throwable arg1) {
				Notifications.error("Unable to create build : " + arg1.getMessage());
			}

			@Override
			public void onSuccess(Method arg0, Build arg1) {
				loadBuilds(workspaceId);
			}
		});

	}
}
