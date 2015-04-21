package com.yeon.mom.service;

public enum ServiceRunStatus {
	INIT_WAIT(false, false), // 서비스 객체가 생성되고 초기화 대기
	INITIALIZING(false, false), // 서비스 초기화를 수행 중
	INIT_FAIL(true, true), // 서비스 초기화 실패
	NORMAL(false, false), // 정상 상태
	PAUSE(true, false), // 서비스 일시 정지
	STOP_WAIT(true, false), // 서비스 중지 대기
	STOP(true, true), // 서비스 중지 완료
	UNEXPECTED_STOP(true, true); // 예상치 못한 이유로 서비스 중지

	/**
	 * 중지된 상태인지의 여부
	 */
	final boolean close;

	/**
	 * 객체를 관리하지 않아도 되는 상태인지의 여부
	 */
	final boolean garbage;

	ServiceRunStatus(boolean closeStatus, boolean garbageStatus) {
		this.close = closeStatus;
		this.garbage = garbageStatus;
	}

	public boolean isClose() {
		return close;
	}

	public boolean isGarbage() {
		return garbage;
	}
}
