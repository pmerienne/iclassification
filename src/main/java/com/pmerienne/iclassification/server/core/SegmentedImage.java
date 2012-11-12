package com.pmerienne.iclassification.server.core;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.pmerienne.iclassification.shared.model.CropZone;
import com.pmerienne.iclassification.shared.model.ImageMetadata;

@Document
public class SegmentedImage {

	@Id
	private String filename;

	@DBRef
	private ImageMetadata originalImage;

	private CropZone cropZone;

	public SegmentedImage() {
		super();
	}

	public SegmentedImage(ImageMetadata originalImage, CropZone cropZone) {
		super();
		this.originalImage = originalImage;
		this.cropZone = cropZone;
	}

	public SegmentedImage(String filename, ImageMetadata originalImage, CropZone cropZone) {
		super();
		this.filename = filename;
		this.originalImage = originalImage;
		this.cropZone = cropZone;
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

	public CropZone getCropZone() {
		return cropZone;
	}

	public void setCropZone(CropZone cropZone) {
		this.cropZone = cropZone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cropZone == null) ? 0 : cropZone.hashCode());
		result = prime * result + ((filename == null) ? 0 : filename.hashCode());
		result = prime * result + ((originalImage == null) ? 0 : originalImage.hashCode());
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
		SegmentedImage other = (SegmentedImage) obj;
		if (cropZone == null) {
			if (other.cropZone != null)
				return false;
		} else if (!cropZone.equals(other.cropZone))
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
		return true;
	}

	@Override
	public String toString() {
		return "SegmentedImage [filename=" + filename + ", originalImage=" + originalImage + ", cropZone=" + cropZone + "]";
	}

}
