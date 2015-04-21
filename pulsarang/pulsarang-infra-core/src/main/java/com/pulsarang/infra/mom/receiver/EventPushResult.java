package com.pulsarang.infra.mom.receiver;

import org.apache.commons.httpclient.HttpStatus;

import com.pulsarang.core.util.MapModel;

public class EventPushResult extends MapModel {
	public static final String PARAM_RESPONSE_CODE = "response_code";
	public static final String PARAM_SERVER_IP = "server_ip";

	public EventPushResult() {
		super();
	}

	public EventPushResult(int responseCode, String serverIp) {
		super();
		setResponseCode(responseCode);
		setServerIp(serverIp);
	}

	public int getResponseCode() {
		return getInteger(PARAM_RESPONSE_CODE);
	}

	public void setResponseCode(int responseCode) {
		setInteger(PARAM_RESPONSE_CODE, responseCode);
	}

	public boolean isRedirected() {
		return HttpStatus.SC_MOVED_PERMANENTLY == getResponseCode();
	}

	public String getServerIp() {
		return getString(PARAM_SERVER_IP);
	}

	public void setServerIp(String serverIp) {
		setString(PARAM_SERVER_IP, serverIp);
	}
}
