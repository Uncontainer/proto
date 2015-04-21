package com.yeon.mom.rabbitmq.producer;

/**
 * 
 * @author pulsarang
 */
@SuppressWarnings("serial")
public class RepublishException extends RuntimeException {
	public RepublishException(Throwable cause) {
		super(cause);
	}
}
