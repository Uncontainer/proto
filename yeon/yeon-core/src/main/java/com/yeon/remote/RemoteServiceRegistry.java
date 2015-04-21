package com.yeon.remote;

import com.yeon.remote.annotation.RemoteService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RemoteServiceRegistry {
	private final Map<String, RemoteServiceExporter> remoteServiceExporterMap;

	public RemoteServiceRegistry() {
		remoteServiceExporterMap = new HashMap<String, RemoteServiceExporter>();
	}

	public Set<String> getRegisteredNameSet() {
		return remoteServiceExporterMap.keySet();
	}

	/**
	 * 주어진 이름의 등록된 {@link RemoteService}을 돌려준다.
	 * 
	 * @param name
	 *            {@link RemoteService} 이름
	 * @return 등록된 {@link RemoteService}, 등록되지 않았을 경우 null
	 */
	public RemoteServiceExporter getRemoteServiceExporter(String name) {
		RemoteServiceExporter remoteServiceExporter = remoteServiceExporterMap.get(name);
		if (remoteServiceExporter == null) {
			return null;
		}

		return remoteServiceExporter;
	}

	public RemoteServiceExporter setRemoteService(Object serviceObject) {
		RemoteServiceExporter remoteServiceExporter = new RemoteServiceExporter(serviceObject);
		remoteServiceExporterMap.put(remoteServiceExporter.getName(), remoteServiceExporter);

		return remoteServiceExporter;
	}

	public RemoteServiceExporter setRemoteService(String name, Object serviceObject) {
		RemoteServiceExporter remoteServiceExporter = new RemoteServiceExporter(name, serviceObject);
		remoteServiceExporterMap.put(remoteServiceExporter.getName(), remoteServiceExporter);

		return remoteServiceExporter;
	}

	/**
	 * 주어진 이름으로 등록된 {@link RemoteService}을 삭제한다.
	 * 
	 * @param name
	 *            {@link RemoteService}의 이름
	 */
	public void removeRemoteService(String name) {
		remoteServiceExporterMap.remove(name);
	}

	public Date getLastRequestReceiveTime(String name) {
		RemoteServiceExporter remoteServiceExporter = remoteServiceExporterMap.get(name);
		if (remoteServiceExporter == null) {
			return null;
		}

		return remoteServiceExporter.getLastRequestedDate();
	}

	public boolean isRegistered(String name) {
		return remoteServiceExporterMap.containsKey(name);
	}
}
