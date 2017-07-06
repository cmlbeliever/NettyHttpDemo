package com.cml.netty.learn.handler.exception;

public class ResourceNotFundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFundException() {
		super();
	}

	public ResourceNotFundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ResourceNotFundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFundException(String message) {
		super(message);
	}

	public ResourceNotFundException(Throwable cause) {
		super(cause);
	}

}
