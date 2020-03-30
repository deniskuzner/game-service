package com.mozzartbet.gameservice.exception;

import static com.mozzartbet.gameservice.exception.InternalException.*;
import static java.lang.String.format;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = -4255058996348530964L;

	final ApplicationExceptionCode code;

	public ApplicationException(ApplicationExceptionCode code, String pattern, Object... args) {
		super(format("[%s] ", code) + format(pattern, args), extractCause(args));
		this.code = code;
	}
	
}