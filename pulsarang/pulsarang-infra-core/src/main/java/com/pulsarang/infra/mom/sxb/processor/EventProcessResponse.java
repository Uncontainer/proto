package com.pulsarang.infra.mom.sxb.processor;

import java.util.Map;

import com.pulsarang.core.util.MapModel;

public class EventProcessResponse extends MapModel {
	public static final int RC_NO_PROCESSOR = 404;
	public static final int RC_SUCCESS = 200;
	public static final int RC_INTERNAL_SERVER_ERROR = 500;
	public static final int RC_REPROCESS = 600;

	public static final EventProcessResponse SUCCESS;

	static {
		SUCCESS = new EventProcessResponse(RC_SUCCESS);
		SUCCESS.unmodifiable();
	}

	private String PARAM_RESPONSE_CODE = "res_code";
	private String PARAM_EXCEPTION = "exception";
	private String PARAM_REPROCESS_ARGUMENT = "reprocess_argument";

	public EventProcessResponse() {
	}

	public EventProcessResponse(int responseCode) {
		setResponseCode(responseCode);
	}

	public int getResponseCode() {
		return getInteger(PARAM_RESPONSE_CODE);
	}

	public void setResponseCode(int responseCode) {
		setInteger(PARAM_RESPONSE_CODE, responseCode);
	}

	public String getException() {
		return getString(PARAM_EXCEPTION);
	}

	public void setException(String exception) {
		setString(PARAM_EXCEPTION, exception);
	}

	public Map<String, Object> getReprocessArgument() {
		return getMap(PARAM_REPROCESS_ARGUMENT);
	}

	public void setReprocessArgument(Map<String, Object> reprocessArgument) {
		setMap(PARAM_REPROCESS_ARGUMENT, reprocessArgument);
	}
}
