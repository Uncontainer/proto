package com.naver.mage4j.core.mage.core.model.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.mage4j.core.mage.core.model.resource.cache.CoreCacheOption;
import com.naver.mage4j.core.mage.core.model.resource.cache.CoreCacheOptionRepository;

@Component
public class Mage_Core_Model_Resource_Cache {
	@Autowired
	private CoreCacheOptionRepository coreCacheRepository;

	/**
	 * Get all cache options
	 *
	 * @return array | false
	 */
	public Map<String, Short> getAllOptions() {
		Map<String, Short> result = new HashMap<String, Short>();
		List<CoreCacheOption> options = coreCacheRepository.findAll();
		for (CoreCacheOption option : options) {
			result.put(option.getCode(), option.getValue());
		}

		return result;
	}
}
