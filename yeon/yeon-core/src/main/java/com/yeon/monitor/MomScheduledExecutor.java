package com.yeon.monitor;

import com.yeon.remote.RemoteContext;
import com.yeon.remote.reload.AbstractReloadService;
import com.yeon.util.ExceptionUtils;
import com.yeon.util.MapModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;

/**
 * 
 * @author pulsarang
 */
public class MomScheduledExecutor {
	private static final Logger LOG = LoggerFactory.getLogger(MomScheduledExecutor.class);

	public static final String SCHEDULER_MONITOR_NAME = "MOM_SCHEDULED_EXECUTOR_MONITOR";
	public static final String EXECUTION_MONITOR_NAME = "MOM_SCHEDULED_EXECUTION_MONITOR";

	private static ScheduledThreadPoolExecutor scheduler;

	public static final int CORE_POOL_SIZE = 16;

	static {
		scheduler = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE, new MomThreadFactory("scheduler"));
	}

	static void destroy() {
		scheduler.shutdownNow();
	}

	public static boolean isShutdown() {
		return scheduler.isShutdown();
	}

	public static ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		return scheduler.schedule(new MonitoredCommand(command), delay, unit);
	}

	public static ScheduledFuture<?> scheduleAtPeriodic(Runnable command, long interval, long delay) {
		long now = System.currentTimeMillis();
		long elapsedTime = now % interval;

		long sleepTime;
		if (elapsedTime < delay) {
			sleepTime = delay - elapsedTime;
		} else {
			sleepTime = interval - (elapsedTime - delay);
		}

		return scheduler.schedule(new MonitoredCommand(command), sleepTime, TimeUnit.MILLISECONDS);
	}

	public static void execute(Runnable command) {
		scheduler.execute(new MonitoredCommand(command));
	}

	public static void scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
		scheduler.scheduleAtFixedRate(new MonitoredCommand(command), initialDelay, period, unit);
	}

	// public static void remove(Runnable runnable) {
	// scheduler.remove(runnable);
	// }

	/**
	 * 
	 * @author pulsarang
	 */
	public interface IdentifyingRunnable extends Runnable {
		String getId();
	}

	private static ConcurrentHashMap<String, ExecutionInfo> executionInfoMap = new ConcurrentHashMap<String, MomScheduledExecutor.ExecutionInfo>();

	private static String getRunnableId(Runnable runnable) {
		if (runnable instanceof IdentifyingRunnable) {
			return ((IdentifyingRunnable) runnable).getId();
		} else {
			return runnable.getClass().getCanonicalName();
		}
	}

	/**
	 * 
	 * @author pulsarang
	 */
	private static class MonitoredCommand implements Runnable {
		final Runnable command;

		private MonitoredCommand(Runnable command) {
			super();
			this.command = command;
		}

		@Override
		public void run() {
			HistoryItem historyItem = new HistoryItem();
			String id = getRunnableId(command);

			if (id != null) {
				logHistory(id, historyItem);
			}

			try {
				command.run();
			} catch (Throwable t) {
				historyItem.throwable = t;
				LOG.warn("[YEON] Fail to execute command.", t);
			} finally {
				historyItem.finishTimeInNano = System.nanoTime();
			}
		}

		private void logHistory(String id, HistoryItem historyItem) {
			ExecutionInfo executionInfo = executionInfoMap.get(id);
			if (executionInfo == null) {
				ensureSize();

				executionInfo = new ExecutionInfo(id);
				ExecutionInfo previousExecutionInfo = executionInfoMap.putIfAbsent(id, executionInfo);
				if (previousExecutionInfo != null) {
					executionInfo = previousExecutionInfo;
				}
			}

			executionInfo.addHistoryItem(historyItem);
		}

		private void ensureSize() {
			final int maxSize = 100;
			if (executionInfoMap.size() <= maxSize) {
				return;
			}

			List<ExecutionInfo> executionInfos = new ArrayList<ExecutionInfo>(executionInfoMap.values());
			Collections.sort(executionInfos, new Comparator<ExecutionInfo>() {
				@Override
				public int compare(ExecutionInfo o1, ExecutionInfo o2) {
					return (int) (o1.lastExecuteTime - o2.lastExecuteTime);
				}
			});

			for (int i = 0, n = maxSize / 10; i < n; i++) {
				executionInfoMap.remove(executionInfos.get(i).id);
			}
		}
	}

	/**
	 * 
	 * @author pulsarang
	 */
	static class ExecutionInfo {
		final String id;
		int executeCount;
		long lastExecuteTime;
		final LinkedList<HistoryItem> history = new LinkedList<HistoryItem>();

		public ExecutionInfo(String id) {
			this.id = id;
			lastExecuteTime = System.currentTimeMillis();
		}

		synchronized void addHistoryItem(HistoryItem item) {
			executeCount++;
			lastExecuteTime = item.startTimeInMillies;

			if (history.size() > CORE_POOL_SIZE) {
				history.remove();
			}

			history.add(item);
		}

		public Map<String, Object> toMap() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			map.put("execute_count", executeCount);
			List<Map<String, Object>> mapHistory = new ArrayList<Map<String, Object>>(history.size());
			for (HistoryItem item : new ArrayList<HistoryItem>(history)) {
				mapHistory.add(item.toMap());
			}
			map.put("history", mapHistory);

			return map;
		}
	}

	/**
	 * 
	 * @author pulsarang
	 */
	static class HistoryItem {
		HistoryItem() {
			startTimeInMillies = System.currentTimeMillis();
			startTimeInNano = System.nanoTime();
		}

		long startTimeInMillies;
		long startTimeInNano;
		long finishTimeInNano;
		Throwable throwable;

		public Map<String, Object> toMap() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("start_time", startTimeInMillies);
			if (finishTimeInNano == 0L) {
				map.put("spent_time", -1L);
			} else {
				map.put("spent_time", finishTimeInNano - startTimeInNano);
			}
			map.put("exception", ExceptionUtils.getFullStackTrace(throwable));

			return map;
		}
	}

	public static void registAsRemoteService(RemoteContext remoteContext) {
		remoteContext.setRemoteService(SCHEDULER_MONITOR_NAME, new SchedulerMonitor());
		remoteContext.setRemoteService(EXECUTION_MONITOR_NAME, new ExecutionMonitor());
	}

	/**
	 * 
	 * @author pulsarang
	 */
	static class SchedulerMonitor extends AbstractReloadService {
		@Override
		public List<MapModel> list(MapModel optionMap) {
			MapModel statusMap = new MapModel();
			statusMap.put("task_count", scheduler.getTaskCount());
			statusMap.put("largest_pool_size", scheduler.getLargestPoolSize());
			statusMap.put("active_count", scheduler.getActiveCount());
			statusMap.put("completed_task_count", scheduler.getCompletedTaskCount());
			BlockingQueue<Runnable> queue = scheduler.getQueue();
			statusMap.put("queue_size", queue.size());
			List<Map<String, Object>> urnnableIds;
			if (!queue.isEmpty()) {
				List<Runnable> runnables = new ArrayList<Runnable>(queue);
				urnnableIds = new ArrayList<Map<String, Object>>(runnables.size());
				for (Runnable runnable : runnables) {
					urnnableIds.add(getTaskInfo(runnable));
				}
			} else {
				urnnableIds = Collections.emptyList();
			}
			statusMap.put("queue_content", urnnableIds);
			statusMap.put("shutdown", scheduler.isShutdown());

			List<MapModel> result = new ArrayList<MapModel>(1);
			result.add(statusMap);

			return result;
		}

		private Map<String, Object> getTaskInfo(Runnable runnable) {
			Map<String, Object> map = new HashMap<String, Object>();

			try {
				// java.util.concurrent.ScheduledThreadPoolExecutor.ScheduledFutureTask
				Method method = runnable.getClass().getMethod("getDelay", TimeUnit.class);
				method.setAccessible(true);
				Long delay = (Long) method.invoke(runnable, TimeUnit.SECONDS);
				map.put("delay", delay);

				// java.util.concurrent.FutureTask.Sync<V>
				Field syncField = runnable.getClass().getSuperclass().getDeclaredField("sync");
				syncField.setAccessible(true);
				Object sync = syncField.get(runnable);

				// java.util.concurrent.Executors.RunnableAdapter<T>
				Field callableField = sync.getClass().getDeclaredField("callable");
				callableField.setAccessible(true);
				Object callable = callableField.get(sync);

				// com.nbp.nmp.mom.config.MomScheduledExecutor.MonitoredCommand
				Field taskField = callable.getClass().getDeclaredField("task");
				taskField.setAccessible(true);
				MonitoredCommand task = (MonitoredCommand) taskField.get(callable);
				map.put("id", getRunnableId(task.command));
			} catch (Exception e) {
				map.put("exception", e.getMessage());
			}

			return map;
		}
	}

	/**
	 * 
	 * @author pulsarang
	 */
	static class ExecutionMonitor extends AbstractReloadService {
		@Override
		public List<MapModel> list(MapModel optionMap) {
			List<MapModel> result = new ArrayList<MapModel>(executionInfoMap.size());
			for (ExecutionInfo info : executionInfoMap.values()) {
				result.add(new MapModel(info.toMap()));
			}

			return result;
		}

		@Override
		public int listCount(MapModel optionMap) {
			return executionInfoMap.size();
		}
	}
}
