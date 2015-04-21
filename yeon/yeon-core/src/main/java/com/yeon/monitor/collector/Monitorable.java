package com.yeon.monitor.collector;

/**
 * 모티터링이 필요한 context의 snapshot을 생성하지 위한 인터페이스를 정의한다.<br/>
 * {@linkplain LocalMonitorablePropertyCollector}에서 주기적으로 snapshot을 생성한다.
 * 
 * @see LocalMonitorablePropertyCollector
 * @author pulsarang
 * @param <T>
 */
public interface Monitorable<T> {
	/**
	 * 호출한 시점의 데이터 복사본을 돌려준다.
	 * 
	 * @return
	 */
	T createShapshot();
}
