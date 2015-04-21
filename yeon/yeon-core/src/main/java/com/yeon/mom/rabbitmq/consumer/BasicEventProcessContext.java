package com.yeon.mom.rabbitmq.consumer;

import com.yeon.mom.EventProcessContext;
import com.yeon.mom.event.Event;
import com.yeon.mom.event.EventProcessOperation;
import com.yeon.mom.event.EventProcessOption;

import java.util.Map;

/**
 * 
 * @author pulsarang
 */
public class BasicEventProcessContext implements EventProcessContext {
	private final Event event;
	private final EventProcessorExecutionInfo executionInfo;
	private EventProcessOperation operation;

	public BasicEventProcessContext(Event event, EventProcessorExecutionInfo executionInfo, boolean redeliver) {
		super();
		if (event == null) {
			throw new IllegalArgumentException();
		}

		this.event = event;
		this.executionInfo = executionInfo;

		if (redeliver) {
			operation = EventProcessOperation.VERIFY;
		} else {
			EventProcessOption eventProcessOption = event.getEventProcessOption();
			operation = eventProcessOption == null ? event.getProcessOperation() : eventProcessOption.getOperation();
		}

		if (operation == EventProcessOperation.REPROCESS && getTryCount() >= getMaxTryCount()) {
			operation = EventProcessOperation.FAIL;
		}
	}

	@Override
	public Event getEvent() {
		return event;
	}

	@Override
	public Map<String, Object> getReprocessArguments() {
		EventProcessOption eventProcessOption = event.getEventProcessOption();
		if (eventProcessOption == null) {
			return null;
		}

		return eventProcessOption.getArguments();
	}

	@Override
	public boolean isReprocess() {
		return event.getEventProcessOption() != null;
	}

	@Override
	public int getTryCount() {
		EventProcessOption eventProcessOption = event.getEventProcessOption();
		if (eventProcessOption == null) {
			return 0;
		}

		return eventProcessOption.getTryCount();
	}

	public EventProcessOperation getEventProcessOperation() {
		return operation;
	}

	@Override
	public void setTryCount(int tryCount) {
		EventProcessOption eventProcessOption = event.getEventProcessOption();
		if (eventProcessOption == null) {
			eventProcessOption = new EventProcessOption(executionInfo.getEventProcessorInfo().getName());
			event.setEventProcessOption(eventProcessOption);
		}

		eventProcessOption.setTryCount(tryCount);
	}

	@Override
	public int getMaxTryCount() {
		return executionInfo.getEventProcessorInfo().getMaxTryCount();
	}
}
