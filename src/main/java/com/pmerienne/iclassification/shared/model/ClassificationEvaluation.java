package com.pmerienne.iclassification.shared.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ClassificationEvaluation {

	// @XmlTransient
	// @JsonIgnore
	// private Integer[][] matrix = new
	// Integer[ImageLabel.values().length][ImageLabel.values().length];
	private Integer errorCount = 0;
	private Integer sucessCount = 0;

	public ClassificationEvaluation() {
		super();
	}

	public ClassificationEvaluation(Integer errorCount, Integer sucessCount) {
		super();
		this.errorCount = errorCount;
		this.sucessCount = sucessCount;
	}

	public void clear() {
		// this.matrix = new
		// Integer[ImageLabel.values().length][ImageLabel.values().length];
		this.errorCount = 0;
		this.sucessCount = 0;

		// for (int i = 0; i < ImageLabel.values().length; i++) {
		// for (int j = 0; j < ImageLabel.values().length; j++) {
		// this.matrix[i][j] = 0;
		// }
		// }
	}

	public void update(ImageLabel actual, ImageLabel expected) {
		// Integer actualIndex = ImageLabel.indexof(actual);
		// Integer expectedIndex = ImageLabel.indexof(expected);

		// if (this.matrix[actualIndex][expectedIndex] == null) {
		// this.matrix[actualIndex][expectedIndex] = 0;
		// }
		//
		// this.matrix[actualIndex][expectedIndex]++;

		if (expected.equals(actual)) {
			this.sucessCount++;
		} else {
			this.errorCount++;
		}
	}

	//
	// public Integer[][] getMatrix() {
	// return matrix;
	// }
	//
	// public void setMatrix(Integer[][] matrix) {
	// this.matrix = matrix;
	// }

	public Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}

	public Integer getSucessCount() {
		return sucessCount;
	}

	public void setSucessCount(Integer sucessCount) {
		this.sucessCount = sucessCount;
	}

	public double successRate() {
		return 100.0 * this.sucessCount / (this.errorCount + this.sucessCount);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((errorCount == null) ? 0 : errorCount.hashCode());
		result = prime * result + ((sucessCount == null) ? 0 : sucessCount.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassificationEvaluation other = (ClassificationEvaluation) obj;
		if (errorCount == null) {
			if (other.errorCount != null)
				return false;
		} else if (!errorCount.equals(other.errorCount))
			return false;
		if (sucessCount == null) {
			if (other.sucessCount != null)
				return false;
		} else if (!sucessCount.equals(other.sucessCount))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClassificationEvaluation [errorCount=" + errorCount + ", sucessCount=" + sucessCount + "]";
	}

}
