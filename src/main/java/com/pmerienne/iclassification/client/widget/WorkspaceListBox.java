package com.pmerienne.iclassification.client.widget;

import com.pmerienne.iclassification.shared.model.Workspace;

public class WorkspaceListBox extends ItemListBox<Workspace> {

	@Override
	protected String getLabel(Workspace workspace) {
		return workspace.getName();
	}
}
