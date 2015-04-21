package com.pulsarang.infra.server;

import java.util.List;

public interface ServerRepositoryCustom {
	List<ServerEntity> findNormalServers(String solutionName, String projectName);

	ServerEntity findServer(ServerEntity server);

	List<String> findSolutionNames();

	List<String> findProjectNames(String solutionName);

	// List<Solution> findSolutions();
	//
	// Solution findSolution(String solutionName);
}
