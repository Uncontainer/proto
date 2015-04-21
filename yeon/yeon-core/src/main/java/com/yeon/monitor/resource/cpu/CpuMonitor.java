package com.yeon.monitor.resource.cpu;

import com.yeon.monitor.MomScheduledExecutor;
import com.yeon.monitor.resource.MomMBeanFactory;
import com.yeon.monitor.resource.annotation.MonitoringBean;
import com.yeon.monitor.resource.annotation.MonitoringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author pulsarang
 */
@MonitoringBean(inclusive = false)
public class CpuMonitor {
	private static final Logger LOG = LoggerFactory.getLogger(CpuMonitor.class);

	public static final int AUTO_REFRESH_INTERVAL = 1000;
	public static final int CACHE_TTL = AUTO_REFRESH_INTERVAL * 2;

	private static final CpuMonitor INSTANCE = new CpuMonitor();

	public static CpuMonitor getInstance() {
		return INSTANCE;
	}

	private final int cpuCount;

	private volatile long processCpuTime;
	private volatile long uptime;
	private volatile long updateTime;

	private volatile double usePercentage;

	private volatile boolean autoUpdate;
	private volatile double minThreshold;

	private final List<ListenerEntry> listenerEntries = new ArrayList<ListenerEntry>();

	private CpuMonitor() {
		cpuCount = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
		minThreshold = Double.MAX_VALUE;
	}

	@MonitoringProperty(name = "usePercentage")
	public double getUsePercentage() {
		if (!autoUpdate) {
			synchronized (this) {
				if ((System.currentTimeMillis() - updateTime) > CACHE_TTL) {
					forceRefresh();
				}
			}
		}

		return usePercentage;
	}

	private void forceRefresh() {
		init();

		try {
			Thread.sleep(AUTO_REFRESH_INTERVAL);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return;
		}

		update();
	}

	private void init() {
		MomMBeanFactory factory = MomMBeanFactory.getInstance();
		processCpuTime = (Long) factory.getProperty("os", "processCpuTime");
		uptime = (Long) factory.getProperty("runtime", "uptime");
	}

	private void update() {
		long prevProcessCpuTime = processCpuTime;
		long prevUptime = uptime;

		init();

		usePercentage = (processCpuTime - prevProcessCpuTime) / ((uptime - prevUptime) * 10000.0) / cpuCount;
		updateTime = System.currentTimeMillis();
	}

	public synchronized void addListener(double threshold, CpuListener listener) {
		listenerEntries.add(new ListenerEntry(threshold, listener));
		if (threshold < minThreshold) {
			minThreshold = threshold;
		}

		if (listenerEntries.size() == 1) {
			autoUpdate = true;
			MomScheduledExecutor.execute(new UsePercentageUpdater());
		}
	}

	public synchronized void removeListener(CpuListener listener) {
		Iterator<ListenerEntry> iter = listenerEntries.iterator();
		double nextMinThreshold = Double.MAX_VALUE;
		while (iter.hasNext()) {
			ListenerEntry entry = iter.next();
			if (entry.listener == listener) {
				iter.remove();
			} else {
				if (entry.threshold < nextMinThreshold) {
					nextMinThreshold = entry.threshold;
				}
			}
		}

		if (listenerEntries.isEmpty()) {
			minThreshold = Double.MAX_VALUE;
			autoUpdate = false;
		} else {
			minThreshold = nextMinThreshold;
		}
	}

	private void autoUpdated() {
		double up = usePercentage;
		if (up < minThreshold) {
			return;
		}

		synchronized (this) {
			for (ListenerEntry entry : listenerEntries) {
				if (up >= entry.threshold) {
					MomScheduledExecutor.execute(new ListenerInvoker(entry.listener, up));
				}
			}
		}
	}

	/**
	 * 
	 * @author pulsarang
	 */
	class UsePercentageUpdater implements Runnable {
		MomMBeanFactory factory = MomMBeanFactory.getInstance();

		UsePercentageUpdater() {
			init();
		}

		@Override
		public void run() {
			while (autoUpdate) {
				try {
					Thread.sleep(AUTO_REFRESH_INTERVAL);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return;
				}

				update();

				autoUpdated();
			}
		}
	}

	/**
	 * 
	 * @author pulsarang
	 */
	static class ListenerEntry {
		private final double threshold;
		private final CpuListener listener;

		ListenerEntry(double threshold, CpuListener listener) {
			super();
			this.threshold = threshold;
			this.listener = listener;
		}
	}

	/**
	 * 
	 * @author pulsarang
	 */
	static class ListenerInvoker implements Runnable {
		private final CpuListener listener;
		private final double value;

		ListenerInvoker(CpuListener listener, double value) {
			super();
			this.listener = listener;
			this.value = value;
		}

		@Override
		public void run() {
			try {
				listener.cpuAlarm(value);
			} catch (Throwable t) {
				LOG.warn("[YEON] Fail to process cpu usage alarm.(" + listener.getClass() + ")", t);
			}
		}
	}
}
