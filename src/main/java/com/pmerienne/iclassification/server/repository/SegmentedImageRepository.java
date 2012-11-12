package com.pmerienne.iclassification.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pmerienne.iclassification.server.core.SegmentedImage;
import com.pmerienne.iclassification.shared.model.CropZone;
import com.pmerienne.iclassification.shared.model.ImageMetadata;

public interface SegmentedImageRepository extends MongoRepository<SegmentedImage, String> {

	SegmentedImage findByOriginalImage(ImageMetadata originalImage);

	SegmentedImage findByOriginalImageAndCropZone(ImageMetadata originalImage, CropZone cropZone);
}
