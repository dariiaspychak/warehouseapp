package com.warehouse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.warehouse.object.external.InvalidOperationError;
import com.warehouse.service.InvalidOperationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@Autowired
	private Logger logger;
	
	@ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = InvalidOperationException.class)
	public InvalidOperationError handleException(InvalidOperationException exception){
		InvalidOperationError error = new InvalidOperationError();
		error.setMessage(exception.getMessage());
		error.setAction(exception.getAction());
		error.setObjectType(exception.getObjectType());
		logger.error(String.format("Exception thrown by method %s invoked with parameters %s", exception.getThrowingMethod(), exception.getParameters()[0]));
		return error;
	}

}
