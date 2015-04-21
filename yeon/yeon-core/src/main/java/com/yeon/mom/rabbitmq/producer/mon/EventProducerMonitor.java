package com.yeon.mom.rabbitmq.producer.mon;

import com.yeon.mom.event.Event;
import com.yeon.mom.event.EventProcessOption;
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
 * @author EC셀러서비스개발팀
 */
public class EventProducerMonitor extends AbstractReloadService implements Monitorable<Map<String, MapModel>> {
	public static final String NAME = "EVENT_PRODUCER_MONITOR";

	private final ConcurrentHashMap<String, EventPublishStat> eventPublishStats = new ConcurrentHashMap<String, EventPublishStat>();

	public void create(Event event) {
		getStat(event).increaseCreateCount();
	}

	public void queue(Event event) {
		getStat(event).increaseQueuingCount();
	}

	public void success(Event event) {
		getStat(event).increaseSuccessCount();
	}

	public void fail(Event event) {
		getStat(event).increaseFailCount();
	}

	public void remove(Event event) {
		getStat(event).increaseRemoveCount();
	}

	public EventPublishStat getStat(Event event) {
		String id;
		EventProcessOption eventProcessOption = event.getEventProcessOption();
		if (eventProcessOption != null) {
			id = eventProcessOption.getProcessorName();
		} else {
			id = event.getEventType().getCode();
		}

		EventPublishStat stat = eventPublishStats.get(id);
		if (stat == null) {
			stat = new EventPublishStat(id);
			EventPublishStat previous = eventPublishStats.putIfAbsent(id, stat);
			if (previous != null) {
				stat = previous;
			}
		}

		return stat;
	}

	@Override
	public Map<String, MapModel> createShapshot() {
		Map<String, MapModel> snapshot = new HashMap<String, MapModel>(eventPublishStats.size());
		for (EventPublishStat eventPublishStat : eventPublishStats.values()) {
			snapshot.put(eventPublishStat.getId(), eventPublishStat.toEntry());
		}

		return snapshot;
	}

	public static Mergable<Map<String, Map<String, Object>>> getMergable() {
		return new Mergable<Map<String, Map<String, Object>>>() {
			@Override
			public Map<String, Map<String, Object>> merge(Map<String, Map<String, Object>> source, Map<String, Map<String, Object>> target) {
				for (Map.Entry<String, Map<String, Object>> targetEntry : target.entrySet()) {
					String id = targetEntry.getKey();
					Map<String, Object> map = source.get(id);
					if (map == null) {
						source.put(id, targetEntry.getValue());
						continue;
					}

					EventPublishStatEntry targetItem = new EventPublishStatEntry(targetEntry.getValue());
					new EventPublishStatEntry(map).merge(targetItem);
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
		return eventPublishStats.size();
	}

	@Override
	public int remove(MapModel optionMap) {
		String name = optionMap.getString("id");
		if (StringUtils.isBlank(name)) {
			int size = eventPublishStats.size();
			eventPublishStats.clear();

			return size;
		} else {
			EventPublishStat monitor = eventPublishStats.get(name);
			if (monitor != null) {
				monitor.clearCount();
				return 1;
			}

			return 0;
		}
	}
}
