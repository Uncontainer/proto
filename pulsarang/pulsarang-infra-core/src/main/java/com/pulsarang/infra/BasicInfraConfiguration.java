package com.pulsarang.infra;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.core.util.LocalAddressHolder;
import com.pulsarang.infra.config.ConfigContext;

public class BasicInfraConfiguration implements InfraConfiguration {
	private static final Logger log = LoggerFactory.getLogger(InfraConfiguration.class);

	private String solutionName;
	private String projectName;
	private InfraEnvironment environment;
	private final String version;

	private String infraAdminAddress;
	private String configBackupPath;

	private int rpcListenerPort = 11702;

	public BasicInfraConfiguration() {
		this.version = loadVersion();
	}

	private static String loadVersion() {
		String version;
		try {
			ClassLoader classLoader = InfraConfiguration.class.getClassLoader();
			InputStream is = classLoader.getResourceAsStream("version");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			version = br.readLine();
			is.close();
			br.close();
		} catch (Throwable e) {
			log.warn("[PIC] Fail to load infra-core version information", e);
			version = null;
		}

		if (StringUtils.isBlank(version)) {
			version = "unknown";
		}

		return version;
	}

	public void setInfraAdminAddress(String infraAdminAddress) {
		URL url;
		try {
			url = new URL(infraAdminAddress);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Invalid infra-admin address.", e);
		}

		this.infraAdminAddress = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
	}

	@Override
	public String getInfraAdminAddress() {
		return infraAdminAddress;
	}

	@Override
	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	@Override
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public String getTicketBackupPath() {
		if (configBackupPath == null) {
			String path = System.getProperty("user.home");

			if (path == null) {
				path = new File("").getAbsolutePath();
			}
			if (!path.endsWith("/") && !path.endsWith("\\")) {
				path += "/";
			}

			path += ".pulsarang/infra/config";

			configBackupPath = path;
		}

		return configBackupPath;
	}

	@Override
	public int getRpcListenerPort() {
		return rpcListenerPort;
	}

	public void setRpcListenerPort(int eventServerPort) {
		this.rpcListenerPort = eventServerPort;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public InfraEnvironment getEnvironment() {
		return environment;
	}

	void initEnvironment(ConfigContext configContext) {
		//TODO [TEST] 제거.
		if(true) {
			environment = InfraEnvironment.LOCAL;
			return;
		}
		
		InfraInfo infraInfo = configContext.get(InfraInfo.CONFIG_ID);
		InfraEnvironment infraAdminServerEnv = InfraEnvironment.valueOf(infraInfo.getEnvironment());

		if (infraAdminServerEnv == InfraEnvironment.REAL) {
			// TODO local이 접속할 경우에 대한 처리 추가.
			environment = infraAdminServerEnv;
			return;
		}

		String envString = System.getProperty("env");
		if (envString == null) {
			// TODO local의 대역폭을 좀 더 자세히 알아봐야 함.
			if (LocalAddressHolder.getLocalAddress().startsWith("10.66.")) {
				environment = InfraEnvironment.LOCAL;
				return;
			}
		} else {
			try {
				environment = InfraEnvironment.valueOf(envString.toUpperCase());
				return;
			} catch (IllegalArgumentException e) {
			}
		}

		environment = infraAdminServerEnv;
	}
}