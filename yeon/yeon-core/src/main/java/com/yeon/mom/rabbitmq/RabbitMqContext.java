package com.yeon.mom.rabbitmq;

import com.yeon.Disposable;
import com.yeon.YeonContext;
import com.yeon.YeonPredefinedClassId;
import com.yeon.YeonPredefinedResourceId;
import com.yeon.config.TicketListener;
import com.yeon.lang.NamedResourceId;
import com.yeon.lang.Resource;
import com.yeon.lang.ResourceServiceUtils;
import com.yeon.lang.listener.ListenTarget;
import com.yeon.mom.*;
import com.yeon.mom.rabbitmq.consumer.MessageConsumerContext;
import com.yeon.mom.rabbitmq.producer.EventProducerContext;
import com.yeon.mom.rabbitmq.resque.RabbitMqResque;
import com.yeon.mom.rabbitmq.resque.RabbitMqResqueWorkerExecutor;
import com.yeon.mom.service.ServiceRunStatus;
import com.yeon.mom.util.TicketUtils;
import com.yeon.monitor.resource.MomMBeanFactory;
import com.yeon.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pulsarang
 * 
 */
public class RabbitMqContext implements MomContext, TicketListener, Disposable {
	private final Logger log = LoggerFactory.getLogger(RabbitMqContext.class);

	RabbitMqConnectionFactory connectionFactory;

	MessageConsumerContext messageConsumerContext;
	EventProducerContext eventProducerContext;

	ServiceRunStatus status = ServiceRunStatus.INIT_WAIT;

	public RabbitMqContext() {
	}

	public ServiceRunStatus getStatus() {
		return status;
	}

	public RabbitMqConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void init(boolean forceProduceEvent, boolean forceSubscribeEvent, boolean localPublishing) {
		if (status != ServiceRunStatus.INIT_WAIT) {
			throw new IllegalStateException("Expect 'WAITING' but '" + status + "'");
		}

		eventProducerContext = new EventProducerContext();
		try {
			YeonContext.getResourceContext().addListener(this);

			log.info("[YEON] Run at '{}' environment", YeonContext.getEnvironment());

			this.connectionFactory = new RabbitMqConnectionFactory();

			// EventProcessor 초기화
			messageConsumerContext = new MessageConsumerContext();
			messageConsumerContext.init(forceSubscribeEvent);

			// EventProducer 초기화.
			eventProducerContext.init(forceProduceEvent, localPublishing, connectionFactory);
			eventProducerContext.get();

			status = ServiceRunStatus.NORMAL;
		} catch (Throwable t) {
			log.error("[YEON] Fail to initialize MomContext", t);
			eventProducerContext.get();

			status = ServiceRunStatus.INIT_FAIL;
		}

		MomMBeanFactory.getInstance().addMBean("mom", new MomMBean(this));
	}

	@Override
	public void dispose() {
		if (status.isClose()) {
			log.info("[YEON] It's already destroyed.");
			return;
		}

		if (status == ServiceRunStatus.INIT_WAIT) {
			return;
		}

		status = ServiceRunStatus.STOP_WAIT;

		try {
			messageConsumerContext.stop();

			if (connectionFactory != null) {
				connectionFactory.destory();
			}
		} finally {
			status = ServiceRunStatus.STOP;
		}
	}

	public MessageConsumerContext getMessageConsumerContext() {
		return messageConsumerContext;
	}

	public EventProducerContext getEventProducerContext() {
		return eventProducerContext;
	}

	@Override
	public EventProducer getEventProducer() {
		return eventProducerContext.get();
	}

	@Override
	public void addEventProcessor(EventProcessor eventProcessor) {
		checkInitialized();

		messageConsumerContext.add(eventProcessor);
	}

	@Override
	public EventProcessor getEventProcessor(String name) {
		checkInitialized();

		return messageConsumerContext.getEventProcessor(name);
	}

	@Override
	public List<EventProcessor> getEventProcessors() {
		checkInitialized();

		return messageConsumerContext.getEventProcessors();
	}

	private void checkInitialized() {
		if (status != ServiceRunStatus.NORMAL) {
			throw new IllegalStateException("Expect '" + ServiceRunStatus.NORMAL + "', but '" + status + "'");
		}
	}

	/*-------------------------------------------------------------------
	 * Resque 관련 method
	 -------------------------------------------------------------------*/
	Map<Class<? extends ResqueWorker>, Resque> resquesByClass = new HashMap<Class<? extends ResqueWorker>, Resque>();
	Map<String, Resque> resquesByName = new HashMap<String, Resque>();

	@Override
	public synchronized Resque getResque(Class<? extends ResqueWorker> clazz) {
		Resque resque = resquesByClass.get(clazz);
		if (resque == null) {
			try {
				ResqueWorker worker = clazz.newInstance();
				resque = registerResqueWorker(worker);
			} catch (InstantiationException e) {
				throw new IllegalArgumentException(e);
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException(e);
			}
		}

		return resque;
	}

	@Override
	public synchronized Resque getResque(String workerName) {
		return resquesByName.get(workerName);
	}

	public synchronized Resque registerResqueWorker(ResqueWorker worker) {
		if (worker == null) {
			throw new IllegalArgumentException("ResqueWorker is null.");
		}

		Resque resque = resquesByClass.get(worker.getClass());
		if (resque != null) {
			throw new IllegalStateException("ResqueWorker has already registered.(" + worker.getClass().getCanonicalName() + ")");
		}

		RabbitMqResqueWorkerExecutor workerExecutor = new RabbitMqResqueWorkerExecutor(worker);
		addEventProcessor(workerExecutor);

		resque = new RabbitMqResque(worker, getEventProducer());

		resquesByClass.put(worker.getClass(), resque);
		resquesByName.put(worker.getName(), resque);

		return resque;
	}

	/*-------------------------------------------------------------------
	 * Ticket 변경사항 반영 method
	 -------------------------------------------------------------------*/

	@Override
	public List<ListenTarget> getTargets() {
		return Arrays.asList(ListenTarget.instance(YeonPredefinedResourceId.MOM),
				ListenTarget.type(YeonPredefinedClassId.EVENT_PROCESSOR),
				ListenTarget.instance(new NamedResourceId("en", TicketUtils.getProjectId(), YeonPredefinedClassId.PROJECT)),
				ListenTarget.instance(new NamedResourceId("en", TicketUtils.getServerId(), YeonPredefinedClassId.SERVER)));
	}

	@Override
	public void validate(ListenTarget target, Resource nextTicket, Resource currentTicket) throws Exception {
		if (YeonPredefinedResourceId.MOM.getResourceId().equals(target.getValue())) {
			if (connectionFactory != null) {
				MomInfo momInfo = ResourceServiceUtils.convert(nextTicket, MomInfo.class);
				connectionFactory.validate(momInfo.getMqAddress());
			}
		}
	}

	@Override
	public void validated(ListenTarget target, Resource currentTicket, Resource previousTicket) {
	}

	@Override
	public void changed(ListenTarget target, Resource currentTicket, Resource previousTicket) {
		if (YeonPredefinedClassId.MOM.equals(currentTicket.getTypeClassId())) {
			if (previousTicket == null) {
				return;
			}

			MomInfo previousMomInfo = ResourceServiceUtils.convert(previousTicket, MomInfo.class);
			MomInfo currentMomInfo = ResourceServiceUtils.convert(currentTicket, MomInfo.class);
			if (!currentMomInfo.getMqAddress().equals(previousMomInfo.getMqAddress())) {
				String previousMqAddress = previousMomInfo == null ? "" : previousMomInfo.getMqAddress();
				log.info("[YEON] MQ changed from '{}' to'{}'", previousMqAddress, currentMomInfo.getMqAddress());

				if (connectionFactory != null) {
					connectionFactory.changeMqAddress(currentMomInfo.getMqAddress());
				}
				messageConsumerContext.reconnectMessageConsumerAll();
			} else if (previousMomInfo.getMomStatus().isProcessEnabled() != currentMomInfo.getMomStatus().isProcessEnabled()) {
				if (currentMomInfo.getMomStatus().isProcessEnabled()) {
					messageConsumerContext.attachMessageConsumerAll();
				} else {
					messageConsumerContext.detachMessageConsumerAll();
				}
			}
		}
		else if (YeonPredefinedClassId.EVENT_PROCESSOR.equals(currentTicket.getTypeClassId())) {
			EventProcessorInfo previousEventProcessorInfo = ResourceServiceUtils.convert(previousTicket, EventProcessorInfo.class);
			EventProcessorInfo currentEventProcessorInfo = ResourceServiceUtils.convert(currentTicket, EventProcessorInfo.class);
			messageConsumerContext.refreshMessageConsumer(currentEventProcessorInfo, previousEventProcessorInfo);
		} else if (YeonPredefinedClassId.SERVER.equals(currentTicket.getTypeClassId())) {
			Server server = ResourceServiceUtils.convert(currentTicket, Server.class);
			if (server.isNormal()) {
				messageConsumerContext.attachMessageConsumerAll();
			} else {
				messageConsumerContext.detachMessageConsumerAll();
			}
		}
		else if (YeonPredefinedClassId.PROJECT.equals(currentTicket.getTypeClassId())) {
			ProjectInfo projectInfo = ResourceServiceUtils.convert(currentTicket, ProjectInfo.class);
			if (projectInfo.isSubscribeEnabled()) {
				messageConsumerContext.attachMessageConsumerAll();
			} else {
				messageConsumerContext.detachMessageConsumerAll();
			}
		}
	}
}
