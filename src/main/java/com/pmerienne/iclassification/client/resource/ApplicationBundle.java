package com.pmerienne.iclassification.client.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ApplicationBundle extends ClientBundle {

	public final static ApplicationBundle INSTANCE = GWT.create(ApplicationBundle.class);

	@Source("com/pmerienne/iclassification/client/resource/image/spinner.gif")
	ImageResource spinner();

}
