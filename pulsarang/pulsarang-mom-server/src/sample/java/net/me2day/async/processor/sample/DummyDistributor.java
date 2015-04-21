/*
 * DummyDistributor.java $version 2010. 10. 15
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package net.me2day.async.processor.sample;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import net.me2day.async.dispatcher.EventProcessContext;
import net.me2day.async.dispatcher.processor.EventProcessingDistributor;

import com.nhncorp.nconfig.config.ConfigInitializeListener;
import com.nhncorp.nconfig.config.Scope;

/**
 * 
 * @author pulsarang
 */
@Component(value = "distributor-1")
@org.springframework.context.annotation.Scope(value = "prototype")
public class DummyDistributor implements EventProcessingDistributor, ConfigInitializeListener {
	private int index;

	private String ticket = "distributor-1";
	private volatile int distributionCount = 5;

	@Override
	public List<Object> getNextTarget() {
		if (index++ < distributionCount) {
			return Arrays.asList((Object)Integer.toString(index + 1));
		}

		return null;
	}

	@Override
	public void setContext(EventProcessContext eventProcessContext) {
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

		int count = MapUtils.getInteger(configurationMap, "distributionCount", 2);
		if (count < 2) {
			count = 2;
		} else if (count > 50) {
			count = 50;
		}

		distributionCount = count;
		configurationMap.put("distributionCount", count);
	}
}
