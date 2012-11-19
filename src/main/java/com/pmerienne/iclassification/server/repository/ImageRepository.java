package com.pmerienne.iclassification.server.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pmerienne.iclassification.shared.model.ImageLabel;
import com.pmerienne.iclassification.shared.model.ImageMetadata;

public interface ImageRepository extends MongoRepository<ImageMetadata, String> {

	ImageMetadata findByWorkspaceIdAndFilename(String workspaceId, String filename);
	
	List<ImageMetadata> findByWorkspaceId(String workspaceId);

	List<ImageMetadata> findByWorkspaceIdAndLabel(String workspaceId, ImageLabel label);
}
