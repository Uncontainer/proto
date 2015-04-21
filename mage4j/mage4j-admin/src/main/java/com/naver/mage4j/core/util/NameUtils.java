package com.naver.mage4j.core.util;

public class NameUtils {
	public final static boolean isInvalidJavaIdentifier(String str) {
		return !isValidJavaIdentifier(str);
	}

	public final static boolean isValidJavaIdentifier(String str) {
		// an empty or null string cannot be a valid identifier
		if (str == null || str.length() == 0) {
			return false;
		}

		char[] c = str.toCharArray();
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
}
