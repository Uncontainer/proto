package com.yeon.server;

import com.yeon.remote.RemoteContext;
import com.yeon.util.LocalAddressHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ServerContext {
	private final ServerCache serverCache;
	private final Server localServer;

	public ServerContext(RemoteContext remoteContext) {
		serverCache = new ServerCache(remoteContext);
		localServer = new Server(LocalAddressHolder.getLocalHostName(), LocalAddressHolder.getLocalAddress(), remoteContext.getRequestListenPort());

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
		// throw new UnsupportedOperationException();
	}

	public List<Server> getServersOfProject(ProjectId projectId) {
		return serverCache.getServersOfProject(projectId);
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

	public Project getProject(ProjectId projectId) {
		return getProject(projectId.getSolutionName(), projectId.getProjectName());
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
