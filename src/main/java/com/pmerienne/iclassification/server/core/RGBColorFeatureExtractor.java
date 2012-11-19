package com.pmerienne.iclassification.server.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import com.pmerienne.iclassification.shared.model.Feature;

import de.lmu.ifi.dbs.jfeaturelib.features.FuzzyHistogram;

@Component
public class RGBColorFeatureExtractor extends AbstractJFeatureExtractor implements FeatureExtractor {

	@Override
	public List<Feature> getFeatures(File file) {
		List<Feature> features = new ArrayList<Feature>();
		try {
			double[] histogram = this.getHistogram(file);

			Feature feature = new Feature(histogram);
			features.add(feature);
		} catch (Exception e) {
			throw new IllegalArgumentException("Unable to get rgb color feature features.", e);
		}
		return features;
	}

	@Override
	public File createFeatureImage(File file) {
		File outputFile;
		try {
			outputFile = File.createTempFile("rgbcolor", ".png");
			BufferedImage bi = ImageIO.read(file);
			Graphics2D g2d = bi.createGraphics();
			double[] histogram = this.getHistogram(file);

			int width = bi.getWidth();
			int height = bi.getHeight();
			int histSize = histogram.length;
			int colWidth = width / histSize;
			int maxValue = 255;

			BasicStroke bs = new BasicStroke(2);
			g2d.setStroke(bs);
			g2d.setColor(Color.GREEN);
			int i = 0;
			for (double colValue : histogram) {
				g2d.drawRect(i * colWidth, 0, colWidth, (int) (height * colValue / maxValue));
				i++;
			}

			ImageIO.write(bi, "png", outputFile);
		} catch (IOException ioe) {
			throw new IllegalArgumentException("Unable to get sift features.", ioe);
		}
		return outputFile;
	}

	protected double[] getHistogram(File file) throws IOException {
		FuzzyHistogram fuzzyHistogram = new FuzzyHistogram();
		List<double[]> rawFeatures = this.getFeatures(fuzzyHistogram, file);

		if (rawFeatures == null || rawFeatures.size() != 1) {
			throw new IllegalArgumentException("Fuzzy histogram features must have a size of 1");
		}
		double[] histogram = rawFeatures.get(0);

		return histogram;
	}

}
