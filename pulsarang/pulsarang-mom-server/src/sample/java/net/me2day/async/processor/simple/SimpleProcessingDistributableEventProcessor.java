package net.me2day.async.processor.simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.me2day.async.dispatcher.EventProcessContext;
import net.me2day.async.dispatcher.ReprocessableException;
import net.me2day.async.event.EventCode;
import net.me2day.async.event.SimpleEventCode;
import net.me2day.async.processor.EventProcessorAdapter;

import org.springframework.stereotype.Component;

@Component("simpleDistProcessor")
public class SimpleProcessingDistributableEventProcessor extends EventProcessorAdapter {
	@Override
	public String getConfigurationTicket() {
		return "simpleDistProcessor";
	}

	@Override
	public List < ? extends EventCode > getEventCodes() {
		return Arrays.asList(new SimpleEventCode("post", "create"));
	}

	@Override
	public void process(EventProcessContext context) throws ReprocessableException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void process(List < Object > targets, EventProcessContext context) throws ReprocessableException {
		List < Object > failed = new ArrayList < Object >();
		for (Object target : targets) {
			try {
				processTarget(target);
			} catch (RuntimeException e) {
				failed.add(target);
			}
		}

		if (!failed.isEmpty()) {
			throw new ReprocessableException(failed);
		}
	}

	private void processTarget(Object target) {
		// TODO code here
	}

	@Override
	public String getDistributorBeanName() {
		return "simpleDistributor";
	}
}
