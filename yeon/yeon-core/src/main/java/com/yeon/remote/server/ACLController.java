package com.yeon.remote.server;

import com.yeon.YeonContext;
import com.yeon.server.Server;
import com.yeon.server.ServerContext;

import java.util.List;

public class ACLController {
	private static final ACLController INSTANCE = new ACLController();

	public static final ACLController getInstance() {
		return INSTANCE;
	}

	private final ServerContext serverContext;

	private ACLController() {
		serverContext = YeonContext.getServerContext();
	}

	public boolean isDeniedIp(String ip) {
		return !isAllowedIp(ip);
	}

	public boolean isAllowedIp(String ip) {
		if (ip == null) {
			return false;
		}

		if (isAdminIp(ip)) {
			return true;
		}

		if (isSameProjectServerIp(ip)) {
			return true;
		}

		return isAllowServerIp(ip);
	}

	private boolean isAdminIp(String ip) {
		List<String> adminServerIps = serverContext.getAdminServerIps();
		return adminServerIps.contains(ip);
	}

	private boolean isSameProjectServerIp(String ip) {
		List<Server> servers = serverContext.getServersOfProject(YeonContext.getSolutionName(), YeonContext.getProjectName());
		for (Server server : servers) {
			if (ip.equals(server.getIp())) {
				return true;
			}
		}

		return false;
	}

	private boolean isAllowServerIp(String ip) {
		// TODO 허용된 서버의 IP로 검색하는 코드 추가.
		return false;
	}
}
