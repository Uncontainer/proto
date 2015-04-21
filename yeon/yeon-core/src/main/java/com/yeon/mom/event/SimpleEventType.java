package com.yeon.mom.event;

import com.yeon.mom.util.EventTypeUtils;

public class SimpleEventType implements EventType {
	private final String className;
	private final String operationName;
	private String code;

	public SimpleEventType(String className, String operationName) {
		super();
		this.className = className;
		this.operationName = operationName;
	}

	@Override
	public String getClassName() {
		return className;
	}

	@Override
	public String getOperationName() {
		return operationName;
	}

	@Override
	public String getCode() {
		String cd = code;
		if (cd == null) {
			cd = code = EventTypeUtils.encode(this);
		}

		return cd;
	}
}
