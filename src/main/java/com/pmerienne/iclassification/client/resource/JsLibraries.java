package com.pmerienne.iclassification.client.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface JsLibraries extends ClientBundle {

	public final static JsLibraries INSTANCE = GWT.create(JsLibraries.class);

	@Source("js/jquery.Jcrop.min.js")
	TextResource jCrop();

	@Source("js/toastr.js")
	TextResource toastr();
}
