package com.pulsarang.mom;

import java.util.Date;
import java.util.Random;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.pulsarang.infra.mom.event.Event;
import com.pulsarang.infra.mom.event.SimpleEventType;
import com.pulsarang.mom.common.cluster.Cluster;
import com.pulsarang.mom.common.cluster.ClusterStatus;

public class AbstractAsyncTestCase {
	@Autowired
	protected Cluster cluster;

	private Random random = new Random();

	public AbstractAsyncTestCase() {
		setLogLevel(Level.INFO);
	}

	@Before
	public void initClusterInfo() {
		cluster.setClusterStatus(ClusterStatus.STOP);

		Thread.interrupted();
	}

	@After
	public void stopCluster() {
		cluster.setClusterStatus(ClusterStatus.STOP);
	}

	protected Event getTestEvent() {
		Event event = new Event();
		event.setTableNo(1);
		event.setEventId(Math.abs(random.nextInt()) + 1L);
		event.setEventType(new SimpleEventType("dummy", "dummy"));
		event.setEventDate(new Date());

		return event;
	}

	protected static void setLogLevel(Level level) {
		Logger.getLogger("com").setLevel(level);
		Logger.getRootLogger().setLevel(level);
	}
}
