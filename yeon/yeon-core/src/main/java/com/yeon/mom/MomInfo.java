package com.yeon.mom;

import com.yeon.YeonPredefinedClassId;
import com.yeon.config.Ticket;

import java.util.List;

/**
 * 
 * @author pulsarang
 */
public class MomInfo extends Ticket {
	private static final String PARAM_ALLOW_SERVER_IPS = "allow_server_ips";
	private static final String PARAM_MQ_ADDRESS = "mq_address";
	private static final String PARAM_ENVIRONMENT = "env";
	private static final String PARAM_MOM_STATUS = "mom_status";

	protected volatile transient MomStatus momStatus;
	
	public MomInfo() {
		super(YeonPredefinedClassId.MOM);
	}

	public List<String> getAllowServerIps() {
		return getList(PARAM_ALLOW_SERVER_IPS, EMPTY_LIST);
	}

	public void setAllowServerIps(List<String> allowServerIps) {
		setList(PARAM_ALLOW_SERVER_IPS, allowServerIps);
	}

	public String getMqAddress() {
		return getString(PARAM_MQ_ADDRESS);
	}

	public void setMqAddress(String mqAddress) {
		setString(PARAM_MQ_ADDRESS, mqAddress);
	}

	public String getEnvironment() {
		return getString(PARAM_ENVIRONMENT);
	}

	public void setEnvironment(String environment) {
		setString(PARAM_ENVIRONMENT, environment);
	}

	public MomStatus getMomStatus() {
		MomStatus ms = momStatus;
		if (ms == null) {
			ms = momStatus = getEnum(PARAM_MOM_STATUS, MomStatus.NORMAL, MomStatus.class);
		}

		return ms;
	}

	public String getMomStatusCode() {
		return getMomStatus().name();
	}

	public void setMomStatusCode(String statusCode) {
		setString(PARAM_MOM_STATUS, statusCode);
		momStatus = null;
	}
}
