package com.pmerienne.iclassification.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pmerienne.iclassification.server.repository.WorkspaceRepository;
import com.pmerienne.iclassification.shared.model.Workspace;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

	@Autowired
	private WorkspaceRepository workspaceRepository;

	@Override
	public Workspace save(Workspace workspace) {
		return this.workspaceRepository.save(workspace);
	}

	@Override
	public void delete(Workspace workspace) {
		this.workspaceRepository.delete(workspace);
	}

	@Override
	public List<Workspace> findAll() {
		return this.workspaceRepository.findAll();
	}

	@Override
	public Workspace findById(String id) {
		return this.workspaceRepository.findOne(id);
	}

}
