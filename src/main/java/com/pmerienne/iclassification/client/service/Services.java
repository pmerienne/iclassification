package com.pmerienne.iclassification.client.service;

import org.fusesource.restygwt.client.Defaults;

import com.google.gwt.core.client.GWT;

public class Services {

	static {
		// Jersey returns java.util.Date as timestamp
		Defaults.setDateFormat(null);

		// Our rest endpoint
		Defaults.setServiceRoot(GWT.getHostPageBaseURL() + "api/");
	}

	private final static WorkspaceRemoteService WORKSPACE_SERVICE = GWT.create(WorkspaceRemoteService.class);
	private final static UserRemoteService USER_SERVICE = GWT.create(UserRemoteService.class);
	private final static ImageRemoteService IMAGE_SERVICE = GWT.create(ImageRemoteService.class);
	private final static ImageLabelRemoteService IMAGE_LABEL_SERVICE = GWT.create(ImageLabelRemoteService.class);

	public static WorkspaceRemoteService getWorkspaceService() {
		return WORKSPACE_SERVICE;
	}

	public static UserRemoteService getUserService() {
		return USER_SERVICE;
	}

	public static ImageRemoteService getImageService() {
		return IMAGE_SERVICE;
	}

	public static ImageLabelRemoteService getImageLabelService() {
		return IMAGE_LABEL_SERVICE;
	}

}
