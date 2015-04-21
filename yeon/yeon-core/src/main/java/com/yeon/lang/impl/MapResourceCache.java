package com.yeon.lang.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yeon.lang.Resource;
import com.yeon.lang.ResourceGetCondition;
import com.yeon.lang.ResourceService;
import com.yeon.lang.listener.ListenTarget;
import com.yeon.lang.listener.ResourceListenerUtils;
import com.yeon.lang.query.ResultSet;
import com.yeon.remote.RemoteContext;
import com.yeon.server.ProjectId;
import com.yeon.util.MapModel;

public class MapResourceCache implements ResourceService {
	private Logger log = LoggerFactory.getLogger(MapResourceCache.class);

	private ResourceService api;

	private Map<String, Resource> data = new ConcurrentHashMap<String, Resource>();

	public MapResourceCache(RemoteContext remoteContext) {
		api = remoteContext.newProjectProxy(ResourceService.class, NAME, ProjectId.INFRA_ADMIN, 60 * 1000);
	}

	@Override
	public void add(Resource resource) {
		api.add(resource);
	}

	@Override
	public void modify(Resource resource) {
		api.modify(resource);
	}

	@Override
	public void remove(String resourceId) {
		api.remove(resourceId);
	}

	@Override
	public MapResource get(String resourceId) {
		if (resourceId == null || resourceId.isEmpty()) {
			return null;
		}

		MapResource resource = (MapResource) data.get(resourceId);
		if (resource == null) {
			resource = (MapResource) api.get(resourceId);
			if (resource == null) {
				resource = MapResource.NULL;
			}

			data.put(resourceId, resource);
		}

		return resource != MapResource.NULL ? resource : null;
	}

	@Override
	public ResultSet select(String query) {
		// TODO query를 parsing하여 cache의 resource를 먼저 검색한 후 api를 호출하는 쿼리로 변경.
		// 또는 관련 resource의 Id만 가져오는 방식으로 변경...
		return api.select(query);
	}

	@Override
	public String getId(ResourceGetCondition condition) {
		return api.getId(condition);
	}

	public void refresh(MapModel option) {
		MapResourceRefreshOption cacheRefreshOption = option.convert(MapResourceRefreshOption.class);
		ListenTarget listenTarget = cacheRefreshOption.getListenTarget();

		MapResource resource = (MapResource) data.get(listenTarget.getValue());
		if (resource == null) {
			return;
		}

		MapResource currentResource = new MapResource(resource);

		MapResource nextResource = get(listenTarget.getValue());
		// TODO [LOW] 빈 내용의 ticket이 왔을 경우 ticket이 없는 것인지, 값이 없는 것인지 확인 필요.
		if (nextResource == null) {
			// 삭제되었을 경우에는 내부 properties를 null로 설정하여 property를 가져올 때 예외가 발생하도록 함.
			resource.toNull();
			return;
		}

		try {
			MapResource temp = new MapResource(nextResource);
			ResourceListenerUtils.validate(listenTarget, temp, currentResource);
		} catch (Exception e) {
			log.error("[YEON] Error loading ticket: " + nextResource, e);
			return;
		}

		Resource previousTicket = currentResource;
		ResourceListenerUtils.validated(listenTarget, nextResource, previousTicket);

		resource.replaceWith(nextResource);

		ResourceListenerUtils.changed(listenTarget, resource, previousTicket);
	}

	public void validate(MapModel option) throws Exception {
		MapResourceRefreshOption cacheRefreshOption = option.convert(MapResourceRefreshOption.class);
		ListenTarget listenTarget = cacheRefreshOption.getListenTarget();

		Resource resource = data.get(listenTarget.getValue());

		ResourceListenerUtils.validate(listenTarget, cacheRefreshOption.getResource(), resource);
	}
}
