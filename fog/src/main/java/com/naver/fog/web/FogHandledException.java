package com.naver.fog.web;

import com.naver.fog.user.User;

public class FogHandledException extends RuntimeException {
	private static final long serialVersionUID = -5178350941525852456L;

	protected User user;
	protected HandleType handleType;
	protected String code;
	protected String errorMessage;
	protected Object targetId;
	protected String redirectUrl;

	public enum HandleType {
		DELEGATE,
		ALERT_AND_BACK,
		ALERT_AND_MOVE,
		FAIL_MESSAGE_JSON;
	}

	public FogHandledException(User user, HandleType handleType, String code, String errorMessage) {
		this(null, user, handleType, code, errorMessage, null);
	}

	public FogHandledException(Object targetId, User user, HandleType handleType, String code, String errorMessage) {
		this(targetId, user, handleType, code, errorMessage, null);
	}

	public FogHandledException(Object targetId, User user, HandleType handleType, String code, String errorMessage, String redirectUrl) {
		super(errorMessage);
		this.code = code;
		this.errorMessage = errorMessage;
		this.user = user;
		this.handleType = handleType;
		this.targetId = targetId;
		this.redirectUrl = redirectUrl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public HandleType getHandleType() {
		return handleType;
	}

	public void setHandleType(HandleType handleType) {
		this.handleType = handleType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Object getTargetId() {
		return targetId;
	}

	public void setTargetId(Object targetId) {
		this.targetId = targetId;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
}
