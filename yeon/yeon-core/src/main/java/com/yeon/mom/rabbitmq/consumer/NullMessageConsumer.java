package com.yeon.mom.rabbitmq.consumer;

import com.yeon.mom.EventProcessor;
import com.yeon.mom.service.ServiceWorkerStatus;
import com.yeon.mom.util.EventProcessorValidationUtil;

/**
 * 
 * @author pulsarang
 */
public class NullMessageConsumer implements MessageConsumer {
	public NullMessageConsumer(EventProcessor eventProcessor) {
		EventProcessorValidationUtil.checkValid(eventProcessor);
	}

	@Override
	public boolean start() {
		return true;
	}

	@Override
	public void stop() {
	}

	@Override
	public ServiceWorkerStatus getStatus(boolean checkAlive) {
		return ServiceWorkerStatus.MSG_WAIT;
	}

	@Override
	public long getIdleSinceTime() {
		return System.currentTimeMillis();
	}

	@Override
	public int getReceiveCount() {
		return 0;
	}
}
