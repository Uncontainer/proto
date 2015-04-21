package com.yeon.remote;

import java.io.Serializable;

public class RemoteServiceResponse implements Serializable {
	private static final long serialVersionUID = 4595862009925717219L;

	// TODO request key 설정 코드 추가.
	private long requestkey;

	private Object result;
	private Throwable throwable;

	public RemoteServiceResponse() {
	}

	public long getRequestkey() {
		return requestkey;
	}

	public void setRequestkey(long requestkey) {
		this.requestkey = requestkey;
	}

	public Object getResult() {
		if (throwable != null) {
			// TODO 예외를 던지는 코드 추가
		}

		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public boolean isFail() {
		// TODO result에 실패 코드를 포함하는 경우에 대한 처리 추가.
		return throwable != null;
	}

	public boolean isSuccess() {
		return !isFail();
	}
}
