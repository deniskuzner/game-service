package com.mozzartbet.gameservice.exception;

public class MatchException extends ApplicationException {

	private static final long serialVersionUID = -5889244901078298620L;

	public MatchException(ApplicationExceptionCode code, String pattern, Object... args) {
		super(code, pattern, args);
	}

	public enum MatchExceptionCode implements ApplicationExceptionCode {
		DUPLICATED_URL
	}

}
