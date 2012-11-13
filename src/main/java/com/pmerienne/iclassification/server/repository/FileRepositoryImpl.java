package com.pmerienne.iclassification.server.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;

@Repository
public class FileRepositoryImpl implements FileRepository, InitializingBean {

	private final static File FILE_DIRECTORY = new File("/data/iclassification/images");

	@Override
	public String save(File file) {
		try {
			String ext = FilenameUtils.getExtension(file.getAbsolutePath());
			String filename = String.valueOf(FileUtils.checksumCRC32(file)) + "." + ext;

			File destFile = new File(FILE_DIRECTORY, filename);
			FileUtils.copyFile(file, destFile);

			return filename;
		} catch (IOException ioe) {
			throw new RuntimeException("Unable to save file", ioe);
		}
	}

	@Override
	public void delete(String filename) {
		File file = this.get(filename);
		if (file != null && file.exists()) {
			file.delete();
		}
	}

	@Override
	public void deleteAll() {
		List<String> filenames = this.findAll();
		for (String filename : filenames) {
			this.delete(filename);
		}
	}

	@Override
	public File get(String filename) {
		File file = new File(FILE_DIRECTORY, filename);
		return file;
	}

	@Override
	public boolean contains(String filename) {
		File file = this.get(filename);
		return file != null && file.exists();
	}

	@Override
	public List<String> findAll() {
		List<String> filenames = new ArrayList<String>();

		@SuppressWarnings("unchecked")
		Collection<File> files = FileUtils.listFiles(FILE_DIRECTORY, new String[] { "jpg", "png" }, false);
		for (File file : files) {
			String filename = FilenameUtils.getName(file.getAbsolutePath());
			filenames.add(filename);
		}

		return filenames;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (!FILE_DIRECTORY.exists()) {
			FILE_DIRECTORY.mkdirs();
		}
	}

}