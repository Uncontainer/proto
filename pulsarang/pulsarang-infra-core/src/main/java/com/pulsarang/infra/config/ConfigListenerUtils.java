package com.pulsarang.infra.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pulsarang.infra.InfraContextFactory;

/**
 * 
 * @author pulsarang
 */
public class ConfigListenerUtils {
	private static final Logger log = LoggerFactory.getLogger(ConfigListenerUtils.class);

	public static void validate(ConfigId configId, Config config) {
		ConfigContext configContext = InfraContextFactory.getInfraContext().getConfigContext();
		List<ConfigListener> listeners = configContext.getAllListeners(configId);

		for (ConfigListener listener : listeners) {
			try {
				listener.validate(configId, config);
			} catch (Exception e) {
				// TODO 적당한 다른 예외로 감싸도록 수정.
				throw new RuntimeException(e);
			}
		}
	}

	public static void changed(ConfigId configId, Config config) {
		ConfigContext configContext = InfraContextFactory.getInfraContext().getConfigContext();
		List<ConfigListener> listeners = configContext.getAllListeners(configId);

		for (ConfigListener listener : listeners) {
			try {
				listener.changed(configId, config);
			} catch (Throwable t) {
				log.error("[PIC] Fail to apply some changed config. But, successful loaded.", t);
			}
		}
	}
}
