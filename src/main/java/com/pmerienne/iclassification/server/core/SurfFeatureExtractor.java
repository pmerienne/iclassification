package com.pmerienne.iclassification.server.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import com.pmerienne.iclassification.shared.model.Feature;

import de.lmu.ifi.dbs.jfeaturelib.features.SURF;

public class SurfFeatureExtractor extends AbstractJFeatureExtractor implements FeatureExtractor {

	@Override
	public List<Feature> getFeatures(File file) {
		List<Feature> features = new ArrayList<Feature>();
		try {

			SURF surf = new SURF();
			List<double[]> rawFeatures = this.getFeatures(surf, file);
			for (double[] rawFeature : rawFeatures) {
				double[] featuresVector = Arrays.copyOfRange(rawFeature, 7, rawFeature.length);
				Feature feature = new Feature(featuresVector);
				features.add(feature);
			}

		} catch (Exception e) {
			throw new IllegalArgumentException("Unable to get surf features.", e);
		}
		return features;
	}

	@Override
	public File createFeatureImage(File file) {
		File outputFile;
		try {
			outputFile = File.createTempFile("surf", ".png");

			SURF surf = new SURF();
			List<double[]> rawFeatures = this.getFeatures(surf, file);

			BufferedImage bi = ImageIO.read(file);
			Graphics2D g2d = bi.createGraphics();
			for (double[] rawFeature : rawFeatures) {

				int x = (int) rawFeature[0];
				int y = (int) rawFeature[1];
				double scale = rawFeature[4];
				double orientation = rawFeature[5];

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
}
