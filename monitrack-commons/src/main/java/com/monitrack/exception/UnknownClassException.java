package com.monitrack.exception;

public class UnknownClassException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnknownClassException(Class<?> entityClass) {
		super("The class '" + entityClass.getSimpleName() + "' is unknown and does not exist in the project");
	}

}
