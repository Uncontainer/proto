package com.yeon.infra;

import com.yeon.infra.server.ServerService;
import com.yeon.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

public class AbstractConfigController {
	@Autowired
	protected ServerService serverService;
	public static final String PARAM_MODE = "mode";
	public static final String MODE_CREATE = "create";
	public static final String MODE_READ = "read";
	public static final String MODE_MODIFY = "modify";

	protected void setModeCreate(ModelMap model) {
		model.addAttribute(PARAM_MODE, MODE_CREATE);
	}

	protected void setModeRead(ModelMap model) {
		model.addAttribute(PARAM_MODE, MODE_READ);
	}

	protected void setModeModify(ModelMap model) {
		model.addAttribute(PARAM_MODE, MODE_MODIFY);
	}

	protected String selectSolution(ModelMap model, ServerSelectCriteria criteria) {
		List<String> solutionNames = serverService.getSolutionNames();
		String solutionName = criteria.getSolutionName();
		if (!solutionNames.contains(solutionName)) {
			solutionName = solutionNames.get(0);
			criteria.setSolutionName(solutionName);
		}

		model.addAttribute("solutionNames", solutionNames);
		model.addAttribute("solutionName", solutionName);

		return solutionName;
	}

	protected String selectProject(ModelMap model, ServerSelectCriteria criteria) {
		selectSolution(model, criteria);

		List<String> projectNames = serverService.getProjectNames(criteria.getSolutionName());
		String projectName = criteria.getProjectName();
		if (!projectNames.contains(projectName)) {
			projectName = projectNames.get(0);
			criteria.setProjectName(projectName);
		}

		model.addAttribute("projectNames", projectNames);
		model.addAttribute("projectName", projectName);

		return projectName;
	}

	protected Server selectServer(ModelMap model, ServerSelectCriteria criteria) {
		String projectName = selectProject(model, criteria);

		List<Server> servers = serverService.getNormalServers((String) model.get("solutionName"), projectName);
		Server selectedServer = selectServer(servers, criteria.getServerName());
		String selectedServerName = (selectedServer != null) ? selectedServer.getName() : "";

		model.addAttribute("serverNames", serverNames(servers));
		model.addAttribute("serverName", selectedServerName);

		return selectedServer;
	}

	protected static Server selectServer(List<Server> servers, String serverName) {
		Server runningServer = null;
		for (Server server : servers) {
			if (server.getRunningStatus().isAlive()) {
				runningServer = server;
			} else {
				continue;
			}

			if (server.getName().equals(serverName)) {
				return server;
			}
		}

		return runningServer;
	}

	protected static List<String> serverNames(List<Server> servers) {
		List<String> serverNames = new ArrayList<String>(servers.size());
		// TODO 서버 상태 체크 코드 추가.
		for (Server server : servers) {
			serverNames.add(server.getName());
		}

		return serverNames;
	}
}
