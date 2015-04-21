package com.naver.mage4j.external.varien.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.naver.mage4j.external.varien.Varien_Exception;
import com.naver.mage4j.external.zend.cache.Zend_Cache_Core;
import com.naver.mage4j.external.zend.cache.Zend_Cache_Exception;

public class Varien_Cache_Core extends Zend_Cache_Core {
	/**
	 * Used to tell chunked data from ordinary
	 */
	public static final String CODE_WORD = "{splitted}";

	public Varien_Cache_Core(Map<String, Object> options) {
		super(options);

		_specificOptions.put("slab_size", 0);

		if (!(getOption("slab_size") instanceof Number)) {
			throw new Varien_Exception("Invalid value for the node <slab_size>. Expected to be integer.");
		}
	}

	public Object load(String id) {
		return load(id, false, false);
	}

	/**
	 * Load data from cached, glue from several chunks if it was splitted upon save.
	 *
	 * @param  string  $id                     Cache id
	 * @param  boolean $doNotTestCacheValidity If set to true, the cache validity won't be tested
	 * @param  boolean $doNotUnserialize       Do not serialize (even if automatic_serialization is true) => for internal use
	 * @return mixed|false Cached datas
	 */
	@Override
	public Object load(final String id, boolean doNotTestCacheValidity, boolean doNotUnserialize) {
		Object data = super.load(id, doNotTestCacheValidity, doNotUnserialize);

		if (data instanceof String && ((String)data).startsWith(CODE_WORD)) {
			// Seems we've got chunked data
			String[] arr = ((String)data).split("|");
			String chunks = arr.length > 2 ? arr[1] : null/*false*/;
			List<Object> chunkData = new ArrayList<Object>();

			if (chunks != null && NumberUtils.isDigits(chunks)) {
				for (int i = 0, chunkCount = Integer.valueOf(chunks); i < chunkCount; i++) {
					Object chunk = super.load(_getChunkId(id, i), doNotTestCacheValidity, doNotUnserialize);

					if (chunk == null) {
						// Some chunk in chain was not found, we can not glue-up the data:
						// clean the mess and return nothing

						_cleanTheMess(id, chunkCount);
						return null;
					}

					chunkData.add(chunk);
				}

				return chunkData;
			}
		}

		// Data has not been splitted to chunks on save
		return data;
	}

	/**
	 * Clean cache entries
	 *
	 * Available modes are :
	 * 'all' (default)  => remove all cache entries ($tags is not used)
	 * 'old'            => remove too old cache entries ($tags is not used)
	 * 'matchingTag'    => remove cache entries matching all given tags
	 *                     ($tags can be an array of strings or a single string)
	 * 'notMatchingTag' => remove cache entries not matching one of the given tags
	 *                     ($tags can be an array of strings or a single string)
	 * 'matchingAnyTag' => remove cache entries matching any given tags
	 *                     ($tags can be an array of strings or a single string)
	 *
	 * @param  string       $mode
	 * @param  array|string $tags
	 * @throws Zend_Cache_Exception
	 * @return boolean True if ok
	 */
	@Override
	public boolean clean(String mode, List<String> tags) {
		return super.clean(mode, _tags(tags));
	}

	public boolean save(Object data) {
		return save(data, null, Collections.<String> emptyList(), -1, 8);
	}

	/**
	 * Save some data in a cache
	 *
	 * @param  mixed $data           Data to put in cache (can be another type than string if automatic_serialization is on)
	 * @param  string $id             Cache id (if not set, the last cache id will be used)
	 * @param  array $tags           Cache tags
	 * @param bool|int $specificLifetime If != false, set a specific lifetime for this cache record (null => infinite lifetime)
	 * @param  int $priority         integer between 0 (very low priority) and 10 (maximum priority) used by some particular backends
	 * @return boolean True if no problem
	 */
	@Override
	public boolean save(Object data, String id, List<String> tags, int specificLifetimeInSeconds, int priority) {
		tags = _tags(tags);

		Integer slabSize = (Integer)getOption("slab_size");
		if (slabSize != null && data instanceof String && ((String)data).length() > slabSize) {
			String str = (String)data;
			int dataBeginIndex = 0;
			int chunkIndex = 0;
			while (dataBeginIndex < str.length()) {
				String chunkId = _getChunkId(id, chunkIndex);
				int dataEndIndex = dataBeginIndex + slabSize;
				if (dataEndIndex > str.length()) {
					dataEndIndex = str.length();
				}

				String chunk = str.substring(dataBeginIndex, dataEndIndex);
				if (!super.save(chunk, chunkId, tags, specificLifetimeInSeconds, priority)) {
					_cleanTheMess(id, chunkIndex + 1);
					return false;
				}

				chunkIndex++;
				dataBeginIndex = dataEndIndex;
			}

			data = CODE_WORD + '|' + chunkIndex;
		}

		return super.save(data, id, tags, specificLifetimeInSeconds, priority);
	}

	/**
	 * Returns ID of a specific chunk on the basis of data's ID
	 *
	 * @param string $id    Main data's ID
	 * @param int    $index Particular chunk number to return ID for
	 * @return string
	 */
	protected String _getChunkId(String id, int index) {
		return id + "[" + index + "]";
	}

	/**
	 * Remove saved chunks in case something gone wrong (e.g. some chunk from the chain can not be found)
	 *
	 * @param string $id     ID of data's info cell
	 * @param int    $chunks Number of chunks to remove (basically, the number after '{splitted}|')
	 * @return null
	 */
	protected void _cleanTheMess(String id, int chunkCount) {
		for (int i = 0; i < chunkCount; i++) {
			remove(_getChunkId(id, i));
		}

		remove(id);
	}

	/**
	 * Prepare tags
	 *
	 * @param array $tags
	 * @return array
	 */
	protected List<String> _tags(List<String> tags) {
		if (tags == null) {
			return null;
		}

		List<String> result = new ArrayList<String>();
		for (String tag : tags) {
			result.add(_id(tag));
		}

		return result;
	}

	/**
	 * Make and return a cache id
	*
	* Checks 'cache_id_prefix' and returns new id with prefix or simply the id if null
	*
	* @param  string $id Cache id
	* @return string Cache id (with or without prefix)
	*/
	@Override
	protected String _id(String id) {
		if (id != null) {
			id = id.replaceAll("([^a-zA-Z0-9_]{1,1})", "_");

			String cacheIdPrefix = (String)_options.get("cache_id_prefix");
			if (cacheIdPrefix != null) {
				id = cacheIdPrefix + id;
			}
		}

		return id;
	}
}
