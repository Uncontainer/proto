package com.pulsarang.infra.monitoring.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.Disposable;
import com.pulsarang.infra.InfraContext;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.monitoring.AbstractMonitor;
import com.pulsarang.infra.monitoring.ProjectInfo;
import com.pulsarang.infra.remote.RemoteContext;
import com.pulsarang.infra.remote.annotation.RemoteService;

/**
 * 
 * @author pulsarang
 */
@RemoteService(name = SystemResourceMonitor.NAME)
public class SystemResourceMonitor extends AbstractMonitor implements Disposable {
	private final Logger log = LoggerFactory.getLogger(SystemResourceMonitor.class);

	public static final String NAME = "SYSTEM_MONITOR";

	private DataCollectionScheduler scheduler;

	private volatile List<SystemMonitoringPropertyInfo> monitoringPropertyInfos;
	private volatile Entry[] record;
	private final CPUUsageMBean cpuUsageBean;

	/**
	 * {@link MomConfigContext}에 의해 초기화.
	 */
	public SystemResourceMonitor() {
		super();

		// MomMBeanFactory 초기화
		MomMBeanFactory.getInstance();

		InfraContext infraContext = InfraContextFactory.getInfraContext();

		RemoteContext rpcContext = infraContext.getRemoteContext();

		rpcContext.setRemoteService(this);
		rpcContext.setRemoteService(new SystemMonitoringItemMetaInfoProvider());

		infraContext.addDisposable(this);

		cpuUsageBean = new CPUUsageMBean();
		MomMBeanFactory.getInstance().addMBean("cpu", cpuUsageBean);
	}

	public Object getProperty(String beanName, String propertyName) {
		return MomMBeanFactory.getInstance().getProperty(beanName, propertyName);
	}

	private void collectRecord(Entry entry, long startTime) {
		entry.reset(startTime);
		cpuUsageBean.refresh();

		MomMBeanFactory factory = MomMBeanFactory.getInstance();
		List<SystemMonitoringPropertyInfo> snapshot = monitoringPropertyInfos;
		for (SystemMonitoringPropertyInfo monitoringProperty : snapshot) {
			try {
				Object property = factory.getProperty(monitoringProperty.getBeanName(), monitoringProperty.getPropertyName());
				entry.addPropery(monitoringProperty.getCanonicalName(), property);
			} catch (Throwable t) {
				log.info("[MOM] Fail to collect system property.", t);
			}
		}
	}

	@Override
	public List<MapModel> list(MapModel option) {
		long fromTime = option.getLong("fromTime", 0L);

		List<MapModel> list = new ArrayList<MapModel>(record.length);

		long currentTime = System.currentTimeMillis() / duration;
		int index = (int) (currentTime % count);
		currentTime *= duration;
		long outdatedTime = currentTime - count * duration;

		synchronized (this) {
			if (record[index].getStartTime() != currentTime) {
				collectRecord(record[index], currentTime);
			}
		}

		for (int i = 0; i < count; i++) {
			if (record[index].getStartTime() <= outdatedTime) {
				break;
			}

			if (record[index].getStartTime() < fromTime) {
				break;
			}

			MapModel map = new MapModel(record[index].getProperties());
			map.setLong("startTime", record[index].startTime);
			map.setLong("duration", duration);
			list.add(map);

			index = (index - 1 + count) % count;
		}

		return list;
	}

	@Override
	public int listCount(MapModel option) {
		return record.length;
	}

	@Override
	public MapModel get(MapModel option) {
		String propertyName = option.getString("propertyName");
		if (propertyName == null) {
			return null;
		}

		String[] tokens = propertyName.split("\\.");
		if (tokens == null || tokens.length < 2) {
			throw new IllegalArgumentException("Invalid monitorig property name '" + propertyName + "'. Expect {beanName}.{propertyName}");
		}

		Object value = getProperty(tokens[0], tokens[1]);
		if (tokens.length > 2) {
			for (int i = 2; i < tokens.length; i++) {
				if (value instanceof Map) {
					value = ((Map<?, ?>) value).get(tokens[i]);
				} else {
					String path = StringUtils.join(tokens, ".");
					throw new IllegalArgumentException("'" + path + "' is not a map");
				}
			}
		}

		return wrapProperty(value);
	}

	public static MapModel wrapProperty(Object value) {
		MapModel model = new MapModel();
		model.setProperty("property", value);

		return model;
	}

	public static Object unwrapProperty(Map<String, Object> map) {
		if (map == null) {
			return null;
		}

		return map.get("property");
	}

	@Override
	protected void validateLocal(ProjectInfo ticket) {
		getMonitoringProperties(ticket);
	}

	@Override
	protected void initializeCompletedLocal(ProjectInfo ticket) {
		monitoringPropertyInfos = getMonitoringProperties(ticket);
	}

	@Override
	protected void initializeCompletedLocal(int count, int duration, ProjectInfo ticket) {
		this.initializeCompletedLocal(ticket);

		if (scheduler == null) {
			scheduler = new DataCollectionScheduler();
		}
		scheduler.init();

		Entry[] newRecord = new Entry[count];
		for (int i = 0; i < count; i++) {
			newRecord[i] = new Entry();
		}

		this.record = newRecord;
	}

	@Override
	public void dispose() throws Exception {
		scheduler.destroy();
	}

	private List<SystemMonitoringPropertyInfo> getMonitoringProperties(ProjectInfo ticket) {
		List<String> monitoringPropertyNames = ticket.getMonitoringProperties();

		if (monitoringPropertyNames == null || monitoringPropertyNames.isEmpty()) {
			return Collections.emptyList();
		}

		List<SystemMonitoringPropertyInfo> newMonitoringPropertyInfos = new ArrayList<SystemMonitoringPropertyInfo>(monitoringPropertyNames.size());
		for (String monitoringPropertyName : monitoringPropertyNames) {
			String[] tokens = monitoringPropertyName.split("\\.");
			if (tokens == null || tokens.length != 2) {
				throw new IllegalArgumentException("Invalid monitorig property name '" + monitoringPropertyName
						+ "'. Expect {beanName}.{propertyName}");
			}

			SystemMonitoringPropertyInfo property = new SystemMonitoringPropertyInfo(tokens[0], tokens[1]);
			newMonitoringPropertyInfos.add(property);
		}

		return newMonitoringPropertyInfos;
	}

	private static class Entry {
		private long startTime;
		private final Map<String, Object> properties = new HashMap<String, Object>();

		public void reset(long newStartTime) {
			this.startTime = newStartTime;
			properties.clear();
		}

		public long getStartTime() {
			return startTime;
		}

		public void addPropery(String name, Object value) {
			properties.put(name, value);
		}

		public Map<String, Object> getProperties() {
			return properties;
		}
	}

	private class DataCollectionScheduler implements Runnable {
		private ScheduledThreadPoolExecutor dataCollectionScheduler = new ScheduledThreadPoolExecutor(1);
		private boolean running = false;

		public void init() {
			if (dataCollectionScheduler != null && !dataCollectionScheduler.isShutdown()) {
				dataCollectionScheduler.shutdownNow();
			}

			dataCollectionScheduler = new ScheduledThreadPoolExecutor(2);
			scheduleNext();
		}

		public void destroy() {
			dataCollectionScheduler.shutdownNow();
		}

		@Override
		public void run() {
			synchronized (this) {
				// 이전 요청이 아직 수행되고 있다는 작업을 진행하지 않는다.
				if (running) {
					log.debug("[MOM] Skip collection system monitoring infomation.");
					return;
				}

				running = true;
			}

			// log.debug("Collect system monitoring infomation...");

			try {
				long startTime = System.currentTimeMillis() / duration;
				int index = (int) (startTime % count);
				startTime = startTime * duration;

				Entry entry;
				synchronized (this) {
					if (index < record.length) {
						entry = record[index];
					} else {
						return;
					}
				}

				collectRecord(entry, startTime);
			} catch (Throwable t) {
				log.info("[MOM] Fail to collect system resource monitoring data.", t);
			} finally {
				synchronized (this) {
					running = false;
				}

				scheduleNext();
			}
		}

		private void scheduleNext() {
			long sleepTime = duration - (System.currentTimeMillis() % duration);
			dataCollectionScheduler.schedule(this, sleepTime, TimeUnit.MILLISECONDS);
		}
	}
}
