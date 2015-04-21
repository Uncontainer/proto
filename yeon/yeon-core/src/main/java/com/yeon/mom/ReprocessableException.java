package com.yeon.mom;

import java.util.Map;

/**
 * 재처리가 필요한 event에 대해서 발생하는 예외.
 * 
 * @see EventProcessor
 * @author pulsarang
 */
@SuppressWarnings("serial")
public class ReprocessableException extends RuntimeException {
	private Map<String, Object> arguments;

	public ReprocessableException() {
	}

	public ReprocessableException(Throwable cause) {
		super(cause);
	}

	public ReprocessableException(String message) {
		super(message);
	}

	public ReprocessableException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReprocessableException(Map<String, Object> arguments) {
		this.arguments = arguments;
	}

	public ReprocessableException(Map<String, Object> arguments, Throwable cause) {
		super(cause);
		this.arguments = arguments;
	}

	public ReprocessableException(Map<String, Object> arguments, String message) {
		super(message);
		this.arguments = arguments;
	}

	public ReprocessableException(Map<String, Object> arguments, String message, Throwable cause) {
		super(message, cause);
		this.arguments = arguments;
	}

	public Map<String, Object> getArguments() {
		return arguments;
	}

	public void setArguments(Map<String, Object> arguments) {
		this.arguments = arguments;
	}
}
