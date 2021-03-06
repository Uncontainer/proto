package com.pulsarang.infra.update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.InfraParameters;
import com.pulsarang.infra.config.Config;
import com.pulsarang.infra.config.ConfigCache;
import com.pulsarang.infra.remote.RemoteServiceBulkProxy;
import com.pulsarang.infra.remote.RemoteServiceResponseList;
import com.pulsarang.infra.remote.RemoteServiceResponseListHolder;
import com.pulsarang.infra.remote.reload.ReloadService;
import com.pulsarang.infra.server.Project;
import com.pulsarang.infra.server.Server;
import com.pulsarang.infra.server.ServerContext;
import com.pulsarang.infra.server.Solution;
import com.pulsarang.infra.server.SolutionProjectPair;

@Controller
@RequestMapping("/update")
public class ConfigUpdateController {
	private final Logger log = LoggerFactory.getLogger(ConfigUpdateController.class);

	@RequestMapping("/refresh")
	public String selectServer(ModelMap model, ConfigUpdateRequest cuRequest) {
		List<Solution> solutions;
		ServerContext serverContext = InfraContextFactory.getInfraContext().getServerContext();
		switch (cuRequest.getScope()) {
		case ALL:
			solutions = new ArrayList<Solution>(serverContext.getSolutions());
			break;
		case SOLUTION:
			List<String> solutionNames = cuRequest.getSolutionNames();
			solutions = new ArrayList<Solution>(solutionNames.size());
			for (String solutionName : solutionNames) {
				solutions.add(serverContext.getSolution(solutionName));
			}
			break;
		case PROJECT:
			List<SolutionProjectPair> projectNames = cuRequest.getProjectNames();
			Map<String, List<Project>> solutionMap = new HashMap<String, List<Project>>();
			for (int i = 0; i < projectNames.size(); i++) {
				SolutionProjectPair pair = projectNames.get(i);
				Project project = serverContext.getProject(pair.getSolutionName(), pair.getProjectName());
				if (project == null) {
					log.error("Fail to find project.({})", pair);
					continue;
				}

				List<Project> projects = solutionMap.get(pair.getSolutionName());
				if (projects == null) {
					projects = new ArrayList<Project>(3);
					solutionMap.put(pair.getSolutionName(), projects);
				}
				projects.add(project);
			}

			solutions = new ArrayList<Solution>(solutionMap.size());
			for (Entry<String, List<Project>> entry : solutionMap.entrySet()) {
				Solution solution = new Solution(entry.getKey(), entry.getValue());
				solutions.add(solution);
			}
			break;
		default:
			throw new UnsupportedOperationException("Unsupport configUpdateScope.(" + cuRequest.getScope() + ")");
		}

		model.addAttribute("solutions", solutions);

		model.addAttribute("configCategory", cuRequest.getConfigCategory());
		model.addAttribute("configName", cuRequest.getConfigName());
		model.addAttribute("callbackUrl", cuRequest.getCallbackUrl());

		return "/update/selectServer";
	}

	@RequestMapping("/refreshSubmit")
	public String refreshSubmit(ModelMap model, //
			@RequestParam("configCategory") String configCategory, //
			@RequestParam("configName") String configName, //
			@RequestParam("callbackUrl") String callbackUrl, //
			@RequestParam(value = "serverName", defaultValue = "") List<String> serverNames) throws Exception {
		List<Server> servers = getTargetServers(serverNames);
		ReloadService reloadService = RemoteServiceBulkProxy.newServerProxy(ReloadService.class, ConfigCache.NAME, servers, 5000);

		MapModel option = new MapModel();
		option.setString(InfraParameters.CATEGORY, configCategory);
		option.setString(InfraParameters.TICKET, configName);
		reloadService.refresh(option);

		RemoteServiceResponseList response = RemoteServiceResponseListHolder.getAndClear();

		model.addAttribute("success", response.isSuccess());
		model.addAttribute("result", response.getEntries());
		model.addAttribute("callbackUrl", callbackUrl);

		return "/update/refreshResult";
	}

	private List<Server> getTargetServers(List<String> serverNames) {
		List<Server> servers = new ArrayList<Server>(serverNames.size());
		ServerContext serverContext = InfraContextFactory.getInfraContext().getServerContext();

		for (String serverName : serverNames) {
			String[] tokens = StringUtils.split(serverName, ".");
			Server server = null;
			Project project = serverContext.getProject(tokens[0], tokens[1]);
			if (project != null) {
				server = project.getServerByName(tokens[2]);
			}

			if (server != null) {
				servers.add(server);
			} else {
				log.error("Unregisterd server. ({})", serverName);
			}
		}

		return servers;
	}
}
