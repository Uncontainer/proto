package com.pulsarang.mom.dispatcher.processor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pulsarang.infra.config.Config;
import com.pulsarang.infra.config.ConfigId;
import com.pulsarang.infra.config.ConfigListener;
import com.pulsarang.mom.common.MomConfigCategory;
import com.pulsarang.mom.common.cluster.Cluster;
import com.pulsarang.mom.common.cluster.ClusterInfo;

/**
 * 
 * @author pulsarang
 */
@Component
public class EventProcessorInvokerPool implements ConfigListener, InitializingBean {
	private static final Logger LOG = LoggerFactory.getLogger(EventProcessorInvokerPool.class);

	@Autowired
	private Cluster cluster;

	private ThreadPoolExecutor executor;
	private AtomicInteger timeoutCount = new AtomicInteger();
	private AtomicLong executionCount = new AtomicLong();

	private volatile int corePoolSize;

	public EventProcessorInvokerPool() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.corePoolSize = cluster.getClusterInfo().getProcessorExecutorPoolSize();
		this.executor = new ThreadPoolExecutor(corePoolSize, corePoolSize * 2, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(1));
	}

	public void execute(final EventProcessorInvoker processorExecutor) {
		if (executor.isShutdown()) {
			throw new IllegalStateException();
		}

		executionCount.incrementAndGet();
		try {
			executor.execute(new ProcessorExecutorWrapper(processorExecutor));
		} catch (RejectedExecutionException e) {
			processorExecutor.cancel();
		}
	}

	public void shutdown() {
		List<Runnable> waitingProcessors = executor.shutdownNow();
		for (Runnable waitingProcessor : waitingProcessors) {
			((EventProcessorInvoker) waitingProcessor).cancel();
		}
	}

	/**
	 * 모니터링 용도로 읽기전용으로만 사용하며, executor에 대한 제어는 절대로 바로 하지 말 것.
	 */
	public ThreadPoolExecutor getExecutor() {
		return executor;
	}

	public int getTimeoutCount() {
		return timeoutCount.get();
	}

	public long getExecutionCount() {
		return executionCount.get();
	}

	@Override
	public List<ConfigId> getIds() {
		return Arrays.asList(new ConfigId(MomConfigCategory.CLUSTER, cluster.getClusterId()));
	}

	@Override
	public void validate(ConfigId configId, Config config) throws Exception {
	}

	@Override
	public void changed(ConfigId configId, Config config) {
		ClusterInfo clusterInfo = (ClusterInfo) config;
		int newCorePoolSize = clusterInfo.getProcessorExecutorPoolSize();
		if (newCorePoolSize != corePoolSize) {
			executor.setCorePoolSize(newCorePoolSize);
			executor.setMaximumPoolSize(newCorePoolSize * 2);
			corePoolSize = newCorePoolSize;
		}
	}

	/**
	 * 
	 * @author pulsarang
	 */
	private static class ProcessorExecutorWrapper implements Runnable {
		private EventProcessorInvoker processorExecutor;

		private ProcessorExecutorWrapper(EventProcessorInvoker processorExecutor) {
			this.processorExecutor = processorExecutor;
		}

		@Override
		public void run() {
			try {
				processorExecutor.run();
			} catch (RuntimeException e) {
				LOG.error("Fail to process event.", e);
			}
		}
	}
}
