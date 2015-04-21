package com.yeon.mom.service;

public enum ServiceWorkerStatus {
	INIT_WAIT(false, false), // 객체가 생성되고 초기화 대기
	INITIALIZING(false, false), // Worker 초기화 수행 중
	INIT_FAIL(true, true), // Worker 초기화 실패
	MSG_WAIT(false, false), // 실행할 메시지 대시
	PROCESSING(false, false), // 메시지 처리
	STOP_WAIT(true, false), // 중지 대기
	STOP(true, true), // 중지
	UNEXPECTED_STOP(true, true); // 예상치 못한 이유로 중지

	/**
	 * 중지된 상태인지의 여부
	 */
	final boolean close;

	/**
	 * 객체를 관리하지 않아도 되는지의 여부
	 */
	final boolean garbage;

	ServiceWorkerStatus(boolean close, boolean garbage) {
		this.close = close;
		this.garbage = garbage;
	}

	public boolean isClose() {
		return close;
	}

	public boolean isGarbage() {
		return garbage;
	}
}
