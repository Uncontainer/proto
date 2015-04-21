package com.yeon.lang.clazz;

import com.yeon.lang.impl.MapClass;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ClassValidator implements Validator {

	@Override
	public boolean supports(java.lang.Class<?> clazz) {
		return MapClass.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		MapClass clazz = (MapClass) target;

		// if (clazz.getDescription() == null || clazz.getDescription().isEmpty()) {
		// errors.rejectValue("description", "required", "description is required.");
		// }

		errors.reject("required", "class error test.");
	}
}
