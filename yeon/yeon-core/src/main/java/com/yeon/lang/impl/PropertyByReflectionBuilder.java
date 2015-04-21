package com.yeon.lang.impl;

import com.yeon.lang.LocalValue;
import com.yeon.lang.Property;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PropertyByReflectionBuilder {
	public static Property buildByName(String classId, int parentDepth) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement element = stackTrace[parentDepth + 1];

		return createProperty(classId, element, null);
	}

	public static Property buildByName(com.yeon.lang.Class clazz, LocalValue name) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement element = null;
		for (StackTraceElement e : stackTrace) {
			if (e.getClassName().startsWith(Property.class.getPackage().getName())) {
				continue;
			} else {
				element = e;
			}
		}

		if (element == null) {
			throw new IllegalArgumentException("Fail to find property candidate StackTraceElement.(" + clazz.getId() + "." + name + ")");
		}

		return createProperty(clazz.getId(), element, name);
	}

	private static MapProperty createProperty(String classId, StackTraceElement element, LocalValue name) {
		Method method;
		try {
			java.lang.Class<?> clazz = java.lang.Class.forName(element.getClassName());
			method = clazz.getDeclaredMethod(element.getMethodName());
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		MapProperty property = new MapProperty();
		property.setDomainClassId(classId);
		property.setRangeClassId(TypeContext.getTypeId(classId, method));

		List<LocalValue> localNames = new ArrayList<LocalValue>();
		if (name == null) {
			name = new LocalValue("en", NameManager.normalizePropertyName(extractNameFromGetter(method)));
		}
		localNames.add(name);
		property.setNames(localNames);

		return property;
	}

	public static String extractNameFromGetter(Method method) {
		String methodName = method.getName();
		String propertyName;
		if (methodName.startsWith("get")) {
			propertyName = methodName.substring(3);
		} else if (methodName.startsWith("is")) {
			propertyName = methodName.substring(2);
		} else {
			throw new RuntimeException("Invalid getter method.(" + method.toString() + ")");
		}

		if (propertyName.length() > 2) {
			if (Character.isUpperCase(propertyName.charAt(1))) {
				return propertyName;
			}

			return Character.toLowerCase(propertyName.charAt(0)) + propertyName.substring(1);
		} else {
			return propertyName.toLowerCase();
		}
	}
}
