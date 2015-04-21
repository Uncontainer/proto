package com.yeon.infra.server;

import com.yeon.remote.annotation.RemoteMethod;
import com.yeon.remote.annotation.RemoteService;
import com.yeon.server.Project;
import com.yeon.server.Server;
import com.yeon.server.ServerApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RemoteService(name = ServerApiService.NAME)
public class ServerApiRemoteService implements ServerApiService {
	@Autowired
	private ServerService serverService;

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

	@Override
	@RemoteMethod
	public List<Server> getServersByIp(String ip) {
		return serverService.getServersByIp(ip);
	}
}
