package com.naver.mage4j.external.zend.cache;

import java.util.List;

public interface CacheBackendInterface {
	/**
	 * Test if a cache is available for the given id and (if yes) return it (false else)
	 *
	 * Note : return value is always "string" (unserialization is done by the core not by the backend)
	 *
	 * @param  string  $id                     Cache id
	 * @param  boolean $doNotTestCacheValidity If set to true, the cache validity won't be tested
	 * @return string|false cached datas
	 */
	Object load(String id, boolean doNotTestCacheValidity);

	/**
	 * Save some string datas into a cache record
	 *
	 * Note : $data is always "string" (serialization is done by the
	 * core not by the backend)
	 *
	 * @param  string $data            Datas to cache
	 * @param  string $id              Cache id
	 * @param  array $tags             Array of strings, the cache record will be tagged by each string entry
	 * @param  int   $specificLifetime If != false, set a specific lifetime for this cache record (null => infinite lifetime)
	 * @return boolean true if no problem
	 */
	boolean save(Object data, String id, List<String> tags, int specificLifetimeInSeconds);

	/**
	 * Remove a cache record
	 *
	 * @param  string $id Cache id
	 * @return boolean True if no problem
	 */
	boolean remove(String id);
}
