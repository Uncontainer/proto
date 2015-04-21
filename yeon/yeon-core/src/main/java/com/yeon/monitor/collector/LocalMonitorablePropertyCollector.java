package com.yeon.monitor.collector;

import com.yeon.mom.MomConstants;
import com.yeon.monitor.MomScheduledExecutor;
import com.yeon.remote.reload.AbstractReloadService;
import com.yeon.util.MapModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 모니터링을 필요한 속성값들을 주기적으로 수집하여, 메모리에 최대 지정된 갯수(10개)만큼 저장한다.
 * 
 * @see Monitorable
 * @see RemoteMonitorablePropertyCollector
 * 
 * @author pulsarang
 */
public class LocalMonitorablePropertyCollector extends AbstractReloadService implements Runnable {
	private final Logger log = LoggerFactory.getLogger(LocalMonitorablePropertyCollector.class);

	public static final String NAME = "MONITORING_PROPERTY_COLLECTOR";

	public static final String PARAM_FETCH_COUNT = "fetchCount";
	public static final String PARAM_PROPERTY_NAMES = "propertyNames";
	public static final String PARAM_CREATE_TIME = "__createTime";

	@SuppressWarnings("rawtypes")
	private final Map<String, Monitorable> monitorables = new ConcurrentHashMap<String, Monitorable>();
	private boolean running = false;

	private final Snapshot[] snapshots;

	static final LocalMonitorablePropertyCollector INSTANCE = new LocalMonitorablePropertyCollector();

	public static LocalMonitorablePropertyCollector getInstance() {
		return INSTANCE;
	}

	LocalMonitorablePropertyCollector() {
		this.snapshots = new Snapshot[MomConstants.MONITORING_SNAPSHOT_KEEPING_COUNT];
		for (int i = 0; i < snapshots.length; i++) {
			this.snapshots[i] = new Snapshot();
		}
	}

	public void start() {
		MomScheduledExecutor.scheduleAtPeriodic(this, MomConstants.MONITORING_INTERVAL, 0L);
	}

	public void setMonitorable(String name, @SuppressWarnings("rawtypes") Monitorable monitorable) {
		if (monitorable == null) {
			throw new IllegalArgumentException("Null monitorable.");
		}

		monitorables.put(name, monitorable);
	}

	@Override
	public void run() {
		synchronized (this) {
			// 이전 요청이 아직 수행되고 있다면 작업을 진행하지 않는다.
			if (running) {
				log.debug("[YEON] Skip collecting monitoring infomation.");
				return;
			}

			running = true;
		}

		try {
			long startTime = System.currentTimeMillis() / MomConstants.MONITORING_INTERVAL;
			int index = (int) (startTime % snapshots.length);
			startTime *= MomConstants.MONITORING_INTERVAL;

			Snapshot snapshot = snapshots[index];
			snapshot.reset(startTime);
			for (@SuppressWarnings("rawtypes")
			Map.Entry<String, Monitorable> entry : monitorables.entrySet()) {
				Object property = entry.getValue().createShapshot();
				snapshot.setValue(entry.getKey(), property);
			}
		} catch (Throwable t) {
			log.info("[YEON] Fail to collect system resource monitoring data.", t);
		} finally {
			synchronized (this) {
				running = false;
			}

			MomScheduledExecutor.scheduleAtPeriodic(this, MomConstants.MONITORING_INTERVAL, 0L);
		}
	}

	@Override
	public List<MapModel> list(MapModel optionMap) {
		int fetchCount = optionMap.getInteger(PARAM_FETCH_COUNT, 2);
		if (fetchCount > snapshots.length) {
			fetchCount = snapshots.length;
		}

		List<String> propertyNames = getPropertyNames(optionMap);

		long startTime = System.currentTimeMillis() / MomConstants.MONITORING_INTERVAL;
		int index = (int) (startTime % snapshots.length);
		startTime *= MomConstants.MONITORING_INTERVAL;
		if (snapshots[index].createTime != startTime) {
			// snapshot을 생성하는 시간과 가져가는 시간이 겹칠 경우...
			startTime = startTime - MomConstants.MONITORING_INTERVAL;
			index = (index - 1 + snapshots.length) % snapshots.length;
		}
		long outdatedTime = startTime - snapshots.length * MomConstants.MONITORING_INTERVAL;

		List<MapModel> list = new ArrayList<MapModel>(snapshots.length);
		for (int i = 0; i < fetchCount; i++) {
			Snapshot snapshot = snapshots[index];
			if (snapshot.createTime <= outdatedTime) {
				break;
			}

			MapModel map;
			if (propertyNames == null) {
				map = new MapModel(snapshot.getValues());
			} else {
				map = new MapModel();
				map.put(PARAM_CREATE_TIME, snapshot.createTime);
				for (String propertyName : propertyNames) {
					map.put(propertyName, snapshot.getValue(propertyName));
				}
			}
			list.add(map);

			index = (index - 1 + snapshots.length) % snapshots.length;
		}

		return list;
	}

	@Override
	public int listCount(MapModel optionMap) {
		return snapshots.length;
	}

	@Override
	public MapModel get(MapModel optionMap) {
		Snapshot snapshot = new Snapshot();
		snapshot.setCreateTime(System.currentTimeMillis());

		List<String> propertyNames = getPropertyNames(optionMap);

		if (propertyNames == null) {
			for (@SuppressWarnings("rawtypes")
			Map.Entry<String, Monitorable> entry : monitorables.entrySet()) {
				Object property = entry.getValue().createShapshot();
				snapshot.setValue(entry.getKey(), property);
			}
		} else {
			for (String propertyName : propertyNames) {
				@SuppressWarnings("rawtypes")
				Monitorable monitorable = monitorables.get(propertyName);
				if (monitorable == null) {
					continue;
				}

				Object property = monitorable.createShapshot();
				snapshot.setValue(propertyName, property);
			}
		}

		return snapshot;
	}

	private List<String> getPropertyNames(Map<String, Object> optionMap) {
		if (optionMap == null) {
			return null;
		}

		@SuppressWarnings("unchecked")
		//
		List<String> candidatePropertyNames = (List<String>) optionMap.get(PARAM_PROPERTY_NAMES);
		if (candidatePropertyNames == null) {
			return null;
		}

		List<String> propertyNames = new ArrayList<String>(candidatePropertyNames.size());
		for (String candidatePropertyName : candidatePropertyNames) {
			if (monitorables.containsKey(candidatePropertyName)) {
				propertyNames.add(candidatePropertyName);
			}
		}

		if (propertyNames.isEmpty()) {
			return null;
		}

		return propertyNames;
	}
}
