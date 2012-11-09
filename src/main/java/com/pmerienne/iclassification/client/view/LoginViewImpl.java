package com.pmerienne.iclassification.client.view;

import java.util.List;

import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.pmerienne.iclassification.client.widget.CreateWorkspaceModal;
import com.pmerienne.iclassification.client.widget.WorkspaceListBox;
import com.pmerienne.iclassification.shared.model.Workspace;

public class LoginViewImpl extends Composite implements LoginView {

	private static HomeViewUiBinderImpl uiBinder = GWT.create(HomeViewUiBinderImpl.class);

	interface HomeViewUiBinderImpl extends UiBinder<Widget, LoginViewImpl> {
	}

	@UiField
	TextBox login;

	@UiField
	PasswordTextBox password;

	@UiField
	WorkspaceListBox workspace;

	@UiField
	CreateWorkspaceModal createWorkspaceModal;

	private Presenter presenter;

	public LoginViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

		this.createWorkspaceModal.addSaveHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String name = createWorkspaceModal.getName();
				presenter.createWorkspace(name);
				createWorkspaceModal.hide();
			}
		});
	}

	@UiHandler("loginButton")
	protected void onLoginClicked(ClickEvent event) {
		this.presenter.login(this.login.getValue(), this.password.getValue(), this.workspace.getSelected());
	}

	@UiHandler("createWorkspaceButton")
	protected void onCreateWorkspaceClicked(ClickEvent event) {
		this.createWorkspaceModal.clear();
		this.createWorkspaceModal.show();
	}

	@Override
	public void setAvailableWorkspaces(List<Workspace> workspaces) {
		this.workspace.setItems(workspaces);
	}

	@Override
	public void clear() {
		this.login.setValue("");
		this.password.setValue("");
		this.workspace.setSelectedIndex(0);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
}
