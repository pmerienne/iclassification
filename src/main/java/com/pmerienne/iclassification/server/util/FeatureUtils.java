package com.pmerienne.iclassification.server.util;

import java.util.ArrayList;
import java.util.List;

import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.image.feature.local.keypoints.Keypoint;

import com.pmerienne.iclassification.shared.model.Feature;

public class FeatureUtils {

	public static List<Feature> keypointsToFeatures(LocalFeatureList<Keypoint> keypoints) {
		// Convert to byte table
		List<Feature> features = new ArrayList<Feature>();
		for (Keypoint keypoint : keypoints) {
			Feature feature = new Feature(toDoubleArray(keypoint.ivec));
			features.add(feature);
		}

		return features;
	}

	public static List<Keypoint> featuresToKeypoints(List<Feature> features) {
		// Convert to byte table
		List<Keypoint> keypoints = new ArrayList<Keypoint>();
		for (Feature feature : features) {
			Keypoint keypoint = new Keypoint();
			keypoint.ivec = toByteArray(feature.getData());
			keypoints.add(keypoint);
		}

		return keypoints;
	}

	public static double[][] toArray(List<Feature> features) {
		double[][] data = new double[features.size()][];

		int i = 0;
		for (Feature feature : features) {
			data[i] = feature.getData();
			i++;
		}

		return data;
	}

	public static List<Feature> toFeatureList(double[][] features) {
		List<Feature> featureList = new ArrayList<Feature>();

		for (double[] feature : features) {
			featureList.add(new Feature(feature));
		}

		return featureList;
	}

	public static double[] toDoubleArray(byte[] bytes) {
		double[] result = new double[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			result[i] = (double) bytes[i];
		}
		return result;
	}

	public static byte[] toByteArray(double[] doubles) {
		byte[] result = new byte[doubles.length];
		for (int i = 0; i < doubles.length; i++) {
			result[i] = (byte) doubles[i];
		}
		return result;
	}
}
