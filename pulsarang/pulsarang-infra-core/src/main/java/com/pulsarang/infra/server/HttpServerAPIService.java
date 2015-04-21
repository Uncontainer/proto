package com.pulsarang.infra.server;

import java.util.List;

import com.pulsarang.infra.util.InfraAdminApiInvoker;

public class HttpServerAPIService implements ServerAPIService {
	@Override
	public List<Server> getServerInfos() {
		return InfraAdminApiInvoker.getInstance().list("/api/config/server/list", 5000, Server.class);
	}

	@Override
	public void updateServer(Server server, boolean startup) {
		String path = "/api/config/server/inform";
		if (startup) {
			path += "/startup";
		} else {
			path += "/shutdown";
		}

		InfraAdminApiInvoker.getInstance().invoke(path, server, 3000);
	}

	@Override
	public Project getProjectInfo(String solutionName, String projectName) {
		// TODO Auto-generated method stub
		return null;
	}
}
