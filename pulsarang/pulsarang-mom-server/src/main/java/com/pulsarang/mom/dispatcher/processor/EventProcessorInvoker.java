package com.pulsarang.mom.dispatcher.processor;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.core.util.NanoStopWatch;
import com.pulsarang.infra.mom.processor.EventProcessorInfo;
import com.pulsarang.infra.monitoring.MonitoringContext;
import com.pulsarang.infra.monitoring.MonitoringContextFactory;

/**
 * 
 * @author pulsarang
 */
public class EventProcessorInvoker implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(EventProcessorInvoker.class);

	private EventProcessContext context;

	private CountDownLatch endLatch;

	private EventProcessorInvokerStatus status;

	public EventProcessorInvoker(EventProcessContext context, CountDownLatch endLatch) {
		this.context = context;
		this.endLatch = endLatch;
		this.status = EventProcessorInvokerStatus.WAIT;
	}

	public EventProcessContext getContext() {
		return context;
	}

	@Override
	public void run() {
		try {
			synchronized (this) {
				if (status != EventProcessorInvokerStatus.WAIT) {
					throw new IllegalStateException("Status must be a wait.");
				}
			}

			runUnsafely();

			boolean wasDaemon = false;
			synchronized (this) {
				wasDaemon = (EventProcessorInvokerStatus.DAEMON == status);
				status = EventProcessorInvokerStatus.SUCCESS;
			}

			// TODO daemon 처리.
			context.getEventEnvelope().acknowlege(/* wasDaemon */);
		} catch (Exception e) {
			context.getEventEnvelope().acknowlege(e);
		} finally {
			endLatch.countDown();
		}
	}

	private void runUnsafely() {
		synchronized (this) {
			if (status == EventProcessorInvokerStatus.CANCELLED) {
				LOG.info("Event processor cancelled.");
				return;
			}

			status = EventProcessorInvokerStatus.RUNNING;
		}

		ProcessorEventQueue processorEventQueue = context.getProcessorEventQueue();
		LOG.debug("Send event to {}", processorEventQueue.getName());

		Exception exception = null;
		NanoStopWatch stopWatch = new NanoStopWatch();
		stopWatch.start();

		try {
			// TODO 응답 코드에 따라 ProcessorEventQueue에서 해당 서버를 제거 또는 대기로 변경하는 로직 추가.
			// 응답 코드에 따라 재처리 여부 판단도 필요.
			processorEventQueue.publish(context);
		} catch (RuntimeException e) {
			exception = e;
			throw e;
		} finally {
			stopWatch.stop();

			MonitoringContext monitoringContext = MonitoringContextFactory.getMonitoringContext();

			// 100 마이크로 초 이하의 처리 시간이 걸렸을 경우는 EventProcessor에서 아무 처리도 하지 않은 것으로 간주한다.
			if (stopWatch.getTimeInNanos() > 100000) {
				// TODO [ACCESS-MONITOR] 모니터링 코드 추가.
				String key = "_a:r:mom/proc/" + processorEventQueue.getName();
				boolean success = (exception == null);

				monitoringContext.getAccessMonitor().add(key, stopWatch, success);
			}
		}
	}

	public boolean cancel() {
		synchronized (this) {
			if (status != EventProcessorInvokerStatus.WAIT) {
				return false;
			}

			status = EventProcessorInvokerStatus.CANCELLED;
		}

		EventProcessorInfo processorInfo = context.getProcessorEventQueue().getProcessorInfo();
		long interval = processorInfo.getRetryInterval();
		context.getEventEnvelope().postpone(interval);

		return true;
	}

	public synchronized boolean detachIfRunning() {
		if (status == EventProcessorInvokerStatus.RUNNING) {
			status = EventProcessorInvokerStatus.DAEMON;
			return true;
		} else {
			return false;
		}
	}

	public synchronized boolean isRunning() {
		return status == EventProcessorInvokerStatus.RUNNING;
	}

	public synchronized boolean isSuccess() {
		return status == EventProcessorInvokerStatus.SUCCESS;
	}

	public synchronized boolean isFail() {
		return !isSuccess();
	}

	public synchronized EventProcessorInvokerStatus getStatus() {
		return status;
	}
}
