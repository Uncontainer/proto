package com.pulsarang.mom.queue.db.h2;

import java.util.HashMap;
import java.util.Map;

import com.pulsarang.infra.InfraContext;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.mom.queue.EventQueue;

public class H2EventQueueFactory {
	static Map<String, EventQueue> queues = new HashMap<String, EventQueue>();

	public synchronized static EventQueue getQueue(String name) {
		InfraContext infraContext = InfraContextFactory.getInfraContext();
		String canonicalName = infraContext.getSolutionName() + "." + infraContext.getProjectName() + "." + name;

		EventQueue queue = queues.get(canonicalName);
		if (queue == null) {
			queue = new H2EventQueue(canonicalName);
			queues.put(canonicalName, queue);

			// 어플리케이션 종료 시점에 queue에 요청이 들어올 수 있으므로 close() 시점을 가능한 미룬다.
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					for (EventQueue queue : queues.values()) {
						try {
							queue.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
		}

		return queue;
	}
}
