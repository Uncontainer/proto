package com.naver.mage4j.core.mage.core.model.cache;

import java.util.Map;

import com.naver.mage4j.external.varien.cache.Varien_Cache_Core;

/**
 * @see Varien_Cache_Core
 * 
 * @author Naver
 */
public class FrontendCache extends Varien_Cache_Core {
	public FrontendCache(Map<String, Object> options) {
		super(options);
	}

	@Override
	public String load(String id) {
		throw new UnsupportedOperationException();
	}
}
