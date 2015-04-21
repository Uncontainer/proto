package com.pulsarang.infra.config;

import java.util.List;

/**
 * @author pulsarang
 */
public interface ConfigListener {
	List<ConfigId> getIds();

	void validate(ConfigId configId, Config config) throws Exception;

	void changed(ConfigId configId, Config config);
}
