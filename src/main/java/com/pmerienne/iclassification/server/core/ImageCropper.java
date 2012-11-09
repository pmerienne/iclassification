package com.pmerienne.iclassification.server.core;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.springframework.stereotype.Component;

@Component
public class ImageCropper {

	public File crop(File inputFile, int x, int y, int width, int height) throws IOException {
		// Buffer image
		BufferedImage img = ImageIO.read(inputFile);

		// Get a correct clip
		Rectangle clip = ImageCropper.createClip(img, x, y, width, height);

		// Get sub image from clip
		BufferedImage clipped = img.getSubimage(clip.x, clip.y, clip.width, clip.height);

		// Write image to temporary file
		String formatName = ImageCropper.getFormatName(inputFile);
		File file = File.createTempFile("crop-", ".tmp." + formatName);
		ImageIO.write(clipped, formatName, file);

		return file;
	}

	private static Rectangle createClip(BufferedImage img, int x, int y, int width, int height) throws IOException {

		/** Checking for negative X Co-ordinate **/
		if (x < 0) {
			x = 0;
		}

		/** Checking for negative Y Co-ordinate **/
		if (y < 0) {
			y = 0;
		}

		/** Checking if the clip area lies outside the rectangle **/
		if ((width + x) <= img.getWidth() && (height + y) <= img.getHeight()) {

			/**
			 * Setting up a clip rectangle when clip area lies within the image.
			 */

			Rectangle clip = new Rectangle(new Dimension(width, height));
			clip.x = x;
			clip.y = y;
			return clip;
		} else {

			/**
			 * Checking if the width of the clip area lies outside the image. If
			 * so, making the image width boundary as the clip width.
			 */
			if ((width + x) > img.getWidth())
				width = img.getWidth() - x;

			/**
			 * Checking if the height of the clip area lies outside the image.
			 * If so, making the image height boundary as the clip height.
			 */
			if ((height + y) > img.getHeight())
				height = img.getHeight() - y;

			/** Setting up the clip are based on our clip area size adjustment **/
			Rectangle clip = new Rectangle(new Dimension(width, height));
			clip.x = x;
			clip.y = y;

			return clip;
		}
	}

	private static String getFormatName(File image) throws IOException {
		String extension = null;
		Iterator<ImageReader> it = ImageIO.getImageReaders(ImageIO.createImageInputStream(image));

		while (extension == null && it.hasNext()) {
			extension = it.next().getFormatName();
		}

		if (extension == null) {
			return "png";
		}

		return extension;
	}
}
