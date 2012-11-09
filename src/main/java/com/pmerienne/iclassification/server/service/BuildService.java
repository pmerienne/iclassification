package com.pmerienne.iclassification.server.service;

import java.util.List;

import com.pmerienne.iclassification.shared.model.Build;
import com.pmerienne.iclassification.shared.model.FeatureConfiguration;
import com.pmerienne.iclassification.shared.model.Workspace;

public interface BuildService {

	List<Build> findAll();
	
	Build findById(String id);

	void delete(Build build);
	
	Build createBuild(Workspace workspace, List<FeatureConfiguration> featureConfigurations);
}
