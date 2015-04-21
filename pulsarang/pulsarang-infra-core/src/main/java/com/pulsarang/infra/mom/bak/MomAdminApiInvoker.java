package com.pulsarang.infra.mom.bak;

import java.io.IOException;
import java.util.List;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.core.util.PeriodicTouchViolationChecker;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.config.ConfigContext;
import com.pulsarang.infra.mom.MomConstants;
import com.pulsarang.infra.mom.MomInfo;
import com.pulsarang.infra.util.ApiUtils;

class MomAdminApiInvoker {
	private static final MomAdminApiInvoker INSTANCE = new MomAdminApiInvoker();

	final String baseUrl;
	final PeriodicTouchViolationChecker exceptionCountChecker;

	public static MomAdminApiInvoker getInstance() {
		return INSTANCE;
	}

	private MomAdminApiInvoker() {
		ConfigContext configContext = InfraContextFactory.getInfraContext().getConfigContext();
		MomInfo momInfo = configContext.get(MomInfo.CONFIGURATION_ID);
		baseUrl = momInfo.getServerAddress();
		exceptionCountChecker = new PeriodicTouchViolationChecker(MomConstants.ADMIN_CALL_FAIL_MAX_ALLOW_COUNT,
				MomConstants.ADMIN_CALL_FAIL_CHECK_DURATION);
	}

	public String invoke(String path, MapModel model, int timeout) {
		checkAdminServerHealth();

		try {
			return ApiUtils.writeMapModel(getRemoteUrl(path), model, timeout);
		} catch (IOException e) {
			exceptionCountChecker.touch();
			throw new RuntimeException("Fail to call '" + getRemoteUrl(path) + "'", e);
		}
	}

	public <T extends MapModel> T get(String path, int timeout, Class<T> clazz) {
		checkAdminServerHealth();

		try {
			return ApiUtils.get(getRemoteUrl(path), timeout, clazz);
		} catch (IOException e) {
			exceptionCountChecker.touch();
			throw new RuntimeException("Fail to call '" + getRemoteUrl(path) + "'", e);
		}
	}

	public <T extends MapModel> List<T> list(String path, int timeout, Class<T> clazz) {
		checkAdminServerHealth();

		try {
			return ApiUtils.list(getRemoteUrl(path), timeout, clazz);
		} catch (IOException e) {
			exceptionCountChecker.touch();
			throw new RuntimeException("Fail to call '" + getRemoteUrl(path) + "'", e);
		}
	}

	private void checkAdminServerHealth() {
		if (exceptionCountChecker.isViolated()) {
			throw new IllegalStateException("MomAdmin server is not running.");
		}
	}

	private String getRemoteUrl(String path) {
		return baseUrl + path;
	}
}
