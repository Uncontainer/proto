package com.yeon.mom;

import java.util.List;


/**
 * Mom 시스템을 지원하기 위한 주 interface.
 * @author pulsarang
 */
public interface MomContext {
	/**
	 * 초기화된 {@link EventProducer}를 돌려준다.
	 * 
	 * @return {@link EventProducer} 객체
	 */
	EventProducer getEventProducer();

	/**
	 * 주어진 {@link EventProcessor}를 등록한다.
	 * 
	 * @param eventProcessor 등록할 {@link EventProcessor}
	 * @throws IllegalStateException MomContext 초기화에 실패했을 경우.
	 * @throws IllegalArgumentException <br/>
	 * - eventProcessor가 null인 경우.<br/>
	 * - 주어진 eventProcessor의 이름({@link EventProcessor#getName()})이 null이거나 빈 문자열인 경우.<br/>
	 * - 주어진 eventProcessor의 수신 event({@link EventProcessor#getSubscribeEventTypes()}) 목록이 null 이거나 비었을 경우.<br/>
	 * - 동일한 이름으로  등록된 event 수신 목록과 겹치는 event 수신 목록이 있을 경우.<br/>
	 */
	void addEventProcessor(EventProcessor eventProcessor);

	/**
	 * 등록된 모든 {@link EventProcessor}의 목록을 돌려준다.
	 * 
	 * @return {@link EventProcessor} 목록. 등록된 {@link EventProcessor}가 없을 경우 빈 리스트 
	 * @throws IllegalStateException MomContext 초기화에 실패했을 경우.
	 */
	List<EventProcessor> getEventProcessors();

	/**
	 * 주어진 이름으로 등록된 {@link EventProcessor}를  돌려준다.
	 * 
	 * @param name {@link EventProcessor} 이름 
	 * @return {@link EventProcessor} 객체. 등록된 {@link EventProcessor}가 없을 경우 null 
	 * @throws IllegalStateException MomContext 초기화에 실패했을 경우.
	 * @throws IllegalArgumentException 주어진 이름이 null이거나 빈 문자열일 경우.
	 */
	EventProcessor getEventProcessor(String name);

	/**
	 * 주어진 worker class의 resque를 가져온다.<br/>
	 * 
	 * @param worker class
	 * @return resque 객체, 등록된 resque가 없을 경우 새로 생성한다.
	 * @throws IllegalArgumentException clazz가 null인 경우. public default 생성자가 없는 경우.
	 * @throws IllegalStateException MomContext 초기화에 실패한 경우.
	 */
	Resque getResque(Class<? extends ResqueWorker> clazz);

	/**
	 * 주어진 이름의 resque를 가져온다.<br/>
	 * 
	 * 
	 * @param name resqueWorker 이름
	 * @return resque 객체, 등록된 resque가 없을 경우 null
	 */
	Resque getResque(String workerName);
}