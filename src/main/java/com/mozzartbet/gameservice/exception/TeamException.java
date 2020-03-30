package com.mozzartbet.gameservice.exception;

public class TeamException extends ApplicationException {

	private static final long serialVersionUID = -1830481972523940284L;
	
	public TeamException(ApplicationExceptionCode code, String pattern, Object... args) {
		super(code, pattern, args);
	}
	
	public enum TeamExceptionCode implements ApplicationExceptionCode {
		DUPLICATED_URL
	}

}
