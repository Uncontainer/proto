package com.yeon.monitor.common.ffm;

/**
 * 
 * @author pulsarang
 */
@SuppressWarnings("serial")
public class MatchExpressionParserException extends RuntimeException {
	public MatchExpressionParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public MatchExpressionParserException(String message) {
		super(message);
	}

	public MatchExpressionParserException(Throwable cause) {
		super(cause);
	}
}
