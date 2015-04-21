package com.pulsarang.infra.monitoring;

import com.pulsarang.infra.monitoring.access.SystemAccessMonitor;
import com.pulsarang.infra.monitoring.resource.SystemResourceMonitor;

public class MonitoringContext {
	SystemResourceMonitor resourceMonitor;
	SystemAccessMonitor accessMonitor;

	MonitoringContext() {
		resourceMonitor = new SystemResourceMonitor();
		accessMonitor = new SystemAccessMonitor();
	}

	public SystemResourceMonitor getResourceMonitor() {
		return resourceMonitor;
	}

	public SystemAccessMonitor getAccessMonitor() {
		return accessMonitor;
	}
}
