package com.yeon.remote.server;

import java.io.IOException;
import java.net.Socket;

public class ResponseListenerServer extends AbstractListenerServer {
	private RequestPool requestPool;

	public ResponseListenerServer(int expectedPort, RequestPool requestPool) throws IOException {
		super(expectedPort);
		this.requestPool = requestPool;
	}

	@Override
	protected Runnable createListener(Socket socket) throws IOException {
		return new ResponseListener(socket, requestPool);
	}
}
