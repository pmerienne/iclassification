package com.pmerienne.iclassification.server.service;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pmerienne.iclassification.server.core.ImageSegmenter;
import com.pmerienne.iclassification.server.core.SegmentedImage;
import com.pmerienne.iclassification.server.repository.FileRepository;
import com.pmerienne.iclassification.server.repository.ImageRepository;
import com.pmerienne.iclassification.server.repository.SegmentedImageRepository;
import com.pmerienne.iclassification.server.util.ImageUtils;
import com.pmerienne.iclassification.server.util.ZipUtils;
import com.pmerienne.iclassification.shared.model.CropZone;
import com.pmerienne.iclassification.shared.model.ImageLabel;
import com.pmerienne.iclassification.shared.model.ImageMetadata;
import com.pmerienne.iclassification.shared.model.Workspace;

@Service
public class ImageServiceImpl implements ImageService {

	private final static Logger LOGGER = Logger.getLogger(ImageServiceImpl.class);

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private FeatureService featureService;

	@Autowired
	private ImageLabelService imageLabelService;

	@Autowired
	private ImageSegmenter imageSegmenter;

	@Autowired
	private SegmentedImageRepository segmentedImageRepository;

	@Override
	public ImageMetadata create(Workspace workspace, File file, ImageLabel label) {
		String filename = this.fileRepository.save(file);
		Dimension imageSize = ImageUtils.getImageSize(file);
		CropZone cropZone = new CropZone((int) imageSize.getWidth() / 4, (int) imageSize.getWidth() / 4,
				(int) imageSize.getWidth() / 2, (int) imageSize.getHeight() / 2);

		ImageMetadata imageFile = new ImageMetadata(filename, workspace, label, cropZone);
		this.imageRepository.save(imageFile);

		return imageFile;
	}
	@Override
	public List<ImageMetadata> importFromZip(Workspace workspace, InputStream zipInputStream) {
		LOGGER.info("Importing images from zip into " + workspace.getName());

		// Unzip file
		File outputFolder = ZipUtils.unzip(zipInputStream);
		@SuppressWarnings("unchecked")
		Collection<File> files = FileUtils.listFiles(outputFolder, null, true);

		// Get all files/labels to import
		Map<File, ImageLabel> toImports = new HashMap<File, ImageLabel>();
		for (File file : files) {
			try {
				// Get label
				String parentName = file.getParentFile().getName();
				ImageLabel label = this.imageLabelService.findByName(parentName);
				if(label == null) {
					label = new ImageLabel(parentName, parentName);
					this.imageLabelService.save(label);
				}
				
				toImports.put(file, label);
			} catch (Exception ex) {
				LOGGER.warn("Cannot import file " + file.getName(), ex);
			}
		}

		// Import all files
		List<ImageMetadata> importedImageFiles = this.importFiles(workspace, toImports);

		LOGGER.info(importedImageFiles.size() + " images successfully imported in " + workspace.getName());
		return importedImageFiles;
	}

	@Override
	public File exportImages(Workspace workspace) {
		File zipFile = null;
		try {
			// Find all workspace images
			List<ImageMetadata> images = this.find(workspace);

			// Put images in a temporary directory
			File tmpDirectory = File.createTempFile("export-", "-tmp");
			tmpDirectory.delete();
			tmpDirectory.mkdirs();
			for (ImageMetadata image : images) {
				InputStream is = null;
				try {
					// Get directory
					File labelDirectory = new File(tmpDirectory, image.getLabel().getName());
					if (!labelDirectory.exists()) {
						labelDirectory.mkdirs();
					}

					File imageFile = new File(labelDirectory, image.getFilename());

					// Write file
					is = new FileInputStream(this.fileRepository.get(image.getFilename()));
					IOUtils.copy(is, new FileOutputStream(imageFile));
				} catch (IOException ioe) {
					LOGGER.error("Unable to export " + image.getFilename(), ioe);
				} finally {
					is.close();
				}
			}

			// Zip temporary directory
			zipFile = ZipUtils.zip(tmpDirectory);

		} catch (IOException e) {
			throw new RuntimeException("Unabel to export workspace", e);
		}
		return zipFile;
	}

	@Override
	public void delete(Workspace workspace, String filename) {
		ImageMetadata imageMetadata = this.imageRepository.findByWorkspaceIdAndFilename(workspace.getId(), filename);
		this.imageRepository.delete(imageMetadata);
	}

	@Override
	public ImageMetadata findById(String id) {
		return this.imageRepository.findOne(id);
	}

	@Override
	public List<ImageMetadata> find(Workspace workspace) {
		List<ImageMetadata> images = this.imageRepository.findByWorkspaceId(workspace.getId());
		return images;
	}

	@Override
	public ImageMetadata find(Workspace workspace, String filename) {
		ImageMetadata imageMetadata = this.imageRepository.findByWorkspaceIdAndFilename(workspace.getId(), filename);
		return imageMetadata;
	}

	@Override
	public List<ImageMetadata> find(final Workspace workspace, final ImageLabel label) {
		List<ImageMetadata> images = this.imageRepository.findByWorkspaceIdAndLabel(workspace.getId(), label);
		return images;
	}

	@Override
	public File getFile(ImageMetadata imageMetadata) {
		return this.fileRepository.get(imageMetadata.getFilename());
	}

	@Override
	public File getSegmentedFile(ImageMetadata imageMetadata) throws IOException {
		File segmentedFile = null;

		// Check in repo that image was already segmented
		CropZone cropZone = imageMetadata.getCropZone();
		SegmentedImage segmentedImage = this.segmentedImageRepository.findByOriginalImageAndCropZone(imageMetadata,
				cropZone);

		if (segmentedImage == null) {
			File inputFile = this.fileRepository.get(imageMetadata.getFilename());

			// Segment file
			segmentedFile = this.imageSegmenter.segment(inputFile, cropZone);

			// Save segmented file
			String filename = this.fileRepository.save(segmentedFile);
			segmentedImage = new SegmentedImage(filename, imageMetadata, cropZone);
			this.segmentedImageRepository.save(segmentedImage);
		}

		segmentedFile = this.fileRepository.get(segmentedImage.getFilename());
		return segmentedFile;
	}

	private ImageMetadata importFile(Workspace workspace, File file, ImageLabel label) throws IOException {
		// Build image file
		ImageMetadata importedImageMetadata = this.create(workspace, file, label);

		// Delete file
		file.delete();

		return importedImageMetadata;
	}

	private List<ImageMetadata> importFiles(Workspace workspace, Map<File, ImageLabel> files) {
		List<ImageMetadata> importedImageMetadatas = new ArrayList<ImageMetadata>();

		for (File file : files.keySet()) {
			try {
				ImageLabel label = files.get(file);
				ImageMetadata imageMetadata = this.importFile(workspace, file, label);
				importedImageMetadatas.add(imageMetadata);

				LOGGER.info(imageMetadata + " successfully imported");
			} catch (IOException ex) {
				LOGGER.warn("Cannot import file " + file.getName(), ex);
			}
		}

		return importedImageMetadatas;
	}

	@Override
	public void setCropZone(Workspace workspace, ImageMetadata imageMetadata, CropZone cropZone) {
		imageMetadata.setCropZone(cropZone);

		// Remove calculated features
		this.featureService.clearFeatures(imageMetadata);

		// Remove segmented file
		SegmentedImage segmentedImage = this.segmentedImageRepository.findByOriginalImage(imageMetadata);
		if (segmentedImage != null) {
			this.segmentedImageRepository.delete(segmentedImage);
		}
		// Save changes
		this.imageRepository.save(imageMetadata);
	}

	public void setImageRepository(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}

	public void setFileRepository(FileRepository fileRepository) {
		this.fileRepository = fileRepository;
	}

}
