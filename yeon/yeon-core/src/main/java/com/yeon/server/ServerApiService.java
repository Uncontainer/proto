package com.yeon.server;

import java.util.List;

public interface ServerApiService {
	String NAME = "_SERVER_API";

	Project getProjectInfo(String solutionName, String projectName);

	List<Server> getServerInfos();

	void updateServer(Server server, boolean startup);

	List<Server> getServersByIp(String ip);
}
