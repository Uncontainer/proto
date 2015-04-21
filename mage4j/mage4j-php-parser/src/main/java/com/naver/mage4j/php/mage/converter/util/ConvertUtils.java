package com.naver.mage4j.php.mage.converter.util;

import org.apache.commons.lang3.StringUtils;

public class ConvertUtils {
	public static String phpToJavaRegex(String phpRegex) {
		if (StringUtils.isEmpty(phpRegex)) {
			return phpRegex;
		}

		if (isPcreDelimiter(phpRegex.charAt(0))) {
			int end = phpRegex.lastIndexOf(phpRegex.charAt(0));
			String op = phpRegex.substring(end + 1);
			phpRegex = phpRegex.substring(1, end);
			if (!op.isEmpty()) {
				phpRegex = "(?" + op + ")" + phpRegex;
			}
		}
		
		return phpRegex;
	}
	

	private static boolean isPcreDelimiter(char ch) {
		return !(Character.isAlphabetic(ch) || Character.isDigit(ch) || Character.isWhitespace(ch) || ch == '/');
	}
}
