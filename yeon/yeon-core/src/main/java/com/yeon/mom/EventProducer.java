package com.yeon.mom;

import com.yeon.mom.event.Event;

/**
 * Event를 생성한다.
 * 
 * @author pulsarang
 */
public interface EventProducer {
	/**
	 * Event를 MQ에 전송한다.
	 * 
	 * @param event 전송할 event
	 */
	void publish(Event event);

	/**
	 * Event를 MQ에 전송한다.
	 * 
	 * @param event 전송할 event
	 * @param async true일 경우 local의 file-queue에 임시 저장된 후 전달된다. MQ 자체의 비동기 전송 기능을 의미하지는 않는다.
	 */
	void publish(Event event, boolean async);

	/**
	 * 처리에 실패한 event를 재전송한다.
	 * 재처리에 필요한 정보를 포함하고 있어야 하며, mom 내부적으로 사용하는 것으로 직접적으로 사용하는 것은 권고하지 않는다.
	 * 
	 * @param event 재전송할 event
	 */
	void retry(Event event);
}
