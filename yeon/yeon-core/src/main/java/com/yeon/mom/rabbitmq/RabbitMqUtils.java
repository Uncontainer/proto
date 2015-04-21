package com.yeon.mom.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.yeon.YeonContext;
import com.yeon.mom.EventProcessorInfoId;
import com.yeon.mom.event.EventType;
import com.yeon.mom.util.EventTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * 
 * @author pulsarang
 */
public class RabbitMqUtils {
	private static final Logger LOG = LoggerFactory.getLogger(RabbitMqUtils.class);

	public static String getExchangeName(EventType eventType) {
		return eventType.getCode();
	}

	public static String getQueueName(EventType eventType, String eventProcessorName) {
		if (eventProcessorName == null) {
			throw new IllegalArgumentException("Null eventProcessorName");
		}

		if (EventTypeUtils.isQueueEventType(eventType)) {
			return getExchangeName(eventType);
		} else {
			String canonicalEventProcessorName;
			if (eventProcessorName.contains(".")) {
				canonicalEventProcessorName = eventProcessorName;
			} else {
				canonicalEventProcessorName = EventProcessorInfoId.getCanonicalEventProcessorName(eventProcessorName);
			}

			return canonicalEventProcessorName;
		}
	}

	public static void subscribeAll(String canonicalProcessorName, List<? extends EventType> eventTypes) throws IOException {
		for (EventType eventType : eventTypes) {
			subscribe(canonicalProcessorName, eventType);
		}
	}

	public static void subscribe(String canonicalProcessorName, EventType eventType) throws IOException {
		subscribe(canonicalProcessorName, eventType, true);
	}

	public static void unsubscribe(String canonicalProcessorName, EventType eventType) throws IOException {
		subscribe(canonicalProcessorName, eventType, false);
	}

	private static void subscribe(String canonicalProcessorName, EventType eventType, boolean subscribe) throws IOException {
		RabbitMqContext mqContext = (RabbitMqContext) YeonContext.getMomContext();

		if (subscribe) {
			LOG.info("[YEON] '{}' subscibe event '{}'", canonicalProcessorName, eventType.getCode());
		} else {
			LOG.info("[YEON] '{}' unsubscibe event '{}'", canonicalProcessorName, eventType.getCode());
		}

		Connection connection = null;
		Channel channel = null;
		try {
			connection = mqContext.getConnectionFactory().createConnection();
			channel = connection.createChannel();

			if (EventTypeUtils.isQueueEventType(eventType)) {
				if (subscribe) {
					String queueName = getExchangeName(eventType);

					channel.queueDeclare(queueName, true, false, false, null);
				}
			} else {
				String exchangeName = getExchangeName(eventType);
				String queueName = getQueueName(eventType, canonicalProcessorName);

				if (subscribe) {
					channel.exchangeDeclare(exchangeName, "fanout", true);
					channel.queueDeclare(queueName, true, false, false, null);
					channel.queueBind(queueName, exchangeName, "");
				} else {
					channel.queueUnbind(queueName, exchangeName, "");
				}
			}
		} finally {
			closeQuietly(connection);
		}
	}

	public static void closeQuietly(Connection connection) {
		if (connection == null) {
			return;
		}

		try {
			connection.close(2000);
		} catch (IOException ignore) {
		}
	}

	public static void closeQuietly(Channel channel) {
		if (channel == null) {
			return;
		}

		try {
			channel.close();
		} catch (IOException ignore) {
		}
	}
}
