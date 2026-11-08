package com.careernex.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Map<String, String>> handleResponseStatusException(
	        ResponseStatusException exception) {

	    Map<String, String> error = Map.of(
	    		"error", exception.getReason() != null
	            ? exception.getReason()
	            : "Request failed",
	            "status", String.valueOf(exception.getStatusCode().value())
	    );

	    return new ResponseEntity<>(
	            error,
	            exception.getStatusCode()
	    );
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> handleIllegalArgumentException(
	        IllegalArgumentException exception) {

	    Map<String, String> error = Map.of(
	            "error", exception.getMessage(),
	            "status", "400"
	    );

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(error);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleGeneralException(
	        Exception exception) {

	    Map<String, String> error = Map.of(
	            "error", "An unexpected error occurred",
	            "status", "500"
	    );

	    return ResponseEntity
	            .status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(error);
	}
}