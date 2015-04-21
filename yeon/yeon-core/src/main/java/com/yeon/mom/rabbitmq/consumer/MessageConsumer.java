package com.yeon.mom.rabbitmq.consumer;

import com.yeon.mom.service.ServiceWorkerStatus;

/**
 * 
 * @author pulsarang
 */
public interface MessageConsumer {
	boolean start();

	void stop();

	ServiceWorkerStatus getStatus(boolean checkAlive);

	long getIdleSinceTime();

	int getReceiveCount();
}
