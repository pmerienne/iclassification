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
			Feature feature = new Feature(keypoint.ivec);
			features.add(feature);
		}

		return features;
	}

	public static List<Keypoint> featuresToKeypoints(List<Feature> features) {
		// Convert to byte table
		List<Keypoint> keypoints = new ArrayList<Keypoint>();
		for (Feature feature : features) {
			Keypoint keypoint = new Keypoint();
			keypoint.ivec = feature.getData();
			keypoints.add(keypoint);
		}

		return keypoints;
	}

	public static byte[][] toByteArray(List<Feature> features) {
		byte[][] bytes = new byte[features.size()][];

		int i = 0;
		for (Feature feature : features) {
			bytes[i] = feature.getData();
			i++;
		}

		return bytes;
	}

	public static List<Feature> toFeatureList(byte[][] features) {
		List<Feature> featureList = new ArrayList<Feature>();

		for (byte[] feature : features) {
			featureList.add(new Feature(feature));
		}

		return featureList;
	}
}
