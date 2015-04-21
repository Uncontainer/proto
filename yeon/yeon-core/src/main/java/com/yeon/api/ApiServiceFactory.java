package com.yeon.api;

import com.yeon.YeonContext;
import com.yeon.mom.event.EventApiService;
import com.yeon.monitor.MonitorApiService;
import com.yeon.server.ProjectId;

public class ApiServiceFactory {
	static EventApiService eventApiService;
	static MonitorApiService monitorApiService;

	static {
		eventApiService = YeonContext.getRemoteContext().newProjectProxy(EventApiService.class, EventApiService.NAME, ProjectId.INFRA_ADMIN, 3000);
		monitorApiService = YeonContext.getRemoteContext().newProjectProxy(MonitorApiService.class, MonitorApiService.NAME, ProjectId.INFRA_ADMIN, 3000);
	}

	public static EventApiService getEventApiService() {
		return eventApiService;
	}

	public static MonitorApiService getMonitorApiService() {
		return monitorApiService;
	}
}
