package com.pmerienne.iclassification.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.pmerienne.iclassification.shared.model.Workspace;

public class WorkspaceLoadedEvent extends GwtEvent<WorkspaceLoadedHandler> {

	private static Type<WorkspaceLoadedHandler> TYPE;

	public static Type<WorkspaceLoadedHandler> getType() {
		return TYPE != null ? TYPE : (TYPE = new Type<WorkspaceLoadedHandler>());
	}

	private final Workspace workspace;

	public WorkspaceLoadedEvent(final Workspace workspace) {
		this.workspace = workspace;
	}

	protected void dispatch(WorkspaceLoadedHandler handler) {
		handler.onWorkspaceLoaded(this);
	}

	public GwtEvent.Type<WorkspaceLoadedHandler> getAssociatedType() {
		return getType();
	}

	public Workspace getWorkspace() {
		return workspace;
	}

}