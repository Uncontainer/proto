package com.yeon.infra.web.freemarker;

import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

import java.util.Map;

public class TemplateModelUtil {
	public static String getStringParam(@SuppressWarnings("rawtypes") Map params, String name, String defaultValue) throws TemplateModelException {
		Object srcObject = params.get(name);
		if (srcObject != null && srcObject instanceof TemplateScalarModel) {
			return ((TemplateScalarModel)srcObject).getAsString();
		}

		return defaultValue;
	}

	public static boolean getBooleanParam(@SuppressWarnings("rawtypes") Map params, String name, boolean defaultValue) throws TemplateException {
		String value = getStringParam(params, name, "");
		return !value.isEmpty() ? Boolean.valueOf(value) : defaultValue;
	}
}
