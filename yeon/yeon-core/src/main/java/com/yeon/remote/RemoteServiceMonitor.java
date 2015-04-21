package com.yeon.remote;

import com.yeon.remote.annotation.RemoteService;
import com.yeon.remote.reload.AbstractReloadService;
import com.yeon.remote.reload.ReloadService;
import com.yeon.util.MapModel;

import java.util.*;

/**
 * @author pulsarang
 * 
 */
@RemoteService(name = RemoteServiceMonitor.NAME)
public class RemoteServiceMonitor extends AbstractReloadService {
	public static final String NAME = "__REMOTE_SERVICE_MONITOR";

	private final RemoteServiceRegistry remoteServiceRegistry;

	RemoteServiceMonitor(RemoteServiceRegistry remoteServiceRegistry) {
		this.remoteServiceRegistry = remoteServiceRegistry;
	}

	@Override
	public List<MapModel> list(MapModel option) {
		Set<String> nameSet = new TreeSet<String>(remoteServiceRegistry.getRegisteredNameSet());
		List<MapModel> reloadInfos = new ArrayList<MapModel>();
		for (String name : nameSet) {
			MapModel item = creteInfoItem(name);
			reloadInfos.add(item);
		}

		return reloadInfos;
	}

	@Override
	public int listCount(MapModel option) {
		return remoteServiceRegistry.getRegisteredNameSet().size();
	}

	@Override
	public MapModel get(MapModel option) {
		String taretName = option.getString(RemoteParameters.OBJECT_NAME);
		if (taretName == null) {
			throw new IllegalArgumentException("'" + RemoteParameters.OBJECT_NAME + "' is required.");
		}

		return creteInfoItem(taretName);
	}

	private MapModel creteInfoItem(String targetName) {
		Entry entry = new Entry();
		entry.setName(targetName);
		RemoteServiceExporter exporterInfo = remoteServiceRegistry.getRemoteServiceExporter(targetName);
		if (exporterInfo != null) {
			entry.setLastRequestedDate(exporterInfo.getLastRequestedDate());
			entry.setReloadable(exporterInfo.getServiceObject() instanceof ReloadService);
		}

		return entry;
	}

	public static class Entry extends MapModel {
		public static final String LAST_REQUESTED_DATE = "last_requested_date";
		public static final String RELOADABLE = "reloadable";
		public static final String NAME = "name";

		public String getName() {
			return getString(NAME);
		}

		public void setName(String name) {
			setString(NAME, name);
		}

		public boolean isReloadable() {
			return getBoolean(RELOADABLE, false);
		}

		public void setReloadable(boolean reloadable) {
			setBoolean(RELOADABLE, reloadable);
		}

		public Date getLastRequestedDate() {
			return getDate(LAST_REQUESTED_DATE);
		}

		public void setLastRequestedDate(Date lastRequestedDate) {
			setDate(LAST_REQUESTED_DATE, lastRequestedDate);
		}
	}
}
