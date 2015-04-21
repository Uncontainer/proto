package com.yeon.mom.rabbitmq.consumer.mon;

import com.yeon.mom.EventProcessContext;
import com.yeon.mom.EventProcessor;
import com.yeon.mom.EventProcessorInfo;
import com.yeon.mom.EventProcessorInfoId;
import com.yeon.mom.event.Event;
import com.yeon.mom.event.EventProcessInfo;
import com.yeon.mom.event.EventType;
import com.yeon.mom.rabbitmq.consumer.SkipProcessException;
import com.yeon.mom.util.TicketUtils;
import com.yeon.util.ExceptionUtils;
import com.yeon.util.LocalAddressHolder;
import com.yeon.util.NanoStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author EC셀러서비스개발팀
 */
public class EventProcessorMonitor implements EventProcessor {
	private final Logger log = LoggerFactory.getLogger(EventProcessorMonitor.class);

	private final String canonicalName;
	private final EventProcessor eventProcessor;
	private final EventProcessorInfo eventProcessorInfo;

	private Date lastPopTime;
	private Date lastProcessEventEnqueTime;

	private final Map<Long, EventProcessInfo> processEvents = new ConcurrentHashMap<Long, EventProcessInfo>();

	private final AtomicLong activeCount = new AtomicLong();
	private final AtomicLong errorCount = new AtomicLong();

	private final AtomicLong processInvokeCount = new AtomicLong();
	private final AtomicLong reprocessInvokeCount = new AtomicLong();
	private final AtomicLong verifyInvokeCount = new AtomicLong();
	private final AtomicLong failInvokeCount = new AtomicLong();
	private final AtomicLong skipCount = new AtomicLong();

	private final AtomicLong totalInvokeCount = new AtomicLong();
	private final AtomicLong totalSpentTime = new AtomicLong();
	private volatile long slowestProcessTime = 0L;

	FailoverableLogSender logSender = FailoverableLogSender.getInstance();

	public EventProcessorMonitor(EventProcessor eventProcessor) {
		this.canonicalName = EventProcessorInfoId.getCanonicalEventProcessorName(eventProcessor.getName());
		this.eventProcessor = eventProcessor;
		this.eventProcessorInfo = TicketUtils.getEventProcessorInfo(canonicalName);
	}

	public EventProcessor getEventProcessor() {
		return eventProcessor;
	}

	@Override
	public String getName() {
		return eventProcessor.getName();
	}

	@Override
	public List<? extends EventType> getSubscribeEvents() {
		return eventProcessor.getSubscribeEvents();
	}

	@Override
	public void process(EventProcessContext context) {
		processInvokeCount.incrementAndGet();

		// 코드 중복은 조금 발생하더라도 유지보수성이 reflection 보다는 좋은 것으로 판단.
		NanoStopWatch stopWatch = preInvoke(context);
		try {
			eventProcessor.process(context);
			postInvoke(context, stopWatch);
		} catch (Throwable t) {
			postInvoke(context, t, stopWatch);
		}
	}

	@Override
	public void reprocess(EventProcessContext context) {
		reprocessInvokeCount.incrementAndGet();

		NanoStopWatch stopWatch = preInvoke(context);
		try {
			eventProcessor.reprocess(context);
			postInvoke(context, stopWatch);
		} catch (Throwable t) {
			postInvoke(context, t, stopWatch);
		}
	}

	@Override
	public void verify(EventProcessContext context) {
		verifyInvokeCount.incrementAndGet();

		NanoStopWatch stopWatch = preInvoke(context);
		try {
			eventProcessor.verify(context);
			postInvoke(context, stopWatch);
		} catch (Throwable t) {
			postInvoke(context, t, stopWatch);
		}
	}

	@Override
	public void fail(EventProcessContext context) {
		failInvokeCount.incrementAndGet();

		NanoStopWatch stopWatch = preInvoke(context);
		try {
			eventProcessor.fail(context);
			postInvoke(context, stopWatch);
		} catch (Throwable t) {
			postInvoke(context, t, stopWatch);
		}
	}

	private NanoStopWatch preInvoke(EventProcessContext context) {
		NanoStopWatch stopWatch = new NanoStopWatch();
		stopWatch.start();

		Event event = context.getEvent();
		lastPopTime = new Date();
		lastProcessEventEnqueTime = event.getEnqueDate();
		activeCount.incrementAndGet();

		if (event.getEventId() == 0L) {
			return stopWatch;
		}

		EventProcessInfo processLog = new EventProcessInfo();
		processLog.setProcessStartTime(new Date());
		processLog.setProcessorName(canonicalName);
		processLog.setServerIp(LocalAddressHolder.getLocalAddress());
		processLog.setEventId(event.getEventId());
		processLog.setEventType(event.getEventType());

		processEvents.put(event.getEventId(), processLog);

		return stopWatch;
	}

	private void postInvoke(EventProcessContext context, NanoStopWatch stopWatch) {
		stopWatch.stop();
		long sentTime = stopWatch.getTimeInMillies();

		activeCount.decrementAndGet();
		totalInvokeCount.incrementAndGet();
		totalSpentTime.addAndGet(sentTime);
		if (slowestProcessTime < sentTime) {
			slowestProcessTime = sentTime;
		}

		Event event = context.getEvent();
		EventProcessInfo processLog = processEvents.remove(event.getEventId());
		if (processLog == null || processLog.getEventId() == null) {
			if (event.getEventId() != 0) {
				log.info("[YEON] Fail to find event process start log.({})", event);
			}
			return;
		}

		// 재처리인 경우는 로깅 레벨과 관계 없이 최종 성공 여부 판단을 위해 로그를 남긴다.
		if (!eventProcessorInfo.isSuccessLoggingEnabled(stopWatch) && context.getTryCount() == 0) {
			return;
		}

		processLog.setProcessFinishTime(new Date());

		logSender.sendLog(processLog);
	}

	private void postInvoke(EventProcessContext context, Throwable throwable, NanoStopWatch stopWatch) {
		stopWatch.stop();

		activeCount.decrementAndGet();
		if (throwable instanceof SkipProcessException) {
			skipCount.incrementAndGet();
		} else {
			errorCount.incrementAndGet();
		}

		boolean fail = true;
		try {
			Event event = context.getEvent();
			EventProcessInfo processInfo = processEvents.remove(event.getEventId());
			if (processInfo == null || processInfo.getEventId() == null) {
				if (event.getEventId() != 0) {
					log.info("[YEON] Fail to find event process start log.({})", event);
				}
				return;
			}

			if (!eventProcessorInfo.isFailLoggingEnabled()) {
				return;
			}

			processInfo.setProcessFinishTime(new Date());
			if (throwable instanceof SkipProcessException) {
				processInfo.setSkipped(true);
				processInfo.setException(((SkipProcessException) throwable).getReason());
				fail = false;
			} else {
				processInfo.setSuccess(false);
				processInfo.setException(ExceptionUtils.getFullStackTrace(throwable));
			}

			logSender.sendLog(processInfo);
		} catch (Throwable t2) {
			log.info("[YEON] Fail to send error log.", t2);
		} finally {
			if (fail) {
				ExceptionUtils.launderThrowable(throwable);
			}
		}
	}

	public void clearCount() {
		activeCount.set(0L);
		errorCount.set(0L);

		totalInvokeCount.set(0L);
		totalSpentTime.set(0L);
		slowestProcessTime = 0L;

		processInvokeCount.set(0L);
		reprocessInvokeCount.set(0L);
		verifyInvokeCount.set(0L);
		failInvokeCount.set(0L);
	}

	public String getCanonicalName() {
		return canonicalName;
	}

	public long getProcessInvokeCount() {
		return processInvokeCount.get();
	}

	public long getReprocessInvokeCount() {
		return reprocessInvokeCount.get();
	}

	public long getVerifyInvokeCount() {
		return verifyInvokeCount.get();
	}

	public long getFailInvokeCount() {
		return failInvokeCount.get();
	}

	public long getErrorCount() {
		return errorCount.get();
	}

	public long getActiveCount() {
		return activeCount.get();
	}

	public long getSkipCount() {
		return skipCount.get();
	}

	public long getTotalInvokeCount() {
		return totalInvokeCount.get();
	}

	public long getTotalSpentTime() {
		return totalSpentTime.get();
	}

	public long getSlowestProcessTime() {
		return slowestProcessTime;
	}

	public Date getLastPopTime() {
		return lastPopTime;
	}

	public Date getLastProcessEventEnqueTime() {
		return lastProcessEventEnqueTime;
	}

	public Map<Long, EventProcessInfo> getProcessEvents() {
		return processEvents;
	}

	public EventProcessorMonitorEntry toEntry() {
		return new EventProcessorMonitorEntry(this);
	}
}
