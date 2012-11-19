package com.pmerienne.iclassification.client.utils;

import com.google.gwt.core.client.GWT;
import com.pmerienne.iclassification.shared.model.ImageMetadata;

public class ImageUtils {

	private final static String BASE_URL = GWT.getHostPageBaseURL() + "api/images/";

	public static String getImageUrl(ImageMetadata imageMetadata) {
		return BASE_URL + imageMetadata.getFilename() + "/file";
	}

	public static String getSegmentedImageUrl(ImageMetadata imageMetadata) {
		return BASE_URL + imageMetadata.getFilename() + "/segmentedFile";
	}

	public static String getFeatureImageUrl(ImageMetadata imageMetadata) {
		return BASE_URL + imageMetadata.getFilename() + "/featureFile";
	}
}
