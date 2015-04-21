package com.pulsarang.infra.server;

import java.util.List;

public interface ServerAPIService {
	String NAME = "_SERVER_API";

	Project getProjectInfo(String solutionName, String projectName);

	List<Server> getServerInfos();

	void updateServer(Server server, boolean startup);
}
