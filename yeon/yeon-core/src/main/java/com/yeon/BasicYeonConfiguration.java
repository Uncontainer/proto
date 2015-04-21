package com.yeon;

import com.yeon.lang.ResourceServiceUtils;
import com.yeon.util.LocalAddressHolder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class BasicYeonConfiguration implements YeonConfiguration {
	private static final Logger log = LoggerFactory.getLogger(YeonConfiguration.class);

	private String solutionName;
	private String projectName;
	private YeonEnvironment environment;
	private final String version;

	private String infraAdminAddress;
	private String configBackupPath;

	private int rpcListenerPort = 11702;

	public BasicYeonConfiguration() {
		this.version = loadVersion();
	}

	private static String loadVersion() {
		String version;
		try {
			ClassLoader classLoader = YeonConfiguration.class.getClassLoader();
			InputStream is = classLoader.getResourceAsStream("yeon/version");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			version = br.readLine();
			is.close();
			br.close();
		} catch (Throwable t) {
			log.warn("[YEON] Fail to load infra-core version information", t);
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
	public YeonEnvironment getEnvironment() {
		return environment;
	}

	void initEnvironment() {
		// TODO [TEST] 제거.
		if (true) {
			environment = YeonEnvironment.LOCAL;
			return;
		}
		
		InfraInfo infraInfo = ResourceServiceUtils.getResourceSafely(YeonPredefinedResourceId.INFRA, InfraInfo.class);

		YeonEnvironment infraAdminServerEnv = YeonEnvironment.valueOf(infraInfo.getEnvironment());

		if (infraAdminServerEnv == YeonEnvironment.REAL) {
			// TODO local이 접속할 경우에 대한 처리 추가.
			environment = infraAdminServerEnv;
			return;
		}

		String envString = System.getProperty("env");
		if (envString == null) {
			// TODO local의 대역폭을 좀 더 자세히 알아봐야 함.
			if (LocalAddressHolder.getLocalAddress().startsWith("10.66.")) {
				environment = YeonEnvironment.LOCAL;
				return;
			}
		} else {
			try {
				environment = YeonEnvironment.valueOf(envString.toUpperCase());
				return;
			} catch (IllegalArgumentException e) {
			}
		}

		environment = infraAdminServerEnv;
	}
}