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

import de.lmu.ifi.dbs.jfeaturelib.LibProperties;
import de.lmu.ifi.dbs.jfeaturelib.features.Histogram;
import de.lmu.ifi.dbs.utilities.Arrays2;

@Component
public class HSVColorFeatureExtractor extends AbstractJFeatureExtractor implements FeatureExtractor {

	static {
		LIB_PROPERTIES.setProperty(LibProperties.HISTOGRAMS_TYPE, "HSB");
		LIB_PROPERTIES.setProperty(LibProperties.HISTOGRAMS_BINS, "64");
	}

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
			outputFile = File.createTempFile("hsvcolor", ".png");
			BufferedImage bi = ImageIO.read(file);
			Graphics2D g2d = bi.createGraphics();
			double[] histogram = this.getHistogram(file);

			int width = bi.getWidth();
			int height = bi.getHeight();
			int histSize = histogram.length;
			double colWidth = width / histSize;
			double maxValue = histogram[Arrays2.max(histogram)];

			BasicStroke bs = new BasicStroke(2);
			g2d.setStroke(bs);
			g2d.setColor(Color.GREEN);
			int i = 0;
			for (double colValue : histogram) {
				g2d.drawRect((int) (i * colWidth), 0, (int) colWidth, (int) (height * colValue / maxValue));
				i++;
			}

			ImageIO.write(bi, "png", outputFile);
		} catch (IOException ioe) {
			throw new IllegalArgumentException("Unable to get sift features.", ioe);
		}
		return outputFile;
	}

	protected double[] getHistogram(File file) throws IOException {
		Histogram histogram = new Histogram();
		List<double[]> rawFeatures = this.getFeatures(histogram, file);

		if (rawFeatures == null || rawFeatures.size() != 1) {
			throw new IllegalArgumentException("HSV histogram features must have a size of 1");
		}
		double[] histData = rawFeatures.get(0);

		return histData;
	}

}
