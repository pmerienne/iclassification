package com.pmerienne.iclassification.shared.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@CompoundIndexes({ @CompoundIndex(name = "ftu_idx", def = "{'filename' : 1, 'type' : 1, 'useCropZone' : 1}") })
public class Feature implements Serializable {

	private static final long serialVersionUID = 67227790922762562L;

	private String filename;

	private FeatureType type;

	private boolean useCropZone;

	private double[] data;

	public Feature() {
	}

	public Feature(double[] data) {
		super();
		this.data = data;
	}

	public Feature(String filename, FeatureType type, boolean useCropZone, double[] data) {
		super();
		this.filename = filename;
		this.type = type;
		this.useCropZone = useCropZone;
		this.data = data;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
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

	public double[] getData() {
		return data;
	}

	public void setData(double[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Feature [filename=" + filename + ", type=" + type + ", useCropZone=" + useCropZone + "]";
	}

}
