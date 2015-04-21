package com.yeon.remote;

import java.io.Serializable;

public class RemoteServiceRequest implements Serializable {
	private static final long serialVersionUID = 8704636932518253674L;

	private long requestkey;
	private String callerIp;

	private String objectName;
	private String methodName;
	private Object[] params;

	public RemoteServiceRequest() {
	}

	public long getRequestkey() {
		return requestkey;
	}

	public void setRequestkey(long requestkey) {
		this.requestkey = requestkey;
	}

	public String getCallerIp() {
		return callerIp;
	}

	public void setCallerIp(String callerIp) {
		this.callerIp = callerIp;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setParameres(Object[] params) {
		this.params = params;
	}

	public Object[] getParameres() {
		return params;
	}
}
