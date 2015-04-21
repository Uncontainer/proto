package com.yeon.mom;

import com.yeon.mom.event.EventType;

import java.util.List;

/**
 * Event 처리를 위한 inteface
 * 
 * @author pulsarang
 */
public interface EventProcessor {
	/**
	 * EventProcessor의 이름을 돌려준다.<br/>
	 * 동일한 프로젝트에서는 같은 이름을 사용할 수 없다.
	 * 
	 * @return eventProcssor의 이름
	 */
	String getName();

	/**
	 * 수신할 event 목록을 돌려준다.<br/>
	 * 같은 이름의 EventProcessor들이 중복적으로 event를 수신할 수는 없다. 겹칠 경우 서버 시작 시에 예외가 발생한다.
	 * 
	 * @return 수신할 event의 목록
	 */
	List<? extends EventType> getSubscribeEvents();

	/**
	 * 첫 event 처리 시 호출된다.
	 * 
	 * @param context event 처리에 필요한 {@link EventProcessContext} 객체
	 * 
	 * @exception ReprocessableException 재처리가 필요할 경우 발생. 재처리에 필요한 값들은 생성자의 argument 파라미터로 절달할 수 있다.
	 */
	void process(EventProcessContext context);

	/**
	 * 재처리가 필요한 event({@link ReprocessableException}을 발생)에 대해서 호출된다.<br/> 
	 * {@link EventProcessContext#getReprocessArguments()}로 예외 발생시에 전달한 재처리 인자들을 이용할 수 있다.
	 * 
	 * @param context event 처리에 필요한 {@link EventProcessContext} 객체
	 * @exception ReprocessableException 재처리가 필요할 경우 발생. 재처리에 필요한 값들은 생성자의 argument 파라미터로 절달할 수 있다.
	 */
	void reprocess(EventProcessContext context);

	/**
	 * 중복 처리의 가능성이 있고, mom에서 그 여부를 판단할 수 없을 경우 호출된다.<br/>
	 * 재처리일 수 있으며 이것은 {@link EventProcessContext#isReprocess()}로 확인 가능하다.
	 * 
	 * @param context event 처리에 필요한 {@link EventProcessContext} 객체
	 * @exception ReprocessableException 재처리가 필요할 경우 발생. 재처리에 필요한 값들은 생성자의 argument 파라미터로 절달할 수 있다.
	 */
	void verify(EventProcessContext context);

	/**
	 * 재처리 정책을 모두 수행한 이후에도 처리에 성공하지 못하였였을 경우 호출된다.<br/>
	 * 여기서 발생된 예외는 local 로깅만 이뤄질 뿐 무시된다.
	 * 
	 * @param context event 처리에 필요한 {@link EventProcessContext} 객체
	 */
	void fail(EventProcessContext context);
}
