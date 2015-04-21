package com.yeon.server;

import com.yeon.YeonConfiguration;
import com.yeon.YeonContext;
import com.yeon.util.LocalAddressHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerInfoNotifier extends Thread {
	private final Logger log = LoggerFactory.getLogger(ServerInfoNotifier.class);

	private final YeonConfiguration yeonConfiguration;
	private final int serverPort;
	private final boolean startup;

	private final ServerApiService api;

	public ServerInfoNotifier(YeonConfiguration yeonConfiguration, boolean startup) {
		this(yeonConfiguration, -1, startup);
	}

	public ServerInfoNotifier(YeonConfiguration yeonConfiguration, int serverPort, boolean startup) {
		this.yeonConfiguration = yeonConfiguration;
		this.serverPort = serverPort;
		this.startup = startup;
		this.api = YeonContext.getRemoteContext().newProjectProxy(ServerApiService.class, ServerApiService.NAME, ProjectId.INFRA_ADMIN, 3000);
	}

	@Override
	public void run() {
		try {
			// infra-admin project의 경우 delay를 두고 정보 송신
			if (ProjectId.INFRA_ADMIN.getProjectName().equals(yeonConfiguration.getProjectName())) {
				Thread.sleep(15 * 1000L);
			}

			Server server = new Server();
			server.setIp(yeonConfiguration.getSolutionName());
			server.setSolutionName(yeonConfiguration.getSolutionName());
			server.setProjectName(yeonConfiguration.getProjectName());
			server.setName(LocalAddressHolder.getLocalHostName());
			server.setIp(LocalAddressHolder.getLocalAddress());
			if (serverPort > 0) {
				server.setPort(serverPort);
			}

			api.updateServer(server, startup);
		} catch (Throwable t) {
			log.warn("[PIC] Fail to send server information to infra-admin. Service is normal. {}", t.getMessage());
		}
	}
}
