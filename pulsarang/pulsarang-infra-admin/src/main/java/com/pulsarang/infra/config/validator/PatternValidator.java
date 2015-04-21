package com.pulsarang.infra.config.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 기술한 정규표현식에 맞는 값인지 검사한다.
 */
public class PatternValidator implements Validator {
	private final Pattern pattern;
	private final String regex;

	public PatternValidator(String regex) {
		if (StringUtils.isEmpty(regex)) {
			throw new IllegalArgumentException();
		}

		this.regex = regex;
		this.pattern = Pattern.compile(regex);
	}

	@Override
	public boolean isValid(String value, String name) {
		Matcher m = pattern.matcher(value);
		if (!m.matches()) {
			return false;
		}

		return true;
	}

	@Override
	public void validate(String value, String name) {
		if (!isValid(value, name)) {
			String message = "'" + name + "' expects '" + regex + "' pattern.(" + value + ")";
			throw new IllegalArgumentException(message);
		}
	}
}
