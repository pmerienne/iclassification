package com.pmerienne.iclassification.server.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class ZipUtils {

	private final static Logger LOGGER = Logger.getLogger(ZipUtils.class);

	private final static Integer BUFFER = 2048;

	public static File unzip(InputStream inputStream) {
		File destinationDirectory = null;
		try {
			destinationDirectory = File.createTempFile("unzip-", "-tmp");
			destinationDirectory.delete();
			destinationDirectory.mkdir();

			unzip(inputStream, destinationDirectory);
		} catch (IOException e) {
			throw new RuntimeException("Unabel to unzip file", e);
		}
		return destinationDirectory;
	}

	public static File zip(File directory) {
		File zipFile = null;
		ZipOutputStream zos = null;
		try {
			zipFile = File.createTempFile("zip-", "-tmp");
			zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
			zip(directory, zos);
		} catch (IOException e) {
			throw new RuntimeException("Unable to zip file", e);
		} finally {
			try {
				zos.close();
			} catch (IOException e) {
				// can't do nothing
			}
		}

		return zipFile;
	}

	private static void unzip(InputStream inputStream, File outputFolder) {
		byte[] buffer = new byte[BUFFER];
		LOGGER.debug("Unzip file into " + outputFolder);

		try {
			// get the zip file content
			ZipInputStream zis = new ZipInputStream(inputStream);

			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {

				String fileName = ze.getName();
				File newFile = new File(outputFolder + File.separator + fileName);

				LOGGER.debug("file unzip : " + newFile.getAbsoluteFile());

				// create all non exists folders
				// else you will hit FileNotFoundException for compressed folder
				new File(newFile.getParent()).mkdirs();

				if (!ze.isDirectory()) {
					FileOutputStream fos = new FileOutputStream(newFile);

					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}

					fos.close();
				}
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();
			LOGGER.debug("Unzip done");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private static void zip(File directory, ZipOutputStream zos) throws IOException {
		// create a new File object based on the directory we
		// get a listing of the directory content
		String[] dirList = directory.list();
		byte[] readBuffer = new byte[2156];
		int bytesIn = 0;
		// loop through dirList, and zip the files
		for (int i = 0; i < dirList.length; i++) {
			File f = new File(directory, dirList[i]);
			if (f.isDirectory()) {
				zip(f, zos);
				continue;
			}
			FileInputStream fis = new FileInputStream(f);
			try {
				ZipEntry anEntry = new ZipEntry(f.getPath());
				// place the zip entry in the ZipOutputStream object
				zos.putNextEntry(anEntry);
				// now write the content of the file to the ZipOutputStream
				while ((bytesIn = fis.read(readBuffer)) != -1) {
					zos.write(readBuffer, 0, bytesIn);
				}
			} finally {
				fis.close();
			}
		}
	}
}
