package com.pulsarang.infra.mom.event;

public class SimpleEventType implements EventType, Comparable<SimpleEventType> {
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
		if (code == null) {
			code = EventTypeUtils.encode(this);
		}

		return code;
	}

	@Override
	public int hashCode() {
		return getCode().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof EventType)) {
			return false;
		}

		return getCode().equals(((EventType) obj).getCode());
	}

	@Override
	public int compareTo(SimpleEventType other) {
		if (other == null) {
			return 1;
		}

		return getCode().compareTo(other.getCode());
	}
}
