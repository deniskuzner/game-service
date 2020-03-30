package com.mozzartbet.gameservice.exception;

public class PlayerException extends ApplicationException {
	
	private static final long serialVersionUID = -5068208834901841913L;
	
	public PlayerException(PlayerExceptionCode code, String pattern, Object... args) {
		super(code, pattern, args);
	}
	
	public enum PlayerExceptionCode implements ApplicationExceptionCode {
		DUPLICATED_URL
	}

}
