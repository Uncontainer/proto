package com.yeon.monitor.collector;

/**
 * 속성 이름과 병합 연산의 쌍으로 병합 가능한 속성을 정의한다.
 * 
 * @author pulsarang
 * @param <T>
 */
public interface MergableProperty<T> {
	/**
	 * 속성 이름
	 * 
	 * @return
	 */
	String getPropertyName();

	Mergable<T> getMergable();
}
