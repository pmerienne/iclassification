package com.pmerienne.iclassification.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class WorkspaceConfigurationPlace extends Place {

	private String workspaceId;

	public WorkspaceConfigurationPlace(String workspaceId) {
		this.workspaceId = workspaceId;
	}

	public String getWorkspaceId() {
		return workspaceId;
	}

	public static class Tokenizer implements PlaceTokenizer<WorkspaceConfigurationPlace> {

		@Override
		public WorkspaceConfigurationPlace getPlace(String token) {
			return new WorkspaceConfigurationPlace(token);
		}

		@Override
		public String getToken(WorkspaceConfigurationPlace place) {
			return place.getWorkspaceId();
		}
	}
}
