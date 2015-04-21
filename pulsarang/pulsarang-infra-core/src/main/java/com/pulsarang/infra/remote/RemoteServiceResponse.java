package com.pulsarang.infra.remote;

import java.lang.reflect.InvocationTargetException;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.server.Server;

public class RemoteServiceResponse extends MapModel {
	private static final String PARAM_CODE = "code";
	private static final String PARAM_MESSAGE = "message";
	private static final String PARAM_RESULT_DATA = "resultData";

	private static final String EC_SUCCESS = "0";
	private static final String EC_FAIL = "1";

	private RemoteServiceRequest request;
	private Server server;

	public RemoteServiceResponse() {
	}

	public RemoteServiceResponse(RemoteServiceRequest request, Server server) {
		init(request, server);
	}

	public void init(RemoteServiceRequest request, Server server) {
		this.request = request;
		this.server = server;
	}

	public RemoteServiceRequest getRequest() {
		return request;
	}

	public Server getServer() {
		return server;
	}

	public String getCode() {
		return getString(PARAM_CODE);
	}

	private void setCode(String code) {
		setString(PARAM_CODE, code);
	}

	public String getMessage() {
		return getString(PARAM_MESSAGE);
	}

	private void setMessage(String message) {
		setString(PARAM_MESSAGE, message);
	}

	public void setSuccessResult(Object result) {
		setCode(EC_SUCCESS);
		setProperty(PARAM_RESULT_DATA, result);
	}

	public void setFailResult(Throwable t) {
		setCode(EC_FAIL);

		if (t instanceof InvocationTargetException) {
			t = t.getCause();
		}

		// TODO stacktrace도 포함하도록...
		setMessage(t.getMessage());
	}

	public Object getResult() {
		if (isFail()) {
			throw new RuntimeException(getMessage());
		}

		return getProperty(PARAM_RESULT_DATA);
	}

	public boolean isSuccess() {
		return !isFail();
	}

	public boolean isFail() {
		String code = getCode();
		if (code == null) {
			throw new IllegalStateException("Result has not initialized.");
		}

		return !EC_SUCCESS.equals(code);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("server=").append(server);
		sb.append(", rquest=").append(request);
		sb.append(", errorCode=").append(getCode());
		sb.append(", errorMessage=").append(getMessage());

		return sb.toString();
	}
}
