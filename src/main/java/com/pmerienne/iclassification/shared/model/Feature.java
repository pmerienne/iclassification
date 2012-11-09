package com.pmerienne.iclassification.shared.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Feature implements Serializable {

	private static final long serialVersionUID = 67227790922762562L;

	@DBRef
	private ImageMetadata imageMetadata;

	private FeatureType type;

	private boolean useCropZone;

	private byte[] data;

	public Feature() {
	}

	public Feature(byte[] data) {
		super();
		this.data = data;
	}

	public Feature(ImageMetadata imageMetadata, FeatureType type, boolean useCropZone) {
		super();
		this.imageMetadata = imageMetadata;
		this.type = type;
		this.useCropZone = useCropZone;
	}

	public ImageMetadata getImageMetadata() {
		return imageMetadata;
	}

	public void setImageMetadata(ImageMetadata imageMetadata) {
		this.imageMetadata = imageMetadata;
	}

	public FeatureType getType() {
		return type;
	}

	public void setType(FeatureType type) {
		this.type = type;
	}

	public boolean isUseCropZone() {
		return useCropZone;
	}

	public void setUseCropZone(boolean useCropZone) {
		this.useCropZone = useCropZone;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Feature [imageMetadata=" + imageMetadata + ", type=" + type + ", useCropZone=" + useCropZone + "]";
	}

}
