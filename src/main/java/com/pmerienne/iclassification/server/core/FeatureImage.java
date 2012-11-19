package com.pmerienne.iclassification.server.core;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.pmerienne.iclassification.shared.model.FeatureType;
import com.pmerienne.iclassification.shared.model.ImageMetadata;

@Document
@CompoundIndexes({ @CompoundIndex(name = "ofu_idx", def = "{'originalImage' : 1, 'featureType' : 1, 'useCropZone' : 1}") })
public class FeatureImage {

	@Id
	private String filename;

	@DBRef
	private ImageMetadata originalImage;

	private FeatureType featureType;

	private boolean useCropZone;

	public FeatureImage() {
		super();
	}

	public FeatureImage(String filename, ImageMetadata originalImage, FeatureType featureType, boolean useCropZone) {
		super();
		this.filename = filename;
		this.originalImage = originalImage;
		this.featureType = featureType;
		this.useCropZone = useCropZone;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public ImageMetadata getOriginalImage() {
		return originalImage;
	}

	public void setOriginalImage(ImageMetadata originalImage) {
		this.originalImage = originalImage;
	}

	public FeatureType getFeatureType() {
		return featureType;
	}

	public void setFeatureType(FeatureType featureType) {
		this.featureType = featureType;
	}

	public boolean isUseCropZone() {
		return useCropZone;
	}

	public void setUseCropZone(boolean useCropZone) {
		this.useCropZone = useCropZone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((featureType == null) ? 0 : featureType.hashCode());
		result = prime * result + ((filename == null) ? 0 : filename.hashCode());
		result = prime * result + ((originalImage == null) ? 0 : originalImage.hashCode());
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
		FeatureImage other = (FeatureImage) obj;
		if (featureType != other.featureType)
			return false;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		if (originalImage == null) {
			if (other.originalImage != null)
				return false;
		} else if (!originalImage.equals(other.originalImage))
			return false;
		if (useCropZone != other.useCropZone)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FeatureImage [filename=" + filename + ", originalImage=" + originalImage + ", featureType=" + featureType + ", useCropZone=" + useCropZone + "]";
	}

}
