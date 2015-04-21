package com.pulsarang.infra.server;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pulsarang.infra.InfraContext;
import com.pulsarang.infra.remote.annotation.RemoteMethod;
import com.pulsarang.infra.remote.annotation.RemoteService;

@Component
@RemoteService(name = ServerAPIService.NAME)
public class ServerApiRemoteService implements ServerAPIService, InitializingBean {
	@Autowired
	private ServerService serverService;

	@Autowired
	private InfraContext infraContext;

	@Override
	public void afterPropertiesSet() throws Exception {
		infraContext.getRemoteContext().setRemoteService(this);
	}

	@Override
	@RemoteMethod
	public Project getProjectInfo(String solutionName, String projectName) {
		return serverService.getProjectInfo(solutionName, projectName);
	}

	@Override
	@RemoteMethod
	public List<Server> getServerInfos() {
		return serverService.getServerInfos();
	}

	@Override
	@RemoteMethod
	public void updateServer(Server server, boolean startup) {
		serverService.updateServer(server, startup);
	}
}
