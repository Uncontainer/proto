package com.yeon.async;

import com.yeon.YeonContext;
import com.yeon.api.ApiServiceFactory;
import com.yeon.monitor.alarm.AlarmLog;
import com.yeon.monitor.alarm.AlarmLogType;
import com.yeon.util.MapModel;
import com.yeon.util.NameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class FailoverQueueFactory {
	private static final Logger LOG = LoggerFactory.getLogger(FailoverQueueFactory.class);

	@SuppressWarnings("rawtypes")
	static Map<String, FailoverQueue> queues = new HashMap<String, FailoverQueue>();

	public synchronized static <T extends MapModel> FailoverQueue<T> getQueue(String name, Class<T> clazz, FailoverQueueType type, int maxSize) {
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

			queue = createQueue(canonicalName, clazz, type, maxSize);
			queues.put(canonicalName, queue);

			// 어플리케이션 종료 시점에 queue에 요청이 들어올 수 있으므로 close() 시점을 가능한 미룬다.
			Runtime.getRuntime().addShutdownHook(new Thread("mom-failover-queue-closer") {
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
		return YeonContext.getSolutionName() + "." + YeonContext.getProjectName() + "." + simpleName;
	}

	private static <T extends MapModel> FailoverQueue<T> createQueue(String canonicalName, Class<T> clazz, FailoverQueueType type, int maxSize) {
		try {
			switch (type) {
			case FILE:
				return new H2FailoverQueue<T>(canonicalName, clazz);
			case HYBRID:
				return new HybridFailoverQueue<T>(canonicalName, clazz, maxSize);
			}
		} catch (Exception e) {
			// TODO [LOW] 환경에 따른 로그 레벨 조정.
			LOG.warn("[YEON] Fail to create persistent queue. It is substituted with memory queue.\n{}", e.getMessage());
			ApiServiceFactory.getMonitorApiService().logAlarm(new AlarmLog(AlarmLogType.ALT_CREATE_QUEUE_FAIL, e, "FailoverQueueFactory"));
		}

		return new BlockingFailoverQueue<T>(canonicalName, maxSize);
	}
}
