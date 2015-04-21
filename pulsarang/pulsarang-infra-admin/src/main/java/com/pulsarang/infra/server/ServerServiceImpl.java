package com.pulsarang.infra.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pulsarang.infra.remote.RemoteServiceBulkProxy;
import com.pulsarang.infra.remote.RemoteServiceResponseList;
import com.pulsarang.infra.remote.RemoteServiceResponseListHolder;
import com.pulsarang.infra.remote.reload.ReloadService;

@Service
public class ServerServiceImpl implements ServerService {
	private final Logger log = LoggerFactory.getLogger(ServerServiceImpl.class);

	@Autowired
	private ServerRepository serverRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Override
	public List<Server> getServerInfos() {
		List<ServerEntity> serverEntities = serverRepository.findNormalServers(null, null);
		List<Server> servers = new ArrayList<Server>(serverEntities.size());
		for (ServerEntity serverEntity : serverEntities) {
			servers.add(serverEntity.getServer());
		}

		return servers;
	}

	@Override
	public void updateServer(Server server, boolean startup) {
		ServerEntity serverEntity = new ServerEntity(server);

		boolean portChanged = false;
		if (startup) {
			ServerEntity savedServer = serverRepository.findServer(serverEntity);
			if (savedServer != null) {
				if (savedServer.getPort() != serverEntity.getPort()) {
					portChanged = true;
					savedServer.setPort(serverEntity.getPort());
				}
				serverEntity = savedServer;
			} else {
				serverEntity.setRegistStatus("WAIT");

				ProjectEntity projectEntity = new ProjectEntity();
				projectEntity.setSolutionName(server.getSolutionName());
				projectEntity.setProjectName(server.getProjectName());
				projectRepository.save(projectEntity);
			}

			serverEntity.setRunningStatus("RUNNING");
			serverEntity.setLastStartupDate(new Date());
		} else {
			ServerEntity savedServer = serverRepository.findServer(serverEntity);
			if (savedServer == null) {
				return;
			}

			serverEntity = savedServer;
			serverEntity.setRunningStatus("SHUTDOWN");
			serverEntity.setLastShutdownDate(new Date());
		}

		String serverName = serverEntity.getName();
		serverEntity.setName(serverName);

		serverRepository.save(serverEntity);

		if (portChanged) {
			ReloadService reloadService = RemoteServiceBulkProxy.newAllSolutionProxy(ReloadService.class, ServerCache.NAME, 5000);
			try {
				reloadService.expire(null);
			} catch (Exception e) {
				// TODO 주기적 재시도 필요.
				log.error("Fail to multicate port change event.", e);
			}

			RemoteServiceResponseList response = RemoteServiceResponseListHolder.getAndClear();
			if (response.isFail()) {
				log.error("Some has failed to expire server cache.({})", response.getFailedEntries());
			}
		}
	}

	@Override
	public List<ServerEntity> listAll() {
		return serverRepository.findAll();
	}

	@Override
	public ServerEntity getServer(Long id) {
		return serverRepository.findOne(id);
	}

	@Override
	public void addServer(ServerEntity server) {
		serverRepository.save(server);
	}

	@Override
	public void modifyServer(ServerEntity server) {
		serverRepository.save(server);
	}

	@Override
	public void removeServer(ServerEntity server) {
		serverRepository.delete(server);
	}

	@Override
	public List<String> getSolutionNames() {
		return serverRepository.findSolutionNames();
	}

	@Override
	public List<String> getProjectNames(String solutionName) {
		return serverRepository.findProjectNames(solutionName);
	}

	@Override
	public List<ServerEntity> getNormalServers(String solutionName, String projectName) {
		return serverRepository.findNormalServers(solutionName, projectName);
	}

	@Override
	public Project getProjectInfo(String solutionName, String projectName) {
		// TODO Auto-generated method stub
		return null;
	}
}
