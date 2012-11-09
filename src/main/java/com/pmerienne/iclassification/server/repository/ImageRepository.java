package com.pmerienne.iclassification.server.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pmerienne.iclassification.shared.model.ImageLabel;
import com.pmerienne.iclassification.shared.model.ImageMetadata;
import com.pmerienne.iclassification.shared.model.Workspace;

public interface ImageRepository extends MongoRepository<ImageMetadata, String> {

	ImageMetadata findByWorkspaceAndFilename(Workspace workspace, String filename);
	
	List<ImageMetadata> findByWorkspace(Workspace workspace);

	List<ImageMetadata> findByWorkspaceAndLabel(Workspace workspace, ImageLabel label);
}
