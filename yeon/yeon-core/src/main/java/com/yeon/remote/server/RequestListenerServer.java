package com.yeon.remote.server;

import com.yeon.remote.RemoteServiceRegistry;

import java.io.IOException;
import java.net.Socket;

public class RequestListenerServer extends AbstractListenerServer {
	private ResponseWriter responseWriter = new ResponseWriter();
	private RemoteServiceRegistry remoteServiceRegistry;

	public RequestListenerServer(int expectedPort, RemoteServiceRegistry remoteServiceRegistry) throws IOException {
		super(expectedPort);

		this.remoteServiceRegistry = remoteServiceRegistry;
	}

	@Override
	protected Runnable createListener(Socket socket) throws IOException {
		return new RequestListener(socket, remoteServiceRegistry, responseWriter);
	}
}
