package com.pulsarang.infra.remote;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.pulsarang.infra.remote.annotation.RemoteService;

/**
 * @author pulsarang
 */
public class RemoteContext {
	private final Map<String, RemoteServiceExporter> remoteServiceExporterMap;

	public RemoteContext() {
		remoteServiceExporterMap = new HashMap<String, RemoteServiceExporter>();
		setRemoteService(new RemoteServiceReloadable(this));
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
		if (serviceObject == null) {
			throw new IllegalArgumentException("RemoteService object is null");
		}

		RemoteServiceExporter remoteServiceExporter = new RemoteServiceExporter(serviceObject);

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
