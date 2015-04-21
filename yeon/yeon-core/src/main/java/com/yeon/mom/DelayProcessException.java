package com.yeon.mom;

import java.util.Date;
import java.util.Map;

public class DelayProcessException extends ReprocessableException {
	private static final long serialVersionUID = 9211992534381280765L;

	private final Date startDate;
	private final Date endDate;

	public DelayProcessException(long duration) {
		this(new Date(), createToDate(duration), null, null, null);
	}

	public DelayProcessException(Date endDate) {
		this(new Date(), endDate, null, null, null);
	}

	public DelayProcessException(Date endDate, String message) {
		this(new Date(), endDate, null, message, null);
	}

	public DelayProcessException(Date endDate, String message, Throwable cause) {
		this(new Date(), endDate, null, message, cause);
	}

	public DelayProcessException(Date endDate, Throwable cause) {
		this(new Date(), endDate, null, null, cause);
	}

	public DelayProcessException(Date startDate, Date endDate, Map<String, Object> arguments, String message, Throwable cause) {
		super(arguments, message, cause);
		if (startDate == null) {
			this.startDate = new Date();
		} else {
			this.startDate = startDate;
		}

		if (endDate == null) {
			throw new IllegalArgumentException("Null endDate.");
		}

		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public static Date createToDate(long duration) {
		return new Date(System.currentTimeMillis() + duration);
	}
}
