package com.yeon;

public interface YeonConfiguration {
	String getInfraAdminAddress();

	String getSolutionName();

	String getProjectName();

	String getTicketBackupPath();

	int getRpcListenerPort();

	String getVersion();

	YeonEnvironment getEnvironment();
}