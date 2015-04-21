package com.pulsarang.infra.util;

import java.util.Arrays;
import java.util.List;

public class EnvironmentUtil {
	public static final String ENV_DEV = "dev";
	public static final String ENV_TEST = "test";
	public static final String ENV_REAL = "real";

	static final List<String> runEnvs = Arrays.asList(ENV_DEV, ENV_TEST, ENV_REAL);

	private static String runEnv;
	private static String dbEnvironment;
	private static String distPhase;

	public static synchronized void init(String deployPhase) {
		if ("local".equals(deployPhase) || "dev".equals(deployPhase)) {
			runEnv = ENV_DEV;
		} else if ("test".equals(deployPhase)) {
			runEnv = ENV_TEST;
		} else if ("staging".equals(deployPhase)) {
			runEnv = ENV_REAL;
		} else if ("real".equals(deployPhase)) {
			runEnv = ENV_REAL;
		} else {
			throw new IllegalArgumentException("Unsupport distribution phase: " + deployPhase);
		}

		EnvironmentUtil.distPhase = deployPhase;
		EnvironmentUtil.dbEnvironment = getDBEnvironment(runEnv);
	}

	public static void checkRunEnv(String runEnv) {
		if (!runEnvs.contains(runEnv)) {
			throw new IllegalArgumentException("Unsupport run environment: " + runEnv);
		}
	}

	public static String getDBEnvironment() {
		return dbEnvironment;
	}

	public static String getDBEnvironment(String runEnv) {
		if (ENV_REAL.equals(runEnv)) {
			return ENV_REAL;
		} else if (ENV_TEST.equals(runEnv)) {
			return ENV_TEST;
		} else {
			return ENV_DEV;
		}
	}

	public static String getDistributionPhase() {
		return distPhase;
	}

	public static String getRunEnvironment() {
		return runEnv;
	}

	public static boolean isRealDB() {
		return ENV_REAL.equals(dbEnvironment);
	}
}
