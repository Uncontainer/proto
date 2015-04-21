package com.pulsarang.infra.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.infra.InfraConfiguration;
import com.pulsarang.infra.config.category.ConfigCategory;
import com.pulsarang.infra.remote.RemoteContext;

/**
 * 
 * @author pulsarang
 */
public class ConfigContext {
	public static final String TICKET_STATUS_STOP = "0";
	public static final String LAST_APPLIED = "last_applied";

	private final Logger log = LoggerFactory.getLogger(ConfigContext.class);

	private final ConfigApiService configApi;
	private final ConfigCache configCache;
	private final ConfigListenerContext listenerContext;

	public ConfigContext(RemoteContext remoteContext, InfraConfiguration infraConfiguration) {
		configApi = new FailoverableConfigApiService(infraConfiguration.getTicketBackupPath());

		configCache = new ConfigCache(configApi);
		listenerContext = new ConfigListenerContext();

		remoteContext.setRemoteService(configCache);
		remoteContext.setRemoteService(listenerContext);
	}

	@SuppressWarnings("unchecked")
	public <T extends Config> T get(String categoryName, String name) {
		return (T) get(new ConfigId(categoryName, name));
	}

	public <T extends Config> T get(ConfigId configId) {
		@SuppressWarnings("unchecked")
		//
		T config = (T) configCache.get(configId);

		if (config != null) {
			log.debug("[PIC] config({}) is from infra-config", configId);
		}

		return config;
	}

	public Config getSafely(ConfigId configId) {
		Config config = get(configId);
		if (config == null) {
			throw new IllegalArgumentException("Ticket is not found in config: " + configId);
		}
		return config;
	}

	public List<ConfigId> getConfigIds(ConfigCategory category) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T extends Config> List<T> listByCategory(ConfigCategory category) {
		// TODO Auto-generated method stub
		return null;
	}

	public Config add(Config config) {
		return configApi.add(config);
	}

	public Config set(ConfigId configId, Config config) {
		// TODO Auto-generated method stub
		return null;
	}

	public void remove(ConfigId configId) {
		// TODO Auto-generated method stub
	}

	public void refresh(ConfigId configId) {
		// TODO Auto-generated method stub
	}

	public void expire(ConfigId configId) {
		configCache.remove(configId);
	}

	public void expireAll() {
		configCache.clear();
	}

	public void addListener(ConfigListener listener) {
		List<ConfigId> configIds = listener.getIds();
		if (configIds == null) {
			log.warn("[PIC] Config listener does not specify configId.({})", listener.getClass().getCanonicalName());
			return;
		}

		for (ConfigId configId : configIds) {
			listenerContext.addListener(configId, listener);
		}
	}

	public List<ConfigListener> getListeners(ConfigId configId) {
		return listenerContext.getListeners(configId);
	}

	public List<ConfigListener> getAllListeners(ConfigId configId) {
		return listenerContext.getAllListeners(configId);
	}

	public void removeListener(ConfigId configId, ConfigListener listener) {
		listenerContext.removeListener(configId, listener);
	}
}
