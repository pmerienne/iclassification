package com.pmerienne.iclassification.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Dictionary implements Serializable {

	private static final long serialVersionUID = -3135249299961575125L;

	public final static Integer DEFAULT_SIZE = 200;

	@Id
	private String id;

	private ImageLabel imageLabel;

	private FeatureConfiguration featureConfiguration;

	@XmlTransient
	@JsonIgnore
	private List<Feature> centroids = new ArrayList<Feature>();

	public Dictionary() {
		super();
	}

	public Dictionary(String id) {
		this();
		this.id = id;
	}

	public Dictionary(ImageLabel imageLabel, FeatureConfiguration featureConfiguration, List<Feature> centroids) {
		this();
		this.imageLabel = imageLabel;
		this.featureConfiguration = featureConfiguration;
		this.centroids = centroids;
	}

	public Dictionary(List<Feature> centroids) {
		this();
		this.centroids = centroids;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Feature> getCentroids() {
		return centroids;
	}

	public void setCentroids(List<Feature> centroids) {
		this.centroids = centroids;
	}

	public ImageLabel getImageLabel() {
		return imageLabel;
	}

	public void setImageLabel(ImageLabel imageLabel) {
		this.imageLabel = imageLabel;
	}

	public FeatureConfiguration getFeatureConfiguration() {
		return featureConfiguration;
	}

	public void setFeatureConfiguration(FeatureConfiguration featureConfiguration) {
		this.featureConfiguration = featureConfiguration;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((featureConfiguration == null) ? 0 : featureConfiguration.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((imageLabel == null) ? 0 : imageLabel.hashCode());
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
		Dictionary other = (Dictionary) obj;
		if (featureConfiguration == null) {
			if (other.featureConfiguration != null)
				return false;
		} else if (!featureConfiguration.equals(other.featureConfiguration))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imageLabel != other.imageLabel)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Dictionary [id=" + id + ", imageLabel=" + imageLabel + ", featureConfiguration=" + featureConfiguration + "]";
	}

}
