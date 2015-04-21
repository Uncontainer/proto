package com.yeon.remote.server;

import com.yeon.remote.RemoteServiceResponse;
import com.yeon.remote.server.io.YeonIoUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ResponseListener implements Runnable {
	private Socket socket;
	private RequestPool requestPool;

	public ResponseListener(Socket socket, RequestPool requestPool) {
		this.socket = socket;
		this.requestPool = requestPool;
	}

	@Override
	public void run() {
		ObjectInputStream ois = null;
		try {
			try {
				ois = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				// TODO logging
				return;
			}

			while (true) {
				RemoteServiceResponse response;
				try {
					response = (RemoteServiceResponse) YeonIoUtils.read(ois);
				} catch (ClassNotFoundException e) {
					// TODO logging
					break;
				} catch (IOException e) {
					// TODO logging
					break;
				} catch (ClassCastException e) {
					// TODO logging
					break;
				}

				// TODO ACL 검사 추가.

				requestPool.responsed(response);
			}
		} finally {
			IOUtils.closeQuietly(ois);
			IOUtils.closeQuietly(socket);
		}
	}
}
