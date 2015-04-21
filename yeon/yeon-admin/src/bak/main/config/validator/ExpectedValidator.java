package com.pulsarang.infra.config.validator;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 예측하는 값의 셋에 포함 되는 값이 들어있는지 검사.
 */
public class ExpectedValidator implements Validator {
	private final List<String> candidates;

	public ExpectedValidator(List<String> candidates) {
		if (candidates == null || candidates.isEmpty()) {
			throw new IllegalArgumentException();
		}

		this.candidates = candidates;
	}

	public ExpectedValidator(String expression) {
		if (StringUtils.isEmpty(expression)) {
			throw new IllegalArgumentException();
		}

		List<String> candidates = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(expression, ",");
		while (st.hasMoreTokens()) {
			candidates.add(st.nextToken().trim());
		}

		this.candidates = candidates;
	}

	@Override
	public boolean isValid(String value, String name) {
		return candidates.contains(value);
	}

	@Override
	public void validate(String value, String name) {
		if (!isValid(value, name)) {
			String message = "'" + name + "' expects one of " + candidates + ".(" + value + ")";
			throw new IllegalArgumentException(message);
		}
	}

	public List<String> getCandidates() {
		return candidates;
	}
}
