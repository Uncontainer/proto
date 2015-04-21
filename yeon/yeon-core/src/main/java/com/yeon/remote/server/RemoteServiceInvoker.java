package com.yeon.remote.server;

import com.yeon.remote.RemoteServiceRequest;
import com.yeon.remote.RemoteServiceResponse;
import com.yeon.server.Server;

import java.io.IOException;

public class RemoteServiceInvoker {
	private RequestPool requestPool;

	public RemoteServiceInvoker(RequestPool requestPool) {
		super();
		this.requestPool = requestPool;
	}

	public RemoteServiceResponse invoke(RemoteServiceRequest request, Server server, int connectionTimeout, int socketTimeout) throws IOException {
		ResponseFuture future = invokeAsync(request, server, connectionTimeout, socketTimeout);

		try {
			return future.get();
		} catch (InterruptedException e) {
			// TODO 적절한 예외 생성하여 발생
			throw new RuntimeException(e);
		}
	}

	public ResponseFuture invokeAsync(RemoteServiceRequest request, Server server, int connectionTimeout, int socketTimeout) throws IOException {
		RequestSender sender = new RequestSender(requestPool, server);

		// TODO sender pooling 코드 추가.

		return sender.invoke(request, connectionTimeout, socketTimeout);
	}
}
