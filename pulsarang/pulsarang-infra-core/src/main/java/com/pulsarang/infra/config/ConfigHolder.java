package com.pulsarang.infra.config;

import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ConfigHolder {
	private static Logger log = LoggerFactory.getLogger(ConfigHolder.class);

	private final ConfigId configId;
	private final ConfigApiService configApi;

	private final ReentrantLock lock = new ReentrantLock();

	private volatile boolean initialized = false;
	private volatile Config config;

	public ConfigHolder(ConfigId configId, ConfigApiService configApi) {
		this.configId = configId;
		this.configApi = configApi;
	}

	public Config get() {
		if (initialized) {
			return config;
		}

		lock.lock();
		try {
			if (initialized) {
				return config;
			}

			refresh();
		} finally {
			initialized = true;
			lock.unlock();
		}

		return config;
	}

	public void refresh() {
		Config newConfig = configApi.get(configId);

		// TODO 빈 내용의 config가 왔을 경우 config가 없는 것인지, 값이 없는 것인지 확인 필요.
		if (newConfig == null || newConfig.isEmpty()) {
			// 삭제되었을 경우에는 내부 properties를 null로 설정하여 property를 가져올 때 예외가
			// 발생하도록 함.
			if (config != null) {
				config.setPropertiesToNull();
			}
			return;
		}

		// newConfig.setConfigCategory(key.getConfigCategoryName());
		// newConfig.setConfigId(.getId());

		try {
			ConfigListenerUtils.validate(configId, newConfig.dup(configId.getConfigClass()));
		} catch (Exception e) {
			log.error("[PIC] Error loading config: " + newConfig, e);
			return;
		}

		Config completing = newConfig.dup(configId.getConfigClass());
		newConfig.put(ConfigContext.LAST_APPLIED, new Date());
		ConfigListenerUtils.changed(configId, completing);

		// TODO property만 교체해도 문제가 없는지 확인. class 변수로 선언하여 사용할 수 있음.
		if (config == null) {
			config = completing;
		} else {
			config.setProperties(completing.getProperties());
		}
	}
}
