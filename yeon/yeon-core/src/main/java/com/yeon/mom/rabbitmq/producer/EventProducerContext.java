package com.yeon.mom.rabbitmq.producer;

import com.yeon.YeonContext;
import com.yeon.YeonEnvironment;
import com.yeon.mom.EventProducer;
import com.yeon.mom.event.Event;
import com.yeon.mom.rabbitmq.RabbitMqConnectionFactory;
import com.yeon.mom.rabbitmq.producer.mon.EventProducerMonitor;
import com.yeon.monitor.collector.LocalMonitorablePropertyCollector;
import com.yeon.server.Solution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author pulsarang
 */
public class EventProducerContext {
	private final Logger log = LoggerFactory.getLogger(EventProducerContext.class);

	private RabbitMqConnectionFactory connectionFactory;
	private EventProducerMonitor eventProducerMonitor;

	private boolean forceProduceEvent;
	private boolean localPublishing;

	EventProducer eventProducer;

	public void init(boolean forceProduceEvent, boolean localPublishing, RabbitMqConnectionFactory connectionFactory) {
		this.forceProduceEvent = forceProduceEvent;
		this.localPublishing = localPublishing;
		this.connectionFactory = connectionFactory;

		eventProducerMonitor = new EventProducerMonitor();

		YeonContext.getRemoteContext().setRemoteService(EventProducerMonitor.NAME, eventProducerMonitor);

		switch (YeonContext.getEnvironment()) {
		case TEST:
			break;
		case LOCAL:
		case CI:
			break;
		default:
			LocalMonitorablePropertyCollector collector = LocalMonitorablePropertyCollector.getInstance();
			collector.setMonitorable(EventProducerMonitor.NAME, eventProducerMonitor);
			break;
		}
	}

	public RabbitMqConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public EventProducerMonitor getEventProducerMonitor() {
		return eventProducerMonitor;
	}

	public boolean isForceProduceEvent() {
		return forceProduceEvent;
	}

	public boolean isLocalPublishing() {
		return localPublishing;
	}

	public EventProducer getEventProducer() {
		return eventProducer;
	}

	public EventProducer get() {
		if (eventProducer != null) {
			return eventProducer;
		}

		try {
			YeonEnvironment environment = YeonContext.getEnvironment();

			if (environment == YeonEnvironment.TEST || Solution.SOLUTION_DUMMY.equals(YeonContext.getSolutionName())) {
				eventProducer = new NullEventProducer();
			} else if (!forceProduceEvent && (environment == YeonEnvironment.LOCAL || environment == YeonEnvironment.CI)) {
				if (localPublishing) {
					eventProducer = new LocalEventProducer();
				} else {
					eventProducer = new NullEventProducer();
				}
			} else {
				eventProducer = new RabbitMqEventProducer(connectionFactory);
				eventProducer = new LoggingEventProducer(eventProducer);
				eventProducer = new QueuedEventProducer(eventProducer, eventProducerMonitor);

				LocalMonitorablePropertyCollector collector = LocalMonitorablePropertyCollector.getInstance();
				collector.setMonitorable(QueuedEventProducer.PUBLISH_WAIT_EVENT, ((QueuedEventProducer) eventProducer).getQueueSizeMonitorable());
			}
		} catch (Throwable t) {
			log.error("[YEON] Fail to initialize eventProducer", t);

			eventProducer = new QueuedEventProducer(new EventProducer() {
				@Override
				public void publish(Event event) {
					throw new IllegalStateException("MomContext initialization failed.");
				}

				@Override
				public void publish(Event event, boolean async) {
					throw new IllegalStateException("MomContext initialization failed.");
				}

				@Override
				public void retry(Event event) {
					throw new IllegalStateException("MomContext initialization failed.");
				}
			}, null);
		}

		return eventProducer;
	}
}
