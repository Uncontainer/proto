package com.yeon.monitor.collector;

import com.yeon.YeonContext;
import com.yeon.remote.bulk.*;
import com.yeon.remote.reload.ReloadService;
import com.yeon.server.ProjectId;
import com.yeon.server.Server;
import com.yeon.util.MapModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 원격 서버의 모니터링 값들을 수집한다.
 * 
 * @author pulsarang
 */
public class RemoteMonitorablePropertyCollector {
	private static final Logger LOG = LoggerFactory.getLogger(RemoteMonitorablePropertyCollector.class);

	ReloadService invoker;

	public RemoteMonitorablePropertyCollector(String solutionName) {
		this(YeonContext.getServerContext().getSolutionSafely(solutionName).getServers());
	}

	public RemoteMonitorablePropertyCollector(String solutionName, String projectName) {
		this(YeonContext.getServerContext().getServersOfProject(solutionName, projectName));
	}

	public RemoteMonitorablePropertyCollector(ProjectId projectId) {
		this(YeonContext.getServerContext().getServersOfProject(projectId));
	}

	public RemoteMonitorablePropertyCollector(List<Server> servers) {
		invoker = RemoteServiceBulkProxy.newServerProxy(ReloadService.class, LocalMonitorablePropertyCollector.NAME, servers, 5000);
	}

	public Map<Long, List<Map<String, Object>>> getHistory(List<String> propertyNames, int fetchCount) {
		MapModel optionMap = new MapModel();
		optionMap.put(LocalMonitorablePropertyCollector.PARAM_FETCH_COUNT, fetchCount);
		if (propertyNames != null) {
			optionMap.put(LocalMonitorablePropertyCollector.PARAM_PROPERTY_NAMES, propertyNames);
		}

		HistoryMerger merger = new HistoryMerger();
		try {
			invoker.list(optionMap);
		} finally {
			RemoteServiceResponseListHolder.getAndClear().iterate(merger);
		}

		return merger.getResult();
	}

	public List<Map<String, Object>> getProperties(List<String> propertyNames) {
		MapModel optionMap = new MapModel();
		if (propertyNames != null) {
			optionMap.put(LocalMonitorablePropertyCollector.PARAM_PROPERTY_NAMES, propertyNames);
		}

		SuccessGetResultMergingIterator<Map<String, Object>> merger = new SuccessGetResultMergingIterator<Map<String, Object>>();
		try {
			invoker.get(optionMap);
		} finally {
			RemoteServiceResponseListHolder.getAndClear().iterate(merger);
		}

		return merger.getMergedResult();
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

	/**
	 * 
	 * @author pulsarang
	 */
	private static class HistoryMerger implements ResponseIterator<List<Map<String, Object>>> {
		Map<Long, List<Map<String, Object>>> result = new HashMap<Long, List<Map<String, Object>>>();

		public Map<Long, List<Map<String, Object>>> getResult() {
			return result;
		}

		@Override
		public void nextSuccess(Server server, List<Map<String, Object>> item) {
			for (Map<String, Object> map : item) {
				Long createTime = (Long) map.get(LocalMonitorablePropertyCollector.PARAM_CREATE_TIME);
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

		@Override
		public void nextFail(Server server, String errorCode, String errorMessage) {
			LOG.info("[YEON] Fail to collect monitoring information.({})\n{}", server, errorMessage);
		}

		@Override
		public void nextSkip(Server server, RemoteServiceInvokeSkipReason skipReason) {
			LOG.debug("[YEON] Skip to call rpc '{}'.({})", server, skipReason.getText());
		}
	}
}
