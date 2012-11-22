package com.pmerienne.iclassification.server.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageUtils {

	public static File convertToJpg(File inputFile) throws IOException {
		File outputFile = File.createTempFile("tojpg-", ".tmp.jpg");

		BufferedImage bufferedImage = ImageIO.read(inputFile);

		// create a blank, RGB, same width and height, and a white background
		BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

		// write to jpeg file
		ImageIO.write(newBufferedImage, "jpg", outputFile);
		return outputFile;
	}

	public static String getFormatName(File file) {
		String extension = null;

		try {
			Iterator<ImageReader> it = ImageIO.getImageReaders(ImageIO.createImageInputStream(file));
			while (extension == null && it.hasNext()) {
				extension = it.next().getFormatName();
			}

			if (extension == null) {
				extension = "png";
			}
		} catch (Exception ex) {
			throw new RuntimeException("Unable to get image format.", ex);
		}

		return extension;
	}

	public static Dimension getImageSize(File file) {
		Dimension dimension = null;
		ImageInputStream in = null;
		try {
			in = ImageIO.createImageInputStream(file);
			final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
			if (readers.hasNext()) {
				ImageReader reader = (ImageReader) readers.next();
				try {
					reader.setInput(in);
					dimension = new Dimension(reader.getWidth(0), reader.getHeight(0));
				} finally {
					reader.dispose();
				}
			}
		} catch (IOException ioe) {
			throw new RuntimeException("Unable to get image size.", ioe);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					// nothing we can do
				}
		}

		return dimension;
	}
}
