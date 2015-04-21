package com.pulsarang.infra.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.pulsarang.core.util.MapModel;
import com.pulsarang.infra.remote.annotation.RemoteService;
import com.pulsarang.infra.remote.reload.AbstractReloadService;

/**
 * 
 * @author pulsarang
 */
@RemoteService(name = ConfigListenerContext.NAME)
public class ConfigListenerContext extends AbstractReloadService {
	public static final String NAME = "__config_init_listener";

	private final ConcurrentHashMap<String, List<ConfigListener>> listenersMap = new ConcurrentHashMap<String, List<ConfigListener>>();

	public void addListener(ConfigId configId, ConfigListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("Listener is empty.");
		}

		String key = getKey(configId);
		List<ConfigListener> listeners = listenersMap.get(key);
		if (listeners == null) {
			listeners = new CopyOnWriteArrayList<ConfigListener>();
			List<ConfigListener> existListeners = listenersMap.putIfAbsent(key, listeners);
			if (existListeners != null) {
				listeners = existListeners;
			}
		}

		listeners.add(listener);
	}

	public List<ConfigListener> getListeners(ConfigId configId) {
		List<ConfigListener> listeners = listenersMap.get(getKey(configId));
		if (listeners == null) {
			return Collections.emptyList();
		}

		return Collections.unmodifiableList(listeners);
	}

	public List<ConfigListener> getAllListeners(ConfigId configId) {
		List<ConfigListener> listeners = new ArrayList<ConfigListener>();

		String key = getKey(null, null);
		List<ConfigListener> solutionListeners = listenersMap.get(key);
		if (solutionListeners != null && !solutionListeners.isEmpty()) {
			listeners.addAll(solutionListeners);
		}

		if (configId.getCategoryName() != null) {
			key = getKey(configId.getCategoryName(), null);
			List<ConfigListener> categoryListeners = listenersMap.get(key);
			if (CollectionUtils.isNotEmpty(categoryListeners)) {
				listeners.addAll(categoryListeners);
			}

			if (StringUtils.isNotEmpty(configId.getName())) {
				key = getKey(configId);
				List<ConfigListener> configListeners = listenersMap.get(key);
				if (CollectionUtils.isNotEmpty(configListeners)) {
					listeners.addAll(configListeners);
				}
			}
		}

		return Collections.unmodifiableList(listeners);
	}

	public void removeListener(ConfigId configId, ConfigListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("Listener is empty.");
		}

		List<ConfigListener> listeners = listenersMap.get(getKey(configId));
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	@Override
	public List<MapModel> list(MapModel option) {
		List<MapModel> result = new ArrayList<MapModel>();
		for (Entry<String, List<ConfigListener>> entry : listenersMap.entrySet()) {
			for (ConfigListener listener : entry.getValue()) {
				MapModel item = new MapModel();
				item.setString("config", entry.getKey());
				item.setString("clazz", listener.toString());

				result.add(item);
			}
		}

		return result;
	}

	@Override
	public int listCount(MapModel option) {
		return listenersMap.size();
	}

	private static String getKey(ConfigId configId) {
		return getKey(configId.getName(), configId.getCategoryName());
	}

	private static String getKey(String name, String configCategoryName) {
		StringBuilder key = new StringBuilder();

		if (configCategoryName != null) {
			key.append(configCategoryName);
		}
		key.append(':');

		if (StringUtils.isNotEmpty(name)) {
			key.append(name);
		}

		return key.toString();
	}
}
