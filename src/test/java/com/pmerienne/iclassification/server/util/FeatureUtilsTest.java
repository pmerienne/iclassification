package com.pmerienne.iclassification.server.util;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class FeatureUtilsTest {

	@Test
	public void testToByteArray() {
		//
		double[] data = new double[] { 12, 25, 36, 120 };
		byte[] expected = new byte[] { 12, 25, 36, 120 };

		byte[] actual = FeatureUtils.toByteArray(data);
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testToDoubleArray() {
		//
		byte[] data = new byte[] { 12, 25, 36, 120 };
		double[] expected = new double[] { 12, 25, 36, 120 };

		double[] actual = FeatureUtils.toDoubleArray(data);
		assertArrayEquals(expected, actual, 0.00001);
	}

}
