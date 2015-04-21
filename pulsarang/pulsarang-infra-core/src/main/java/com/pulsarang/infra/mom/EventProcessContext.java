package com.pulsarang.infra.mom;

import java.util.Map;

import com.pulsarang.infra.mom.event.Event;

/**
 * event 처리 context
 * 
 * @see EventProcessor
 * @author pulsarang
 */
public interface EventProcessContext {
	/**
	 * 처리한 Event를 돌려준다. Event는 null이 아님을 보장한다.
	 * 
	 * @return 처리할 event.
	 */
	Event getEvent();

	/**
	 * 재처리에 필요한 인자들을 돌려준다.<br/>
	 * 재처리 인자는 {@link ReprocessableException} 발생시에 생성자의 argument에 명시할 수 있다.
	 * 
	 * @return 재처리 인자. {@link ReprocessableException} 발생시 전달받은 인지가 없다면 null
	 */
	Map<String, Object> getReprocessArguments();

	/**
	 * 재처리 여부를 검사한다.
	 * 
	 * @return 재처리일 경우 true, 그렇지 않으면 false
	 */
	boolean isReprocess();
}
