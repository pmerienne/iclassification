package com.pmerienne.iclassification.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.pmerienne.iclassification.shared.model.Workspace;

public interface LoginView extends IsWidget {

	void setAvailableWorkspaces(List<Workspace> workspaces);

	void setPresenter(Presenter presenter);

	void clear();

	interface Presenter {

		void login(String login, String password, Workspace workspace);
		
		void createWorkspace(String name);
	}
}
