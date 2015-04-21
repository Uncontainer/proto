package com.yeon.mom;

import com.yeon.YeonParameters;
import com.yeon.YeonPredefinedClassId;
import com.yeon.config.Ticket;
import com.yeon.mom.service.ServiceStatus;
import com.yeon.server.ProjectId;
import com.yeon.util.LocalAddressHolder;

import java.util.List;

/**
 * 
 * @author pulsarang
 */
public class ProjectInfo extends Ticket {
	protected static final String PARAM_PROCESS_SERVER_IPS = "proc_svr_ips";

	private ProjectId projectId;

	private transient ServiceStatus status;

	public ProjectInfo() {
		super(YeonPredefinedClassId.PROJECT);
	}

	public void setSolutionName(String solutionName) {
		setString(YeonParameters.SOLUTION_NAME, solutionName);
		projectId = null;
	}

	public String getSolutionName() {
		return getString(YeonParameters.SOLUTION_NAME);
	}

	public void setProjectName(String projectName) {
		setString(YeonParameters.PROJECT_NAME, projectName);
		projectId = null;
	}

	public String getProjectName() {
		return getString(YeonParameters.PROJECT_NAME);
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

	public ServiceStatus getStatus() {
		ServiceStatus ss = status;
		if (ss == null) {
			ss = status = getEnum(PARAM_STATUS, ServiceStatus.NORMAL, ServiceStatus.class);
		}

		return ss;
	}

	public void setStatus(ServiceStatus status) {
		setEnum(PARAM_STATUS, status);
		this.status = status;
	}

	public String getStatusCode() {
		return getStatus().name();
	}

	public void setStatusCode(String statusCode) {
		setString(PARAM_STATUS, statusCode);
		status = null;
	}

	public List<String> getProcessServerIps() {
		return getList(PARAM_PROCESS_SERVER_IPS, EMPTY_LIST);
	}

	public void setProcessServerIps(List<String> processServerIps) {
		setList(PARAM_PROCESS_SERVER_IPS, processServerIps);
	}

	public boolean isSubscribeEnabled() {
		if (getStatus() != ServiceStatus.NORMAL) {
			return false;
		}

		List<String> processServerIps = getProcessServerIps();
		if (!processServerIps.isEmpty() && !processServerIps.contains(LocalAddressHolder.getLocalAddress())) {
			return false;
		}

		return true;
	}
}
