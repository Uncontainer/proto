package com.yeon.util;

import java.lang.reflect.InvocationTargetException;

public class ExceptionUtils {
	public static void launderThrowable(Throwable throwable) {
		if (throwable instanceof RuntimeException) {
			throw (RuntimeException) throwable;
		} else if (throwable instanceof InvocationTargetException) {
			if (throwable.getCause() instanceof RuntimeException) {
				throw (RuntimeException) throwable.getCause();
			}
			throw new RuntimeException(throwable);
		}
		throw new RuntimeException(throwable);
	}

	public static String getFullStackTrace(Throwable throwable) {
		if (throwable == null) {
			return null;
		}

		return org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(throwable);
	}
}
