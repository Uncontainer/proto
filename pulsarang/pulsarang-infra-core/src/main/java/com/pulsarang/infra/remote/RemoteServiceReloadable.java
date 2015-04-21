package com.pulsarang.infra.remote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.remote.annotation.RemoteService;
import com.pulsarang.infra.remote.reload.AbstractReloadService;
import com.pulsarang.infra.remote.reload.ReloadService;

/**
 * TODO naming 변경. RemoteConext에 병합...
 * 
 * @author pulsarang
 * 
 */
@RemoteService(name = RemoteServiceReloadable.NAME)
public class RemoteServiceReloadable extends AbstractReloadService {
	public static final String NAME = "__reload_info";

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

	private final RemoteContext rpcContext;

	public static final String LAST_REQUESTED_DATE = "last_requested_date";

	public static final String RELOADABLE = "reloadable";

	RemoteServiceReloadable(RemoteContext rpcContext) {
		this.rpcContext = rpcContext;
	}

	@Override
	public List<MapModel> list(MapModel option) {
		Set<String> nameSet = new TreeSet<String>(rpcContext.getRegisteredNameSet());
		List<MapModel> reloadInfos = new ArrayList<MapModel>();
		for (String name : nameSet) {
			MapModel item = creteEventInfoItem(name);
			reloadInfos.add(item);
		}

		return reloadInfos;
	}

	@Override
	public int listCount(MapModel option) {
		return rpcContext.getRegisteredNameSet().size();
	}

	@Override
	public MapModel get(MapModel option) {
		String taretName = option.getString(RemoteServiceRequest.TARGET_NAME);
		if (taretName == null) {
			throw new IllegalArgumentException("'" + RemoteServiceRequest.TARGET_NAME + "' is required.");
		}

		return creteEventInfoItem(taretName);
	}

	private MapModel creteEventInfoItem(String targetName) {
		MapModel item = new MapModel();
		item.setString(RemoteServiceRequest.TARGET_NAME, targetName);
		RemoteServiceExporter listenerInfo = rpcContext.getRemoteServiceExporter(targetName);
		if (listenerInfo != null) {
			item.setString(LAST_REQUESTED_DATE, dateFormat.format(listenerInfo.getLastRequestedDate()));
			boolean reloadable = (listenerInfo.getTargetObject() instanceof ReloadService);
			item.setBoolean(RELOADABLE, reloadable);
		}

		return item;
	}
}
