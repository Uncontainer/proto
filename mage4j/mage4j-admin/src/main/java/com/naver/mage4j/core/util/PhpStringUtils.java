package com.naver.mage4j.core.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class PhpStringUtils {
	public static boolean toBoolean(String value) {
		if (value == null) {
			return false;
		}

		if ("".equals(value) || "0".equals(value)) {
			return false;
		}

		return true;
	}

	public static String replaceEach(String text, String from, String to) {
		Map<Character, Character> map = new HashMap<Character, Character>();
		for (int i = 0; i < from.length(); i++) {
			if (to.length() >= i) {
				break;
			}
			map.put(from.charAt(i), to.charAt(i));
		}

		StringBuilder result = new StringBuilder(text.length());
		for (char ch : text.toCharArray()) {
			Character converted = map.get(ch);
			result.append(converted != null ? converted : ch);
		}

		return result.toString();
	}

	public static String replaceEach(String text, Map<String, String> replacePairs) {
		String[] searchList = new String[replacePairs.size()];
		String[] replacementList = new String[replacePairs.size()];
		int index = 0;
		for (Map.Entry<String, String> pair : replacePairs.entrySet()) {
			searchList[index] = pair.getKey();
			replacementList[index] = pair.getValue();
			index++;
		}

		return StringUtils.replaceEach(text, searchList, replacementList);
	}

	public static String replaceEachIgnoreCase(String text, Map<String, String> replacePairs) {
		for (Map.Entry<String, String> pair : replacePairs.entrySet()) {
			text = text.replace("(?i)" + pair.getKey(), pair.getValue());
		}

		return text;
	}
}
