package com.yeon.mom;

import com.yeon.YeonPredefinedClassId;
import com.yeon.config.Ticket;
import com.yeon.mom.event.Event;
import com.yeon.mom.service.ServiceStatus;
import com.yeon.server.ProjectId;
import com.yeon.util.LocalAddressHolder;
import com.yeon.util.NanoStopWatch;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author pulsarang
 */
public class EventProcessorInfo extends Ticket {
	protected static final String PARAM_LOG_LEVEL = "log_level";
	protected static final String PARAM_PROCESS_SERVER_IPS = "proc_svr_ips";
	protected static final String PARAM_SUBSCRIBE_EVENTS = "svsc_evnts";
	protected static final String PARAM_MAX_TRY_COUNT = "max_try_cnt";
	protected static final String PARAM_MIN_TRY_INTERVAL = "min_try_interval";
	protected static final String PARAM_SLOW_TIME = "slow_time";

	protected static final String PARAM_USE_DETECTING_LOSS_EVENT = "use_detecting_loss_event";
	protected static final String PARAM_MAX_DELAY_TIME = "max_delay_time";

	protected static final String PARAM_RETRY_ON_ALL_EXCEPTION = "retry_on_all_exception";

	protected static final String PARAM_THREAD_COUNT = "thread_count";
	protected static final String PARAM_TTL = "ttl";
	protected static final String PARAM_MAX_TPS = "max_tps";

	protected volatile transient ServiceStatus status;
	protected volatile transient LogLevel logLevel;

	protected volatile transient EventProcessorInfoId eventProcessorInfoId;

	public static final long DEFAULT_TTL = TimeUnit.DAYS.toMillis(7);

	/**
	 * @author pulsarang
	 */
	public enum LogLevel {
		ALL, SLOW, EXCEPTION, NONE;

		public boolean isEnabled(LogLevel level) {
			return this.ordinal() <= level.ordinal();
		}
	}

	public EventProcessorInfo() {
		super(YeonPredefinedClassId.EVENT_PROCESSOR);
	}

	public EventProcessorInfo(Map<String, Object> properties) {
		super(YeonPredefinedClassId.EVENT_PROCESSOR, properties);
	}

	public String getName() {
		return getString(PARAM_NAME);
	}

	public void setName(String name) {
		setString(PARAM_NAME, name);
		eventProcessorInfoId = new EventProcessorInfoId(name);
	}

	public ProjectId getProjectId() {
		return eventProcessorInfoId == null ? null : eventProcessorInfoId.getProjectId();
	}

	public String getSolutionName() {
		ProjectId projectId = getProjectId();

		return projectId != null ? projectId.getSolutionName() : null;
	}

	public String getProjectName() {
		ProjectId projectId = getProjectId();

		return projectId != null ? projectId.getProjectName() : null;
	}

	public List<String> getSubscribeEventCodes() {
		return getList(PARAM_SUBSCRIBE_EVENTS, EMPTY_LIST);
	}

	public void setSubscribeEventCodes(List<String> subscribeEvents) {
		setList(PARAM_SUBSCRIBE_EVENTS, subscribeEvents);
	}

	public List<String> getProcessServerIps() {
		return getList(PARAM_PROCESS_SERVER_IPS, EMPTY_LIST);
	}

	public void setProcessServerIps(List<String> processServerIps) {
		setList(PARAM_PROCESS_SERVER_IPS, processServerIps);
	}

	public int getSlowTime() {
		return getInteger(PARAM_SLOW_TIME, 3000);
	}

	public void setSlowTime(int slowTime) {
		setInteger(PARAM_SLOW_TIME, slowTime);
	}

	public int getMaxTryCount() {
		return getInteger(PARAM_MAX_TRY_COUNT, 10);
	}

	public void setMaxTryCount(int count) {
		setInteger(PARAM_MAX_TRY_COUNT, count);
	}

	public int getMinTryInterval() {
		return getInteger(PARAM_MIN_TRY_INTERVAL, 10000);
	}

	public void setMinTryInterval(int minTryInterval) {
		setInteger(PARAM_MIN_TRY_INTERVAL, minTryInterval);
	}

	public ServiceStatus getStatus() {
		if (status == null) {
			status = getEnum(PARAM_STATUS, ServiceStatus.NORMAL, ServiceStatus.class);
		}

		return status;
	}

	public String getStatusCode() {
		return getStatus().name();
	}

	public void setStatus(ServiceStatus status) {
		setEnum(PARAM_STATUS, status);
		this.status = status;
	}

	public void setStatusCode(String statusCode) {
		setString(PARAM_STATUS, statusCode);
		status = null;
	}

	public LogLevel getLogLevel() {
		if (logLevel == null) {
			logLevel = getEnum(PARAM_LOG_LEVEL, LogLevel.ALL, LogLevel.class);
		}

		return logLevel;
	}

	public String getLogLevelCode() {
		return getLogLevel().name();
	}

	public void setLogLevelCode(String logLevelCode) {
		setString(PARAM_LOG_LEVEL, logLevelCode);
		logLevel = null;
	}

	public Boolean isUseDetectingLossEvent() {
		return getBoolean(PARAM_USE_DETECTING_LOSS_EVENT, true);
	}

	public void setUseDetectingLossEvent(Boolean useDetectingLossEvent) {
		setBoolean(PARAM_USE_DETECTING_LOSS_EVENT, useDetectingLossEvent);
	}

	public long getMaxDelayTime() {
		return getLong(PARAM_MAX_DELAY_TIME, 60000L);
	}

	public void setMaxDelayTime(long maxDelayTime) {
		setLong(PARAM_MAX_DELAY_TIME, maxDelayTime);
	}

	public boolean isRetryOnAllException() {
		return getBoolean(PARAM_RETRY_ON_ALL_EXCEPTION, false);
	}

	public void setRetryOnAllException(boolean retryOnAllException) {
		setBoolean(PARAM_RETRY_ON_ALL_EXCEPTION, retryOnAllException);
	}

	public int getThreadCount() {
		return getInteger(PARAM_THREAD_COUNT, 1);
	}

	public void setThreadCount(int threadCount) {
		setInteger(PARAM_THREAD_COUNT, threadCount);
	}

	public long getTtl() {
		return getLong(PARAM_TTL, DEFAULT_TTL);
	}

	public void setTtl(long ttl) {
		setLong(PARAM_TTL, ttl);
	}

	public int getMaxTps() {
		return getInteger(PARAM_MAX_TPS, -1);
	}

	public void setMaxTps(int maxTps) {
		setInteger(PARAM_MAX_TPS, maxTps);
	}

	public boolean isExpired(Event event) {
		Date eventDate = event.getEventDate();
		if (eventDate == null) {
			return true;
		}

		return (System.currentTimeMillis() - eventDate.getTime()) > getTtl();
	}

	public boolean isSubscribeEnabled() {
		if (getStatus() != ServiceStatus.NORMAL && getStatus() != ServiceStatus.STOP) {
			return false;
		}

		List<String> processServerIps = getProcessServerIps();
		if (!processServerIps.isEmpty() && !processServerIps.contains(LocalAddressHolder.getLocalAddress())) {
			return false;
		}

		return true;
	}

	public boolean isFailLoggingEnabled() {
		return isUseDetectingLossEvent() || getLogLevel().isEnabled(LogLevel.EXCEPTION);
	}

	public boolean isSuccessLoggingEnabled(NanoStopWatch stopWatch) {
		if (isUseDetectingLossEvent()) {
			return true;
		}

		if (getLogLevel().isEnabled(LogLevel.ALL)) {
			return true;
		}

		if (getLogLevel().isEnabled(LogLevel.SLOW) && (stopWatch.getTimeInMillies() >= getSlowTime())) {
			return true;
		}

		return false;
	}

	public boolean isLoggingFailoverRequired() {
		return isUseDetectingLossEvent();
	}
}
