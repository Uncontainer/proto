package com.naver.mage4j.core.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
	public static boolean match(String pattern, String subject, List<String> matches) {
		Matcher matcher = Pattern.compile(pattern).matcher(subject);
		if (!matcher.matches()) {
			return false;
		}

		for (int i = 1; i < matcher.groupCount(); i++) {
			matches.add(matcher.group(i));
		}

		return true;
	}
}
