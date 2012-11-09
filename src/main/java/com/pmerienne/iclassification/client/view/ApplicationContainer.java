package com.pmerienne.iclassification.client.view;

import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.pmerienne.iclassification.client.place.ImagesPlace;
import com.pmerienne.iclassification.client.place.WorkspaceConfigurationPlace;
import com.pmerienne.iclassification.shared.model.Workspace;

public class ApplicationContainer extends Composite {

	private static ApplicationContainerUiBinder uiBinder = GWT
			.create(ApplicationContainerUiBinder.class);

	interface ApplicationContainerUiBinder extends
			UiBinder<Widget, ApplicationContainer> {
	}

	@UiField
	NavLink workspaceLink;

	@UiField
	NavLink imagesLink;

	@UiField
	NavLink labelsLink;

	@UiField
	NavLink adminLink;

	@UiField
	SimplePanel content;

	public ApplicationContainer() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public SimplePanel getContent() {
		return content;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspaceLink.setText(workspace.getName());
		this.workspaceLink.setHref("#WorkspaceConfigurationPlace:"
				+ workspace.getId());

		this.imagesLink.setHref("#ImagesPlace:" + workspace.getId());

		// this.labelsLink.setHref("#WorkspaceConfigurationPlace:" +
		// workspace.getId());
		//
		// this.adminLink.setHref("#WorkspaceConfigurationPlace:" +
		// workspace.getId());
	}

	public void setPlace(Class<?> placeClass) {
		this.workspaceLink.setActive(false);
		this.imagesLink.setActive(false);
		this.labelsLink.setActive(false);
		this.adminLink.setActive(false);

		if (WorkspaceConfigurationPlace.class.equals(placeClass)) {
			this.workspaceLink.setActive(true);
		} else if (ImagesPlace.class.equals(placeClass)) {
			this.imagesLink.setActive(true);
		}
	}
}
