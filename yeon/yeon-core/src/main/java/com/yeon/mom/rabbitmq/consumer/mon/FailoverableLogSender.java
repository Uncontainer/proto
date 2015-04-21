package com.yeon.mom.rabbitmq.consumer.mon;

import com.yeon.api.ApiServiceFactory;
import com.yeon.async.AsyncMapModelProcessor;
import com.yeon.async.FailoverQueueType;
import com.yeon.mom.event.EventApiService;
import com.yeon.mom.event.EventProcessInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author pulsarang
 */
public class FailoverableLogSender extends AsyncMapModelProcessor<EventProcessInfo> {
	public static final String NAME = "loggingProcessEvent";
	private final Logger log = LoggerFactory.getLogger(FailoverableLogSender.class);

	EventApiService eventApi = ApiServiceFactory.getEventApiService();

	static FailoverableLogSender instance = null;

	public static synchronized FailoverableLogSender getInstance() {
		if (instance == null) {
			instance = new FailoverableLogSender();
		}

		return instance;
	}

	private FailoverableLogSender() {
		super(NAME, EventProcessInfo.class, 1000, FailoverQueueType.FILE);
	}

	public void sendLog(EventProcessInfo eventProcessLog) {
		log.debug("[YEON] Send event execution info.({}@{})", eventProcessLog.getProcessorName(), eventProcessLog.getEventId());
		try {
			eventApi.logProcessing(eventProcessLog);
		} catch (Throwable t) {
			log.info("[YEON] Fail to send monitoring information.", t);
			put(eventProcessLog);
		}
	}

	@Override
	protected void process(EventProcessInfo eventProcessLog) {
		eventApi.logProcessing(eventProcessLog);
	}
}
