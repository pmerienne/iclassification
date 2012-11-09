package com.pmerienne.iclassification.server.core;

import static com.googlecode.javacv.cpp.opencv_core.CV_CMP_EQ;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCmpS;
import static com.googlecode.javacv.cpp.opencv_core.cvCopy;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_core.cvSet;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.GC_INIT_WITH_RECT;
import static com.googlecode.javacv.cpp.opencv_imgproc.GC_PR_FGD;
import static com.googlecode.javacv.cpp.opencv_imgproc.grabCut;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.pmerienne.iclassification.server.util.ImageUtils;
import com.pmerienne.iclassification.shared.model.CropZone;

@Component
public class ImageSegmenter {

	private final static Logger LOGGER = Logger.getLogger(ImageSegmenter.class);

	private final static Integer DEFAULT_ITERATION = 2;

	public File segment(File inputFile, CropZone cropZone) throws IOException {
		File outputFile = null;

		IplImage input = null;
		IplImage mask = null;
		IplImage output = null;

		try {
			// Check crop zone
			if (cropZone == null) {
				Dimension imageSize = ImageUtils.getImageSize(inputFile);
				cropZone = new CropZone(1, 1, (int) imageSize.getWidth(), (int) imageSize.getHeight());
			}

			// Load input image
			BufferedImage bufferedImage = ImageIO.read(inputFile);
			input = IplImage.createFrom(bufferedImage);

			// Create mask
			mask = cvCreateImage(cvGetSize(input), IPL_DEPTH_8U, 1);

			// Create temporary output image
			String formatName = ImageUtils.getFormatName(inputFile);
			outputFile = File.createTempFile("segment-", ".tmp." + formatName);
			output = cvCreateImage(cvGetSize(input), input.depth(), input.nChannels());
			cvSet(output, cvScalar(0, 0, 0, 0));

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
			cvCopy(input, output, mask);

			// Save image
			cvSaveImage(outputFile.getAbsolutePath(), output);

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
			if (output != null) {
				cvReleaseImage(output);
			}
		}

		return outputFile;
	}

}
