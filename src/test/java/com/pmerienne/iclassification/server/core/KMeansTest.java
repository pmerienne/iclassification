package com.pmerienne.iclassification.server.core;

import static org.junit.Assert.*;

import org.junit.Test;

import cern.colt.Arrays;

public class KMeansTest {

	@Test
	public void fullTest() {
		// Test data
		double[][] testData = new double[][] { { 100, 2, -3 }, { 75, 0, 5 }, { 54, 2, 45 }, { 49, -3, 51 }, { 0, 1, 63 }, { 6, -3, 66 } };
		int nbCluster = 3;

		// Eval data
		double[][] evalData = new double[][] { { 92, 0, 3 }, { 81, -2, -3 }, { 56, -2, 52 }, { 45, -2, 53 }, { 2, -1, 61 }, { -2, 3, 63 } };

		// CLsuter
		KMeans kMeans = new KMeans(testData, nbCluster);
		double[][] centroids = kMeans.cluster();
		assertNotNull(centroids);
		assertEquals(3, centroids.length);

		// assign
		int[] actualAssignement = kMeans.assign(evalData);
		assertEquals(6, actualAssignement.length);
		assertEquals(actualAssignement[0], actualAssignement[1]);
		assertEquals(actualAssignement[2], actualAssignement[3]);
		assertEquals(actualAssignement[4], actualAssignement[5]);
	}

	protected void display(double[][] centroids) {
		for (int i = 0; i < centroids.length; i++) {
			System.out.println(Arrays.toString(centroids[i]));
		}
	}
}
