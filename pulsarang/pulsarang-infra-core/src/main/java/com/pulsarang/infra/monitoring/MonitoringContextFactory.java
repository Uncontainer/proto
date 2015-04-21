package com.pulsarang.infra.monitoring;

public class MonitoringContextFactory {

	private static MonitoringContext monitoringContext = new MonitoringContext();

	public static MonitoringContext getMonitoringContext() {
		return monitoringContext;
	}
}
