package com.pmerienne.iclassification.client.activity;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.pmerienne.iclassification.client.factory.ClientFactory;
import com.pmerienne.iclassification.client.place.ImageLabelsPlace;
import com.pmerienne.iclassification.client.place.ImagesPlace;
import com.pmerienne.iclassification.client.place.LoginPlace;
import com.pmerienne.iclassification.client.place.WorkspaceConfigurationPlace;

public class ApplicationActivityMapper implements ActivityMapper {

	private final Activity defaultActivity;

	private ClientFactory clientFactory;

	public ApplicationActivityMapper(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
		this.defaultActivity = new LoginActivity(clientFactory);
	}

	@Override
	public Activity getActivity(Place place) {
		Activity activity = defaultActivity;

		if (place instanceof LoginPlace) {
			activity = new LoginActivity(this.clientFactory);

		} else if (place instanceof WorkspaceConfigurationPlace) {
			activity = new WorkspaceConfigurationActivity(this.clientFactory, ((WorkspaceConfigurationPlace) place).getWorkspaceId());

		} else if (place instanceof ImagesPlace) {
			activity = new ImagesActivity(this.clientFactory, ((ImagesPlace) place).getWorkspaceId());

		} else if (place instanceof ImageLabelsPlace) {
			activity = new ImageLabelsActivity(this.clientFactory);
		}

		return activity;
	}

}
