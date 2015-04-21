package com.yeon.remote.server;

import com.yeon.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractListenerServer extends Thread implements Disposable {
	private final Logger log = LoggerFactory.getLogger(AbstractListenerServer.class);

	private ServerSocket serverSocket = null;
	private volatile boolean run = false;

	private final int port;

	private final ExecutorService executor;

	public AbstractListenerServer(int expectedPort) throws IOException {
		if (expectedPort <= 100 || expectedPort > 65535) {
			throw new IllegalArgumentException();
		}

		int max = expectedPort + 10;
		if (max > 65535) {
			max = 65535;
		}

		while (expectedPort <= max) {
			try {
				serverSocket = new ServerSocket(expectedPort);
				break;
			} catch (SocketException e) {
				log.info("[PIC] Port has already binded({}). Try another.({})", expectedPort, (expectedPort + 1));
			}
			expectedPort++;
		}

		if (expectedPort > max) {
			throw new IllegalArgumentException("Available rpc listen port has not found.");
		}

		this.port = expectedPort;
		this.run = true;
		this.executor = Executors.newFixedThreadPool(32);

		log.info("[PIC] Infra rpc listener server started. Listen at {}", expectedPort);
	}

	public int getPort() {
		return port;
	}

	@Override
	public void run() {
		if (serverSocket == null) {
			return;
		}

		while (run) {
			try {
				Socket clientSocket = serverSocket.accept();
				log.debug("[PIC] '{}' connected.", clientSocket.getInetAddress().getHostAddress());

				executor.execute(createListener(clientSocket));
			} catch (Throwable t) {
				if (run) {
					log.error("[PIC] Fail to listen rpc request.", t);
				}
			}
		}
	}

	public void close() {
        try{
            serverSocket.close();
        } catch (IOException e) {
        }
//		IOUtils.closeQuietly(serverSocket);
	}

	@Override
	public void dispose() {
		executor.shutdownNow();
		close();
	}

	protected abstract Runnable createListener(Socket socket) throws IOException;
}
