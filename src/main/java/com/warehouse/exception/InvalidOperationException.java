package com.warehouse.exception;

public class InvalidOperationException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String throwingMethod;
	private Object[] parameters;
	private String message;
	private String objectType;
	private String action;
	
	
	public String getThrowingMethod() {
		return throwingMethod;
	}
	public InvalidOperationException setThrowingMethod(String throwingMethod) {
		this.throwingMethod = throwingMethod;
		return this;
	}
	public Object[] getParameters() {
		return parameters;
	}
	public InvalidOperationException setParameters(Object[] parameters) {
		this.parameters = parameters;
		return this;
	}
	@Override
	public String getMessage() {
		return message;
	}
	public InvalidOperationException setMessage(String message) {
		this.message = message;
		return this;
	}
	public String getObjectType() {
		return objectType;
	}
	public InvalidOperationException setObjectType(String objectType) {
		this.objectType = objectType;
		return this;
	}
	public String getAction() {
		return action;
	}
	public InvalidOperationException setAction(String action) {
		this.action = action;
		return this;
	}

}
