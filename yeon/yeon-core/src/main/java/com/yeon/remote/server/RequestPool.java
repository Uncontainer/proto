package com.yeon.remote.server;

import com.yeon.remote.RemoteServiceResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestPool {
	// TODO 적절한 초기크기 설정
	// TODO 오래된 request들을 주기적으로 검사하여 timeout 발생시키는 코드 추가.
	private final Map<Long, ResponseFuture> futureMap = new ConcurrentHashMap<Long, ResponseFuture>();

	public void responsed(RemoteServiceResponse response) {
		ResponseFuture future = futureMap.remove(response.getRequestkey());
		if (future == null) {
			// TODO 로깅 추가
			return;
		}

		future.setResponse(response);
	}

	public void addRequest(long key, ResponseFuture future) {
		ResponseFuture previousFuture = futureMap.put(key, future);

		if (previousFuture != null) {
			futureMap.put(key, previousFuture);

			throw new IllegalArgumentException("Duplicate request key.(" + key + ")");
		}
	}
}
