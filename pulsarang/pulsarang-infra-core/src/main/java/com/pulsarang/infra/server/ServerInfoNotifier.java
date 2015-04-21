package com.pulsarang.infra.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.core.util.LocalAddressHolder;
import com.pulsarang.infra.InfraConfiguration;

public class ServerInfoNotifier extends Thread {
	private final Logger log = LoggerFactory.getLogger(ServerInfoNotifier.class);

	private final InfraConfiguration infraConfiguration;
	private final int serverPort;
	private final boolean startup;

	ServerAPIService api = new HttpServerAPIService();

	public ServerInfoNotifier(InfraConfiguration infraConfiguration, boolean startup) {
		this(infraConfiguration, -1, startup);
	}

	public ServerInfoNotifier(InfraConfiguration infraConfiguration, int serverPort, boolean startup) {
		this.infraConfiguration = infraConfiguration;
		this.serverPort = serverPort;
		this.startup = startup;
	}

	@Override
	public void run() {
		try {
			// infra-admin project의 경우 delay를 두고 정보 송신
			if (SolutionProjectPair.INFRA_ADMIN.getProjectName().equals(infraConfiguration.getProjectName())) {
				Thread.sleep(15 * 1000L);
			}

			Server server = new Server();
			server.setIp(infraConfiguration.getSolutionName());
			server.setSolutionName(infraConfiguration.getSolutionName());
			server.setProjectName(infraConfiguration.getProjectName());
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
