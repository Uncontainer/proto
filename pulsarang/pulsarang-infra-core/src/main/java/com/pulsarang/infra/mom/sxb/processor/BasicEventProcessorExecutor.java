package com.pulsarang.infra.mom.sxb.processor;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.infra.mom.EventProcessor;
import com.pulsarang.infra.mom.ReprocessableException;
import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.mom.event.EventProcessOption;
import com.pulsarang.infra.mom.sxb.SxbDestinationUtils;

public class BasicEventProcessorExecutor implements EventProcessorExecutor {
	private final Logger log = LoggerFactory.getLogger(BasicEventProcessorExecutor.class);

	private EventProcessor eventProcessor;
	private String simpleName;
	private String canonicalName;

	private volatile boolean processing = false;

	public BasicEventProcessorExecutor(EventProcessor eventProcessor) {
		super();
		this.simpleName = eventProcessor.getName();
		this.canonicalName = SxbDestinationUtils.getCanonicalEventProcessorName(simpleName);
	}

	public boolean isProcessing() {
		return processing;
	}

	@Override
	public EventProcessor getEventProcessor() {
		return eventProcessor;
	}

	public String getCanonicalName() {
		return canonicalName;
	}

	@Override
	public EventProcessResponse execute(EventProcessRequest request) {
		Event event = request.getEvent();
		MomEventProcessContext context = new MomEventProcessContext(event);
		EventProcessOption option = event.getEventProcessOption();
		EventProcessResponse response;

		try {
			processing = true;
			execute(context, option);
			response = EventProcessResponse.SUCCESS;
			// TODO 응답 코드 추가.
		} catch (Throwable t) {
			if (t instanceof ReprocessableException) {
				response = handleReprocessableException((ReprocessableException) t, event, option);
			} else {
				response = launderThrowable(t);
			}
		} finally {
			processing = false;
		}

		return response;
	}

	private void execute(MomEventProcessContext context, EventProcessOption option) {
		if (option == null) {
			eventProcessor.process(context);
		} else {
			if (EventProcessOption.OP_REPROCESS.equals(option.getOperation())) {
				eventProcessor.reprocess(context);
			} else if (EventProcessOption.OP_VERIFY.equals(option.getOperation())) {
				eventProcessor.verify(context);
			} else if (EventProcessOption.OP_FAIL.equals(option.getOperation())) {
				try {
					eventProcessor.fail(context);
				} catch (Throwable t) {
					log.info("[MOM] Fail to process fail processing.", t);
				}
			} else {
				throw new IllegalArgumentException("Unsupported operation.(" + option.getOperation() + ")");
			}
		}
	}

	private EventProcessResponse handleReprocessableException(ReprocessableException e, Event event, EventProcessOption option) {
		EventProcessResponse response = new EventProcessResponse(EventProcessResponse.RC_REPROCESS);
		response.setReprocessArgument(e.getArguments());

		return response;
	}

	private static EventProcessResponse launderThrowable(Throwable t) {
		if (t instanceof InvocationTargetException) {
			t = t.getCause();
		}

		EventProcessResponse response = new EventProcessResponse(EventProcessResponse.RC_INTERNAL_SERVER_ERROR);
		response.setException(ExceptionUtils.getFullStackTrace(t));

		return response;
	}
}