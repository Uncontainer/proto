package com.yeon.lang;

public interface ClassService {
	String add(Class clazz);

	Class get(String id);

	int modify(Class clazz);

	int remove(String id);
}
