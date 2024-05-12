package com.stg.recruit.exception;

public class RecruitException extends Exception {
	private String errorMessage;

	public RecruitException() {
		super();
	}

	@Override
	public String getMessage() {
		return errorMessage;
	}

	public RecruitException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

}