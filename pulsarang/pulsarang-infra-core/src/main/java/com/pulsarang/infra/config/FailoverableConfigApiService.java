package com.pulsarang.infra.config;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.core.util.MapModelFileUtils;
import com.pulsarang.infra.config.category.ConfigCategory;
import com.pulsarang.infra.remote.RemoteServiceProxy;
import com.pulsarang.infra.server.SolutionProjectPair;

/**
 * @author pulsarang
 */
public class FailoverableConfigApiService implements ConfigApiService {
	private static Logger log = LoggerFactory.getLogger(FailoverableConfigApiService.class);

	private ConfigApiService api;

	private final File configBackupDir;

	public FailoverableConfigApiService(String path) {
		configBackupDir = new File(path);
		if (!configBackupDir.exists()) {
			configBackupDir.mkdirs();
		}

		// TODO 서버 설정...
		api = RemoteServiceProxy.newProjectProxy(ConfigApiService.class, ConfigApiService.NAME, SolutionProjectPair.INFRA_ADMIN, 3000);
	}

	@Override
	public Config get(ConfigId configId) {
		File file = getBackupFile(configId + ".txt");
		Config config = null;
		try {
			config = api.get(configId);
		} catch (Exception e) {
			log.warn("[PIC] Fail to load remote config.(" + configId + ")", e);
			return (Config) MapModelFileUtils.loadItem(file, configId.getConfigClass());
		}

		MapModelFileUtils.saveItem(file, config);

		return config;
	}

	@Override
	public Config add(Config config) {
		Config newConfig = api.add(config);

		File file = getBackupFile(config.getConfigId() + ".txt");
		MapModelFileUtils.saveItem(file, newConfig);

		return newConfig;
	}

	@Override
	public Config set(Config config) {
		Config newConfig = api.set(config);

		File file = getBackupFile(config.getConfigId() + ".txt");
		MapModelFileUtils.saveItem(file, newConfig);

		return newConfig;
	}

	@Override
	public void remove(ConfigId configId) {
		api.remove(configId);
	}

	@Override
	public List<ConfigId> getIds(ConfigCategory category) {
		return api.getIds(category);
	}

	@Override
	public <T extends Config> List<T> listByCategory(ConfigCategory category) {
		return api.listByCategory(category);
	}

	@Override
	public void refresh(ConfigId configId) {
		api.refresh(configId);
	}

	private File getBackupFile(String fileName) {
		return new File(configBackupDir, fileName);
	}
}
