package com.yeon.lang.impl;

import com.yeon.lang.Resource;
import com.yeon.lang.listener.ListenTarget;
import com.yeon.util.MapModel;

public class MapResourceRefreshOption extends MapModel {
	ListenTarget listenTarget;
	Resource resource;

	public ListenTarget getListenTarget() {
		return listenTarget;
	}

	public void setListenTarget(ListenTarget listenTarget) {
		this.listenTarget = listenTarget;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
}
