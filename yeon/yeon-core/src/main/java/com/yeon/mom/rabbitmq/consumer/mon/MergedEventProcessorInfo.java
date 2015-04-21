package com.yeon.mom.rabbitmq.consumer.mon;

import com.yeon.YeonContext;
import com.yeon.mom.EventProcessorInfo;
import com.yeon.mom.rabbitmq.consumer.MessageConsumerGroup;
import com.yeon.remote.bulk.RemoteServiceBulkProxy;
import com.yeon.remote.bulk.RemoteServiceInvokeSkipReason;
import com.yeon.remote.bulk.RemoteServiceResponseListHolder;
import com.yeon.remote.bulk.ResponseIterator;
import com.yeon.remote.reload.AbstractReloadService;
import com.yeon.remote.reload.ReloadService;
import com.yeon.server.ProjectId;
import com.yeon.server.Server;
import com.yeon.util.MapModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author pulsarang
 */
public class MergedEventProcessorInfo {
	private final Logger log = LoggerFactory.getLogger(MergedEventProcessorInfo.class);
	public static final String SERVICE_NAME = "MOM_EVENT_PROCESSOR_INFO";

	private final Map<String, Entry> entryMap = new HashMap<String, Entry>();
	private List<Server> failedServers;
	private final ResponseIterator<List<Map<String, Object>>> merger;

	private MergedEventProcessorInfo() {
		merger = new EventProcessInfoMerger();
	}

	public Map<String, Entry> getEntryMap() {
		return entryMap;
	}

	public List<Server> getFailedServers() {
		return failedServers;
	}

	public static MergedEventProcessorInfo create(ProjectId projectId) {
		return create(projectId.getSolutionName(), projectId.getProjectName());
	}

	public static MergedEventProcessorInfo create(String solutionName, String projectName) {
		MergedEventProcessorInfo info = new MergedEventProcessorInfo();

		ReloadService invoker = RemoteServiceBulkProxy.newProjectProxy(ReloadService.class, SERVICE_NAME, new ProjectId(solutionName, projectName), 3000);
		try {
			invoker.listCount(null);
		} finally {
			RemoteServiceResponseListHolder.getAndClear().iterate(info.merger);
		}

		return info;
	}

	public static boolean isSame(EventProcessorInfo info1, EventProcessorInfo info2) {
		if (info1 == null) {
			return info2 == null;
		} else if (info2 == null) {
			return false;
		}

		return info1.getStatusCode().equals(info2.getStatusCode());
	}

	public static boolean isDifferent(EventProcessorInfo info1, EventProcessorInfo info2) {
		return !isSame(info1, info2);
	}

	public static void registReloadService(Map<String, MessageConsumerGroup> messageConsumerMap) {
		YeonContext.getRemoteContext().setRemoteService(SERVICE_NAME, new EventProcessorInfoReloadService(messageConsumerMap));
	}

	/**
	 * @author EC셀러서비스개발팀
	 */
	class EventProcessInfoMerger implements ResponseIterator<List<Map<String, Object>>> {
		@Override
		public synchronized void nextSuccess(Server server, List<Map<String, Object>> result) {
			if (result == null || result.isEmpty()) {
				return;
			}

			for (Map<String, Object> map : result) {
				EventProcessorInfo eventProcessorInfo = MapModel.fromMap(map, EventProcessorInfo.class);
				Entry item = entryMap.get(eventProcessorInfo.getName());
				if (item == null) {
					item = new Entry();
					item.eventProcessorInfo = eventProcessorInfo;
					entryMap.put(eventProcessorInfo.getName(), item);
					continue;
				}

				if (isDifferent(item.eventProcessorInfo, eventProcessorInfo)) {
					item.sameAll = false;
				}
			}
		}

		@Override
		public synchronized void nextFail(Server server, String errorCode, String errorMessage) {
			if (failedServers == null) {
				failedServers = new ArrayList<Server>();
			}
			failedServers.add(server);
		}

		@Override
		public void nextSkip(Server server, RemoteServiceInvokeSkipReason skipReason) {
			log.debug("[YEON] Skip to call rpc '{}'.({})", server, skipReason.getText());
		}
	}

	/**
	 * @author EC셀러서비스개발팀
	 */
	public static class Entry {
		boolean sameAll = true;
		EventProcessorInfo eventProcessorInfo;

		public boolean isSameAll() {
			return sameAll;
		}

		public EventProcessorInfo getEventProcessorInfo() {
			return eventProcessorInfo;
		}
	}

	/**
	 * @author EC셀러서비스개발팀
	 */
	static class EventProcessorInfoReloadService extends AbstractReloadService {
		Map<String, MessageConsumerGroup> messageConsumerMap;

		public EventProcessorInfoReloadService(Map<String, MessageConsumerGroup> messageConsumerMap) {
			super();
			this.messageConsumerMap = messageConsumerMap;
		}

		@Override
		public List<MapModel> list(MapModel optionMap) {
			List<MapModel> eventProcessorInfos = new ArrayList<MapModel>();
			for (MessageConsumerGroup messageConsumer : messageConsumerMap.values()) {
				EventProcessorInfo eventProcessorInfo = messageConsumer.getEventProcessorInfo();
				EventProcessorInfo syncEventProcessorInfo = createSyncEventProcessorInfo(eventProcessorInfo);

				eventProcessorInfos.add(syncEventProcessorInfo);
			}

			return eventProcessorInfos;
		}

		private EventProcessorInfo createSyncEventProcessorInfo(EventProcessorInfo eventProcessorInfo) {
			EventProcessorInfo syncEventProcessorInfo = new EventProcessorInfo();

			syncEventProcessorInfo.setName(eventProcessorInfo.getName());
			syncEventProcessorInfo.setStatusCode(eventProcessorInfo.getStatusCode());

			return syncEventProcessorInfo;
		}
	}
}
