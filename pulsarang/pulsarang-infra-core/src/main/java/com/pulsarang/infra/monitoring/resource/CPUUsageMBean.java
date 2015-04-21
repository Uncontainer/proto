package com.pulsarang.infra.monitoring.resource;

import com.pulsarang.infra.monitoring.resource.annotation.MonitoringBean;

@MonitoringBean(inclusive = true)
public class CPUUsageMBean {
	private long processCpuTime;
	private long uptime;
	private double usePercentage;
	private int cpuCount;

	public double getUsePercentage() {
		return usePercentage;
	}

	public synchronized void refresh() {
		MomMBeanFactory factory = MomMBeanFactory.getInstance();
		if (processCpuTime == 0L) {
			processCpuTime = (Long) factory.getProperty("os", "processCpuTime");
			uptime = (Long) factory.getProperty("runtime", "uptime");
			cpuCount = (Integer) factory.getProperty("os", "availableProcessors");

			return;
		}

		long prevProcessCpuTime = processCpuTime;
		long prevUptime = uptime;

		processCpuTime = (Long) factory.getProperty("os", "processCpuTime");
		uptime = (Long) factory.getProperty("runtime", "uptime");

		usePercentage = (processCpuTime - prevProcessCpuTime) / ((uptime - prevUptime) * 10000.0) / cpuCount;
	}

}
