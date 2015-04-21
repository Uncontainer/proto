package com.pulsarang.infra.config;

import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.config.category.ConfigCategoryEntity;
import com.pulsarang.infra.config.category.ConfigCategoryService;
import com.pulsarang.infra.config.info.ConfigInfoEntity;
import com.pulsarang.infra.config.info.ConfigInfoService;
import com.yeon.server.Project;
import com.yeon.server.Server;
import com.yeon.server.ServerContext;
import com.yeon.server.Solution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConfigServerSelector {
	@Autowired
	private ConfigCategoryService ccService;

	@Autowired
	private ConfigInfoService ciService;

	public List<Server> getServers(ConfigEntityId configEntityId) {
		ConfigCategoryEntity configCategory = ccService.getConfigCategory(configEntityId.getCategory());
		if (configCategory == null) {
			throw new IllegalArgumentException("Fail to find config category.(" + configEntityId.getCategory() + ")");
		}

		ConfigInfoEntity configInfoEntity = ciService.getConfigInfo(configEntityId);
		ServerContext serverContext = InfraContextFactory.getInfraContext().getServerContext();
		List<Server> servers;

		switch (configCategory.getScopeType()) {
		case ALL: {
			servers = new ArrayList<Server>();
			for (Solution solution : serverContext.getSolutions()) {
				servers.addAll(solution.getServers());
			}
			break;
		}
		case SOLUTION: {
			String solutionName = configInfoEntity.getSolutionName();
			Solution solution = serverContext.getSolution(solutionName);
			if (solution == null) {
				throw new IllegalArgumentException("Fail to find solution.(" + solutionName + ")");
			}

			servers = solution.getServers();
			break;
		}
		case PROJECT: {
			String solutionName = configInfoEntity.getSolutionName();
			String projectName = configInfoEntity.getProjectName();
			Project project = serverContext.getProject(solutionName, projectName);
			if (project == null) {
				throw new IllegalArgumentException("Fail to find project.(" + solutionName + "." + projectName + ")");
			}

			servers = project.getServers();
			break;
		}
		default:
			throw new IllegalArgumentException("Unsupported scopeType.(" + configCategory.getScopeType() + ")");
		}

		return null;
	}
}
