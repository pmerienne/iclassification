package com.pmerienne.iclassification.shared.model;

import java.io.Serializable;

public class FeatureConfiguration implements Serializable, Comparable<FeatureConfiguration> {

	private static final long serialVersionUID = 5676531076342863935L;

	private FeatureType type;

	private boolean useCropZone;

	private int dictionarySize;

	public FeatureConfiguration() {
		super();
	}

	public FeatureConfiguration(FeatureType type, boolean useCropZone, int dictionarySize) {
		super();
		this.type = type;
		this.useCropZone = useCropZone;
		this.dictionarySize = dictionarySize;
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

	public int getDictionarySize() {
		return dictionarySize;
	}

	public void setDictionarySize(int dictionarySize) {
		this.dictionarySize = dictionarySize;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dictionarySize;
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
		FeatureConfiguration other = (FeatureConfiguration) obj;
		if (dictionarySize != other.dictionarySize)
			return false;
		if (type != other.type)
			return false;
		if (useCropZone != other.useCropZone)
			return false;
		return true;
	}

	@Override
	public int compareTo(FeatureConfiguration o) {
		if (o == null) {
			return 1;
		}

		if (this.type.equals(o.getType()) && this.useCropZone == o.isUseCropZone()) {
			return 0;
		} else if (this.type.equals(o.getType()) && this.useCropZone != o.isUseCropZone()) {
			return this.useCropZone ? 1 : -1;
		} else {
			return String.CASE_INSENSITIVE_ORDER.compare(this.type.name(), o.getType().name());
		}
	}

	@Override
	public String toString() {
		return "FeatureConfiguration [type=" + type + ", useCropZone=" + useCropZone + ", dictionarySize=" + dictionarySize + "]";
	}

}
