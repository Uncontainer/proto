package com.yeon;

import com.yeon.infra.server.ServerApiRemoteService;
import com.yeon.lang.remote.RemoteMapResourceService;
import com.yeon.remote.RemoteContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class RemoteServicePreparer implements InitializingBean {
	@Autowired
	private ServerApiRemoteService serverApiRemoteService;

	@Autowired
	private RemoteMapResourceService resourceService;

	@Override
	public void afterPropertiesSet() throws Exception {
		RemoteContext.prepareRemoteService(null, serverApiRemoteService);
		RemoteContext.prepareRemoteService(null, resourceService);
	}
}
