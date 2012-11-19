package com.pmerienne.iclassification.client.widget;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;
import com.pmerienne.iclassification.shared.model.CropZone;

public class CroppedImage extends Image {

	private CropZone cropZone;

	private JavaScriptObject jcropApi;

	public CroppedImage(String url) {
		super(url);
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		Element element = this.getElement();
		this._initCroppping(element);
	}

	@Override
	protected void onDetach() {
		Element element = this.getElement();
		if (element != null) {
			this._releaseCroppping(element);
		}
		super.onDetach();
	}

	// public void initCropping() {
	// Element element = this.getElement();
	// this._initCroppping(element);
	// }
	//
	// public void releaseCropping() {
	// Element element = this.getElement();
	// if (element != null) {
	// this._releaseCroppping(element);
	// }
	// }

	private native void _initCroppping(Element element) /*-{
		var instance = this;

		var update = function(c) {
			var x = parseInt(c.x);
			var y = parseInt(c.y);
			var w = parseInt(c.w);
			var h = parseInt(c.h);
			instance.@com.pmerienne.iclassification.client.widget.CroppedImage::updateCropZone(IIII)(x, y, w, h);
		};

		var onReady = function() {
			var api = this;
			instance.@com.pmerienne.iclassification.client.widget.CroppedImage::setJcropApi(Lcom/google/gwt/core/client/JavaScriptObject;)(api);
		};

		var options = {
			onChange : update,
			onSelect : update,
			boxWidth : 400
		};

		$wnd.$(element).Jcrop(options, onReady);
	}-*/;

	private native void _releaseCroppping(Element element) /*-{
		var api = this.@com.pmerienne.iclassification.client.widget.CroppedImage::jcropApi;
		if (api) {
			api.destroy();
		}
	}-*/;

	protected void setJcropApi(JavaScriptObject jcropApi) {
		this.jcropApi = jcropApi;

		if (this.cropZone != null) {
			this.updateJavascriptCropZone(this.jcropApi, this.cropZone.getX(), this.cropZone.getY(), this.cropZone.getWidth(), this.cropZone.getHeight());
		}
	}

	protected void updateCropZone(int x, int y, int width, int height) {
		CropZone cropZone = new CropZone(x, y, width, height);
		this.cropZone = cropZone;
	}

	protected native void updateJavascriptCropZone(JavaScriptObject jCropApi, int x, int y, int width, int height) /*-{
		var bounds = [ x, y, x + width, y + height ];
		jCropApi.setSelect(bounds);
	}-*/;

	public CropZone getCropZone() {
		return this.cropZone;
	}

	public void setCropZone(CropZone cropZone) {
		this.cropZone = cropZone;
		if (this.jcropApi != null) {
			this.updateJavascriptCropZone(this.jcropApi, cropZone.getX(), cropZone.getY(), cropZone.getWidth(), cropZone.getHeight());
		}
	}

	@Override
	public String toString() {
		return "CroppedImage [cropZone=" + cropZone + "]";
	}

}
