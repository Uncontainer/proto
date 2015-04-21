package com.yeon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileYeonConfiguration extends BasicYeonConfiguration {
	private final Logger log = LoggerFactory.getLogger(YeonConfiguration.class);

	private static final String ADMIN_ADDRESS = "admin_address";
	private static final String RPC_LISTENER_PORT = "rpc_listener_port";

	private Properties props;
	private final String filePath;

	public PropertyFileYeonConfiguration() {
		this("pulsarang-config.properties");
	}

	public PropertyFileYeonConfiguration(String filePath) {
		this.filePath = filePath;
		if (filePath == null) {
			throw new IllegalArgumentException();
		}

		loadProperties();

		super.setInfraAdminAddress(getProperty(ADMIN_ADDRESS, true));
		super.setSolutionName(getProperty(YeonParameters.SOLUTION_NAME, true));
		super.setProjectName(getProperty(YeonParameters.PROJECT_NAME, true));

		String strRpcListenerPort = getProperty(RPC_LISTENER_PORT, false);
		if (strRpcListenerPort != null) {
			super.setRpcListenerPort(Integer.parseInt(strRpcListenerPort));
		}
	}

	private void loadProperties() {
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream is = classLoader.getResourceAsStream(filePath);
		if (is == null) {
			throw new IllegalArgumentException("Fail to find file: " + filePath);
		}

		props = new Properties();
		try {
			props.load(is);
		} catch (IOException e) {
			log.error("[PIC] Fail to load '" + filePath + "' file.", e);
			throw new RuntimeException(e);
		}

		try {
			is.close();
		} catch (IOException ignore) {
			// do nothing
		}
	}

	private String getProperty(String name, boolean required) {
		String property = props.getProperty(name);
		if (property == null && required) {
			throw new IllegalArgumentException(String.format("Fail to find '%s' in %s.", name, filePath));
		}

		return property;
	}
}