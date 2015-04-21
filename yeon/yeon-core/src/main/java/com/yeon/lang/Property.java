package com.yeon.lang;

public interface Property extends Resource {
	Class getDomain();

	Class getRange();

	String toString(Object object);
}