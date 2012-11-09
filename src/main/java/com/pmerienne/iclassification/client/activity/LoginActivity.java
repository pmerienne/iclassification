package com.pmerienne.iclassification.client.activity;

import java.util.List;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.pmerienne.iclassification.client.event.WorkspaceLoadedEvent;
import com.pmerienne.iclassification.client.factory.ClientFactory;
import com.pmerienne.iclassification.client.place.WorkspaceConfigurationPlace;
import com.pmerienne.iclassification.client.service.Services;
import com.pmerienne.iclassification.client.utils.Notifications;
import com.pmerienne.iclassification.client.view.LoginView;
import com.pmerienne.iclassification.shared.model.User;
import com.pmerienne.iclassification.shared.model.Workspace;

public class LoginActivity extends AbstractActivity implements LoginView.Presenter {

	private ClientFactory clientFactory;

	public LoginActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		LoginView view = this.clientFactory.getLoginView();
		view.setPresenter(this);
		panel.setWidget(view);

		this.loadWorkspaces();
	}

	private void loadWorkspaces() {
		Services.getWorkspaceService().findAll(new MethodCallback<List<Workspace>>() {
			@Override
			public void onSuccess(Method method, List<Workspace> response) {
				LoginView view = clientFactory.getLoginView();
				view.setAvailableWorkspaces(response);
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				Notifications.error("Unable to load workspaces. Cause : " + exception.getMessage());
			}
		});
	}

	@Override
	public void login(final String login, final String password, final Workspace workspace) {
		final LoginView view = this.clientFactory.getLoginView();

		Services.getUserService().login(login, password, new MethodCallback<User>() {
			@Override
			public void onSuccess(Method arg0, User user) {
				view.clear();
				clientFactory.getPlaceController().goTo(new WorkspaceConfigurationPlace(workspace.getId()));
				clientFactory.getEventBus().fireEvent(new WorkspaceLoadedEvent(workspace));
			}

			@Override
			public void onFailure(Method arg0, Throwable t) {
				view.clear();
				Notifications.error("Unable to log in. Cause : " + t.getMessage());
			}
		});
	}

	@Override
	public void createWorkspace(String name) {
		Services.getWorkspaceService().create(name, new MethodCallback<Workspace>() {
			@Override
			public void onFailure(Method arg0, Throwable t) {
				Notifications.error("Unable to create workspace : " + t.getMessage());
			}

			@Override
			public void onSuccess(Method arg0, Workspace workspace) {
				Notifications.info("Workspace " + workspace.getName() + " created");
				loadWorkspaces();
			}
		});
	}

}
