package com.yeon.lang.impl;

import com.yeon.lang.BuiltinType;
import com.yeon.lang.ResourceGetCondition;
import com.yeon.lang.ResourceServiceUtils;
import com.yeon.lang.ResourceType;

import java.lang.reflect.Method;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class TypeContext {
	private static final Map<java.lang.Class<?>, BuiltinType> type;

	static {
		type = new HashMap<Class<?>, BuiltinType>();

		type.put(String.class, BuiltinType.STRING);
		type.put(Integer.class, BuiltinType.INTEGER);
		type.put(int.class, BuiltinType.INTEGER);
		type.put(Long.class, BuiltinType.LONG);
		type.put(long.class, BuiltinType.LONG);
		type.put(Float.class, BuiltinType.FLOAT);
		type.put(float.class, BuiltinType.FLOAT);
		type.put(Double.class, BuiltinType.DOUBLE);
		type.put(double.class, BuiltinType.DOUBLE);
		type.put(Short.class, BuiltinType.SHORT);
		type.put(short.class, BuiltinType.SHORT);
		type.put(Date.class, BuiltinType.DATE);
	}

	public static String getTypeId(String classId, Method method) {
		BuiltinType type = matchBuiltinType(method.getReturnType());
		if (type != null) {
			return type.getId();
		}

		ResourceGetCondition condition = new ResourceGetCondition();
		condition.setResourceType(ResourceType.CLASS);
		condition.setClassId(classId);
		condition.setName("en", NameManager.normalizePropertyName(PropertyByReflectionBuilder.extractNameFromGetter(method)));
		condition.setRegistIfNotExist(true);

		return ResourceServiceUtils.getId(condition);
	}

	public static BuiltinType matchBuiltinType(java.lang.Class<?> javaClass) {
		return type.get(javaClass);
	}
}
