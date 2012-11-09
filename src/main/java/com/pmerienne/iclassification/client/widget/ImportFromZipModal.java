package com.pmerienne.iclassification.client.widget;

import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.Form.SubmitCompleteHandler;
import com.github.gwtbootstrap.client.ui.Form.SubmitEvent;
import com.github.gwtbootstrap.client.ui.Form.SubmitHandler;
import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.pmerienne.iclassification.shared.model.Workspace;

public class ImportFromZipModal extends Modal {

	private static ImportFormUiBinder uiBinder = GWT.create(ImportFormUiBinder.class);

	interface ImportFormUiBinder extends UiBinder<Widget, ImportFromZipModal> {
	}

	private final static String UPLOAD_URL = GWT.getHostPageBaseURL() + "api/images/importFromZip";

	@UiField
	Form form;

	@UiField
	InputElement workspaceInput;

	public ImportFromZipModal() {
		super();
		this.setTitle("Import images from zip");
		this.setAnimation(true);
		this.add(uiBinder.createAndBindUi(this));
		this.form.setAction(UPLOAD_URL);
	}

	@UiHandler("importButton")
	protected void onImportClicked(ClickEvent event) {
		this.form.submit();
		this.hide();
	}

	public HandlerRegistration addSubmitCompleteHandler(SubmitCompleteHandler handler) {
		return this.form.addSubmitCompleteHandler(handler);
	}

	/**
	 * Adds a {@link SubmitEvent} handler.
	 * 
	 * @param handler
	 *            the handler
	 * @return the handler registration used to remove the handler
	 */
	public HandlerRegistration addSubmitHandler(SubmitHandler handler) {
		return this.form.addSubmitHandler(handler);
	}

	public void setWorkspace(Workspace workspace) {
		this.workspaceInput.setValue(workspace.getId());
	}
}
