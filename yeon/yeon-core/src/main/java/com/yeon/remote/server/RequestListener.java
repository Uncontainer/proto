package com.yeon.remote.server;

import com.yeon.remote.*;
import com.yeon.remote.server.io.YeonIoUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class RequestListener implements Runnable {
	private final Logger log = LoggerFactory.getLogger(RequestListener.class);

	private Socket socket;
	private ResponseWriter responseWriter;
	private RemoteServiceRegistry remoteServiceRegistry;

	public RequestListener(Socket socket, RemoteServiceRegistry remoteServiceRegistry, ResponseWriter responseWriter) throws IOException {
		this.socket = socket;
		this.remoteServiceRegistry = remoteServiceRegistry;
		this.responseWriter = responseWriter;
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

			RemoteServiceRequest request = null;
			while (true) {
				try {
					request = (RemoteServiceRequest) YeonIoUtils.read(ois);
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
				// TODO local server로 셋팅
				// Server localServer =
				// InfraContextFactory.getInfraContext().getServerContext().getLocalServer();
				RemoteServiceResponse response = new RemoteServiceResponse();
				// TODO orginal caller IP 표현 코드 추가.
				request.setCallerIp(socket.getInetAddress().getHostAddress());

				try {
					if (ACLController.getInstance().isDeniedIp(request.getCallerIp())) {
						throw new Exception("Not allowed ip: " + request.getCallerIp());
					}

					execute(request, response);
				} catch (ForwardException e) {
					// TODO 해당 서버에서 처리할 요청이 아닐 경우 다른 서버로 리다이렉트하는 로직 추가.
					// YeonContext.getRemoteContext().getRemoteServiceInvoker().invoke(request, server, connectionTimeout, socketTimeout);
				} catch (Exception e) {
					log.warn("[PIC] Fail to process rpc request.(" + request + ")", e);
					response.setThrowable(e);
				}

				responseWriter.writeResponse(response);
			}
		} finally {
			IOUtils.closeQuietly(ois);
			IOUtils.closeQuietly(socket);
		}

	}

	/**
	 * paramMap의 {@value RemoteServiceRequest#METHOD_NAME}에 명시된 operation을 수행한다.
	 * 
	 * @param request
	 *            <ul>
	 *            <li>{@value RemoteServiceRequest#METHOD_NAME} : 수행할 작업의 이름</li>
	 *            <li>{@value RemoteServiceRequest#CALLER_IP} : ACL에 없는 IP일 경우 에러를 리턴한다.</li>
	 *            </ul>
	 * @return
	 * @throws Exception
	 */
	private void execute(RemoteServiceRequest request, RemoteServiceResponse response) throws Exception {
		String objectName = request.getObjectName();
		if (objectName == null) {
			throw new Exception(RemoteParameters.OBJECT_NAME + " is empty.");
		}
		if (!remoteServiceRegistry.isRegistered(objectName)) {
			throw new Exception("Unregistered target '" + objectName + "'");
		}

		String opName = request.getMethodName();
		if (opName == null) {
			throw new Exception(RemoteParameters.OBJECT_NAME + " is empty.");
		}

		RemoteServiceExporter remoteService = remoteServiceRegistry.getRemoteServiceExporter(request.getObjectName());
		if (remoteService == null) {
			throw new Exception("There is no registered reloadable : " + request);
		}

		remoteService.execute(request, response);
	}
}