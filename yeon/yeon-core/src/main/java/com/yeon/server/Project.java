package com.yeon.server;

import com.yeon.YeonPredefinedClassId;
import com.yeon.config.Ticket;

import java.util.List;

/**
 * TODO 변경 event를 받을 수 있도록 관리되는 객체로 변경.
 * 
 * @author ghost
 * 
 */
public class Project extends Ticket {
	public static final String PARAM_SOLUTION_NAME = "solution_name";
	public static final String PARAM_PROJECT_NAME = "project_name";
	public static final String PARAM_PORT_PREFIX = "port_prefix";

	private String name;
	private List<Server> servers;

	public Project() {
		super(YeonPredefinedClassId.PROJECT);
	}

	public Project(String name, List<Server> servers) {
		this();
		if (name == null || servers == null) {
			throw new IllegalArgumentException();
		}

		this.name = name;
		this.servers = servers;
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

	public int getPortPrefix() {
		return getInteger(PARAM_PROJECT_NAME, 117);
	}

	public void setPortPrefix(int portPrefix) {
		setInteger(PARAM_PORT_PREFIX, portPrefix);
	}

	public String getSolutionName() {
		return getString(PARAM_SOLUTION_NAME);
	}

	public void setSolutionName(String solutionName) {
		setString(PARAM_SOLUTION_NAME, solutionName);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
