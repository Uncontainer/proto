package com.pulsarang.infra.server;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.pulsarang.core.util.LocalAddressHolder;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.remote.RemoteContext;

public class ServerContext {
	private final ServerCache serverCache;
	private final Server localServer;

	public ServerContext(RemoteContext remoteContext, int serverPort) {
		serverCache = new ServerCache();
		localServer = new Server(LocalAddressHolder.getLocalHostName(), LocalAddressHolder.getLocalAddress(), serverPort);

		remoteContext.setRemoteService(serverCache);
	}

	public List<String> getAdminServerIps() {
		return serverCache.getAdminServerIps();
	}

	public Server getLocalServer() {
		return localServer;
	}

	public List<Server> getServersByIp(String ip) {
		// TODO IP로 서버 목록을 가져오는 코드 추가.
		return Collections.emptyList();
//		throw new UnsupportedOperationException();
	}

	public List<Server> getServersOfProject(String projectName) {
		return serverCache.getServersOfProject(InfraContextFactory.getInfraContext().getSolutionName(), projectName);
	}

	public List<Server> getServersOfProject(SolutionProjectPair pair) {
		return serverCache.getServersOfProject(pair.getSolutionName(), pair.getProjectName());
	}

	public List<Server> getServersOfProject(String solutionName, String projectName) {
		return serverCache.getServersOfProject(solutionName, projectName);
	}

	public Solution getSolution(String solutionName) {
		return serverCache.getSolution(solutionName);
	}

	public Solution getSolutionSafely(String solutionName) {
		Solution solution = serverCache.getSolution(solutionName);
		if (solution == null) {
			throw new IllegalArgumentException("Solution does not exist.(" + solutionName + ")");
		}

		return solution;
	}

	public Project getProject(SolutionProjectPair pair) {
		return getProject(pair.getSolutionName(), pair.getProjectName());
	}

	public Project getProject(String solutionName, String projectName) {
		Solution solution = getSolution(solutionName);
		if (solution == null) {
			return null;
		}

		return solution.getProject(projectName);
	}

	public Project getProjectSafely(String solutionName, String projectName) {
		Solution solution = getSolutionSafely(solutionName);
		Project project = solution.getProject(projectName);
		if (project == null) {
			throw new IllegalArgumentException("Project does not exist.(" + projectName + ")");
		}

		return project;
	}

	public Collection<Solution> getSolutions() {
		return serverCache.getSolutions();
	}
}
