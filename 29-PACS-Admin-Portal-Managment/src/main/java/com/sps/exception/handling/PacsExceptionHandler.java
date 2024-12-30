package com.sps.exception.handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PacsExceptionHandler {

	@ExceptionHandler({ PacsAppException.class })
	public ResponseEntity<ExceptionInfo> handelPacsExcep(PacsAppException excep) {
		ExceptionInfo excepInfo = new ExceptionInfo();
		excepInfo.setMessage(excep.getMessage());
		excepInfo.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(excepInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ PacsMemberNoRecordFound.class })
	public ResponseEntity<ExceptionInfo> memberNotFound(PacsMemberNoRecordFound excep) {
		ExceptionInfo excepInfo = new ExceptionInfo();
		excepInfo.setMessage(excep.getMessage());
		excepInfo.setStatusCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(excepInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<ExceptionInfo> handelAllExcep(Exception excep) {
		ExceptionInfo excepInfo = new ExceptionInfo();
		excepInfo.setMessage(excep.getMessage());
		excepInfo.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(excepInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
