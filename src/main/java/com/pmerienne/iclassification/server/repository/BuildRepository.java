package com.pmerienne.iclassification.server.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pmerienne.iclassification.shared.model.Build;

public interface BuildRepository extends MongoRepository<Build, String> {

	List<Build> findByWorkspaceId(String workspaceId);
}
