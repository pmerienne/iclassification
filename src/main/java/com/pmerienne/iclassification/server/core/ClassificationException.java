package com.pmerienne.iclassification.server.core;

public class ClassificationException extends Exception {

	private static final long serialVersionUID = 5611304158150117076L;

	public ClassificationException() {
		super();
	}

	public ClassificationException(String message) {
		super(message);
	}

	public ClassificationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClassificationException(Throwable cause) {
		super(cause);
	}
}
