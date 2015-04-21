/*
 * TPSMonitorContext.java $version 2010. 10. 15
 * 
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.pulsarang.infra.monitoring.access;

import org.apache.commons.lang.StringUtils;

import com.pulsarang.core.util.NanoStopWatch;

/**
 * 
 * @author pulsarang
 */
public class AccessMonitorContext {
	private static final ThreadLocal<AccessMonitorContext> CONTEXT_HOLDER = new ThreadLocal<AccessMonitorContext>() {
		@Override
		protected AccessMonitorContext initialValue() {
			return new AccessMonitorContext();
		}
	};

	private boolean initialized = false;
	private Boolean success;
	private String queryString;
	private final NanoStopWatch stopWatch = new NanoStopWatch();

	public static AccessMonitorContext getContext() {
		return CONTEXT_HOLDER.get();
	}

	public void init() {
		success = null;
		queryString = StringUtils.EMPTY;
		// 처음에 한번만 초기화 되더라도 filter나 interceptor 등에 의해 지속적으로 초기화 됨을 가정한다.  
		initialized = true;
	}

	public void destroy() {
		CONTEXT_HOLDER.remove();
	}

	public void addParameter(String key, String value) {
		if (!initialized) {
			// 초기화 되지 않은 상태에서 로직을 탈 경우 queryString이 무한히 커질 수 있다.
			return;
		}

		if (!queryString.isEmpty()) {
			queryString += "&";
		}

		queryString += key + "=" + value;
	}

	public String getQueryString() {
		return queryString;
	}

	public Boolean isSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public NanoStopWatch getStopWatch() {
		return stopWatch;
	}
}
