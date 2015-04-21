package com.pulsarang.infra.config.validator;

import java.math.BigDecimal;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

public class RangeValidator implements Validator {
	private boolean lowerBoundInclusive = true;
	private boolean upperBoundInclusive = true;
	private BigDecimal lowerBoundValue;
	private BigDecimal upperBoundValue;

	public RangeValidator(String expression) {
		// http://www.sonatype.com/books/mvnref-book/reference/pom-relationships-sect-version-ranges.html

		if (StringUtils.isEmpty(expression)) {
			throw new IllegalArgumentException();
		}

		StringTokenizer st = new StringTokenizer(expression, ",");
		String lowerBound = st.nextToken();
		String upperBound = st.nextToken();

		if (lowerBound == null || upperBound == null) {
			throw new IllegalArgumentException("Unexpected range expression: " + expression);
		}

		lowerBound = lowerBound.trim();
		if (lowerBound.charAt(0) == '(') {
			lowerBoundInclusive = false;
		} else if (lowerBound.charAt(0) == '[') {
			lowerBoundInclusive = true;
		} else {
			throw new IllegalArgumentException();
		}

		String strLowerBoundValue = lowerBound.substring(1).trim();
		if (strLowerBoundValue.isEmpty()) {
			lowerBoundValue = null;
		} else {
			try {
				lowerBoundValue = new BigDecimal(strLowerBoundValue);
			} catch (Exception e) {
				throw new IllegalArgumentException("Illegal lower bound value: " + strLowerBoundValue);
			}
		}

		upperBound = upperBound.trim();
		char upperBoundInclusiveChar = upperBound.charAt(upperBound.length() - 1);
		if (upperBoundInclusiveChar == ')') {
			upperBoundInclusive = false;
		} else if (upperBoundInclusiveChar == ']') {
			upperBoundInclusive = true;
		} else {
			throw new IllegalArgumentException();
		}

		String strUpperBoundValue = upperBound.substring(0, upperBound.length() - 1).trim();
		if (strUpperBoundValue.isEmpty()) {
			lowerBoundValue = null;
		} else {
			try {
				upperBoundValue = new BigDecimal(strUpperBoundValue);
			} catch (Exception e) {
				throw new IllegalArgumentException("Illegal upper bound value: " + strUpperBoundValue);
			}
		}
	}

	@Override
	public boolean isValid(String value, String name) {
		BigDecimal numValue;
		try {
			numValue = new BigDecimal(value);
		} catch (NumberFormatException e) {
			return false;
		}

		if (lowerBoundValue != null) {
			int compareResult = lowerBoundValue.compareTo(numValue);
			if (lowerBoundInclusive) {
				if (compareResult > 0) {
					return false;
				}
			} else {
				if (compareResult >= 0) {
					return false;
				}
			}
		}

		if (upperBoundValue != null) {
			int compareResult = upperBoundValue.compareTo(numValue);
			if (upperBoundInclusive) {
				if (compareResult < 0) {
					return false;
				}
			} else {
				if (compareResult <= 0) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public void validate(String value, String name) {
		if (isValid(value, name)) {
			return;
		}

		String message = "'" + name + "' must be greater than " + lowerBoundValue + " and less than " + upperBoundValue;
		throw new IllegalArgumentException(message);
	}
}
