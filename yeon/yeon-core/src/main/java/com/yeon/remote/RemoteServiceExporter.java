package com.yeon.remote;

import com.yeon.remote.annotation.RemoteMethod;
import com.yeon.remote.annotation.RemoteService;
import com.yeon.util.NameUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RemoteServiceExporter {
	private final String name;
	private final Object serviceObject;
	private Map<String, MethodInvoker> methodInvokerMap;
	private Date lastRequestedDate;

	public RemoteServiceExporter(Object serviceObject) {
		super();

		if (serviceObject == null) {
			throw new IllegalArgumentException("Null service object.");
		}

		Class<? extends Object> clazz = serviceObject.getClass();

		RemoteService remoteServiceInfo = clazz.getAnnotation(RemoteService.class);
		if (remoteServiceInfo == null) {
			throw new IllegalArgumentException("Fail to find RemoteService annotation.(" + clazz.getCanonicalName() + ")");
		}

		this.name = remoteServiceInfo.name();
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Blank name.(" + serviceObject.getClass().getCanonicalName() + ")");
		}

		this.serviceObject = serviceObject;

		init(clazz);
	}

	public RemoteServiceExporter(String name, Object serviceObject) {
		super();
		if (serviceObject == null) {
			throw new IllegalArgumentException("Null target object.");
		}

		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Blank name.(" + serviceObject.getClass().getCanonicalName() + ")");
		}

		this.name = name;
		this.serviceObject = serviceObject;

		init(serviceObject.getClass());
	}

	private void init(Class<?> clazz) {
		methodInvokerMap = new HashMap<String, RemoteServiceExporter.MethodInvoker>();
		traverse(clazz);
	}

	public String getName() {
		return name;
	}

	public Object getServiceObject() {
		return serviceObject;
	}

	public Date getLastRequestedDate() {
		return lastRequestedDate;
	}

	public void execute(RemoteServiceRequest request, RemoteServiceResponse response) throws Exception {
		lastRequestedDate = new Date();

		String methodName = request.getMethodName();
		MethodInvoker methodInvoker = methodInvokerMap.get(methodName);
		if (methodInvoker == null) {
			throw new IllegalArgumentException("Fail to find method.(" + request.getObjectName() + "." + request.getMethodName() + ")");
		}

		try {
			Object result = methodInvoker.invoke(request);
			response.setResult(result);
		} catch (Throwable t) {
			response.setThrowable(t);
		}
	}

	private void traverse(Class<?> clazz) {
		add(clazz);
		if (!clazz.isInterface()) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != Object.class) {
				add(superClass);
			}
		}

		Class<?>[] superInterfaces = clazz.getInterfaces();
		for (Class<?> superInterface : superInterfaces) {
			add(superInterface);
		}
	}

	private void add(Class<?> clazz) {
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			RemoteMethod annotation = method.getAnnotation(RemoteMethod.class);
			if (annotation == null) {
				continue;
			}

			if (!Modifier.isPublic(method.getModifiers())) {
				throw new IllegalArgumentException("RemoteMethod annotation must be declared at public method.");
			}

			String methodName = method.getName();
			if (!annotation.name().isEmpty()) {
				methodName = annotation.name();
				if (NameUtils.isInvalidJavaIdentifier(methodName)) {
					throw new IllegalArgumentException("Invalid method name '" + methodName + "'.(" + clazz.getCanonicalName() + ")");
				}
			}

			MethodInvoker existMethodInvoker = methodInvokerMap.get(methodName);
			if (existMethodInvoker != null) {
				if (isEqualMethodSignature(existMethodInvoker.method, method)) {
					continue;
				}

				throw new IllegalArgumentException("Duplicated method name '" + methodName + "'.(" + serviceObject.getClass().getCanonicalName() + ")");
			}

			MethodInvoker methodInvoker = new MethodInvoker(serviceObject, method);

			methodInvokerMap.put(methodName, methodInvoker);
		}
	}

	private static boolean isEqualMethodSignature(Method x, Method y) {
		if (!x.getName().equals(y.getName())) {
			return false;
		}

		if (!x.getReturnType().equals(y.getReturnType())) {
			return false;
		}

		Class<?>[] params1 = x.getParameterTypes();
		Class<?>[] params2 = y.getParameterTypes();
		if (params1.length != params2.length) {
			return false;
		}

		for (int i = 0; i < params1.length; i++) {
			if (params1[i] != params2[i]) {
				return false;
			}
		}

		return true;
	}

	static class MethodInvoker {
		private Object targetObject;
		private Method method;

		public MethodInvoker(Object targetObject, Method method) {
			super();
			this.targetObject = targetObject;
			this.method = method;
		}

		public Object invoke(RemoteServiceRequest request) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			// TODO method.getParameterTypes()를 이용한 validation 수행 고려
			return method.invoke(targetObject, request.getParameres(/* method.getParameterTypes() */));
		}
	}
}
