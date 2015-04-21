/*
 * PostponingReprocessableException.java $version 2010. 10. 15
 * 
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.pulsarang.infra.mom;

import java.util.Map;


/**
 * 
 * @author pulsarang
 */
@SuppressWarnings("serial")
public class PostponingReprocessableException extends ReprocessableException {

	private long duration;

	public PostponingReprocessableException(long duration) {
		super();
		this.duration = duration;
	}

	public PostponingReprocessableException(long duration, Throwable cause) {
		super(cause);
		this.duration = duration;
	}

	public PostponingReprocessableException(long duration, Map<String, Object> arguments) {
		super(arguments);
		this.duration = duration;
	}

	public PostponingReprocessableException(long duration, Map<String, Object> arguments, Throwable cause) {
		super(arguments, cause);
		this.duration = duration;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

}
