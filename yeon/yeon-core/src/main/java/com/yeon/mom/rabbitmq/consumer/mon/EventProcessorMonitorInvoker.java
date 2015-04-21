package com.yeon.mom.rabbitmq.consumer.mon;

import com.yeon.YeonContext;
import com.yeon.remote.reload.ReloadService;
import com.yeon.server.Server;
import com.yeon.util.MapModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author EC셀러서비스개발팀
 */
public class EventProcessorMonitorInvoker {
	private ReloadService invoker;

	public EventProcessorMonitorInvoker(Server server) {
		this.invoker = YeonContext.getRemoteContext().newServerProxy(ReloadService.class, EventProcessorMonitorManager.NAME, server, 3000);
	}

	public List<EventProcessorMonitorEntry> list() throws Exception {
		List<MapModel> list = invoker.list(null);

		List<EventProcessorMonitorEntry> result = new ArrayList<EventProcessorMonitorEntry>(list.size());
		for (Map<String, Object> map : list) {
			EventProcessorMonitorEntry entry = new EventProcessorMonitorEntry(map);
			result.add(entry);
		}

		return result;
	}

	public void clearAll() throws Exception {
		invoker.remove(null);
	}

	public void clear(String id) throws Exception {
		MapModel option = new MapModel();
		option.setString("id", id);

		invoker.remove(option);
	}
}
