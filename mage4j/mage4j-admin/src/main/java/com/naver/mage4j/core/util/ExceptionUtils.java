package com.naver.mage4j.core.util;

import java.lang.reflect.InvocationTargetException;

public class ExceptionUtils {
	public static void launderThrowable(Throwable throwable) {
		throw toRuntimeException(throwable);
	}

	public static RuntimeException toRuntimeException(Throwable throwable) {
		if (throwable instanceof RuntimeException) {
			return (RuntimeException)throwable;
		} else if (throwable instanceof InvocationTargetException) {
			Throwable cause = throwable.getCause();
			if (cause != null) {
				if (cause instanceof RuntimeException) {
					return (RuntimeException)cause;
				} else {
					return new RuntimeException(cause);
				}
			} else {
				return new RuntimeException(throwable);
			}
		}
		return new RuntimeException(throwable);
	}

	public static String getFullStackTrace(Throwable throwable) {
		if (throwable == null) {
			return null;
		}

		return ExceptionUtils.getFullStackTrace(throwable);
	}

	public static String getMessage(Throwable throwable) {
		return getMessage(throwable, 0);
	}

	private static String getMessage(Throwable throwable, int depth) {
		if (throwable == null) {
			return null;
		}

		if (depth > 20) {
			return throwable.getMessage();
		}

		if (throwable instanceof InvocationTargetException) {
			Throwable cause = throwable.getCause();
			if (cause != null) {
				throwable = cause;
			}
		}

		String message = throwable.getMessage();
		if (message != null) {
			return message;
		}

		return getMessage(throwable, depth + 1);
	}
}
