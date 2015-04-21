package com.naver.mage4j.php.lang;

public interface PhpType {
	String getName();

	boolean isAssignableFrom(PhpType type);

	Class<?> getJavaClass();
}
