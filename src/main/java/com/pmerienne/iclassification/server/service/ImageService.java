package com.pmerienne.iclassification.server.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.pmerienne.iclassification.shared.model.CropZone;
import com.pmerienne.iclassification.shared.model.ImageLabel;
import com.pmerienne.iclassification.shared.model.ImageMetadata;
import com.pmerienne.iclassification.shared.model.Workspace;

public interface ImageService {

	ImageMetadata create(Workspace workspace, File file, ImageLabel label);

	List<ImageMetadata> importFromZip(Workspace workspace, InputStream zipInputStream);

	File exportImages(Workspace workspace);

	ImageMetadata findById(String id);

	void delete(Workspace workspace, String filename);

	ImageMetadata find(Workspace workspace, String filename);

	List<ImageMetadata> find(Workspace workspace, ImageLabel label);

	List<ImageMetadata> find(Workspace workspace);

	File getFile(ImageMetadata imageMetadata);

	File getSegmentedFile(ImageMetadata imageMetadata) throws IOException;

	void setCropZone(Workspace workspace, ImageMetadata imageMetadata, CropZone cropZone);
}
