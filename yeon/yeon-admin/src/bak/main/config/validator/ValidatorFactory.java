package com.pulsarang.infra.config.validator;

import org.apache.commons.lang.StringUtils;

public class ValidatorFactory {

	public static Validator createValidator(String validationExpression) {
		if (StringUtils.isEmpty(validationExpression)) {
			return null;
		}

		if (validationExpression.startsWith("enum:")) {
			return new ExpectedValidator(validationExpression.substring(5));
		} else if (validationExpression.startsWith("regex:")) {
			return new PatternValidator(validationExpression.substring(6));
		} else if (validationExpression.startsWith("range:")) {
			return new RangeValidator(validationExpression.substring(6));
		} else {
			throw new IllegalArgumentException("Unsupport validation expression: " + validationExpression);
		}
	}
}
