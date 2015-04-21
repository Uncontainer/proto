package com.yeon.lang.property;

import com.yeon.lang.impl.MapProperty;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PropertyValidator implements Validator {

	@Override
	public boolean supports(java.lang.Class<?> arg0) {
		return MapProperty.class.isAssignableFrom(arg0);
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		// TODO Auto-generated method stub
	}
}
