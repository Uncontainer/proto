package com.pulsarang.infra.server;

import java.util.List;


public interface ServerService extends ServerAPIService {
	List<ServerEntity> listAll();

	ServerEntity getServer(Long id);

	void addServer(ServerEntity server);

	void modifyServer(ServerEntity server);

	void removeServer(ServerEntity server);

	List<String> getSolutionNames();

	List<String> getProjectNames(String solutionName);

	List<ServerEntity> getNormalServers(String string, String projectName);
}
