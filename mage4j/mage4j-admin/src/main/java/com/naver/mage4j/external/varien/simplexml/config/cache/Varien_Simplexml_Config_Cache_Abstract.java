package com.naver.mage4j.external.varien.simplexml.config.cache;

import java.util.Collections;
import java.util.Map;

import com.naver.mage4j.external.varien.Varien_Object;

public abstract class Varien_Simplexml_Config_Cache_Abstract extends Varien_Object {
	public Varien_Simplexml_Config_Cache_Abstract() {
		this(Collections.<String, Object> emptyMap());
	}

	/**
	 * Constructor
	 * 
	 * Initializes components and allows to save the cache
	 *
	 * @param array $data
	 */
	public Varien_Simplexml_Config_Cache_Abstract(Map<String, Object> data) {
		super(data);

		//		setComponents(new HashMap<String, Object>());
		//		setIsAllowedToSave(true);
	}
}
