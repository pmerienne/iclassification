package com.pmerienne.iclassification.server.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.engine.DoGSIFTEngineOptions;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.springframework.stereotype.Component;

import com.pmerienne.iclassification.server.util.FeatureUtils;
import com.pmerienne.iclassification.shared.model.Feature;

@Component
public class SiftFeatureExtractor implements FeatureExtractor {

	@Override
	public List<Feature> getFeatures(File file) {
		List<Feature> features = null;
		try {
			InputStream is = new FileInputStream(file);

			LocalFeatureList<Keypoint> siftKeypoints = this.getSiftFeatures(is);
			features = FeatureUtils.keypointsToFeatures(siftKeypoints);
		} catch (IOException ioe) {
			throw new IllegalArgumentException("Unable to get sift features.", ioe);
		}
		return features;
	}

	@Override
	public File createFeatureImage(File file) {
		File outputFile;
		try {
			outputFile = File.createTempFile("sift", ".png");
			InputStream is = new FileInputStream(file);
			LocalFeatureList<Keypoint> siftKeypoints = this.getSiftFeatures(is);

			BufferedImage bi = ImageIO.read(file);
			Graphics2D g2d = bi.createGraphics();
			for (Keypoint keypoint : siftKeypoints) {
				int x = (int) keypoint.x;
				int y = (int) keypoint.y;
				double scale = keypoint.scale * 6.0;
				double orientation = (double) keypoint.ori;

				double sin = Math.sin(orientation);
				double cos = Math.cos(orientation);
				BasicStroke bs = new BasicStroke(2);
				g2d.setStroke(bs);
				g2d.setColor(Color.GREEN);
				g2d.drawLine(x, y, (int) (x - (sin - cos) * scale), (int) (y + (sin + cos) * scale));

				g2d.setColor(Color.YELLOW);
				g2d.drawRect(x - 2, y - 2, 2, 2);
			}

			ImageIO.write(bi, "png", outputFile);
		} catch (IOException ioe) {
			throw new IllegalArgumentException("Unable to get sift features.", ioe);
		}
		return outputFile;
	}

	private LocalFeatureList<Keypoint> getSiftFeatures(InputStream is) throws IOException {
		// Read image in OpenIMAJ format
		MBFImage image = ImageUtilities.readMBF(is);

		// Configure sift engine
		DoGSIFTEngineOptions<FImage> options = new DoGSIFTEngineOptions<FImage>();
		DoGSIFTEngine siftEngine = new DoGSIFTEngine(options);

		// Extract keypoints
		LocalFeatureList<Keypoint> imageKeypoints = siftEngine.findFeatures(Transforms.calculateIntensityNTSC(image));
		return imageKeypoints;
	}
}
