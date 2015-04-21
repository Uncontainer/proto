package com.yeon.server;

import com.yeon.YeonParameters;
import com.yeon.YeonPredefinedClassId;
import com.yeon.config.Ticket;
import com.yeon.util.LocalAddressHolder;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 *
 * @author pulsarang
 */
public class Server extends Ticket {
    public static final String PARAM_NAME = "name";
    public static final String PARAM_PROTOCOL = "protocol";
    public static final String PARAM_IP = "ip";
	public static final String PARAM_PORT = "port";
	public static final String PARAM_REGIST_STATUS = "regist_status";
	public static final String PARAM_RUNNING_STATUS = "running_status";

	protected ServerRegistStatus registStatus;
	protected ServerRunningStatus runningStatus;

	private ProjectId projectId;

	public Server() {
		super(YeonPredefinedClassId.SERVER);
	}

	public Server(Map<String, Object> properties) {
		super(YeonPredefinedClassId.SERVER, properties);
	}

	public Server(String name, String ip, int port) {
		super(YeonPredefinedClassId.SERVER);

		setName(name);
		setIp(ip);
		setPort(port);
	}

	public String getIp() {
		return getString(PARAM_IP);
	}

	public void setIp(String ip) {
		setString(PARAM_IP, ip);
	}

	public int getPort() {
		return getInteger(PARAM_PORT, 0);
	}

	public void setPort(int port) {
		setInteger(PARAM_PORT, port);
	}

	public void setName(String name) {
		setString(PARAM_NAME, name);
	}

	public String getName() {
		return getString(PARAM_NAME);
	}

	public void setSolutionName(String solutionName) {
		setString(YeonParameters.SOLUTION_NAME, solutionName);
	}

	public String getSolutionName() {
		return getString(YeonParameters.SOLUTION_NAME);
	}

	public void setProjectName(String projectName) {
		setString(YeonParameters.PROJECT_NAME, projectName);
	}

	public String getProjectName() {
		return getString(YeonParameters.PROJECT_NAME);
	}

	public boolean isNormal() {
		return getRegistStatus() == ServerRegistStatus.NORMAL;
	}

	public ServerRegistStatus getRegistStatus() {
		ServerRegistStatus srs = registStatus;
		if (srs == null) {
			srs = registStatus = getEnum(PARAM_REGIST_STATUS, ServerRegistStatus.WAIT, ServerRegistStatus.class);
		}

		return srs;
	}

	public void setRegistStatus(ServerRegistStatus registStatus) {
		setEnum(PARAM_REGIST_STATUS, registStatus);
		this.registStatus = registStatus;
	}

	public ServerRunningStatus getRunningStatus() {
		ServerRunningStatus srs = runningStatus;
		if (srs == null) {
			srs = runningStatus = getEnum(PARAM_RUNNING_STATUS, ServerRunningStatus.SHUTDOWN, ServerRunningStatus.class);
		}

		return srs;
	}

	public void setRunningStatus(ServerRunningStatus runningStatus) {
		setEnum(PARAM_RUNNING_STATUS, runningStatus);
		this.runningStatus = runningStatus;
	}

	public ProjectId getProjectId() {
		ProjectId id = projectId;
		if (id == null) {
			String solutionName = getSolutionName();
			String projectName = getProjectName();
			if (solutionName != null && projectName != null) {
				id = projectId = new ProjectId(solutionName, projectName);
			}
		}

		return id;
	}

	@Override
	public String toString() {
		return "tcp://" + getName() + "." + getProjectName() + "." + getSolutionName() + ":" + getPort();
	}

	public String getCanonicalName() {
		return getIp() + "$" + getProjectId().toString();
	}

	public static String getServerId(Server server) {
		return server.getCanonicalName();
	}

	public static Server parseServerId(String serverId) {
		String[] tokens = StringUtils.split(serverId, "$");
		Server server = new Server();
		server.setIp(tokens[0]);
		ProjectId projectId = new ProjectId(tokens[1]);
		server.setSolutionName(projectId.getSolutionName());
		server.setProjectName(projectId.getProjectName());

		return server;
	}

	public boolean isLocal() {
		return LocalAddressHolder.getLocalAddress().equals(getIp());
	}
}
