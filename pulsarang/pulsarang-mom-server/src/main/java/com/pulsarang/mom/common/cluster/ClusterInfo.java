package com.pulsarang.mom.common.cluster;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import com.pulsarang.infra.config.Config;

public class ClusterInfo extends Config {
	// dispatcher configuration
	public static final String CONTROLLER_EXECUTION_TIMEOUT = "controller_execution_timeout";
	public static final String DISPATCHER_POLLING_INTERVAL = "dispatcher_polling_interval";
	public static final String PROCESSOR_EXECUTOR_CORE_POOL_SIZE = "processor_executor_core_pool_size";

	public static final String HEAVY_USER_CONTROLLER_COUNT = "heavy_user_controller_count";
	public static final String LIGHT_USER_CONTROLLER_COUNT = "light_user_controller_count";
	public static final String REPROCESS_CONTROLLER_COUNT = "reprocess_controller_count";

	// cluster configuration
	public static final String CLUSTER_MAIN_DB_NO = "cluster_main_db_no";
	public static final String CLUSTER_FAILOVER_DB_NO = "cluster_failover_db_no";
	public static final String CLUSTER_LOG_DB_NO = "cluster_log_db_no";
	public static final String CLUSTER_SERVER_IPS = "cluster_server_ips";
	public static final String CLUSTER_STATUS = "cluster_status";

	private static final long DEFAULT_POLLING_INTERVAL = TimeUnit.SECONDS.toMillis(10);

	public long getDispatcherPollingInterval() {
		return getLong(DISPATCHER_POLLING_INTERVAL, DEFAULT_POLLING_INTERVAL);
	}

	public long getControllerExecutionTimeout() {
		// TODO caching 처리.
		long millies = getLong(CONTROLLER_EXECUTION_TIMEOUT);
		if (millies < 0) {
			return 0L;
		}

		if (millies < TimeUnit.MINUTES.toMillis(1)) {
			return TimeUnit.MINUTES.toMillis(1);
		}

		return millies;
	}

	public int getProcessorExecutorPoolSize() {
		return getInteger(PROCESSOR_EXECUTOR_CORE_POOL_SIZE, 100);
	}

	public void setProcessorExecutorPoolSize(int size) {
		setProperty(PROCESSOR_EXECUTOR_CORE_POOL_SIZE, size);
	}

	public ClusterStatus getClusterStatus() {
		String strClusterStatus = getString(CLUSTER_STATUS);
		return ClusterStatus.valueOf(strClusterStatus.toUpperCase());
	}

	public void setClusterStatus(ClusterStatus clusterStatus) {
		setProperty(ClusterInfo.CLUSTER_STATUS, clusterStatus.toString());
	}

	public int getServerCount() {
		return ((Collection<?>) getProperty(CLUSTER_SERVER_IPS)).size();
	}

	public Collection<String> getClusterServerIps() {
		return getProperty(CLUSTER_SERVER_IPS);
	}

	// main_db_no=10
	// failover_db_no=10
	// log_db_no=100
	// status=normal
	// server_ips=10.25.66.192
	//
	// heavy_user_controller_count=1
	// light_user_controller_count=1
	// reprocess_controller_count=1
	//
	// dispatcher_polling_interval=3000
	// processor_executor_core_pool_size=100
	// controller_execution_timeout=3600000
}
