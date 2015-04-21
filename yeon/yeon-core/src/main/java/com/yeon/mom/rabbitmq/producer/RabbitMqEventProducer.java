package com.yeon.mom.rabbitmq.producer;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.yeon.mom.EventProducer;
import com.yeon.mom.MomConstants;
import com.yeon.mom.event.Event;
import com.yeon.mom.event.EventType;
import com.yeon.mom.rabbitmq.RabbitMqConnectionFactory;
import com.yeon.mom.rabbitmq.RabbitMqUtils;
import com.yeon.mom.util.EventTypeUtils;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author pulsarang
 */
public class RabbitMqEventProducer implements EventProducer {
	private final RabbitMqConnectionFactory connectionFactory;

	final ConcurrentHashMap<String, Sender> producerMap = new ConcurrentHashMap<String, Sender>();

	public RabbitMqEventProducer(RabbitMqConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	@Override
	public void publish(Event event) {
		publish(event, false);
	}

	@Override
	public void publish(Event event, boolean async) {
		if (event == null) {
			throw new IllegalArgumentException("Null event");
		}

		Sender producer = getMessageProducer(event.getEventType());
		try {
			event.setEnqueDate(new Date());
			producer.sendToExchange(event);
		} catch (IOException e) {
			throw new RepublishException(e);
		}
	}

	@Override
	public void retry(Event event) {
		if (event == null) {
			throw new IllegalArgumentException("Null event");
		}

		Sender producer = getMessageProducer(event.getEventType());
		try {
			event.setEnqueDate(new Date());
			producer.sendToQueue(event);
		} catch (IOException e) {
			throw new RepublishException(e);
		}
	}

	private Sender getMessageProducer(EventType eventType) {
		Sender sender = producerMap.get(eventType.getCode());
		if (sender == null) {
			sender = new Sender(eventType);
			Sender existPublisher = producerMap.putIfAbsent(eventType.getCode(), sender);
			if (existPublisher != null) {
				sender = existPublisher;
			}
		}

		return sender;
	}

	public static final BasicProperties PERSISTENT_UTF8_TEXT_PLAIN = new BasicProperties.Builder()
		.contentType("text/plain")
		.contentEncoding(MomConstants.MESSAGE_CHARSET.name())
		.deliveryMode(2)
		.priority(0)
		.build();

	/**
	 * RabbutMq 서버가 죽었을 경우 속도를 조절하는 로직은 {@link QueuedEventProducer#process(Event)}에서 수행.
	 */
	private class Sender {
		final EventType eventType;

		private Sender(EventType eventType) {
			this.eventType = eventType;
		}

		private void sendToQueue(Event event) throws IOException {
			Connection connection = null;
			Channel channel = null;
			try {
				connection = connectionFactory.createConnection();
				channel = connection.createChannel();

				String queueName = RabbitMqUtils.getQueueName(eventType, event.getEventProcessorName());

				channel.queueDeclare(queueName, true, false, false, null);
				channel.basicPublish("", queueName, PERSISTENT_UTF8_TEXT_PLAIN, toBytes(event));
			} finally {
				if (channel != null) {
					channel.close();
				}

				if (connection != null) {
					connection.close();
				}
			}
		}

		private void sendToExchange(Event event) throws IOException {
			Connection connection = null;
			Channel channel = null;
			try {
				connection = connectionFactory.createConnection();
				channel = connection.createChannel();

				if (EventTypeUtils.isQueueEventType(event.getEventType())) {
					String queueName = RabbitMqUtils.getExchangeName(eventType);
					channel.queueDeclare(queueName, true, false, false, null);
					channel.basicPublish("", queueName, PERSISTENT_UTF8_TEXT_PLAIN, toBytes(event));
				} else {
					String exchangeName = RabbitMqUtils.getExchangeName(eventType);
					channel.exchangeDeclare(exchangeName, "fanout", true);
					channel.basicPublish(exchangeName, "", PERSISTENT_UTF8_TEXT_PLAIN, toBytes(event));
				}
			} finally {
				if (channel != null && channel.isOpen()) {
					channel.close();
				}

				if (connection != null) {
					connection.close();
				}
			}
		}

		private byte[] toBytes(Event event) {
			String json = event.toJson();
			if (json.length() > 20000) {
				throw new IllegalArgumentException("Too big event.");
			}
			byte[] body = json.getBytes(MomConstants.MESSAGE_CHARSET);
			return body;
		}
	}
}
