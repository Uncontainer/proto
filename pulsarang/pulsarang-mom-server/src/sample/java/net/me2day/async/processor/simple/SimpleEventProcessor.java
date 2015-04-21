package net.me2day.async.processor.simple;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import net.me2day.async.dispatcher.EventProcessContext;
import net.me2day.async.dispatcher.ReprocessableException;
import net.me2day.async.event.EventCode;
import net.me2day.async.event.SimpleEventCode;
import net.me2day.async.processor.EventProcessorAdapter;

@Component("simpleProcessor")
public class SimpleEventProcessor extends EventProcessorAdapter {

	@Override
	public String getConfigurationTicket() {
		return "simpleProcessor";
	}

	@Override
	public List < ? extends EventCode > getEventCodes() {
		return Arrays.asList(new SimpleEventCode("post", "create"));
	}

	@Override
	public void process(EventProcessContext context) throws ReprocessableException {
		// TODO code here
	}
}
