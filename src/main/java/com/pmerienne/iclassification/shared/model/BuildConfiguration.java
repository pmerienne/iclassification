package com.pmerienne.iclassification.shared.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BuildConfiguration {

	private String workspaceId;

	private List<FeatureConfiguration> featureConfigurations = new ArrayList<FeatureConfiguration>();

	public BuildConfiguration() {
		super();
	}

	public String getWorkspaceId() {
		return workspaceId;
	}

	public void setWorkspaceId(String workspaceId) {
		this.workspaceId = workspaceId;
	}

	public List<FeatureConfiguration> getFeatureConfigurations() {
		return featureConfigurations;
	}

	public void setFeatureConfigurations(List<FeatureConfiguration> featureConfigurations) {
		this.featureConfigurations = featureConfigurations;
	}

	@Override
	public String toString() {
		return "BuildConfiguration [workspaceId=" + workspaceId + ", featureConfigurations=" + featureConfigurations + "]";
	}

}
