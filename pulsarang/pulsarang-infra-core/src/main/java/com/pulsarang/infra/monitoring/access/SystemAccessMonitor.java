package com.pulsarang.infra.monitoring.access;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.core.util.Tracable;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.monitoring.AbstractMonitor;
import com.pulsarang.infra.monitoring.ProjectInfo;
import com.pulsarang.infra.remote.annotation.RemoteService;

/**
 * 
 * @author pulsarang
 */
@RemoteService(name = SystemAccessMonitor.NAME)
public class SystemAccessMonitor extends AbstractMonitor {
	public static final String NAME = "SYSTEM_TPS_MONITOR";

	private ResponseSuccessDecider successDecider;
	private final ConcurrentHashMap<String, AccessMonitor> methodTPSMonitorMap;

	public SystemAccessMonitor() {
		super();

		successDecider = new DefaultResponseSuccessDecider();
		methodTPSMonitorMap = new ConcurrentHashMap<String, AccessMonitor>();

		InfraContextFactory.getInfraContext().getRemoteContext().setRemoteService(this);
	}

	public int getMonitoringTpsCount() {
		return count;
	}

	public int getMonitoringTpsDuration() {
		return duration;
	}

	public void setResponseSuccessDecider(ResponseSuccessDecider successDecider) {
		if (successDecider == null) {
			this.successDecider = new DefaultResponseSuccessDecider();
		} else {
			this.successDecider = successDecider;
		}
	}

	public void add(String key, Object response, Throwable throwable, Tracable tracable) {
		Boolean success = AccessMonitorContext.getContext().isSuccess();
		if (success == null) {
			success = successDecider.isSuccess(response, throwable);
		}

		getTPSMonitor(key).addResult(tracable, success);
	}

	public void add(String key, Tracable tracable, boolean success) {
		getTPSMonitor(key).addResult(tracable, success);
	}

	private AccessMonitor getTPSMonitor(String key) {
		AccessMonitor methodTPSMonitor = methodTPSMonitorMap.get(key);
		if (methodTPSMonitor == null) {
			methodTPSMonitor = new AccessMonitor(count, duration);
			AccessMonitor temp = methodTPSMonitorMap.putIfAbsent(key, methodTPSMonitor);
			if (temp != null) {
				methodTPSMonitor = temp;
			}
		}

		return methodTPSMonitor;
	}

	@Override
	public List<MapModel> list(MapModel option) {
		List<MapModel> list = new ArrayList<MapModel>(methodTPSMonitorMap.size());

		long fromTime = option.getLong("fromTime", 0L);

		for (Entry<String, AccessMonitor> entry : methodTPSMonitorMap.entrySet()) {
			list.add(item(entry.getKey(), entry.getValue(), fromTime));
		}

		return list;
	}

	private MapModel item(String type, AccessMonitor item, long fromTime) {
		MapModel result = new MapModel();
		result.setString("key", type.toString());
		result.setLong("total", item.getTotal());
		result.setList("status", item.getCurrentStatusByMap(fromTime));

		return result;
	}

	@Override
	public int listCount(MapModel option) {
		return methodTPSMonitorMap.size();
	}

	@Override
	protected void validateLocal(ProjectInfo ticket) {
	}

	@Override
	protected void initializeCompletedLocal(ProjectInfo ticket) {
	}

	@Override
	protected void initializeCompletedLocal(int count, int duration, ProjectInfo ticket) {
		if (methodTPSMonitorMap == null) {
			return;
		}

		List<AccessMonitor> tpsMonotors = new ArrayList<AccessMonitor>(methodTPSMonitorMap.values());
		for (AccessMonitor tpsMonitor : tpsMonotors) {
			tpsMonitor.setCountAndDuration(count, duration);
		}
	}
}
