package com.mozzartbet.gameservice.exception;

public class MatchStatsException extends ApplicationException {

	private static final long serialVersionUID = -2858019222340452024L;
	
	public MatchStatsException(ApplicationExceptionCode code, String pattern, Object... args) {
		super(code, pattern, args);
	}	

	public enum MatchStatsExceptionCode implements ApplicationExceptionCode {
		DUPLICATED_MATCH_STATS
	}
}
