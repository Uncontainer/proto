package com.pulsarang.infra.monitoring.collector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.remote.RemoteServiceBulkProxy;
import com.pulsarang.infra.remote.RemoteServiceResponse;
import com.pulsarang.infra.remote.RemoteServiceResponseList;
import com.pulsarang.infra.remote.RemoteServiceResponseListHolder;
import com.pulsarang.infra.remote.reload.ReloadService;
import com.pulsarang.infra.server.Server;

public class RemoteMonitorablePropertyCollector {
	ReloadService proxy;

	public RemoteMonitorablePropertyCollector(String solutionName) {
		this(InfraContextFactory.getInfraContext().getServerContext().getSolution(solutionName).getServers());
	}

	public RemoteMonitorablePropertyCollector(String solutionName, String projectName) {
		this(InfraContextFactory.getInfraContext().getServerContext().getServersOfProject(solutionName, projectName));
	}

	public RemoteMonitorablePropertyCollector(List<Server> servers) {
		proxy = RemoteServiceBulkProxy.newServerProxy(ReloadService.class, MonitorablePropertyCollector.NAME, servers, 5000);
	}

	public Map<Long, List<Map<String, Object>>> getHistory(List<String> propertyNames, int fetchCount) {
		MapModel option = new MapModel();
		option.setInteger(MonitorablePropertyCollector.PARAM_FETCH_COUNT, fetchCount);
		if (propertyNames != null) {
			option.setList(MonitorablePropertyCollector.PARAM_PROPERTY_NAMES, propertyNames);
		}

		try {
			proxy.list(option);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		RemoteServiceResponseList responses = RemoteServiceResponseListHolder.getAndClear();

		Map<Long, List<Map<String, Object>>> result = new HashMap<Long, List<Map<String, Object>>>();

		for (RemoteServiceResponse response : responses.getSuccessEntries()) {
			List<Map<String, Object>> item = (List<Map<String, Object>>) response.getResult();

			for (Map<String, Object> map : item) {
				Long createTime = (Long) map.get(MonitorablePropertyCollector.PARAM_CREATE_TIME);
				if (createTime == null || createTime == 0L) {
					continue;
				}

				List<Map<String, Object>> list = result.get(createTime);
				if (list == null) {
					list = new ArrayList<Map<String, Object>>();
					result.put(createTime, list);
				}

				list.add(map);
			}
		}

		return result;
	}

	public List<Map<String, Object>> getProperties(List<String> propertyNames) {
		MapModel option = new MapModel();
		if (propertyNames != null) {
			option.setList(MonitorablePropertyCollector.PARAM_PROPERTY_NAMES, propertyNames);
		}

		try {
			proxy.get(option);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		RemoteServiceResponseList responses = RemoteServiceResponseListHolder.getAndClear();

		List<RemoteServiceResponse> successEntries = responses.getSuccessEntries();

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(successEntries.size());
		for (RemoteServiceResponse responseEntry : successEntries) {
			Map<String, Object> invokeResponse = (MapModel) responseEntry.getResult();
			result.add(invokeResponse);
		}

		return result;
	}

	public <T> List<T> getPropery(String propertyName, Class<T> clazz) {
		List<Map<String, Object>> serverPropertiesList = getProperties(Arrays.asList(propertyName));

		boolean isMapModel = MapModel.class.isAssignableFrom(clazz);

		List<T> result = new ArrayList<T>(serverPropertiesList.size());
		for (Map<String, Object> serverProperties : serverPropertiesList) {
			Object objServerProperty = serverProperties.get(propertyName);
			if (objServerProperty == null) {
				continue;
			}

			if (isMapModel && objServerProperty instanceof Map) {
				result.add((T) MapModel.fromMap((Map) objServerProperty, (Class) clazz));
			} else {
				result.add((T) objServerProperty);
			}
		}

		return result;
	}
}
