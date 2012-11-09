package com.pmerienne.iclassification.server.service;

import java.util.List;

import com.pmerienne.iclassification.shared.model.Workspace;

public interface WorkspaceService {

	Workspace save(Workspace workspace);

	void delete(Workspace workspace);

	List<Workspace> findAll();

	Workspace findById(String id);
}
