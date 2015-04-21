package com.yeon.util;

import org.apache.commons.lang.StringUtils;

import java.util.Locale;

public class LangUtils {
	public static boolean isHangul(char ch) {
		Character.UnicodeBlock block = Character.UnicodeBlock.of(ch);

		return isHangul(block);
	}

	public static final Locale getLocale(String str) {
		if (StringUtils.isEmpty(str)) {
			throw new IllegalArgumentException("Empty string");
		}

		// TODO locale 식별 로직 보강.
		char[] charArray = str.toCharArray();
		for (char ch : charArray) {
			if (Character.isIdentifierIgnorable(ch)) {
				continue;
			}

//			if (Character.isAlphabetic(ch)) {
            if(Character.isLetter(ch)) {
				return Locale.ENGLISH;
			} else if (isHangul(ch)) {
				return Locale.KOREAN;
			} else {
				continue;
			}
		}

		return null;
	}

	public static final boolean isHangul(Character.UnicodeBlock block) {
		return block == Character.UnicodeBlock.HANGUL_SYLLABLES ||
				block == Character.UnicodeBlock.HANGUL_JAMO ||
//				block == Character.UnicodeBlock.HANGUL_JAMO_EXTENDED_A ||
//				block == Character.UnicodeBlock.HANGUL_JAMO_EXTENDED_B ||
				block == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO;
	}
}
