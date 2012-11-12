package com.pmerienne.iclassification.server.core;

import static com.googlecode.javacv.cpp.opencv_core.CV_CMP_EQ;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCmpS;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.GC_INIT_WITH_RECT;
import static com.googlecode.javacv.cpp.opencv_imgproc.GC_PR_FGD;
import static com.googlecode.javacv.cpp.opencv_imgproc.grabCut;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.siox.SioxSegmentator;
import org.siox.util.Utils;
import org.springframework.stereotype.Component;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.pmerienne.iclassification.server.util.ImageUtils;
import com.pmerienne.iclassification.shared.model.CropZone;

@Component
public class ImageSegmenter {

	private final static Integer DEFAULT_ITERATION = 2;

	public File segment(File inputFile, CropZone cropZone) throws IOException {
		BufferedImage input = ImageIO.read(inputFile);

		// Get an image mask using grap cut
		BufferedImage mask = this.getGrabCutMask(inputFile, cropZone);

		// Use the mask with a siox algorithme
		BufferedImage output = this.applySiox(input, mask, cropZone);

		// Create temporary png file
		File outputFile = File.createTempFile("segment-", ".tmp.png");
		ImageIO.write(output, "png", outputFile);

		return outputFile;
	}

	protected BufferedImage getGrabCutMask(File inputFile, CropZone cropZone) throws IOException {
		BufferedImage outputMask = null;

		IplImage input = null;
		IplImage mask = null;

		try {
			// Check crop zone
			if (cropZone == null) {
				Dimension imageSize = ImageUtils.getImageSize(inputFile);
				cropZone = new CropZone((int) imageSize.getWidth() / 4, (int) imageSize.getWidth() / 4,
						(int) imageSize.getWidth() / 2, (int) imageSize.getHeight() / 2);
			}

			// Load input image
			BufferedImage bufferedImage = ImageIO.read(inputFile);
			input = IplImage.createFrom(bufferedImage);

			// Create mask
			mask = cvCreateImage(cvGetSize(input), IPL_DEPTH_8U, 1);

			// Define bounding rectangle, pixels outside this rectangle will be
			// labeled as background
			CvRect rect = new CvRect(cropZone.getX(), cropZone.getY(), cropZone.getWidth(), cropZone.getHeight());

			// GrabCut segmentation
			int iterCount = DEFAULT_ITERATION;
			int mode = GC_INIT_WITH_RECT;
			CvMat fgdModel = new CvMat(null);
			CvMat bgdModel = new CvMat(null);
			grabCut(input, mask, rect, fgdModel, bgdModel, iterCount, mode);

			// Extract only foreground and copy it to output
			cvCmpS(mask, GC_PR_FGD, mask, CV_CMP_EQ);

			outputMask = mask.getBufferedImage();
		} catch (Exception ex) {
			throw new IOException("Unable to segment image " + inputFile, ex);
		} finally {
			// Release images
			if (input != null) {
				input.release();
			}
			if (mask != null) {
				cvReleaseImage(mask);
			}
		}

		return outputMask;
	}

	protected BufferedImage applySiox(BufferedImage input, BufferedImage mask, CropZone cropZone) throws IOException {
		BufferedImage output = null;

		int width = input.getWidth();
		int height = input.getHeight();
		float[] limits = null;

		int[] argbImage = convertToARGB(input);

		float[] cm = createConfidenceMatrix(mask, cropZone);
		int smoothness = 10;
		double sizeFactorToKeep = 4.0;

		SioxSegmentator sioxSegmentator = new SioxSegmentator(width, height, limits);
		boolean success = sioxSegmentator.segmentate(argbImage, cm, smoothness, sizeFactorToKeep);
		if (!success) {
			throw new IOException("Siox segment failed");
		}

		for (int k = 0; k < argbImage.length; k++) {
			argbImage[k] = Utils.setAlpha(cm[k], argbImage[k]);
		}

		output = getImageFromArray(argbImage, width, height);

		return output;
	}

	private float[] createConfidenceMatrix(BufferedImage mask, CropZone cropZone) {
		int width = mask.getWidth();
		int height = mask.getHeight();
		float[] cm = new float[width * height];
		int[] pixels = new int[width * height];
		pixels = mask.getData().getPixels(0, 0, width, height, pixels);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int index = j * width + i;
				if (cropZone.contains(i, j)) {
					if (pixels[index] != 0) {
						cm[index] = SioxSegmentator.FOREGROUND_CONFIDENCE;
					} else {
						cm[index] = SioxSegmentator.BACKGROUND_CONFIDENCE;
					}
				} else {
					cm[index] = SioxSegmentator.UNKNOWN_REGION_CONFIDENCE;
				}
			}
		}

		return cm;
	}

	private static int[] convertToARGB(BufferedImage image) {
		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		final int width = image.getWidth();
		final int height = image.getHeight();
		final boolean hasAlphaChannel = image.getAlphaRaster() != null;

		int[] result = new int[height * width];

		if (hasAlphaChannel) {
			final int pixelLength = 4;
			for (int pixel = 0, i = 0; pixel < pixels.length; pixel += pixelLength) {
				int argb = 0;
				argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
				argb += ((int) pixels[pixel + 1] & 0xff); // blue
				argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
				result[i] = argb;
				i++;
			}
		} else {
			final int pixelLength = 3;
			for (int pixel = 0, i = 0; pixel < pixels.length; pixel += pixelLength) {
				int argb = 0;
				argb += -16777216; // 255 alpha
				argb += ((int) pixels[pixel] & 0xff); // blue
				argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
				result[i] = argb;
				i++;
			}
		}

		return result;
	}

	private static BufferedImage getImageFromArray(int[] pixels, int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		image.setRGB(0, 0, width, height, pixels, 0, width);
		return image;
	}

}
