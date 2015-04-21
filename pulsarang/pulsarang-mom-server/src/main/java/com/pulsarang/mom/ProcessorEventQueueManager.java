package com.pulsarang.mom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pulsarang.core.util.CollectionUtil;
import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.InfraContext;
import com.pulsarang.infra.config.Config;
import com.pulsarang.infra.config.ConfigContext;
import com.pulsarang.infra.config.ConfigId;
import com.pulsarang.infra.config.ConfigListener;
import com.pulsarang.infra.mom.event.EventType;
import com.pulsarang.infra.mom.processor.EventProcessorInfo;
import com.pulsarang.infra.mom.processor.EventProcessorSubscriber;
import com.pulsarang.infra.remote.reload.AbstractReloadService;
import com.pulsarang.infra.server.Server;
import com.pulsarang.mom.dispatcher.processor.EventProcessorInvokerPool;
import com.pulsarang.mom.dispatcher.processor.ProcessorEventQueue;

/**
 * 
 * @author pulsarang
 */
@Component
public class ProcessorEventQueueManager extends AbstractReloadService implements ConfigListener, EventProcessorSubscriber {
	private final Logger log = LoggerFactory.getLogger(ProcessorEventQueueManager.class);

	@Autowired
	private InfraContext infraContext;

	@Autowired
	private EventProcessorInvokerPool pool;

	private Map<String, ProcessorEventQueue> queueByName;
	private Map<EventType, List<ProcessorEventQueue>> queuesByType;

	public void init() throws Exception {
		log.info("Initializing ProcessorEventQueueManager...");

		infraContext.getConfigContext().addListener(this);

		initProcessors();
	}

	public void destroy() throws Exception {
		log.info("Destorying ProcessorEventQueueManager...");

		for (ProcessorEventQueue queue : queueByName.values()) {
			queue.stop();
		}
	}

	private void initProcessors() {
		Map<String, ProcessorEventQueue> queueByName = new HashMap<String, ProcessorEventQueue>();
		Map<EventType, List<ProcessorEventQueue>> queuesByType = new HashMap<EventType, List<ProcessorEventQueue>>();

		List<? extends Config> eventProcessorInfos = infraContext.getConfigContext().listByCategory(EventProcessorInfo.CATEGORY);
		if (eventProcessorInfos == null) {
			// TODO 에러 처리
			return;
		}
		Iterator<? extends Config> iter = eventProcessorInfos.iterator();
		while (iter.hasNext()) {
			EventProcessorInfo eventProcessorInfo = (EventProcessorInfo) iter.next();
			addProcessor(queueByName, queuesByType, eventProcessorInfo);
		}

		this.queueByName = queueByName;
		this.queuesByType = queuesByType;
	}

	public ProcessorEventQueue getProcessorEventQueue(String processorName) {
		return queueByName.get(processorName);
	}

	public List<ProcessorEventQueue> getProcessorEventQueues(EventType eventType) {
		return queuesByType.get(eventType.getCode());
	}

	@Override
	public void subscribe(Server server, EventProcessorInfo paramEventProcessorInfo) {

		String eventProcessorName = paramEventProcessorInfo.getName();
		List<EventType> subscribeEventTypes = paramEventProcessorInfo.getSubscribeEventTypes();

		ProcessorEventQueue processorEventQueue = queueByName.get(eventProcessorName);
		if (processorEventQueue != null) {
			processorEventQueue.addServer(server);
			return;
		}

		ConfigId configId = new ConfigId(EventProcessorInfo.CATEGORY, eventProcessorName);
		ConfigContext configContext = infraContext.getConfigContext();
		EventProcessorInfo eventProcessorInfo = configContext.get(configId);
		if (eventProcessorInfo.isNull()) {
			eventProcessorInfo = new EventProcessorInfo();
			eventProcessorInfo.setConfigCategoryName(EventProcessorInfo.CATEGORY.getName());
			eventProcessorInfo.setConfigName(configId.getName());
			eventProcessorInfo.setSubscribeEventTypes(subscribeEventTypes);

			configContext.add(eventProcessorInfo);
			configContext.refresh(configId);
		} else {
			if (CollectionUtil.isDifferent(eventProcessorInfo.getSubscribeEventTypes(), subscribeEventTypes)) {
				configContext.refresh(configId);
			}
		}

		addProcessor(this.queueByName, this.queuesByType, eventProcessorInfo);
	}

	@Override
	public void unsubscribe(Server server, String eventProcessorName) {
		ProcessorEventQueue processorEventQueue = queueByName.get(eventProcessorName);
		if (processorEventQueue == null) {
			return;
		}

		// TODO 서버 제거.
	}

	private void addProcessor(Map<String, ProcessorEventQueue> queueByName, Map<EventType, List<ProcessorEventQueue>> queuesByType,
			EventProcessorInfo eventProcessorInfo) {
		List<EventType> subscribeEventTypes = eventProcessorInfo.getSubscribeEventTypes();
		for (EventType subscribeEventType : subscribeEventTypes) {
			List<ProcessorEventQueue> queues = queuesByType.get(subscribeEventType);
			if (queues == null) {
				queues = new ArrayList<ProcessorEventQueue>();
				queuesByType.put(subscribeEventType, queues);
			}

			infraContext.getConfigContext().addListener(eventProcessorInfo);

			ProcessorEventQueue queue = new ProcessorEventQueue(true, eventProcessorInfo, pool);
			queue.start();

			queueByName.put(eventProcessorInfo.getName(), queue);
			queues.add(queue);
		}
	}

	@Override
	public List<MapModel> list(MapModel option) {
		List<MapModel> result = new ArrayList<MapModel>();

		for (Map.Entry<EventType, List<ProcessorEventQueue>> entry : queuesByType.entrySet()) {
			MapModel treeNode = new MapModel();
			treeNode.setString("eventCode", entry.getKey().getCode());
			treeNode.setList("distributors", entry.getValue());

			result.add(treeNode);
		}

		return result;
	}

	@Override
	public int listCount(MapModel option) {
		return queuesByType.size();
	}

	@Override
	public void validate(ConfigId configId, Config config) throws Exception {
	}

	@Override
	public void changed(ConfigId configId, Config config) {
		// TODO 부분 업데이트 가능한지 확인...
		initProcessors();
	}

	@Override
	public List<ConfigId> getIds() {
		return Arrays.asList(new ConfigId(EventProcessorInfo.CATEGORY, null));
	}
}
