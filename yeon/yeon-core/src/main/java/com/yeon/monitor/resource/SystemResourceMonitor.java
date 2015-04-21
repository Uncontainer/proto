package com.yeon.monitor.resource;

import com.yeon.YeonContext;
import com.yeon.remote.RemoteContext;
import com.yeon.remote.reload.AbstractReloadService;
import com.yeon.util.MapModel;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 
 * @author pulsarang
 */
public class SystemResourceMonitor extends AbstractReloadService {
	public static final String NAME = "SYSTEM_MONITOR";

	private static final SystemResourceMonitor INSTANCE = new SystemResourceMonitor();

	public static SystemResourceMonitor getInstance() {
		return INSTANCE;
	}

	/**
	 * {@link MomConfigContext}에 의해 초기화.
	 */
	private SystemResourceMonitor() {
		super();

		// MomMBeanFactory 초기화
		MomMBeanFactory.getInstance();

		RemoteContext remoteContext = YeonContext.getRemoteContext();

		remoteContext.setRemoteService(NAME, this);
		remoteContext.setRemoteService(SystemMonitoringItemMetaInfoProvider.NAME, new SystemMonitoringItemMetaInfoProvider());
	}

	public Object getProperty(String beanName, String propertyName) {
		return MomMBeanFactory.getInstance().getProperty(beanName, propertyName);
	}

	@Override
	public MapModel get(MapModel optionMap) {
		String propertyName = (String) optionMap.get("propertyName");
		if (propertyName == null) {
			return null;
		}

		String[] tokens = propertyName.split("\\.");
		if (tokens == null || tokens.length < 2) {
			throw new IllegalArgumentException("Invalid monitorig property name '" + propertyName + "'. Expect {beanName}.{propertyName}");
		}

		Object value = getProperty(tokens[0], tokens[1]);
		if (tokens.length > 2) {
			for (int i = 2; i < tokens.length; i++) {
				if (value instanceof Map) {
					value = ((Map<?, ?>) value).get(tokens[i]);
				} else {
					String path = StringUtils.join(tokens, ".");
					throw new IllegalArgumentException("'" + path + "' is not a map");
				}
			}
		}

		return wrapProperty(value);
	}

	public static MapModel wrapProperty(Object value) {
		MapModel map = new MapModel(2);
		map.put("property", value);

		return map;
	}

	public static Object unwrapProperty(Map<String, Object> map) {
		if (map == null) {
			return null;
		}

		return map.get("property");
	}
}
