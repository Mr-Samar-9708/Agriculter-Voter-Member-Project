package com.sps.exception.handling;

public class PacsMemberNoRecordFound extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7248969751972741983L;

	public PacsMemberNoRecordFound() {
		super();
	}
	
	public PacsMemberNoRecordFound(String msg){
		super(msg);
	}
}
