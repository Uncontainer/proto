package com.pulsarang.mom.store.proc;

public enum ConsumerResponseCode {
	SUCCESS(200), // 성공적으로 처리.
	ACCEPTED(202), // 처리에는 실패하였으나 재처리는 수행하지 않음.
	BAD_REQUEST(400), // Event에 필수 정보가 포함되어 있지 않음.
	FORBIDDEN(403), // ACL에 없는 Dispatcher로부터 Event가 수신되었을 경우.
	NOT_ACCEPTABLE(406), // 선처리 되어야 하는 event를 수신하지 못하였을 경우.
	CONFLICT(409), // 이미 처리된 Event.
	INTERNAL_SERVER_ERROR(500), // 원인을 알지 못하는 Event 처리 실패.
	SERVICE_UNAVAILABLE(503), // 연동 시스템 오류 등으로 인해 event를 처리하지 못하였을 경우. Retry-After 옵션으로 재시도 여부를 알려줄 수 있다.
	;

	final int code;

	ConsumerResponseCode(int code) {
		this.code = code;
	}
}
