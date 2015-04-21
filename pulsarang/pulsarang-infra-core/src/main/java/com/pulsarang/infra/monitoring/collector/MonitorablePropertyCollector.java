package com.pulsarang.infra.monitoring.collector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.Disposable;
import com.pulsarang.infra.remote.reload.AbstractReloadService;

public class MonitorablePropertyCollector extends AbstractReloadService implements Runnable, Disposable {
	private final Logger log = LoggerFactory.getLogger(MonitorablePropertyCollector.class);

	public static final String NAME = "MONITORING_PROPERTY_COLLECTOR";

	public static final String PARAM_FETCH_COUNT = "fetchCount";
	public static final String PARAM_PROPERTY_NAMES = "propertyNames";
	public static final String PARAM_CREATE_TIME = "__createTime";

	private final ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(2);
	@SuppressWarnings("rawtypes")
	private final Map<String, Monitorable> monitorables = new ConcurrentHashMap<String, Monitorable>();
	private boolean running = false;

	protected int count = 10;
	public static final int duration = (int) TimeUnit.SECONDS.toMillis(30);
	private final Snapshot[] snapshots;

	static MonitorablePropertyCollector INSTANCE = new MonitorablePropertyCollector();

	public static MonitorablePropertyCollector getInstance() {
		return INSTANCE;
	}

	MonitorablePropertyCollector() {
		this.snapshots = new Snapshot[count];
		for (int i = 0; i < count; i++) {
			this.snapshots[i] = new Snapshot();
		}
	}

	public void start() {
		scheduleNext();
	}

	@Override
	public void dispose() throws Exception {
		scheduler.shutdownNow();
	}

	public void setMonitorable(String name, @SuppressWarnings("rawtypes") Monitorable monitorable) {
		monitorables.put(name, monitorable);
	}

	@Override
	public void run() {
		synchronized (this) {
			// 이전 요청이 아직 수행되고 있다면 작업을 진행하지 않는다.
			if (running) {
				log.debug("[MOM] Skip collecting monitoring infomation.");
				return;
			}

			running = true;
		}

		try {
			long startTime = System.currentTimeMillis() / duration;
			int index = (int) (startTime % count);
			startTime *= duration;

			Snapshot snapshot = snapshots[index];
			snapshot.reset(startTime);
			for (@SuppressWarnings("rawtypes")
			Map.Entry<String, Monitorable> e : monitorables.entrySet()) {
				Object property = e.getValue().createShapshot();
				snapshot.setProperty(e.getKey(), property);
			}
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
		scheduler.schedule(this, sleepTime, TimeUnit.MILLISECONDS);
	}

	@Override
	public List<MapModel> list(MapModel option) {
		int fetchCount = option.getInteger(PARAM_FETCH_COUNT, 2);
		if (fetchCount > count) {
			fetchCount = count;
		}

		List<String> propertyNames = option.getList(PARAM_PROPERTY_NAMES);

		long startTime = System.currentTimeMillis() / duration;
		int index = (int) (startTime % count);
		startTime *= duration;
		if (snapshots[index].createTime != startTime) {
			// snapshot을 생성하는 시간과 가져가는 시간이 겹칠 경우...
			startTime = startTime - duration;
			index = (index - 1 + count) % count;
		}
		long outdatedTime = startTime - count * duration;

		List<MapModel> list = new ArrayList<MapModel>(snapshots.length);
		for (int i = 0; i < fetchCount; i++) {
			Snapshot snapshot = snapshots[index];
			if (snapshot.createTime <= outdatedTime) {
				break;
			}

			MapModel map;
			if (propertyNames == null) {
				map = new MapModel(snapshot);
			} else {
				map = new MapModel();
				map.setLong(PARAM_CREATE_TIME, snapshot.createTime);
				for (String propertyName : propertyNames) {
					map.setProperty(propertyName, snapshot.getProperty(propertyName));
				}
			}

			list.add(map);

			index = (index - 1 + count) % count;
		}

		return list;
	}

	@Override
	public int listCount(MapModel option) {
		return snapshots.length;
	}

	@Override
	public MapModel get(MapModel option) {
		Snapshot snapshot = new Snapshot();
		snapshot.setCreateTime(System.currentTimeMillis());

		List<String> propertyNames = option.getList(PARAM_PROPERTY_NAMES);

		if (propertyNames == null) {
			for (@SuppressWarnings("rawtypes")
			Map.Entry<String, Monitorable> e : monitorables.entrySet()) {
				Object property = e.getValue().createShapshot();
				snapshot.setProperty(e.getKey(), property);
			}
		} else {
			for (String propertyName : propertyNames) {
				@SuppressWarnings("rawtypes")
				Monitorable monitorable = monitorables.get(propertyName);
				if (monitorable == null) {
					continue;
				}

				Object property = monitorable.createShapshot();
				snapshot.setProperty(propertyName, property);
			}
		}

		return snapshot;
	}

	public static class Snapshot extends MapModel {
		long createTime;

		public Snapshot() {
		}

		public Snapshot(Map<String, Object> properties) {
			super(properties);
		}

		void reset(long createTime) {
			properties.clear();
			setCreateTime(createTime);
		}

		public long getCreateTime() {
			return getLong(PARAM_CREATE_TIME, 0L);
		}

		public void setCreateTime(long startTime) {
			this.createTime = startTime;
			setProperty(PARAM_CREATE_TIME, startTime);
		}
	}
}
