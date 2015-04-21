package com.yeon.mom.rabbitmq.producer.mon;

import com.yeon.YeonContext;
import com.yeon.remote.reload.ReloadService;
import com.yeon.server.Server;
import com.yeon.util.MapModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author EC셀러서비스개발팀
 */
public class EventProducerMonitorInvoker {
	private final Server server;

	public EventProducerMonitorInvoker(Server server) {
		this.server = server;
	}

	public List<EventPublishStatEntry> list() throws Exception {
		ReloadService invoker = YeonContext.getRemoteContext().newServerProxy(ReloadService.class, EventProducerMonitor.NAME, server, 3000);
		List<MapModel> list = invoker.list(null);

		List<EventPublishStatEntry> result = new ArrayList<EventPublishStatEntry>(list.size());
		for (MapModel map : list) {
			EventPublishStatEntry entry = new EventPublishStatEntry();
			entry.setValues(map);

			result.add(entry);
		}

		return result;
	}

	public void clearAll() throws Exception {
		ReloadService invoker = YeonContext.getRemoteContext().newServerProxy(ReloadService.class, EventProducerMonitor.NAME, server, 3000);
		invoker.remove(null);
	}

	public void clear(String id) throws Exception {
		ReloadService invoker = YeonContext.getRemoteContext().newServerProxy(ReloadService.class, EventProducerMonitor.NAME, server, 3000);
		MapModel param = new MapModel();
		param.setString("id", id);

		invoker.remove(param);
	}
}
