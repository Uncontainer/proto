package com.yeon.lang;

import com.yeon.lang.impl.InteractiveResourceService;
import com.yeon.lang.impl.MapResourceCache;
import com.yeon.lang.impl.RemoteResourceServiceReactor;
import com.yeon.lang.listener.ListenTarget;
import com.yeon.lang.listener.ResourceListener;
import com.yeon.lang.listener.ResourceListenerRegistry;
import com.yeon.remote.RemoteContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ResourceContext {
	private final Logger log = LoggerFactory.getLogger(ResourceContext.class);

	private final ResourceService resourceService;
	private final ResourceListenerRegistry resourceListenerRegistry = new ResourceListenerRegistry();

	public ResourceContext(RemoteContext remoteContext) {
		resourceService = new InteractiveResourceService(new MapResourceCache(remoteContext));
		remoteContext.setRemoteService(new RemoteResourceServiceReactor());

		// TODO resourceService를 remoteService로 등록하여 변경을 수신할 수 있도록 수정.
	}

	public ResourceService getResourceService() {
		return resourceService;
	}

	public ResourceListenerRegistry getResourceListenerRegistry() {
		return resourceListenerRegistry;
	}

	public void addListener(ResourceListener listener) {
		for (ListenTarget target : listener.getTargets()) {
			if (target.getValue() == null) {
				log.warn("Target resourceId not found.\nListener registration has delayed.({} - {})", target.getScope(), target.getResourceIdentifiable());
				// TODO Scheduler로 등록하여 주기적으로 resourceId를 가져와 listener로 등록하는 코드 추가.
			} else {
				resourceListenerRegistry.addListener(target, listener);
			}
		}
	}

	public List<ResourceListener> getListeners(ListenTarget target) {
		return resourceListenerRegistry.getListeners(target);
	}

	public boolean removeListener(ListenTarget target, ResourceListener listener) {
		return resourceListenerRegistry.removeListener(target, listener);
	}
}
