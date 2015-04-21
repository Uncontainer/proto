package com.pulsarang.infra.mom;

import java.util.concurrent.TimeUnit;

import com.pulsarang.infra.server.SolutionProjectPair;

public class MomConstants {
	// 동일한 event 처리 시 확보되는 최소 간격.
	public static final long MIN_RETRY_INTERVAL = TimeUnit.SECONDS.toMillis(10);

	// EventProcessor 호출 횟수 제어.
	public static final int EVENT_PROCESS_FAIL_MAX_ALLOW_COUNT = 3;
	public static final long EVENT_PROCESS_FAIL_CHECK_DURATION = TimeUnit.MINUTES.toMillis(1);
	public static final long SLEEP_TIME_ON_FREQUENT_EVENT_PROCESS_FAIL = TimeUnit.SECONDS.toMillis(5);

	// Event 관련 로그 송신 횟수 제어.
	public static final int LOG_SEND_FAIL_LOGGING_MAX_ALLOW_COUNT = 1;
	public static final long LOG_SEND_FAIL_LOGGING_CHECK_DURATION = TimeUnit.SECONDS.toMillis(3);

	// mom-admin 호출 횟수 제어.
	public static final int ADMIN_CALL_FAIL_MAX_ALLOW_COUNT = 5;
	public static final long ADMIN_CALL_FAIL_CHECK_DURATION = TimeUnit.SECONDS.toMillis(10);

	// RabbitMQ에 접속 시도 실패 횟수 제어.
	public static final int RABBIT_MQ_CONNECT_MAX_TRY_COUNT = 2;
	public static final long RABBIT_MQ_CONNECT_FAIL_CHECK_DURATION = TimeUnit.SECONDS.toMillis(5);

	public static final SolutionProjectPair MOM_SERVER = new SolutionProjectPair("sxb", "mom-server");
}
