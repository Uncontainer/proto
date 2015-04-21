package com.pulsarang.infra.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.InfraParameters;
import com.pulsarang.infra.remote.annotation.RemoteService;
import com.pulsarang.infra.remote.reload.AbstractReloadService;

/**
 * 
 * @author pulsarang
 */
@RemoteService(name = ConfigCache.NAME)
public class ConfigCache extends AbstractReloadService {
	public static final String NAME = "__config";

	private final ConfigApiService configApi;

	final ConcurrentHashMap<ConfigId, ConfigHolder> configHolderMap;

	public ConfigCache(ConfigApiService configApi) {
		this.configApi = configApi;
		this.configHolderMap = new ConcurrentHashMap<ConfigId, ConfigHolder>();
	}

	public Config get(ConfigId configId) {
		ConfigHolder holder = configHolderMap.get(configId);
		if (holder == null) {
			holder = new ConfigHolder(configId, configApi);

			ConfigHolder temp = configHolderMap.putIfAbsent(configId, holder);
			if (temp != null) {
				holder = temp;
			}
		}

		return holder.get();
	}

	public void remove(ConfigId key) {
		configHolderMap.remove(key);
	}

	public void clear() {
		configHolderMap.clear();
	}

	@Override
	public List<MapModel> list(MapModel option) {
		List<MapModel> result = new ArrayList<MapModel>();
		for (Map.Entry<ConfigId, ConfigHolder> entry : configHolderMap.entrySet()) {
			ConfigId configId = entry.getKey();

			MapModel item = new MapModel();
			item.setString(InfraParameters.CATEGORY, configId.getCategoryName());
			item.setString(InfraParameters.TICKET, configId.getName());
			Config config = entry.getValue().get();
			if (config != null) {
				item.setProperty("properties", config.getProperties());
			}

			result.add(item);
		}
		return result;
	}

	@Override
	public int listCount(MapModel option) {
		return configHolderMap.size();
	}

	@Override
	public MapModel get(MapModel option) {
		ConfigId key = createKey(option);
		ConfigHolder holder = configHolderMap.get(key);
		if (holder == null || holder.get() == null) {
			throw new IllegalArgumentException("Config does not exist.(" + key + ")");
		}

		return holder.get();
	}

	@Override
	public void refresh(MapModel option) {
		ConfigId key = createKey(option);
		ConfigHolder holder = configHolderMap.get(key);
		if (holder == null) {
			return;
		}

		holder.refresh();
	}

	@Override
	public void validate(MapModel option) {
		ConfigId configId = createKey(option);

		// TODO MapModel을 그대로 사용할 수 있을까...
		Config config = Config.fromMap(option.getProperties(), configId.getConfigClass());

		ConfigListenerUtils.validate(configId, config);
	}

	private ConfigId createKey(MapModel option) {
		String categoryName = option.getString(InfraParameters.CATEGORY);
		String name = option.getString(InfraParameters.TICKET);

		return new ConfigId(categoryName, name);
	}
}
