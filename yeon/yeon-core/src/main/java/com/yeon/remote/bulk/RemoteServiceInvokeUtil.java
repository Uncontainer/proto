package com.yeon.remote.bulk;

import com.yeon.Disposable;
import com.yeon.YeonContext;
import com.yeon.remote.RemoteServiceRequest;
import com.yeon.remote.RemoteServiceResponse;
import com.yeon.remote.server.RemoteServiceInvoker;
import com.yeon.server.Server;
import com.yeon.server.ServerContext;
import com.yeon.util.LocalAddressHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RemoteServiceInvokeUtil {
	private static final Logger log = LoggerFactory.getLogger(RemoteServiceInvokeUtil.class);

	private static final ExecutorService executorServie;

	static String solutionName;
	static String projectName;
	static ServerContext serverContext;

	static {
		solutionName = YeonContext.getSolutionName();
		projectName = YeonContext.getProjectName();
		serverContext = YeonContext.getServerContext();

		executorServie = Executors.newFixedThreadPool(256);

		YeonContext.getYeonContext().addDisposable(new Disposable() {
			@Override
			public void dispose() throws Exception {
				executorServie.shutdownNow();
			}
		});
	}

	public static RemoteServiceResponseList sendRequest(List<Server> servers, RemoteServiceRequest request, int connectionTimeout, int socketTimeout, boolean skipLocalhost) {
		if (request == null) {
			throw new IllegalArgumentException();
		}

		List<RemoteServiceResponse> responses = new ArrayList<RemoteServiceResponse>();

		CountDownLatch latch = new CountDownLatch(servers.size());
		List<Sender> senders = new ArrayList<Sender>(servers.size());

		for (final Server server : servers) {
			if (skipLocalhost && LocalAddressHolder.getLocalAddress().equals(server.getIp()) && solutionName.equals(server.getSolutionName()) && projectName.equals(server.getProjectName())) {
				log.debug("[PIC] Same server project skiped: {}:{}:{}", new Object[] { server.getIp(), server.getSolutionName(), server.getProjectName() });
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

	public static RemoteServiceResponse invoke(RemoteServiceRequest request, Server server, int connectionTimeout, int socketTimeout) throws IOException {
		RemoteServiceInvoker remoteServiceInvoker = YeonContext.getRemoteContext().getRemoteServiceInvoker();
		return remoteServiceInvoker.invoke(request, server, connectionTimeout, socketTimeout);
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
				RemoteServiceResponse response = new RemoteServiceResponse();
				response.setRequestkey(request.getRequestkey());
				response.setThrowable(TIMEOUT_EXCEPTION);

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
					RemoteServiceResponse response = new RemoteServiceResponse();
					response.setRequestkey(request.getRequestkey());
					response.setThrowable(t);
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
