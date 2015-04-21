package com.pulsarang.infra.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.remote.annotation.RemoteService;
import com.pulsarang.infra.remote.reload.AbstractReloadService;

@RemoteService(name = ServerCache.NAME)
public class ServerCache extends AbstractReloadService {
	public static final String NAME = "__server";

	private volatile Map<String, Solution> solutionMap;
	private volatile List<String> adminServerIps;

	ServerAPIService api = new HttpServerAPIService();

	public ServerCache() {
	}

	public List<Server> getServersOfSolution(String solutionName) {
		Solution solution = getSolution(solutionName);
		if (solution == null) {
			return Collections.emptyList();
		}

		return solution.getServers();
	}

	public List<Server> getServersOfProject(String solutionName, String projectName) {
		Solution solution = getSolution(solutionName);
		if (solution == null) {
			return Collections.emptyList();
		}

		Project project = solution.getProject(projectName);
		if (project == null) {
			return Collections.emptyList();
		}

		return project.getServers();
	}

	public List<String> getAdminServerIps() {
		if (adminServerIps == null) {
			List<Server> servers = getServersOfProject(SolutionProjectPair.INFRA_ADMIN.getSolutionName(),
					SolutionProjectPair.INFRA_ADMIN.getProjectName());
			if (servers.isEmpty()) {
				adminServerIps = Collections.emptyList();
			} else {
				List<String> serverIps = new ArrayList<String>(servers.size());
				for (Server server : servers) {
					serverIps.add(server.getIp());
				}
				this.adminServerIps = serverIps;
			}
		}

		return adminServerIps;
	}

	public Solution getSolution(String solutionName) {
		return getSolutionMap().get(solutionName);
	}

	public Collection<Solution> getSolutions() {
		return getSolutionMap().values();
	}

	synchronized Map<String, Solution> getSolutionMap() {
		if (solutionMap == null) {
			List<Server> serverInfos = api.getServerInfos();

			Map<String, Map<String, List<Server>>> mapSolutions = new HashMap<String, Map<String, List<Server>>>();
			for (Server server : serverInfos) {
				Map<String, List<Server>> solution = mapSolutions.get(server.getSolutionName());
				if (solution == null) {
					solution = new HashMap<String, List<Server>>();
					mapSolutions.put(server.getSolutionName(), solution);
				}

				List<Server> mapProject = solution.get(server.getProjectName());
				if (mapProject == null) {
					mapProject = new ArrayList<Server>();
					solution.put(server.getProjectName(), mapProject);
				}

				mapProject.add(server);
			}

			Map<String, Solution> tempSolutionMap = new HashMap<String, Solution>();
			for (Entry<String, Map<String, List<Server>>> mapSolution : mapSolutions.entrySet()) {
				List<Project> projects = new ArrayList<Project>(mapSolution.getValue().size());
				for (Entry<String, List<Server>> mapProject : mapSolution.getValue().entrySet()) {
					Project project = new Project(mapProject.getKey(), mapProject.getValue());
					projects.add(project);
				}

				Solution solution = new Solution(mapSolution.getKey(), projects);
				tempSolutionMap.put(mapSolution.getKey(), solution);
			}

			this.solutionMap = tempSolutionMap;
		}

		return solutionMap;
	}

	@Override
	public List<MapModel> list(MapModel option) {
		if (solutionMap == null) {
			return null;
		}

		List<MapModel> result = new ArrayList<MapModel>();

		for (Solution solution : solutionMap.values()) {
			for (Project project : solution.getProjects()) {
				for (Server server : project.getServers()) {
					result.add(server);
				}
			}
		}

		return result;
	}

	@Override
	public int listCount(MapModel option) {
		if (solutionMap == null) {
			return 0;
		}

		int count = 0;
		for (Solution solution : solutionMap.values()) {
			for (Project project : solution.getProjects()) {
				count += project.getServers().size();
			}
		}

		return count;
	}

	@Override
	public synchronized void expire(MapModel option) {
		solutionMap = null;
		adminServerIps = null;
	}
}
