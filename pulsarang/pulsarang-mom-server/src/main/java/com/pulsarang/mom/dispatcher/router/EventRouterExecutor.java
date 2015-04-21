package com.pulsarang.mom.dispatcher.router;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.pulsarang.mom.ProcessorEventQueueManager;
import com.pulsarang.mom.common.cluster.Cluster;
import com.pulsarang.mom.dispatcher.processor.ProcessorEventQueue;
import com.pulsarang.mom.store.EventEnvelope;
import com.pulsarang.mom.store.proc.EventConsumer;

/**
 * 동일한 event-target 에 대해서 순서를 보장하는 역할을 한다.
 * 
 * @author pulsarang
 * 
 */
@Component
@Scope("prototype")
public class EventRouterExecutor implements EventConsumer {
	@Autowired
	private Cluster cluster;

	@Autowired
	private ProcessorEventQueueManager processorEventQueueManager;

	private final ReentrantLock lock = new ReentrantLock();

	/**
	 * 별도의 queue를 사용하지 않고 lock으로 처리한다. queueing은 {@link TableEventScanner}에서 수행하는 효과를 갖는다.
	 */
	@Override
	public synchronized void process(EventEnvelope eventEnvelope, boolean verify) {
		lock.tryLock();
		try {
			long awaitMillies = cluster.getClusterInfo().getControllerExecutionTimeout();
			List<ProcessorEventQueue> processorEventQueues = processorEventQueueManager.getProcessorEventQueues(eventEnvelope.getEvent()
					.getEventType());
			EventRouter eventRouter = new EventRouter(processorEventQueues, awaitMillies);

			eventRouter.execute(eventEnvelope, verify);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean cancel() {
		// TODO Auto-generated method stub
		return false;
	}

}
