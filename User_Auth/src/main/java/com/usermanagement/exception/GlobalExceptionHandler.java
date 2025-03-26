package com.usermanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> handleInvalidEnumException(HttpMessageNotReadableException ex) {
		String errorMessage = "Invalid input! Ensure that the roles match one of the accepted values: [ROLE_USER, ROLE_ADMIN, ROLE_MANAGER]";
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}
}
