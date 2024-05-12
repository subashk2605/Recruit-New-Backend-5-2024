package com.stg.recruit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
	@ExceptionHandler(value = RecruitException.class)
	public ResponseEntity<String> globalEntity(Exception exception) {

		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.CONFLICT);

	}
}
