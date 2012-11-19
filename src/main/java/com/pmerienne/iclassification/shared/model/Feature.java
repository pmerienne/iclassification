package com.pmerienne.iclassification.shared.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@CompoundIndexes({ @CompoundIndex(name = "ftu_idx", def = "{'filename' : 1, 'type' : 1, 'useCropZone' : 1}") })
public class Feature implements Serializable {

	private static final long serialVersionUID = 67227790922762562L;

	@Id
	private String id;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filename == null) ? 0 : filename.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + (useCropZone ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Feature other = (Feature) obj;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		if (type != other.type)
			return false;
		if (useCropZone != other.useCropZone)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Feature [filename=" + filename + ", type=" + type + ", useCropZone=" + useCropZone + "]";
	}

}
