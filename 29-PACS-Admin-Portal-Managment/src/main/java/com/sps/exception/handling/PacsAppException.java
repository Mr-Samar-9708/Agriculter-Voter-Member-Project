package com.sps.exception.handling;

public class PacsAppException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -32894875610701876L;

	public PacsAppException() {
		super();
	}

	public PacsAppException(String msg) {
		super(msg);
	}
}
