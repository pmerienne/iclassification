package com.pmerienne.iclassification.client.widget;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class CreateWorkspaceModal extends Modal {

	private static CreateWorkspaceModalUiBinder uiBinder = GWT.create(CreateWorkspaceModalUiBinder.class);

	interface CreateWorkspaceModalUiBinder extends UiBinder<Widget, CreateWorkspaceModal> {
	}

	@UiField
	TextBox name;

	@UiField
	Button saveButton;

	public CreateWorkspaceModal() {
		super();
		this.setTitle("Create new worskpace");
		this.setAnimation(true);
		this.add(uiBinder.createAndBindUi(this));
	}

	public void addSaveHandler(ClickHandler handler) {
		this.saveButton.addClickHandler(handler);
	}

	public String getName() {
		return this.name.getValue();
	}

	public void clear() {
		this.name.setValue("");
	}
}
