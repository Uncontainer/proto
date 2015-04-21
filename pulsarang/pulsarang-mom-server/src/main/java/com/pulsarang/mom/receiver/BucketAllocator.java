package com.pulsarang.mom.receiver;

import org.springframework.stereotype.Component;

import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.server.Server;

@Component
public class BucketAllocator {
	public Server allocateServer(int bucketNo) {
		// TODO 서버간의 통신을 통해 해당 bucket를 받는 로직 추가.
		// throw new UnsupportedOperationException();
		return InfraContextFactory.getInfraContext().getServerContext().getLocalServer();
	}

	public void checkSync(int bucketNo) {
		// TODO 서버의 bucket 정보 동기화 재확인.
		// throw new UnsupportedOperationException();
	}
}
