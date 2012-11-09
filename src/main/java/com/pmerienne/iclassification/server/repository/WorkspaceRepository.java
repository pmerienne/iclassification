package com.pmerienne.iclassification.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pmerienne.iclassification.shared.model.Workspace;

public interface WorkspaceRepository extends MongoRepository<Workspace, String> {
}
