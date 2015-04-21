package com.pulsarang.infra.remote;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.core.util.LocalAddressHolder;
import com.pulsarang.infra.Disposable;
import com.pulsarang.infra.InfraContext;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.remote.server.JsonSocketStream;
import com.pulsarang.infra.server.Server;
import com.pulsarang.infra.server.ServerContext;

public class RemoteServiceInvokeUtil {
	private static final Logger log = LoggerFactory.getLogger(RemoteServiceInvokeUtil.class);

	private static final ExecutorService executorServie;

	static String solutionName;
	static String projectName;
	static ServerContext serverContext;

	static {
		InfraContext infraContext = InfraContextFactory.getInfraContext();
		solutionName = infraContext.getSolutionName();
		projectName = infraContext.getProjectName();
		serverContext = infraContext.getServerContext();

		executorServie = Executors.newFixedThreadPool(256);

		infraContext.addDisposable(new Disposable() {
			@Override
			public void dispose() throws Exception {
				executorServie.shutdownNow();
			}
		});
	}

	public static RemoteServiceResponseList sendRequest(List<Server> servers, RemoteServiceRequest request, int connectionTimeout, int socketTimeout,
			boolean skipLocalhost) {
		if (request == null) {
			throw new IllegalArgumentException();
		}

		List<RemoteServiceResponse> responses = new ArrayList<RemoteServiceResponse>();

		CountDownLatch latch = new CountDownLatch(servers.size());
		List<Sender> senders = new ArrayList<Sender>(servers.size());

		for (final Server server : servers) {
			if (skipLocalhost && LocalAddressHolder.getLocalAddress().equals(server.getIp()) && solutionName.equals(server.getSolutionName())
					&& projectName.equals(server.getProjectName())) {
				log.debug("[PIC] Same server project skiped: {}:{}:{}",
						new Object[] { server.getIp(), server.getSolutionName(), server.getProjectName() });
				latch.countDown();
				continue;
			}

			Sender sender = new Sender(server, request, connectionTimeout, socketTimeout, latch);
			senders.add(sender);
			executorServie.execute(sender);
		}

		try {
			latch.await(socketTimeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}

		for (Sender sender : senders) {
			responses.add(sender.getResponse());
		}

		return new RemoteServiceResponseList(responses);
	}

	public static RemoteServiceResponse invoke(RemoteServiceRequest request, Server server, int connectionTimeout, int socketTimeout)
			throws IOException {
		Socket socket = new Socket();
		InetSocketAddress endpoint = new InetSocketAddress(server.getIp(), server.getPort());
		socket.connect(endpoint, connectionTimeout);
		socket.setSoTimeout(socketTimeout);

		JsonSocketStream stream = new JsonSocketStream(socket);

		try {
			RemoteServiceResponse response = stream.writeRequestAndGetResponse(request);
			response.init(request, server);

			return response;
		} finally {
			stream.close();
		}
	}

	static class Sender implements Runnable {
		static final Exception TIMEOUT_EXCEPTION = new Exception("timeout");

		private final Server server;
		private final RemoteServiceRequest request;
		private final int connectionTimeout;
		private final int socketTimeout;
		private final CountDownLatch latch;
		private volatile RemoteServiceResponse response;

		private Sender(Server server, RemoteServiceRequest request, int connectionTimeout, int socketTimeout, CountDownLatch latch) {
			super();
			this.server = server;
			this.request = request;
			this.connectionTimeout = connectionTimeout;
			this.socketTimeout = socketTimeout;
			this.latch = latch;
		}

		public RemoteServiceResponse getResponse() {
			if (response == null) {
				RemoteServiceResponse response = new RemoteServiceResponse(request, server);
				response.setFailResult(TIMEOUT_EXCEPTION);

				this.response = response;
			}

			return response;
		}

		@Override
		public void run() {
			try {
				try {
					this.response = invoke(request, server, connectionTimeout, socketTimeout);
				} catch (Throwable t) {
					if (t instanceof InvocationTargetException) {
						t = ((InvocationTargetException) t).getTargetException();
					}

					log.error("[PIC] Fail to send rpc requst.", t);
					RemoteServiceResponse response = new RemoteServiceResponse(request, server);
					response.setFailResult(t);
					this.response = response;
				}
			} catch (Throwable t) {
				log.error("[PIC] Fail to create response.", t);
			} finally {
				latch.countDown();
			}
		}
	}
}
