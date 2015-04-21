package com.yeon.mom.rabbitmq.consumer.mon;

import com.yeon.mom.event.EventProcessInfo;
import com.yeon.util.MapModel;

import java.util.*;

/**
 * @author EC셀러서비스개발팀
 */
public class EventProcessorMonitorEntry extends MapModel {
	public static final String PARAM_ID = "id";
	public static final String PARAM_ACTIVE_COUNT = "ac";
	public static final String PARAM_ERROR_COUNT = "ec";
	public static final String PARAM_SKIP_COUNT = "sc";
	public static final String PARAM_PROCESS_INVOKE_COUNT = "pic";
	public static final String PARAM_REPROCESS_INVOKE_COUNT = "ric";
	public static final String PARAM_VERIFY_INVOKE_COUNT = "vic";
	public static final String PARAM_FAIL_INVOKE_COUNT = "fic";
	public static final String PARAM_LAST_POP_TIME = "lpt";
	public static final String PARAM_PROCESS_EVENTS = "pes";
	public static final String PARAM_TOTAL_INVOKE_COUNT = "tic";
	public static final String PARAM_TOTAL_SPENT_TIME = "tst";
	public static final String PARAM_SLOWEST_PROCESS_TIME = "spt";
	public static final String PARAM_STATUS = "sts";
	public static final String PARAM_LAST_PROCESS_EVENT_ENQUE_TIME = "lpeet";

	public EventProcessorMonitorEntry() {
	}

	public EventProcessorMonitorEntry(Map<String, Object> properties) {
		super(properties);
	}

	public EventProcessorMonitorEntry(EventProcessorMonitor monitor) {
		this.setId(monitor.getCanonicalName());
		this.setActiveCount(monitor.getActiveCount());
		this.setErrorCount(monitor.getErrorCount());
		this.setSkipCount(monitor.getSkipCount());
		this.setProcessInvokeCount(monitor.getProcessInvokeCount());
		this.setReprocessInvokeCount(monitor.getReprocessInvokeCount());
		this.setVerifyInvokeCount(monitor.getVerifyInvokeCount());
		this.setFailInvokeCount(monitor.getFailInvokeCount());
		this.setLastPopTime(monitor.getLastPopTime());
		this.setLastProcessEventEnqueTime(monitor.getLastProcessEventEnqueTime());
		this.setTotalInvokeCount(monitor.getTotalInvokeCount());
		this.setTotalSpentTime(monitor.getTotalSpentTime());
		this.setSlowestProcessTime(monitor.getSlowestProcessTime());
		this.setProcessEvents(monitor.getProcessEvents());
	}

	public String getId() {
		return getString(PARAM_ID);
	}

	public void setId(String id) {
		setString(PARAM_ID, id);
	}

	public String getStatus() {
		return getString(PARAM_STATUS);
	}

	public void setStatus(String status) {
		setString(PARAM_STATUS, status);
	}

	public long getActiveCount() {
		return getLong(PARAM_ACTIVE_COUNT, 0L);
	}

	public void setActiveCount(long count) {
		setLong(PARAM_ACTIVE_COUNT, count);
	}

	public long getErrorCount() {
		return getLong(PARAM_ERROR_COUNT, 0L);
	}

	public void setErrorCount(long count) {
		setLong(PARAM_ERROR_COUNT, count);
	}

	public long getSkipCount() {
		return getLong(PARAM_SKIP_COUNT, 0L);
	}

	public void setSkipCount(long count) {
		setLong(PARAM_SKIP_COUNT, count);
	}

	public long getProcessInvokeCount() {
		return getLong(PARAM_PROCESS_INVOKE_COUNT, 0L);
	}

	public void setProcessInvokeCount(long count) {
		setLong(PARAM_PROCESS_INVOKE_COUNT, count);
	}

	public long getReprocessInvokeCount() {
		return getLong(PARAM_REPROCESS_INVOKE_COUNT, 0L);
	}

	public void setReprocessInvokeCount(long count) {
		setLong(PARAM_REPROCESS_INVOKE_COUNT, count);
	}

	public long getVerifyInvokeCount() {
		return getLong(PARAM_VERIFY_INVOKE_COUNT, 0L);
	}

	public void setVerifyInvokeCount(long count) {
		setLong(PARAM_VERIFY_INVOKE_COUNT, count);
	}

	public long getFailInvokeCount() {
		return getLong(PARAM_FAIL_INVOKE_COUNT, 0L);
	}

	public void setFailInvokeCount(long count) {
		setLong(PARAM_FAIL_INVOKE_COUNT, count);
	}

	public Date getLastPopTime() {
		Long lastPopTime = getLong(PARAM_LAST_POP_TIME);
		if (lastPopTime == null) {
			return null;
		} else {
			return new Date(lastPopTime);
		}
	}

	public void setLastPopTime(Date lastPopTime) {
		if (lastPopTime == null) {
			removeValue(PARAM_LAST_POP_TIME);
		} else {
			setLong(PARAM_LAST_POP_TIME, lastPopTime.getTime());
		}
	}

	public Date getLastProcessEventEnqueTime() {
		Long lastPopTime = getLong(PARAM_LAST_PROCESS_EVENT_ENQUE_TIME);
		if (lastPopTime == null) {
			return null;
		} else {
			return new Date(lastPopTime);
		}
	}

	public void setLastProcessEventEnqueTime(Date lastProcessEventEnqueTime) {
		if (lastProcessEventEnqueTime == null) {
			removeValue(PARAM_LAST_PROCESS_EVENT_ENQUE_TIME);
		} else {
			setLong(PARAM_LAST_PROCESS_EVENT_ENQUE_TIME, lastProcessEventEnqueTime.getTime());
		}
	}

	public long getTotalInvokeCount() {
		return getLong(PARAM_TOTAL_INVOKE_COUNT, 0L);
	}

	public void setTotalInvokeCount(long count) {
		setLong(PARAM_TOTAL_INVOKE_COUNT, count);
	}

	public long getTotalSpentTime() {
		return getLong(PARAM_TOTAL_SPENT_TIME, 0L);
	}

	public void setTotalSpentTime(long time) {
		setLong(PARAM_TOTAL_SPENT_TIME, time);
	}

	public long getSlowestProcessTime() {
		return getLong(PARAM_SLOWEST_PROCESS_TIME, 0L);
	}

	public void setSlowestProcessTime(long time) {
		setLong(PARAM_SLOWEST_PROCESS_TIME, time);
	}

	public Map<Long, EventProcessInfo> getProcessEvents() {
		Map<Long, EventProcessInfo> result;
		List<Map<String, Object>> rawProcessEvents = getValue(PARAM_PROCESS_EVENTS);
		if (rawProcessEvents != null) {
			result = new HashMap<Long, EventProcessInfo>();
			for (Map<String, Object> rawEventProcessInfo : rawProcessEvents) {
				EventProcessInfo eventProcessInfo = new EventProcessInfo();
				eventProcessInfo.setValues(rawEventProcessInfo);
				result.put(eventProcessInfo.getEventId(), eventProcessInfo);
			}
		} else {
			result = Collections.emptyMap();
		}

		return result;
	}

	public void setProcessEvents(Map<Long, EventProcessInfo> processEvents) {
		if (processEvents == null || processEvents.isEmpty()) {
			List<Map<String, Object>> rawProcessEvents = new ArrayList<Map<String, Object>>(processEvents.size());
			for (EventProcessInfo processEvent : processEvents.values()) {
				rawProcessEvents.add(processEvent.getValues());
			}

			setValue(PARAM_PROCESS_EVENTS, rawProcessEvents);
		} else {
			removeValue(PARAM_PROCESS_EVENTS);
		}
	}

	public void merge(EventProcessorMonitorEntry other) {
		setActiveCount(getActiveCount() + other.getActiveCount());
		setErrorCount(getErrorCount() + other.getErrorCount());
		setSkipCount(getSkipCount() + other.getSkipCount());
		setProcessInvokeCount(getProcessInvokeCount() + other.getProcessInvokeCount());
		setReprocessInvokeCount(getReprocessInvokeCount() + other.getReprocessInvokeCount());
		setVerifyInvokeCount(getVerifyInvokeCount() + other.getVerifyInvokeCount());
		setFailInvokeCount(getFailInvokeCount() + other.getFailInvokeCount());
		setTotalInvokeCount(getTotalInvokeCount() + other.getTotalInvokeCount());
		setTotalSpentTime(getTotalSpentTime() + other.getTotalSpentTime());
		setStatus(getStatus());

		if (getSlowestProcessTime() < other.getSlowestProcessTime()) {
			setSlowestProcessTime(other.getSlowestProcessTime());
		}

		setLastPopTime(getNew(getLastPopTime(), other.getLastPopTime()));
		setLastProcessEventEnqueTime(getNew(getLastProcessEventEnqueTime(), other.getLastProcessEventEnqueTime()));

		List<Map<String, Object>> sourceEvents = getValue(PARAM_PROCESS_EVENTS);
		List<Map<String, Object>> targetEvents = other.getValue(PARAM_PROCESS_EVENTS);
		if (sourceEvents == null) {
			if (targetEvents != null) {
				setValue(PARAM_PROCESS_EVENTS, targetEvents);
			}
		} else {
			if (targetEvents != null) {
				sourceEvents.addAll(targetEvents);
			}
		}
	}

	public EventProcessorMonitorEntry diff(EventProcessorMonitorEntry old) {
		if (old == null) {
			throw new IllegalArgumentException("Null entry");
		}

		if (getId() != null && !getId().equals(old.getId())) {
			throw new IllegalArgumentException("QueueId is different.");
		}

		EventProcessorMonitorEntry result = new EventProcessorMonitorEntry();
		result.setId(getId());
		result.setLastPopTime(getLastPopTime());
		result.setLastProcessEventEnqueTime(getLastProcessEventEnqueTime());
		result.setActiveCount(getActiveCount());
		result.setErrorCount(getErrorCount() - old.getErrorCount());
		result.setSkipCount(getSkipCount() - old.getSkipCount());
		result.setProcessInvokeCount(getProcessInvokeCount() - old.getProcessInvokeCount());
		result.setReprocessInvokeCount(getReprocessInvokeCount() - old.getReprocessInvokeCount());
		result.setVerifyInvokeCount(getVerifyInvokeCount() - old.getVerifyInvokeCount());
		result.setFailInvokeCount(getFailInvokeCount() - old.getFailInvokeCount());
		result.setTotalInvokeCount(getTotalInvokeCount() - old.getTotalInvokeCount());
		result.setTotalSpentTime(getTotalSpentTime() - old.getTotalSpentTime());
		if (getSlowestProcessTime() < old.getSlowestProcessTime()) {
			setSlowestProcessTime(old.getSlowestProcessTime());
		}
		result.setStatus(getStatus());
		result.setValue(EventProcessorMonitorEntry.PARAM_PROCESS_EVENTS, getValue(EventProcessorMonitorEntry.PARAM_PROCESS_EVENTS));

		return result;
	}

	private static Date getNew(Date dx, Date dy) {
		if (dx == null) {
			return dy;
		} else {
			return (dy == null || dx.after(dy)) ? dx : dy;
		}
	}
}
