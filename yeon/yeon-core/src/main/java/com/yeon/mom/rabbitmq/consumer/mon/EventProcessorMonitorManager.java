package com.yeon.mom.rabbitmq.consumer.mon;

import com.yeon.mom.EventProcessor;
import com.yeon.monitor.collector.Mergable;
import com.yeon.monitor.collector.Monitorable;
import com.yeon.remote.reload.AbstractReloadService;
import com.yeon.util.MapModel;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author pulsarang
 */
public class EventProcessorMonitorManager extends AbstractReloadService implements Monitorable<Map<String, MapModel>> {
	public static final String NAME = "EVENT_PROCESSORS";

	ConcurrentHashMap<String, EventProcessorMonitor> items = new ConcurrentHashMap<String, EventProcessorMonitor>();

	public EventProcessorMonitorManager() {
	}

	public synchronized EventProcessorMonitor add(EventProcessor eventProcessor) {
		EventProcessorMonitor monitor = new EventProcessorMonitor(eventProcessor);
		EventProcessorMonitor previousItem = items.put(monitor.getCanonicalName(), monitor);
		if (previousItem != null) {
			throw new IllegalStateException("EventProcessorMonitor has already registered.(" + monitor.getCanonicalName() + ")");
		}

		return monitor;
	}

	public EventProcessorMonitor get(String name) {
		return items.get(name);
	}

	@Override
	public Map<String, MapModel> createShapshot() {
		Map<String, MapModel> snapshot = new HashMap<String, MapModel>(items.size());
		for (Map.Entry<String, EventProcessorMonitor> entry : items.entrySet()) {
			snapshot.put(entry.getKey(), entry.getValue().toEntry());
		}

		return snapshot;
	}

	public static Mergable<Map<String, Map<String, Object>>> getMergable() {
		return new Mergable<Map<String, Map<String, Object>>>() {
			@Override
			public Map<String, Map<String, Object>> merge(Map<String, Map<String, Object>> source, Map<String, Map<String, Object>> target) {
				for (Map.Entry<String, Map<String, Object>> targetEntry : target.entrySet()) {
					String processorName = targetEntry.getKey();
					Map<String, Object> map = source.get(processorName);
					if (map == null) {
						source.put(processorName, targetEntry.getValue());
						continue;
					}

					EventProcessorMonitorEntry targetProcessorStat = new EventProcessorMonitorEntry(targetEntry.getValue());
					new EventProcessorMonitorEntry(map).merge(targetProcessorStat);
				}

				return source;
			}
		};
	}

	@Override
	public List<MapModel> list(MapModel optionMap) {
		return new ArrayList<MapModel>(createShapshot().values());
	}

	@Override
	public int listCount(MapModel optionMap) {
		return items.size();
	}

	@Override
	public int remove(MapModel optionMap) {
		String id = optionMap.getString("id");
		if (StringUtils.isBlank(id)) {
			for (EventProcessorMonitor monitor : items.values()) {
				monitor.clearCount();
			}

			return items.size();
		} else {
			EventProcessorMonitor monitor = items.get(id);
			if (monitor != null) {
				monitor.clearCount();
				return 1;
			}

			return 0;
		}
	}

	public static Mergable<Map<String, Object>> getLastPopTimeMergable() {
		Mergable<Map<String, Object>> mergable = new Mergable<Map<String, Object>>() {
			@Override
			public Map<String, Object> merge(Map<String, Object> source, Map<String, Object> target) {
				for (Map.Entry<String, Object> targetLastPopTimeEntry : target.entrySet()) {
					Number sourceLastPopTime = (Number) source.get(targetLastPopTimeEntry.getKey());
					Number tagetLastPopTime = (Number) targetLastPopTimeEntry.getValue();
					if (sourceLastPopTime == null || (tagetLastPopTime != null && sourceLastPopTime.longValue() < tagetLastPopTime.longValue())) {
						source.put(targetLastPopTimeEntry.getKey(), tagetLastPopTime.longValue());
					}
				}

				return source;
			}
		};

		return mergable;
	}
}
