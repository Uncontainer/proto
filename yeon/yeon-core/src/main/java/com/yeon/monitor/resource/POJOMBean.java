package com.yeon.monitor.resource;

import com.yeon.monitor.resource.annotation.MonitoringBean;
import com.yeon.monitor.resource.annotation.MonitoringIgnoreProperty;
import com.yeon.monitor.resource.annotation.MonitoringProperty;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.Locale.ENGLISH;

/**
 * 
 * @author pulsarang
 */
public class POJOMBean implements MomMBean {
	private final String name;
	private final Object bean;
	final Map<String, Method> methodMap;
	private boolean inclusive = true;

	public POJOMBean(String name, Object bean) {

		if (StringUtils.isEmpty(name) || bean == null) {
			throw new IllegalArgumentException();
		}

		this.name = name;
		this.bean = bean;
		this.methodMap = new HashMap<String, Method>();

		MonitoringBean monitoringClass = bean.getClass().getAnnotation(MonitoringBean.class);
		if (monitoringClass != null) {
			inclusive = monitoringClass.inclusive();
		}

		add(methodMap, bean.getClass());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isExistProperty(String name) {
		return methodMap.containsKey(name);
	}

	@Override
	public Object getProperty(String name) {
		Method method = methodMap.get(name);
		if (method == null) {
			throw new IllegalArgumentException("Unregistered property '" + name + "'");
		}

		try {
			return method.invoke(bean);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<MomMBeanPropertyInfo> getPropertyInfos() {
		List<MomMBeanPropertyInfo> infos = new ArrayList<MomMBeanPropertyInfo>(methodMap.size());
		for (Entry<String, Method> entry : methodMap.entrySet()) {
			String name = entry.getKey();
			Class<?> javaType = entry.getValue().getReturnType();

			infos.add(new MomMBeanPropertyInfo(name, javaType));
		}

		return infos;
	}

	@Override
	public String toString() {
		return getName();
	}

	private void add(Map<String, Method> methodMap, Class<?> clazz) {
		if (!clazz.isInterface()) {
			if (Modifier.isPublic(clazz.getModifiers())) {
				extractGetterMethods(methodMap, clazz);
				return;
			}

			Class<?> superClass = clazz.getSuperclass();
			if (superClass != Object.class) {
				extractGetterMethods(methodMap, superClass);
			}

			Class<?>[] superInterfaces = clazz.getInterfaces();
			for (Class<?> superInterface : superInterfaces) {
				extractGetterMethods(methodMap, superInterface);
			}
		} else {
			extractGetterMethods(methodMap, clazz);
			Class<?>[] superInterfaces = clazz.getInterfaces();
			for (Class<?> superInterface : superInterfaces) {
				add(methodMap, superInterface);
			}
		}
	}

	private void extractGetterMethods(Map<String, Method> methodMap, Class<?> clazz) {
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (!isCandidateMethod(method, clazz)) {
				continue;
			}

			String propertyName = getPropertyName(method);
			if (propertyName != null) {
				methodMap.put(propertyName, method);
			}
		}
	}

	private boolean isCandidateMethod(Method method, Class<?> clazz) {
		if (method.getParameterTypes().length > 0) {
			return false;
		}

		if (!clazz.isInterface()) {
			if (!Modifier.isPublic(method.getModifiers())) {
				return false;
			}

			if (Modifier.isAbstract(method.getModifiers())) {
				return false;
			}
		}

		Class<?> returnType = method.getReturnType();
		if (void.class == returnType || Void.class == returnType) {
			return false;
		}

		if (method.getAnnotation(MonitoringIgnoreProperty.class) != null) {
			return false;
		}

		return true;
	}

	private String getPropertyName(Method method) {
		String methodName = method.getName();
		String propertyName;
		if (methodName.startsWith("get")) {
			propertyName = methodName.substring(3);
		} else if (methodName.startsWith("is")) {
			Class<?> returnType = method.getReturnType();
			if (Boolean.class != returnType && boolean.class != returnType) {
				return null;
			}

			propertyName = methodName.substring(2);
		} else {
			return null;
		}
		
		propertyName = propertyName.substring(0, 1).toLowerCase(ENGLISH) + propertyName.substring(1);
		if ("class".equals(propertyName)) {
			return null;
		}

		MonitoringProperty monitoringProperty = method.getAnnotation(MonitoringProperty.class);
		if (monitoringProperty != null) {
			if (StringUtils.isNotEmpty(monitoringProperty.name())) {
				propertyName = monitoringProperty.name();
			}
		} else if (!inclusive) {
			return null;
		}

		return propertyName;
	}
}
