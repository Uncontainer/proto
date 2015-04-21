/*
 * DummySimpleEventProcessor.java $version 2010. 10. 15
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package net.me2day.async.processor.sample;

import java.util.List;
import java.util.Map;
import java.util.Random;

import net.me2day.async.dispatcher.EventProcessContext;
import net.me2day.async.dispatcher.ReprocessableException;
import net.me2day.async.dispatcher.processor.EventProcessor;
import net.me2day.async.event.EventCode;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.configuration.Configuration;
import org.springframework.stereotype.Component;

import com.nhncorp.nconfig.config.ConfigContext;
import com.nhncorp.nconfig.config.ConfigInitializeListener;
import com.nhncorp.nconfig.config.Scope;

/**
 * 
 * @author pulsarang
 */
@Component(value = "dummyProcessor-2")
public class DummySimpleEventProcessor implements EventProcessor, ConfigInitializeListener {
	private Random rand = new Random();

	private String ticket;

	private volatile int failPercentage = -1;
	private volatile long sleepTime = -1;

	public DummySimpleEventProcessor() {
		ticket = "dummyProcessor-1";
	}

	@Override
	public void process(EventProcessContext context) throws ReprocessableException {
		actDummy();
	}

	@Override
	public void process(List<Object> target, EventProcessContext context) throws ReprocessableException {
		actDummy();
	}

	@Override
	public void fail(EventProcessContext context) {
		try {
			actDummy();
		} catch (Exception ignore) {
		}
	}

	@Override
	public void fail(List<Object> target, EventProcessContext context) {
		try {
			actDummy();
		} catch (Exception ignore) {
		}
	}

	private void actDummy() throws ReprocessableException {
		if (failPercentage < 0) {
			failPercentage = getConfiguration().getInt("failPercentage");
		}

		if (sleepTime < 0) {
			sleepTime = getConfiguration().getInt("sleepTime");
		}

		boolean fail = (Math.abs(rand.nextInt()) % 100) < failPercentage;

		if (sleepTime > 0) {
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException ingored) {
			}
		}

		if (fail) {
			throw new ReprocessableException(null);
		}
	}

	private Configuration getConfiguration() {
		return ConfigContext.getConfiguration(ticket);
	}

	@Override
	public void validate(Scope scope, Map<String, Object> configurationMap) throws Exception {
		if (!ticket.equals(scope.getTarget())) {
			return;
		}
	}

	@Override
	public void initializeCompleted(Scope scope, Map<String, Object> configurationMap) {
		if (!ticket.equals(scope.getTarget())) {
			return;
		}

		int percentage = MapUtils.getIntValue(configurationMap, "failPercentage", 10);
		if (percentage > 100) {
			percentage = 100;
		}
		configurationMap.put("failPercentage", percentage);

		long time = MapUtils.getLongValue(configurationMap, "sleepTime", 100);
		configurationMap.put("sleepTime", time);

		failPercentage = percentage;
		sleepTime = time;
	}

	@Override
	public String getConfigurationTicket() {
		return null;
	}

	@Override
	public List<? extends EventCode> getEventCodes() {
		return null;
	}

	@Override
	public boolean skipProcessing(EventProcessContext context) {
		return false;
	}
}
