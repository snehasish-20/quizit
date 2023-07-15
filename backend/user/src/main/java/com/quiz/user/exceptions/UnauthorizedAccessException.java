package com.quiz.user.exceptions;

public class UnauthorizedAccessException extends RuntimeException{

	
	private static final long serialVersionUID = 1L;

	public UnauthorizedAccessException(String message) {
		super(message);
	}
}
