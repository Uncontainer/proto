package com.yeon;

import com.yeon.config.Ticket;

import java.util.List;

public class InfraInfo extends Ticket {
	private static final String PARAM_ALLOW_SERVER_IPS = "allow_server_ips";
	private static final String PARAM_ENVIRONMENT = "env";

	public InfraInfo() {
		super(YeonPredefinedClassId.INFRA);
	}

	public List<String> getAllowServerIps() {
		return getValue(PARAM_ALLOW_SERVER_IPS, EMPTY_LIST);
	}

	public void setAllowServerIps(List<String> allowServerIps) {
		setValue(PARAM_ALLOW_SERVER_IPS, allowServerIps);
	}

	public String getEnvironment() {
		return getValue(PARAM_ENVIRONMENT);
	}

	public void setEnvironment(String environment) {
		setValue(PARAM_ENVIRONMENT, environment);
	}
}
