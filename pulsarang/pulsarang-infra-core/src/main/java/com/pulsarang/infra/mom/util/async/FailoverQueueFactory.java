package com.pulsarang.infra.mom.util.async;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.InfraContext;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.util.NameUtils;

public class FailoverQueueFactory {
	private static final Logger log = LoggerFactory.getLogger(FailoverQueueFactory.class);

	@SuppressWarnings("rawtypes")
	static Map<String, FailoverQueue> queues = new HashMap<String, FailoverQueue>();

	public synchronized static <T extends MapModel> FailoverQueue<T> getQueue(String name, Class<T> clazz, boolean persist, int maxSize) {
		if (clazz == null) {
			throw new IllegalArgumentException("Element class is null.");
		}

		if (maxSize < 1) {
			throw new IllegalArgumentException("Queue size must greater then 0.");
		}

		String canonicalName = getCanonicalName(name);

		@SuppressWarnings("unchecked")
		//
		FailoverQueue<T> queue = queues.get(canonicalName);
		if (queue == null) {
			if (NameUtils.isInvalidJavaIdentifier(name)) {
				throw new IllegalArgumentException("Queue name must be a java identifier.");
			}

			queue = createQueue(canonicalName, clazz, persist, maxSize);
			queues.put(canonicalName, queue);

			// 어플리케이션 종료 시점에 queue에 요청이 들어올 수 있으므로 close() 시점을 가능한 미룬다.
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					for (FailoverQueue<T> queue : queues.values()) {
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
		InfraContext infraContext = InfraContextFactory.getInfraContext();
		return infraContext.getSolutionName() + "." + infraContext.getProjectName() + "." + simpleName;
	}

	private static <T extends MapModel> FailoverQueue<T> createQueue(String canonicalName, Class<T> clazz, boolean persist, int maxSize) {
		try {
			if (persist) {
				return new H2FailoverQueue<T>(canonicalName, clazz);
			}
		} catch (Exception e) {
			// TODO 환경에 따른 로그 레벨 조정.
			log.error("[MOM] Fail to create persistent queue", e);
		}

		return new MemoryFailoverQueue<T>(canonicalName, persist, maxSize);
	}
}
