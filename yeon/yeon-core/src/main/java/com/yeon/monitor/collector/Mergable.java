package com.yeon.monitor.collector;

/**
 * 동일한 속성에 대한 서로 다른 값을 병합하기 위한 명세를 정의한다. ex) 합계 속성일 경우 두 값의 합, 최근 업데이트 날짜일 경우 최신일.
 * 
 * @author pulsarang
 * @param <T>
 */
public interface Mergable<T> {
	/**
	 * taget의 값을 source에 병합한 값을 돌려준다. 반환 값은 다음 병합의 source로 들어간다.
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	T merge(T source, T target);

	Mergable<Integer> INT_SUMMARIZER = new Mergable<Integer>() {
		@Override
		public Integer merge(Integer source, Integer target) {
			return source.intValue() + target.intValue();
		}
	};
}
