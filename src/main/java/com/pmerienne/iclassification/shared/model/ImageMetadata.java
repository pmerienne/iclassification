package com.pmerienne.iclassification.shared.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ImageMetadata implements Serializable {

	private static final long serialVersionUID = 6528251320569977618L;

	@Id
	private String filename;

	@DBRef
	private Workspace workspace;

	@DBRef
	private ImageLabel label;

	private CropZone cropZone;

	public ImageMetadata() {
		super();
	}

	public ImageMetadata(String filename) {
		super();
		this.filename = filename;
	}

	public ImageMetadata(String filename, ImageLabel label) {
		super();
		this.filename = filename;
		this.label = label;
	}

	public ImageMetadata(String filename, Workspace workspace, ImageLabel label) {
		super();
		this.filename = filename;
		this.workspace = workspace;
		this.label = label;
	}

	public ImageMetadata(String filename, Workspace workspace, ImageLabel label, CropZone cropZone) {
		super();
		this.filename = filename;
		this.workspace = workspace;
		this.label = label;
		this.cropZone = cropZone;
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public ImageLabel getLabel() {
		return label;
	}

	public void setLabel(ImageLabel label) {
		this.label = label;
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
		result = prime * result + ((filename == null) ? 0 : filename.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
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
		ImageMetadata other = (ImageMetadata) obj;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ImageMetadata [filename=" + filename + ", label=" + label + ", workspace=" + workspace + ", cropZone=" + cropZone + "]";
	}

}
