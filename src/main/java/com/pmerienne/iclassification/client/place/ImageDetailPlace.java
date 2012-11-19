package com.pmerienne.iclassification.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class ImageDetailPlace extends Place {

	private String workspaceId;

	private String imageId;

	public ImageDetailPlace(String workspaceId, String imageId) {
		super();
		this.workspaceId = workspaceId;
		this.imageId = imageId;
	}

	public String getWorkspaceId() {
		return workspaceId;
	}

	public String getImageId() {
		return imageId;
	}

	public static class Tokenizer implements PlaceTokenizer<ImageDetailPlace> {

		@Override
		public ImageDetailPlace getPlace(String token) {
			String[] tokens = token.split(":");
			String workspaceId = tokens[0];
			String imageId = tokens[1];
			return new ImageDetailPlace(workspaceId, imageId);
		}

		@Override
		public String getToken(ImageDetailPlace place) {
			return place.getWorkspaceId() + ":" + place.getImageId();
		}
	}
}