package com.pulsarang.infra.util;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.core.util.PeriodicTouchViolationChecker;
import com.pulsarang.infra.InfraContextFactory;

public class InfraAdminApiInvoker {
	private static final InfraAdminApiInvoker INSTANCE = new InfraAdminApiInvoker();

	final String baseUrl;
	final PeriodicTouchViolationChecker exceptionCountChecker;

	public static InfraAdminApiInvoker getInstance() {
		return INSTANCE;
	}

	private InfraAdminApiInvoker() {
		baseUrl = InfraContextFactory.getInfraContext().getInfraConfiguration().getInfraAdminAddress();
		// admin 죽었을 경우 최대 10초에 5번의 시도만 한다.
		exceptionCountChecker = new PeriodicTouchViolationChecker(5, TimeUnit.SECONDS.toMillis(10));
	}

	public String invoke(String path, MapModel model, int timeout) {
		checkAdminServerHealth();

		try {
			return ApiUtils.writeMapModel(baseUrl + path, model, timeout);
		} catch (IOException e) {
			exceptionCountChecker.touch();
			throw new RuntimeException(e);
		}
	}

	public <T extends MapModel> T invoke(String path, MapModel model, int timeout, Class<T> clazz) {
		String json = invoke(path, model, timeout);

		return MapModel.fromJson(json, clazz);
	}

	public <T extends MapModel> T get(String path, int timeout, Class<T> clazz) {
		checkAdminServerHealth();

		try {
			return ApiUtils.get(baseUrl + path, timeout, clazz);
		} catch (IOException e) {
			exceptionCountChecker.touch();
			throw new RuntimeException(e);
		}
	}

	public <T extends MapModel> List<T> list(String path, int timeout, Class<T> clazz) {
		checkAdminServerHealth();

		try {
			return ApiUtils.list(baseUrl + path, timeout, clazz);
		} catch (IOException e) {
			exceptionCountChecker.touch();
			throw new RuntimeException(e);
		}
	}

	private void checkAdminServerHealth() {
		if (exceptionCountChecker.isViolated()) {
			throw new IllegalStateException("InfraAdmin server is not running.");
		}
	}
}
