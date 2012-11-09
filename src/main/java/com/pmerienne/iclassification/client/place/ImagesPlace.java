package com.pmerienne.iclassification.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class ImagesPlace extends Place {

	private String workspaceId;

	public ImagesPlace(String workspaceId) {
		this.workspaceId = workspaceId;
	}

	public String getWorkspaceId() {
		return workspaceId;
	}

	public static class Tokenizer implements PlaceTokenizer<ImagesPlace> {

		@Override
		public ImagesPlace getPlace(String token) {
			return new ImagesPlace(token);
		}

		@Override
		public String getToken(ImagesPlace place) {
			return place.getWorkspaceId();
		}
	}
}
