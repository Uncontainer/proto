package com.yeon.mom.rabbitmq.consumer;

/**
 * 
 * @author pulsarang
 */
public interface MessageConsumerFactory {
	MessageConsumer createMomMessageConsumer(int id, EventProcessorExecutionInfo executionInfo);
}
