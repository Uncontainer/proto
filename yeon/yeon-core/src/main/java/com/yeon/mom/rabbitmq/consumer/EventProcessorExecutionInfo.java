package com.yeon.mom.rabbitmq.consumer;

import com.yeon.YeonPredefinedClassId;
import com.yeon.config.TicketListener;
import com.yeon.lang.NamedResourceId;
import com.yeon.lang.Resource;
import com.yeon.lang.listener.ListenTarget;
import com.yeon.mom.EventProcessor;
import com.yeon.mom.EventProcessorInfo;
import com.yeon.mom.EventProcessorInfoId;
import com.yeon.mom.MomConstants;
import com.yeon.mom.util.TicketUtils;
import com.yeon.util.PeriodicTouchViolationChecker;

import java.util.Arrays;
import java.util.List;

public class EventProcessorExecutionInfo implements TicketListener {
	private final EventProcessor eventProcessor;
	private final EventProcessorInfo eventProcessorInfo;

	private final PeriodicTouchViolationChecker exceptionCountChecker;

	private volatile PeriodicTouchViolationChecker maxTpsChecker;

	private final String canonicalName;

	static PeriodicTouchViolationChecker mqConnectFailCountChecker;

	static {
		mqConnectFailCountChecker = new PeriodicTouchViolationChecker(MomConstants.MQ_CONNECT_MAX_TRY_COUNT, MomConstants.MQ_CONNECT_FAIL_CHECK_DURATION);
	}

	public EventProcessorExecutionInfo(EventProcessor eventProcessor) {
		super();
		if (eventProcessor == null) {
			throw new IllegalArgumentException("Null eventProcessor.");
		}

		this.eventProcessor = eventProcessor;
		this.canonicalName = EventProcessorInfoId.getCanonicalEventProcessorName(eventProcessor.getName());

		this.eventProcessorInfo = TicketUtils.getEventProcessorInfo(canonicalName);
		if (this.eventProcessorInfo == null) {
			throw new IllegalArgumentException("Fail to find eventProcessor information.(" + canonicalName + ")");
		}

		this.exceptionCountChecker = new PeriodicTouchViolationChecker(MomConstants.EVENT_PROCESS_FAIL_MAX_ALLOW_COUNT, MomConstants.EVENT_PROCESS_FAIL_CHECK_DURATION);

		maxTpsChecker = new PeriodicTouchViolationChecker(eventProcessorInfo.getMaxTps(), 1000L);
	}

	public String getSimpleName() {
		return eventProcessor.getName();
	}

	public String getCanonicalName() {
		return canonicalName;
	}

	public EventProcessor getEventProcessor() {
		return eventProcessor;
	}

	public EventProcessorInfo getEventProcessorInfo() {
		return eventProcessorInfo;
	}

	public PeriodicTouchViolationChecker getExceptionCountChecker() {
		return exceptionCountChecker;
	}

	public PeriodicTouchViolationChecker getMqConnectFailCountChecker() {
		return mqConnectFailCountChecker;
	}

	public PeriodicTouchViolationChecker getMaxTpsChecker() {
		return maxTpsChecker;
	}

	@Override
	public List<ListenTarget> getTargets() {
		return Arrays.asList(ListenTarget.instance(new NamedResourceId("en", canonicalName, YeonPredefinedClassId.EVENT_PROCESSOR)));
	}

	@Override
	public void validate(ListenTarget key, Resource nextTicket, Resource currentTicket) throws Exception {
		// TODO type 변환 코드 추가.
		if (((EventProcessorInfo) currentTicket).getMaxTps() == 0) {
			throw new IllegalArgumentException("Invalid max tps.(" + 0 + ")");
		}
	}

	@Override
	public void validated(ListenTarget key, Resource currentTicket, Resource previousTicket) {
	}

	@Override
	public void changed(ListenTarget key, Resource currentTicket, Resource previousTicket) {
		// TODO type 변환 코드 추가.
		int newMaxTps = ((EventProcessorInfo) currentTicket).getMaxTps();
		if (maxTpsChecker.getMaxAcceptCount() != newMaxTps) {
			maxTpsChecker = new PeriodicTouchViolationChecker(newMaxTps, 1000L);
		}
	}
}
