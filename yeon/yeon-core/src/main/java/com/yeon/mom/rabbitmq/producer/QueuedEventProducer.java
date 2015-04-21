package com.yeon.mom.rabbitmq.producer;

import com.yeon.YeonContext;
import com.yeon.YeonPredefinedClassId;
import com.yeon.YeonPredefinedResourceId;
import com.yeon.async.AsyncMapModelProcessor;
import com.yeon.async.FailoverQueueType;
import com.yeon.config.TicketListener;
import com.yeon.lang.NamedResourceId;
import com.yeon.lang.Resource;
import com.yeon.lang.ResourceServiceUtils;
import com.yeon.lang.listener.ListenTarget;
import com.yeon.mom.EventProducer;
import com.yeon.mom.MomConstants;
import com.yeon.mom.MomInfo;
import com.yeon.mom.event.Event;
import com.yeon.mom.event.EventProcessOperation;
import com.yeon.mom.rabbitmq.producer.mon.EventProducerMonitor;
import com.yeon.mom.util.TicketUtils;
import com.yeon.monitor.collector.Monitorable;
import com.yeon.monitor.resource.annotation.MonitoringBean;
import com.yeon.monitor.resource.annotation.MonitoringProperty;
import com.yeon.server.Server;
import com.yeon.util.ExceptionUtils;
import com.yeon.util.PeriodicTouchViolationChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author pulsarang
 */
public class QueuedEventProducer extends AsyncMapModelProcessor<Event> implements EventProducer, TicketListener {
	public static final String PUBLISH_WAIT_EVENT = "publishWaitEvent";

	private final Logger log = LoggerFactory.getLogger(QueuedEventProducer.class);

	final EventProducer eventProducer;

	final PeriodicTouchViolationChecker rabbitMqPublishFailCountChecker;

	private final EventProducerMonitor eventProducerMonitor;

	public QueuedEventProducer(EventProducer eventProducer, EventProducerMonitor eventProducerMonitor) {
		super(PUBLISH_WAIT_EVENT, Event.class, 1000, FailoverQueueType.FILE);

		this.eventProducer = eventProducer;
		this.eventProducerMonitor = eventProducerMonitor;
		this.rabbitMqPublishFailCountChecker = new PeriodicTouchViolationChecker(MomConstants.RABBIT_MQ_PUBLISH_MAX_TRY_COUNT, MomConstants.RABBIT_MQ_PUBLISH_FAIL_CHECK_DURATION);

		YeonContext.getResourceContext().addListener(this);
	}

	@Override
	protected MonitorMBean getMBean() {
		return new ProducerMonitorMBean(this);
	}

	public Monitorable<Integer> getQueueSizeMonitorable() {
		Monitorable<Integer> monitorable = new Monitorable<Integer>() {
			@Override
			public Integer createShapshot() {
				return queue.size();
			}
		};

		return monitorable;
	}

	@Override
	public void publish(Event event) {
		publish(event, false);
	}

	@Override
	public void publish(Event event, boolean async) {
		log.debug("[YEON] Publish event.({})", event);

		eventProducerMonitor.create(event);

		if (event.getEventDate() == null) {
			event.setEventDate(new Date());
		}

		if (event.hasEventProcessOption()) {
			throw new IllegalArgumentException("Not empty event process option.");
		}

		if (async) {
			try {
				put(event);
			} catch (Exception e) {
				log.error("[YEON] Fail to queueing event.({})", event);
				process(event);
			}
		} else {
			process(event);
		}
	}

	@Override
	public void retry(Event event) {
		log.debug("[YEON] Retry event.({})", event);

		event.setEventDate(new Date());

		eventProducerMonitor.create(event);

		if (!event.hasEventProcessOption()) {
			throw new IllegalArgumentException("Empty event process option.");
		}

		process(event);
	}

	@Override
	protected void process(Event event) {
		if (!isPublishEnabled()) {
			pauseDequeuing();

			put(event);
			return;
		}

		try {
			if (event.hasEventProcessOption()) {
				eventProducer.retry(event);
			} else {
				eventProducer.publish(event, false);
			}

			eventProducerMonitor.success(event);
		} catch (Throwable t) {
			eventProducerMonitor.fail(event);

			if (t instanceof RepublishException) {
				log.error("[YEON] Fail to publish event.(" + event + ")\nTry republish later.", t);

				if (rabbitMqPublishFailCountChecker.touch()) {
					try {
						Thread.sleep(MomConstants.RABBIT_MQ_PUBLISH_FAIL_SLEEP_TIME);
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
						throw new RuntimeException(ie);
					}
				}

				event.setValue("_pbs_try_cnt", event.getValue("_pbs_try_cnt", 0) + 1);
				put(event);
			} else {
				eventProducerMonitor.remove(event);

				log.error("[YEON] Fail to publish event.(" + event + ")", t);
			}
		}
	}

	public boolean isPublishEnabled() {
		MomInfo momInfo = TicketUtils.getMomInfo();
		Server server = TicketUtils.getServer();

		if (momInfo.getMomStatus().isPublishDisabled()) {
			return false;
		}

		if (server == null) {
			log.warn("[YEON] Not registered server.");
			return false;
		}

		return server.isNormal();
	}

	@Override
	public void put(Event event) {
		try {
			super.put(event);
			eventProducerMonitor.queue(event);
		} catch (Throwable t) {
			eventProducerMonitor.remove(event);
			log.error("[YEON] Fail to queue event.(" + event + ")", t);
			ExceptionUtils.launderThrowable(t);
		}
	}

	@Override
	public void itemRemoved(Event event) {
		eventProducerMonitor.remove(event);
	}

	@Override
	public void uncompletedItemDetected(Event event) {
		event.setEventProcessOperation(EventProcessOperation.VERIFY);
		publish(event);
	}

	@Override
	public List<ListenTarget> getTargets() {
		return Arrays.asList(ListenTarget.instance(YeonPredefinedResourceId.MOM), ListenTarget.instance(new NamedResourceId("en", TicketUtils.getServerId(), YeonPredefinedClassId.SERVER)));
	}

	@Override
	public void validate(ListenTarget key, Resource nextResource, Resource currentResource) throws Exception {
	}

	@Override
	public void validated(ListenTarget key, Resource currentResource, Resource previousResource) {
	}

	@Override
	public void changed(ListenTarget target, Resource currentResource, Resource previousResource) {
		if (previousResource == null) {
			return;
		}

		if (YeonPredefinedResourceId.MOM.getResourceId().equals(target.getValue())) {
			MomInfo previousMomInfo = ResourceServiceUtils.convert(previousResource, MomInfo.class);
			MomInfo currentMomInfo = ResourceServiceUtils.convert(currentResource, MomInfo.class);
			if (previousMomInfo.getMomStatus().isPublishDisabled()) {
				if (currentMomInfo.getMomStatus().isPublishEnabled()) {
					super.resumeDequeuing();
				}
			} else {
				if (currentMomInfo.getMomStatus().isPublishDisabled()) {
					super.pauseDequeuing();
				}
			}
		} else {
			Server previousServer = ResourceServiceUtils.convert(previousResource, Server.class);
			Server currentServer = ResourceServiceUtils.convert(currentResource, Server.class);
			if (!previousServer.isNormal()) {
				if (currentServer.isNormal()) {
					super.resumeDequeuing();
				}
			} else {
				if (!currentServer.isNormal()) {
					super.pauseDequeuing();
				}
			}
		}
	}

	/**
	 * @author EC셀러서비스개발팀
	 * 
	 */
	@MonitoringBean(inclusive = false)
	public static class ProducerMonitorMBean extends MonitorMBean {
		public ProducerMonitorMBean(QueuedEventProducer proc) {
			super(proc);
		}

		@MonitoringProperty(name = "publishEnabled")
		public boolean getPublishEnabled() {
			return ((QueuedEventProducer) proc).isPublishEnabled();
		}
	}
}
