package com.pulsarang.infra.remote.server;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.remote.RemoteContext;
import com.pulsarang.infra.remote.RemoteServiceExporter;
import com.pulsarang.infra.remote.RemoteServiceRequest;
import com.pulsarang.infra.remote.RemoteServiceResponse;

public class RemoteServiceRequestExecutor implements Runnable {
	private final Logger log = LoggerFactory.getLogger(RemoteServiceRequestExecutor.class);

	private final JsonSocketStream stream;

	public RemoteServiceRequestExecutor(Socket socket) throws IOException {
		stream = new JsonSocketStream(socket);
	}

	@Override
	public void run() {
		RemoteServiceRequest request = new RemoteServiceRequest();
		// TODO local server로 셋팅
		// Server localServer = InfraContextFactory.getInfraContext().getServerContext().getLocalServer();
		RemoteServiceResponse response = new RemoteServiceResponse(request, null);
		try {
			request.setProperties(stream.readMap());
			request.setCallerIp(stream.getSocket().getInetAddress().getHostAddress());

			try {
				if (ACLController.getInstance().isDeniedIp(request.getCallerIp())) {
					throw new Exception("Not allowed ip: " + request.getCallerIp());
				}

				execute(request, response);
			} catch (Exception e) {
				log.warn("[PIC] Fail to process rpc request.(" + request + ")", e);
				response.setFailResult(e);
			}

			stream.writeMap(response);
		} catch (Exception e) {
			log.warn("[PIC] Fail to process rpc request.(" + request + ")", e);
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				log.warn("Fail to close socket.", e);
			}
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
		RemoteContext rpcContext = InfraContextFactory.getInfraContext().getRemoteContext();

		String targetName = request.getTarget();
		if (targetName == null) {
			throw new Exception(RemoteServiceRequest.TARGET_NAME + " is empty.");
		}
		if (!rpcContext.isRegistered(targetName)) {
			throw new Exception("Unregistered target '" + targetName + "'");
		}

		String opName = request.getMethodName();
		if (opName == null) {
			throw new Exception(RemoteServiceRequest.METHOD_NAME + " is empty.");
		}

		RemoteServiceExporter remoteService = rpcContext.getRemoteServiceExporter(request.getTarget());
		if (remoteService == null) {
			throw new Exception("There is no registered reloadable : " + request);
		}

		remoteService.execute(request, response);
	}
}