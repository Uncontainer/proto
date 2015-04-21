package com.yeon.remote;

import com.yeon.Disposable;
import com.yeon.remote.annotation.RemoteService;
import com.yeon.remote.server.RemoteServiceInvoker;
import com.yeon.remote.server.RequestListenerServer;
import com.yeon.remote.server.RequestPool;
import com.yeon.remote.server.ResponseListenerServer;
import com.yeon.server.ProjectId;
import com.yeon.server.Server;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author pulsarang
 */
public class RemoteContext implements Disposable {
	private RequestPool requestPool;
	private RemoteServiceInvoker remoteServiceInvoker;
	private RequestListenerServer requestListenerServer;
	private ResponseListenerServer responseListenerServer;

	private RemoteServiceRegistry remoteServiceRegistry;
	private ProjectId projectId;

	public RemoteContext(ProjectId projectId) {
		this.projectId = projectId;
		init();
	}

	private void init() {
		remoteServiceRegistry = new RemoteServiceRegistry();
		if (services != null) {
			for (Object service : services) {
				setRemoteService(service);
			}
		}
		if (namedServiceMap != null) {
			for (Entry<String, Object> namedService : namedServiceMap.entrySet()) {
				setRemoteService(namedService.getKey(), namedService.getValue());
			}
		}

		requestPool = new RequestPool();
		remoteServiceInvoker = new RemoteServiceInvoker(requestPool);

		try {
			requestListenerServer = new RequestListenerServer(22102, remoteServiceRegistry);
			responseListenerServer = new ResponseListenerServer(22103, requestPool);
		} catch (IOException e) {
			// TODO 리소스 정리 코드 추가.
			throw new RuntimeException(e);
		}

		setRemoteService(new RemoteServiceMonitor(remoteServiceRegistry));

		// TODO 리소스 해제 기능 추가.
	}

	@Override
	public void dispose() {
		requestListenerServer.close();
		// TODO request 처리 상황 확인 이후 close
		responseListenerServer.close();
	}

	public int getRequestListenPort() {
		return requestListenerServer.getPort();
	}

	public RemoteServiceInvoker getRemoteServiceInvoker() {
		return remoteServiceInvoker;
	}

	public Set<String> getRegisteredNameSet() {
		return remoteServiceRegistry.getRegisteredNameSet();
	}

	public RemoteServiceRegistry getRemoteServiceRegistry() {
		return remoteServiceRegistry;
	}

	public RemoteServiceExporter setRemoteService(Object serviceObject) {
		return remoteServiceRegistry.setRemoteService(serviceObject);
	}

	public RemoteServiceExporter setRemoteService(String name, Object serviceObject) {
		return remoteServiceRegistry.setRemoteService(name, serviceObject);
	}

	/**
	 * 주어진 이름의 등록된 {@link RemoteService}을 돌려준다.
	 * 
	 * @param name
	 *            {@link RemoteService} 이름
	 * @return 등록된 {@link RemoteService}, 등록되지 않았을 경우 null
	 */
	public RemoteServiceExporter getRemoteService(String name) {
		return remoteServiceRegistry.getRemoteServiceExporter(name);
	}

	/**
	 * 주어진 이름으로 등록된 {@link RemoteService}을 삭제한다.
	 * 
	 * @param nam
	 *            {@link RemoteService}의 이름
	 */
	public void removeRemoteService(String name) {
		remoteServiceRegistry.removeRemoteService(name);
	}

	public boolean isRegistered(String name) {
		return remoteServiceRegistry.isRegistered(name);
	}

	public <T> T newSolutionProxy(Class<T> clazz, String objectName, String solutionName, int socketTimeout) {
		RemoteServiceExporter remoteService = remoteServiceRegistry.getRemoteServiceExporter(objectName);
		if (remoteService != null && projectId.getSolutionName().equals(solutionName)) {
			return (T) remoteService.getServiceObject();
		}

		return RemoteServiceProxy.newProxy(clazz, objectName, new RemoteServiceProxy.SolutionServerSelector(solutionName), socketTimeout);
	}

	public <T> T newProjectProxy(Class<T> clazz, String objectName, ProjectId projectId, int socketTimeout) {
		RemoteServiceExporter remoteService = remoteServiceRegistry.getRemoteServiceExporter(objectName);
		if (remoteService != null && this.projectId.equals(projectId)) {
			return (T) remoteService.getServiceObject();
		}

		return RemoteServiceProxy.newProxy(clazz, objectName, new RemoteServiceProxy.ProjectServerSelector(projectId.getSolutionName(), projectId.getProjectName()), socketTimeout);
	}

	public <T> T newServerProxy(Class<T> clazz, String objectName, Server server, int socketTimeout) {
		RemoteServiceExporter remoteService = remoteServiceRegistry.getRemoteServiceExporter(objectName);
		if (remoteService != null && server.isLocal()) {
			return (T) remoteService.getServiceObject();
		}

		return RemoteServiceProxy.newProxy(clazz, objectName, new RemoteServiceProxy.SingleServerSelector(server), socketTimeout);
	}

	private static Map<String, Object> namedServiceMap;
	private static List<Object> services;

	public synchronized static void prepareRemoteService(String name, Object serviceObject) {
		if (name == null) {
			if (services == null) {
				services = new ArrayList<Object>();
			}
			services.add(serviceObject);
		} else {
			if (namedServiceMap == null) {
				namedServiceMap = new HashMap<String, Object>();
			}
			namedServiceMap.put(name, serviceObject);
		}
	}
}
