package com.pmerienne.iclassification.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface WorkspaceLoadedHandler extends EventHandler {

	void onWorkspaceLoaded(WorkspaceLoadedEvent event);
}