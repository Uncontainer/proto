package com.pulsarang.infra.server;

import java.util.List;

public class Project {
	private String name;
	private List<Server> servers;

	public Project() {
	}

	public Project(String name, List<Server> servers) {
		super();
		if (name == null || servers == null) {
			throw new IllegalArgumentException();
		}

		this.name = name;
		this.servers = servers;
	}

	public String getName() {
		return name;
	}

	public List<Server> getServers() {
		return servers;
	}

	public Server getServerByName(String serverName) {
		if (serverName == null) {
			return null;
		}

		for (Server server : servers) {
			if (serverName.equals(server.getName())) {
				return server;
			}
		}

		return null;
	}

	public Server getServerByIp(String serverIp) {
		if (serverIp == null) {
			return null;
		}

		for (Server server : servers) {
			if (serverIp.equals(server.getIp())) {
				return server;
			}
		}

		return null;
	}
}
