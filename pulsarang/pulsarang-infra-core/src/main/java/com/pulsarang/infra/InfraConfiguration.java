package com.pulsarang.infra;

public interface InfraConfiguration {
	String getInfraAdminAddress();

	String getSolutionName();

	String getProjectName();

	String getTicketBackupPath();

	int getRpcListenerPort();

	String getVersion();

	InfraEnvironment getEnvironment();
}