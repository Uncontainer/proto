package com.yeon.infra.server;

import com.yeon.remote.bulk.RemoteServiceBulkProxy;
import com.yeon.remote.bulk.RemoteServiceResponseList;
import com.yeon.remote.bulk.RemoteServiceResponseListHolder;
import com.yeon.remote.reload.ReloadService;
import com.yeon.server.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerServiceImpl implements ServerService {
	private final Logger log = LoggerFactory.getLogger(ServerServiceImpl.class);

	@Autowired
	private ServerDao serverDao;

	@Autowired
	private ProjectDao projectDao;

	@Override
	public List<Server> getServerInfos() {
		return serverDao.findNormalServers(null, null);
	}

	@Override
	public void updateServer(Server server, boolean startup) {
		boolean portChanged = false;
		if (startup) {
			Server savedServer = serverDao.findServer(server);
			if (savedServer != null) {
				if (savedServer.getPort() != server.getPort()) {
					portChanged = true;
					savedServer.setPort(server.getPort());
				}
				server = savedServer;
			} else {
				server.setRegistStatus(ServerRegistStatus.WAIT);

				Project projectEntity = new Project();
				projectEntity.setSolutionName(server.getSolutionName());
				projectEntity.setName(server.getProjectName());
				projectDao.insert(projectEntity);
			}

			server.setRunningStatus(ServerRunningStatus.RUNNING);
			// TODO 시작/종료 시간 셋팅 로직 주석 풀기
			// server.setLastStartupDate(new Date());
		} else {
			Server savedServer = serverDao.findServer(server);
			if (savedServer == null) {
				return;
			}

			server = savedServer;
			server.setRunningStatus(ServerRunningStatus.SHUTDOWN);
			// TODO 시작/종료 시간 셋팅 로직 주석 풀기
			// server.setLastShutdownDate(new Date());
		}

		String serverName = server.getName();
		server.setName(serverName);

		serverDao.insert(server);

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
	public List<Server> listAll() {
		return serverDao.selectAll();
	}

	@Override
	public Server getServer(String id) {
		return serverDao.select(id);
	}

	@Override
	public void addServer(Server server) {
		serverDao.insert(server);
	}

	@Override
	public void modifyServer(Server server) {
		serverDao.modify(server);
	}

	@Override
	public void removeServer(Server server) {
		serverDao.delete(server);
	}

	@Override
	public List<String> getSolutionNames() {
		return serverDao.findSolutionNames();
	}

	@Override
	public List<String> getProjectNames(String solutionName) {
		return serverDao.findProjectNames(solutionName);
	}

	@Override
	public List<Server> getNormalServers(String solutionName, String projectName) {
		return serverDao.findNormalServers(solutionName, projectName);
	}

	@Override
	public Project getProjectInfo(String solutionName, String projectName) {
		return projectDao.select(solutionName, projectName);
	}

	@Override
	public List<Server> getServersByIp(String ip) {
		return serverDao.selectByIp(ip);
	}
}
