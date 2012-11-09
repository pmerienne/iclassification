package com.pmerienne.iclassification.shared.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Build {

	@Id
	private String id;

	private State state = State.WAITING;

	private Date date;

	@DBRef
	@XmlTransient
	@JsonIgnore
	private List<ImageLabel> imageLabels = new ArrayList<ImageLabel>();

	private List<FeatureConfiguration> featureConfigurations = new ArrayList<FeatureConfiguration>();

	@DBRef
	@XmlTransient
	@JsonIgnore
	private List<Dictionary> dictionaries = new ArrayList<Dictionary>();

	private ClassificationEvaluation classificationEvaluation;

	public Build() {
		this.date = new Date();
	}

	public Build(List<ImageLabel> imageLabels, List<FeatureConfiguration> featureConfigurations) {
		this();
		this.imageLabels = imageLabels;
		this.featureConfigurations = featureConfigurations;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<FeatureConfiguration> getFeatureConfigurations() {
		return featureConfigurations;
	}

	public void setFeatureConfigurations(List<FeatureConfiguration> featureConfigurations) {
		this.featureConfigurations = featureConfigurations;
	}

	public List<ImageLabel> getImageLabels() {
		return imageLabels;
	}

	public void setImageLabels(List<ImageLabel> imageLabels) {
		this.imageLabels = imageLabels;
	}

	public List<Dictionary> getDictionaries() {
		return dictionaries;
	}

	public void setDictionaries(List<Dictionary> dictionaries) {
		this.dictionaries = dictionaries;
	}

	public ClassificationEvaluation getClassificationEvaluation() {
		return classificationEvaluation;
	}

	public void setClassificationEvaluation(ClassificationEvaluation classificationEvaluation) {
		this.classificationEvaluation = classificationEvaluation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classificationEvaluation == null) ? 0 : classificationEvaluation.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		Build other = (Build) obj;
		if (classificationEvaluation == null) {
			if (other.classificationEvaluation != null)
				return false;
		} else if (!classificationEvaluation.equals(other.classificationEvaluation))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (state != other.state)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Build [id=" + id + ", state=" + state + ", date=" + date + ", classificationEvaluation=" + classificationEvaluation + "]";
	}

	public static enum State {
		WAITING, CREATING_DICTIONARIES, EVALUATING, FINISH, FAILED;
	}
}
