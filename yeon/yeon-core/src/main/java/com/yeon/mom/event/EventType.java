package com.yeon.mom.event;

public interface EventType {
	String CLASS_QUEUE = "_queue";

	String getClassName();

	String getOperationName();

	String getCode();
}
