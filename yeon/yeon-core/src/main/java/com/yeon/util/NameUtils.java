package com.yeon.util;

public class NameUtils {
	public final static boolean isInvalidJavaIdentifier(String s) {
		return !isValidJavaIdentifier(s);
	}

	public final static boolean isValidJavaIdentifier(String s) {
		// an empty or null string cannot be a valid identifier
		if (s == null || s.length() == 0) {
			return false;
		}

		char[] c = s.toCharArray();
		if (!Character.isJavaIdentifierStart(c[0])) {
			return false;
		}

		for (int i = 1; i < c.length; i++) {
			if (!Character.isJavaIdentifierPart(c[i])) {
				return false;
			}
		}

		return true;
	}

	public static final boolean isInvalidYeonIdentifier(String str) {
		return !isValidYeonIdentifier(str);
	}

	public static final boolean isValidYeonIdentifier(String str) {
		if (str == null || str.isEmpty()) {
			return false;
		}

		if (isValidJavaIdentifier(str)) {
			return true;
		}

		if (str.charAt(0) == '-') {
			return false;
		}

		return isValidJavaIdentifier(str.replaceAll("-", ""));
	}
}
