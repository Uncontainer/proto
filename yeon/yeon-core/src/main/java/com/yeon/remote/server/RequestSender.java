package com.yeon.remote.server;

import com.yeon.remote.RemoteServiceRequest;
import com.yeon.remote.server.io.YeonIoUtils;
import com.yeon.server.Server;
import com.yeon.util.LocalAddressHolder;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

public class RequestSender {
	private static final AtomicLong keyGenerator = new AtomicLong();

	private RequestPool requestPool;
	private Server server;

	public RequestSender(RequestPool requestPool, Server server) {
		this.requestPool = requestPool;
		this.server = server;
	}

	public ResponseFuture invoke(RemoteServiceRequest request, int connectionTimeout, int socketTimeout) throws IOException {
		Socket socket = new Socket();
		InetSocketAddress endpoint = new InetSocketAddress(server.getIp(), server.getPort());
		socket.setSoTimeout(socketTimeout);
		socket.connect(endpoint, connectionTimeout);

		ObjectOutputStream oos = null;
		try {
			// TODO request에 아래 정보 셋팅 코드 추가.
			long key = keyGenerator.incrementAndGet();
			String responseServerIp = LocalAddressHolder.getLocalAddress();
			int responseServerPort = 0;
			request.setRequestkey(key);

			oos = new ObjectOutputStream(socket.getOutputStream());
			YeonIoUtils.writeAndFlush(oos, request);

			ResponseFuture future = new ResponseFuture(key, request);

			requestPool.addRequest(key, future);

			return future;
		} finally {
			if (oos != null) {
				oos.close();
			}
			socket.close();
		}
	}
}
