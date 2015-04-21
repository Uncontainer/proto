package com.pulsarang.infra.config.validator;

public interface Validator {
	boolean isValid(String value, String name);

	void validate(String value, String name);
}
