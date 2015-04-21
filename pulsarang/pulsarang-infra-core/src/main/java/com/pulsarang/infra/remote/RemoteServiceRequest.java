package com.pulsarang.infra.remote;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.pulsarang.core.util.MapModel;

public class RemoteServiceRequest extends MapModel {
	public static final String TARGET_NAME = "_target_name";
	public static final String METHOD_NAME = "_operation_name";
	public static final String METHOD_PARAMETERS = "_method_parameters";
	public static final String CALLER_IP = "_caller_ip";

	public RemoteServiceRequest() {
	}

	public RemoteServiceRequest(MapModel model) {
		super(model);
	}

	public String getTarget() {
		return getString(TARGET_NAME);
	}

	public void setTarget(String target) {
		setString(TARGET_NAME, target);
	}

	public String getMethodName() {
		return getString(METHOD_NAME);
	}

	public void setMethodName(String methodName) {
		setString(METHOD_NAME, methodName);
	}

	public String getCallerIp() {
		return getString(CALLER_IP);
	}

	public void setCallerIp(String CallerIp) {
		setString(CALLER_IP, CallerIp);
	}

	public void setParameres(Object[] args) {
		if (args == null || args.length == 0) {
			return;
		}

		setList(METHOD_PARAMETERS, Arrays.asList(args));
	}

	public Object[] getParameres(Class<?>[] parameterTypes) {
		List<Object> list = getList(METHOD_PARAMETERS);
		if (list == null) {
			return null;
		}

		if (list.size() != parameterTypes.length) {
			throw new IllegalArgumentException("Parameter size is different.");
		}

		Object[] params = new Object[list.size()];
		for (int i = 0; i < list.size(); i++) {
			Object object = list.get(i);
			if (parameterTypes[i].isAssignableFrom(MapModel.class)) {
				@SuppressWarnings("unchecked")
				Class<? extends MapModel> clazz = (Class<? extends MapModel>) parameterTypes[i];
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (object == null) ? Collections.EMPTY_MAP : (Map<String, Object>) object;

				object = MapModel.fromMap(map, clazz);
			}

			params[i] = object;
		}

		return params;
	}
}
