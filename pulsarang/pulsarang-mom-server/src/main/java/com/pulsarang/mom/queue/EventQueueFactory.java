package com.pulsarang.mom.queue;

import java.util.HashMap;
import java.util.Map;

import com.pulsarang.infra.InfraContext;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.util.NameUtils;
import com.pulsarang.mom.queue.db.h2.H2EventQueue;

public class EventQueueFactory {
	static Map<String, EventQueue> queues = new HashMap<String, EventQueue>();

	public synchronized static EventQueue getQueue(String name, boolean persist) {
		String canonicalName = getCanonicalName(name);

		EventQueue queue = queues.get(canonicalName);
		if (queue == null) {
			if (NameUtils.isInvalidJavaIdentifier(name)) {
				throw new IllegalArgumentException("Queue name must be a java identifier.");
			}

			queue = createQueue(canonicalName, persist);
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

	public static String getCanonicalName(String simpleName) {
		InfraContext infra = InfraContextFactory.getInfraContext();
		return infra.getSolutionName() + "." + infra.getProjectName() + "." + simpleName;
	}

	private static EventQueue createQueue(String canonicalName, boolean persist) {
		return new H2EventQueue(canonicalName);
	}
}
