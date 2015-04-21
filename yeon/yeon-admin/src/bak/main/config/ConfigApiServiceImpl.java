package com.pulsarang.infra.config;

import com.pulsarang.infra.InfraContext;
import com.pulsarang.infra.config.category.ConfigCategory;
import com.yeon.remote.annotation.RemoteService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RemoteService(name = ConfigApiService.NAME, publish = false)
public class ConfigApiServiceImpl implements ConfigApiService, InitializingBean {
	@Autowired
	private ConfigService configService;

	@Autowired
	private InfraContext infraContext;

	@Override
	public void afterPropertiesSet() throws Exception {
		infraContext.getRemoteContext().setRemoteService(this);
	}

	@Override
	public Config get(ConfigId configId) {
		ConfigEntityId configEntityId = new ConfigEntityId(configId);
		Map<String, Object> config = configService.get(configEntityId);
		if (config == null) {
			return null;
		}

		return Config.fromMap(config, Config.class);
	}

	@Override
	public Config add(Config config) {
		return configService.create(config);
	}

	@Override
	public Config set(Config config) {
		return configService.update(config);
	}

	@Override
	public void remove(ConfigId configId) {
		ConfigEntityId configEntityId = new ConfigEntityId(configId);
		configService.remove(configEntityId);
	}

	@Override
	public void refresh(ConfigId configId) {
		configService.refresh(new ConfigEntityId(configId));
	}

	@Override
	public List<ConfigId> getIds(ConfigCategory category) {
		return configService.listIdsByCategory(category);
	}

	@Override
	public List<Config> listByCategory(ConfigCategory category) {
		List<ConfigId> configIds = getIds(category);
		if (configIds.isEmpty()) {
			return Collections.emptyList();
		}

		List<Config> configs = new ArrayList<Config>(configIds.size());
		for (ConfigId configId : configIds) {
			Config config = get(configId);
			if (config != null) {
				configs.add(config);
			}
		}

		return configs;
	}
}
