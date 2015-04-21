package com.pulsarang.infra.server;

import java.util.Map;

import com.pulsarang.core.util.LocalAddressHolder;
import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.InfraParameters;

public class Server extends MapModel {
	public static final String SERVER_NAME = "server_name";
	public static final String SERVER_IP = "server_ip";
	public static final String SERVER_PORT = "server_port";
	public static final String REGIST_STATUS = "regist_status";

	public Server() {
		super();
	}

	public Server(String name, String ip, int port) {
		super();
		setName(name);
		setIp(ip);
		setPort(port);
	}

	public Server(Map<String, Object> properties) {
		super(properties);
	}

	public String getIp() {
		return (String) getProperty(SERVER_IP);
	}

	public void setIp(String ip) {
		setProperty(SERVER_IP, ip);
	}

	public int getPort() {
		return (Integer) getProperty(SERVER_PORT, 0);
	}

	public void setPort(int port) {
		setProperty(SERVER_PORT, port);
	}

	public void setName(String name) {
		setProperty(SERVER_NAME, name);
	}

	public String getName() {
		return (String) getProperty(SERVER_NAME);
	}

	public void setSolutionName(String solutionName) {
		setProperty(InfraParameters.SOLUTION_NAME, solutionName);
	}

	public String getSolutionName() {
		return (String) getProperty(InfraParameters.SOLUTION_NAME);
	}

	public void setProjectName(String projectName) {
		setProperty(InfraParameters.PROJECT_NAME, projectName);
	}

	public String getProjectName() {
		return (String) getProperty(InfraParameters.PROJECT_NAME);
	}

	public boolean isLocal() {
		return LocalAddressHolder.getLocalAddress().equals(getIp());
	}

	@Override
	public String toString() {
		return "tcp://[" + getSolutionName() + "].[" + getProjectName() + "]." + getIp() + ":" + getPort();
	}
}
