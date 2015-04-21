package com.naver.mage4j.external.zend;

public class Zend_Exception extends RuntimeException {
	public Zend_Exception() {
		super();
	}

	public Zend_Exception(String message, Throwable cause) {
		super(message, cause);
	}

	public Zend_Exception(String message) {
		super(message);
	}

	public Zend_Exception(Throwable cause) {
		super(cause);
	}
}
