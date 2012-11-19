package com.pmerienne.iclassification.server.core;

import org.openimaj.knn.DoubleNearestNeighbours;
import org.openimaj.knn.DoubleNearestNeighboursExact;
import org.openimaj.knn.NearestNeighboursFactory;
import org.openimaj.ml.clustering.DoubleCentroidsResult;
import org.openimaj.ml.clustering.kmeans.DoubleKMeans;
import org.openimaj.ml.clustering.kmeans.KMeansConfiguration;

public class KMeans {

	private double[][] centroids;
	private double[][] data;
	private Integer nbCluster;

	public KMeans(double[][] data, int nbCluster) {
		this.data = data;
		this.nbCluster = nbCluster;
	}

	public KMeans(double[][] centroids) {
		this.centroids = centroids;
	}

	public double[][] cluster() {
		if (this.data == null) {
			throw new IllegalStateException("Cannot cluster : no data found.");
		}
		if (this.nbCluster == null) {
			throw new IllegalStateException("Cannot cluster : nbCluster is null.");
		}

		// Create kmeans
		int K = this.data.length;
		int M = this.nbCluster;
		NearestNeighboursFactory<DoubleNearestNeighboursExact, double[]> factory = new DoubleNearestNeighboursExact.Factory();
		KMeansConfiguration<DoubleNearestNeighbours, double[]> conf = new KMeansConfiguration<DoubleNearestNeighbours, double[]>(K, M, factory);
		DoubleKMeans doubleKMeans = new DoubleKMeans(conf);

		// Cluster data
		DoubleCentroidsResult result = doubleKMeans.cluster(this.data);
		this.centroids = result.centroids;

		return this.centroids;
	}

	public int[] assign(double[][] data) {
		if (this.centroids == null) {
			throw new IllegalStateException("Cannot cluster : no centroids found.");
		}

		DoubleCentroidsResult byteCentroidsResult = new DoubleCentroidsResult();
		byteCentroidsResult.centroids = centroids;

		int[] assignedClusterIndexes = byteCentroidsResult.defaultHardAssigner().assign(data);
		return assignedClusterIndexes;
	}

	public double[][] getCentroids() {
		return centroids;
	}

}
