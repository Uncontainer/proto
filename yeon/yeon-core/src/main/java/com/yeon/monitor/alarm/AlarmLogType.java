package com.yeon.monitor.alarm;

public class AlarmLogType {
	// 공통:queue 관련 알람
	public static final String ALT_CREATE_QUEUE_FAIL = "E1101";
	public static final String ALT_QUEUE_OVERFLOW = "E1102";
	public static final String ALT_HYBIRD_QUEUE_MODE_CHANGED = "E1103";

	// 공통:스케쥴링 관련 알람.
	public static final String ALT_SCHEDULE_FAIL = "E1201";

	// Producer:EventBuffer 관련 알람.
	public static final String ALT_EVENT_BUFFER_NOT_FLUSHED = "E2101";
	public static final String ALT_EVENT_BUFFER_FORCE_FLUSHED = "E2102";
	public static final String ALT_EVENT_BUFFER_ZERO_REF_COUNT = "E2103";
	public static final String ALT_EVENT_BUFFER_NONZERO_REF_FLUSH = "E2104";

	// EventProcessor:일반 관련 알람.
	public static final String ALT_DECODE_EVENT_FAIL = "E3101";
	public static final String ALT_MESSAGE_CONSUMER_STOP = "E3102";
	public static final String ALT_PROCESSOR_MQ_CONNECT_FAIL = "E3103";

}
