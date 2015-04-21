package com.pulsarang.mom.store.proc;

import com.pulsarang.mom.store.EventEnvelope;

public interface EventConsumer {
	void process(EventEnvelope item, boolean verify);

	/**
	 * 실행 예정인 작업들을 취소한다.
	 * 
	 * @return 작업이 이미 시작되어 취소가 불가능할 경우 false
	 */
	boolean cancel();
}
