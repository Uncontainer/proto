package com.pulsarang.mom.common.dao;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.pulsarang.core.util.PeriodicTouchViolationChecker;

/**
 * 지정된 시간 간격 동간 지정한 횟수 이상의 DB관련 예외 발생 시 DB 장애로 인지하고 cluster의 상태를 변경하여 절체하도록 한다.
 * 
 * @author pulsarang
 */
@Component
public class DBMonitor {
	private static final Logger LOG = LoggerFactory.getLogger(DBMonitor.class);

	private final PeriodicTouchViolationChecker checker = new PeriodicTouchViolationChecker(5, TimeUnit.MINUTES.toMillis(1));

	public void catchException(SQLException exception) {
		// TODO UniqueKey 예외 검사.
		// if (exception instanceof DataIntegrityViolationException) {
		// return;
		// }

		if (checker.touch()) {
			LOG.warn("DB fail.");
			// TODO 담당자에게 에러 메일/메시지를 전송하도록 함.
			// clusterInfo.setClusterStatus(ClusterStatus.DB_FAIL);
		}
	}
}
