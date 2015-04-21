package com.naver.mage4j.php.mage.converter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaClassUtils {
	private static final Logger log = LoggerFactory.getLogger(JavaClassUtils.class);

	private static List<Method> getMethodsIgnoreParameters(Class<?> clazz, String methodName) {
		try {
			Method[] methods = clazz.getMethods();
			List<Method> result = new ArrayList<Method>(1);
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					result.add(method);
				}
			}

			return result;
		} catch (NoClassDefFoundError e) {
			log.warn("", e);
			return Collections.emptyList();
		}
	}

	public static Method getMethodWithArgumentCount(Class<?> clazz, String methodName, List<?> args) {
		List<Method> methods = getMethodsIgnoreParameters(clazz, methodName);
		if (args == null) {
			args = Collections.emptyList();
		}

		if (methods.size() > 1) {
			List<Method> fitMethods = new ArrayList<Method>();
			for (Method method : methods) {
				if (method.getParameterCount() == args.size()) {
					fitMethods.add(method);
				}
			}
			if (fitMethods.size() == 1) {
				return fitMethods.get(0);
			}

			fitMethods.clear();
			for (Method method : methods) {
				if (method.getParameterCount() >= args.size()) {
					fitMethods.add(method);
				}
			}
			if (fitMethods.size() == 1) {
				return fitMethods.get(0);
			}

			throw new IllegalArgumentException("Class '" + clazz.getName() + "' has more than one '" + methodName + "' method.");
		} else if (methods.size() == 0) {
			throw new IllegalArgumentException("Class '" + clazz.getName() + "' has no '" + methodName + "' method.");
		} else {
			return methods.get(0);
		}
	}

	public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
		try {
			return clazz.getMethod(name, parameterTypes);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isStatic(Method method) {
		return (method.getModifiers() & Modifier.STATIC) != 0;
	}
}
