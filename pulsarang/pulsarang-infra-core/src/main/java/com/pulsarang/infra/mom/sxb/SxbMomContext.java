package com.pulsarang.infra.mom.sxb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.infra.Disposable;
import com.pulsarang.infra.InfraContext;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.InfraEnvironment;
import com.pulsarang.infra.mom.EventProcessor;
import com.pulsarang.infra.mom.EventProducer;
import com.pulsarang.infra.mom.MomContext;
import com.pulsarang.infra.mom.Resque;
import com.pulsarang.infra.mom.ResqueWorker;
import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.mom.sxb.processor.SxbEventDispatcher;
import com.pulsarang.infra.mom.sxb.producer.LocalEventProducer;
import com.pulsarang.infra.mom.sxb.producer.NullEventProducer;
import com.pulsarang.infra.mom.sxb.producer.QueuedEventProducer;
import com.pulsarang.infra.mom.sxb.producer.SxbEventProducer;
import com.pulsarang.infra.mom.sxb.resque.SxbResque;
import com.pulsarang.infra.mom.sxb.resque.SxbResqueWorkerExecutor;
import com.pulsarang.infra.server.Solution;

/**
 * @author pulsarang
 * 
 */
public class SxbMomContext implements MomContext, Disposable {
	private static final Logger log = LoggerFactory.getLogger(SxbMomContext.class);

	private EventProducer eventProducer;
	private EventReceiverService eventReceiver;
	private SxbEventDispatcher eventDispatcher;

	boolean forceProduceEvent;
	boolean forceSubscribeEvent;

	public static enum Status {
		WAITING, INITIALIZED, INIT_FAIL, PAUSE, STOP
	}

	Status status = Status.WAITING;

	public SxbMomContext() {
	}

	public void init(InfraContext infraContext, boolean forceProduceEvent, boolean forceSubscribeEvent) {
		if (status != Status.WAITING) {
			throw new IllegalStateException("Expect 'WAITING' but '" + status + "'");
		}

		this.forceProduceEvent = forceProduceEvent;
		this.forceSubscribeEvent = forceSubscribeEvent;

		try {
			InfraEnvironment environment = infraContext.getInfraConfiguration().getEnvironment();
			this.eventProducer = createEventProducer(environment);
			this.eventDispatcher = new SxbEventDispatcher(environment, forceSubscribeEvent);
			this.eventReceiver = new EventReceiverService(eventDispatcher);

			infraContext.getRemoteContext().setRemoteService(this.eventReceiver);

			status = Status.INITIALIZED;
		} catch (Throwable t) {
			log.error("[MOM] Fail to initialize MomContext", t);
			if (eventProducer == null) {
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
				});
			}

			status = Status.INIT_FAIL;
		}
	}

	public Status getStatus() {
		return status;
	}

	@Override
	public void dispose() throws Exception {
		if (status == Status.STOP) {
			log.info("[MOM] It's already destroyed.");
			return;
		}

		if (status == Status.WAITING) {
			return;
		}

		eventDispatcher.destroy();

		status = Status.STOP;
	}

	@Override
	public void addEventProcessor(EventProcessor eventProcessor) {
		checkInitialized();

		eventDispatcher.addEventProcessor(eventProcessor);
	}

	@Override
	public EventProcessor getEventProcessor(String name) {
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Empty envet processor name.");
		}

		return eventDispatcher.get(name);
	}

	private EventProducer createEventProducer(InfraEnvironment environment) {
		EventProducer eventProducer;
		InfraContext infraContext = InfraContextFactory.getInfraContext();

		if (environment == InfraEnvironment.TEST || Solution.SOLUTION_DUMMY.equals(infraContext.getSolutionName())) {
			eventProducer = new NullEventProducer();
		} else if (!forceProduceEvent && (environment == InfraEnvironment.LOCAL || environment == InfraEnvironment.CI)) {
			eventProducer = new LocalEventProducer();
		} else {
			eventProducer = new SxbEventProducer();
			eventProducer = new QueuedEventProducer(eventProducer);
		}

		return eventProducer;
	}

	@Override
	public List<EventProcessor> getEventProcessors() {
		checkInitialized();

		return eventDispatcher.list();
	}

	@Override
	public EventProducer getEventProducer() {
		return eventProducer;
	}

	private void checkInitialized() {
		if (status != Status.INITIALIZED) {
			throw new IllegalStateException("Expect '" + Status.INITIALIZED + "', but '" + status + "'");
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

		SxbResqueWorkerExecutor workerExecutor = new SxbResqueWorkerExecutor(worker);
		addEventProcessor(workerExecutor);

		resque = new SxbResque(worker);

		resquesByClass.put(worker.getClass(), resque);
		resquesByName.put(worker.getName(), resque);

		return resque;
	}
}
