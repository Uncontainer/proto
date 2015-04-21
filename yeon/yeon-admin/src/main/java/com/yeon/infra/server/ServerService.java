package com.yeon.infra.server;

import com.yeon.server.Server;
import com.yeon.server.ServerApiService;

import java.util.List;

public interface ServerService extends ServerApiService {
	List<Server> listAll();

	Server getServer(String id);

	void addServer(Server server);

	void modifyServer(Server server);

	void removeServer(Server server);

	List<String> getSolutionNames();

	List<String> getProjectNames(String solutionName);

	List<Server> getNormalServers(String string, String projectName);
}
