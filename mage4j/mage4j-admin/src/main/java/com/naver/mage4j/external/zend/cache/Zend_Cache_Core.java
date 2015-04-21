package com.naver.mage4j.external.zend.cache;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naver.mage4j.core.util.NameUtils;
import com.naver.mage4j.external.zend.Zend_Cache;
import com.naver.mage4j.external.zend.cache.backend.Zend_Cache_Backend_ExtendedInterface;
import com.naver.mage4j.external.zend.cache.backend.Zend_Cache_Backend_Interface;

public class Zend_Cache_Core {
	public static final Object NULL = new Object();

	private final Logger log = LoggerFactory.getLogger(Zend_Cache_Core.class);

	/**
	 * Backend Object
	 *
	 * @var Zend_Cache_Backend_Interface $_backend
	 */
	protected Zend_Cache_Backend_Interface _backend = null;

	/**
	 * Array of capabilities of the backend (only if it implements Zend_Cache_Backend_ExtendedInterface)
	 */
	protected Map<String, Object> _backendCapabilities = new HashMap<String, Object>();

	/**
	 * Array of options which have to be transfered to backend
	 *
	 * @var array $_directivesList
	 */
	protected static List<String> _directivesList = Arrays.asList("lifetime", "logging", "logger");

	/**
	 * Available options
	 *
	 * ====> (boolean) write_control :
	 * - Enable / disable write control (the cache is read just after writing to detect corrupt entries)
	 * - Enable write control will lightly slow the cache writing but not the cache reading
	 * Write control can detect some corrupt cache files but maybe it"s not a perfect control
	 *
	 * ====> (boolean) caching :
	 * - Enable / disable caching
	 * (can be very useful for the debug of cached scripts)
	 *
	 * =====> (string) cache_id_prefix :
	 * - prefix for cache ids (namespace)
	 *
	 * ====> (boolean) automatic_serialization :
	 * - Enable / disable automatic serialization
	 * - It can be used to save directly datas which aren"t strings (but it"s slower)
	 *
	 * ====> (int) automatic_cleaning_factor :
	 * - Disable / Tune the automatic cleaning process
	 * - The automatic cleaning process destroy too old (for the given life time)
	 *   cache files when a new cache file is written :
	 *     0               => no automatic cache cleaning
	 *     1               => systematic cache cleaning
	 *     x (integer) > 1 => automatic cleaning randomly 1 times on x cache write
	 *
	 * ====> (int) lifetime :
	 * - Cache lifetime (in seconds)
	 * - If null, the cache is valid forever.
	 *
	 * ====> (boolean) logging :
	 * - If set to true, logging is activated (but the system is slower)
	 *
	 * ====> (boolean) ignore_user_abort
	 * - If set to true, the core will set the ignore_user_abort PHP flag inside the
	 *   save() method to avoid cache corruptions in some cases (default false)
	 *
	 * @var array $_options available options
	 */
	protected Map<String, Object> _options;

	/**
	 * True if the backend implements Zend_Cache_Backend_ExtendedInterface
	 *
	 * @var boolean $_extendedBackend
	 */
	protected boolean _extendedBackend = false;

	/**
	* Not used for the core, just a sort a hint to get a common setOption() method (for the core and for frontends)
	*
	* @var array $_specificOptions
	*/
	protected Map<String, Object> _specificOptions = new HashMap<String, Object>();

	private Random random = new Random();

	public Zend_Cache_Core(Map<String, Object> options) {
		_options = new HashMap<String, Object>();
		_options.put("write_control", true);
		_options.put("caching", true);
		_options.put("cache_id_prefix", null);
		_options.put("automatic_serialization", false);
		_options.put("automatic_cleaning_factor", 10);
		_options.put("lifetime", 3600);
		_options.put("logging", false);
		_options.put("logger", null);
		_options.put("ignore_user_abort", false);

		for (Entry<String, Object> each : options.entrySet()) {
			setOption(each.getKey(), each.getValue());
		}
	}

	/**
	 * Public frontend to set an option
	 *
	 * There is an additional validation (relatively to the protected _setOption method)
	 *
	 * @param  string $name  Name of the option
	 * @param  mixed  $value Value of the option
	 * @throws Zend_Cache_Exception
	 * @return void
	 */
	public void setOption(String name, Object value) {
		name = name.toLowerCase();
		if (_options.containsKey(name)) {
			// This is a Core option
			_options.put(name, value);
			return;
		}

		if (_specificOptions.containsKey(name)) {
			// This a specic option of this frontend
			_specificOptions.put(name, value);
			return;
		}
	}

	/**
	 * Public frontend to get an option value
	 *
	 * @param  string $name  Name of the option
	 * @throws Zend_Cache_Exception
	 * @return mixed option value
	 */
	public Object getOption(String name) {
		name = name.toLowerCase();

		if (_options.containsKey(name)) {
			return _options.get(name);
		}

		if (_specificOptions.containsKey(name)) {
			return _specificOptions.get(name);
		}

		throw new Zend_Cache_Exception("Incorrect option name : " + name);
	}

	/**
	* Set the backend
	*
	* @param  Zend_Cache_Backend $backendObject
	* @throws Zend_Cache_Exception
	* @return void
	*/
	public void setBackend(Zend_Cache_Backend_Interface backendObject)
	{
		_backend = backendObject;
		// some options (listed in $_directivesList) have to be given
		// to the backend too (even if they are not "backend specific")
		Map<String, Object> directives = new HashMap<String, Object>();
		for (String name : Zend_Cache_Core._directivesList) {
			if (_options.containsKey(name)) {
				directives.put(name, _options.get(name));
			}
		}
		_backend.setDirectives(directives);

		if (backendObject instanceof Zend_Cache_Backend_ExtendedInterface) {
			_extendedBackend = true;
			_backendCapabilities = ((Zend_Cache_Backend_ExtendedInterface)_backend).getCapabilities();
		}
	}

	/**
	 * Save some data in a cache
	 *
	 * @param  mixed $data           Data to put in cache (can be another type than string if automatic_serialization is on)
	 * @param  string $id             Cache id (if not set, the last cache id will be used)
	 * @param  array $tags           Cache tags
	 * @param  int $specificLifetime If != false, set a specific lifetime for this cache record (null => infinite lifetime)
	 * @param  int   $priority         integer between 0 (very low priority) and 10 (maximum priority) used by some particular backends
	 * @throws Zend_Cache_Exception
	 * @return boolean True if no problem
	 */
	public boolean save(Object data, String id, List<String> tags, int specificLifetimeInSeconds, int priority) {
		if (!MapUtils.getBoolean(_options, "caching", true)) {
			return true;
		}

		if (id == null) {
			id = _lastId;
		} else {
			id = _id(id);
		}

		_validateIdOrTag(id);
		_validateTagsArray(tags);

		if (MapUtils.getBoolean(_options, "automatic_serialization", false)) {
			// we need to serialize datas before storing them
			throw new UnsupportedOperationException("Serialzation has not supported yet.");
			//            $data = serialize($data);
		} else {
			if (!(data instanceof String)) {
				throw new Zend_Cache_Exception("Datas must be string or set automatic_serialization = true");
			}
		}

		// automatic cleaning
		int automaticCleaningFactor = MapUtils.getInteger(_options, "automatic_cleaning_factor", 10);
		if (automaticCleaningFactor > 0) {
			if (random.nextInt(automaticCleaningFactor) == 1) {
				//                //  new way                 || deprecated way
				//                if (_extendedBackend || method_exists($this->_backend, 'isAutomaticCleaningAvailable')) {
				//                    $this->_log("Zend_Cache_Core::save(): automatic cleaning running", 7);
				//                    $this->clean(Zend_Cache::CLEANING_MODE_OLD);
				//                } else {
				//                    $this->_log("Zend_Cache_Core::save(): automatic cleaning is not available/necessary with current backend", 4);
				//                }
			}
		}

		log.debug("Zend_Cache_Core: save item '{}'", 7);
		//		Boolean ignoreUserAbort = MapUtils.getBoolean(_options, "ignore_user_abort", false);
		//		if (ignoreUserAbort) {
		//			$abort = ignore_user_abort(true);
		//		}
		boolean result;
		if (_extendedBackend && MapUtils.getBoolean(_backendCapabilities, "priority", false)) {
			result = _backend.save(data, id, tags, specificLifetimeInSeconds/*, priority*/);
		} else {
			result = _backend.save(data, id, tags, specificLifetimeInSeconds);
		}
		//		if (ignoreUserAbort) {
		//			ignore_user_abort($abort);
		//		}

		if (!result) {
			// maybe the cache is corrupted, so we remove it !
			log.warn("Zend_Cache_Core::save(): failed to save item '{}' -> removing it", id);
			_backend.remove(id);
			return false;
		}

		if (MapUtils.getBoolean(_options, "write_control", true)) {
			Object data2 = _backend.load(id, true);
			if (data != data2) {
				log.warn("Zend_Cache_Core::save(): write control of item '{}' failed -> removing it", id);
				_backend.remove(id);
				return false;
			}
		}

		return true;
	}

	/**
	 * Last used cache id
	 */
	private String _lastId = null;

	/**
	 * Test if a cache is available for the given id and (if yes) return it (false else)
	 *
	 * @param  string  $id                     Cache id
	 * @param  boolean $doNotTestCacheValidity If set to true, the cache validity won"t be tested
	 * @param  boolean $doNotUnserialize       Do not serialize (even if automatic_serialization is true) => for internal use
	 * @return mixed|false Cached datas
	 */
	public Object load(String id, boolean doNotTestCacheValidity, boolean doNotUnserialize) {
		if (!MapUtils.getBoolean(_options, "caching", true)) {
			return NULL;
		}

		id = _id(id); // cache id may need prefix
		_lastId = id;
		_validateIdOrTag(id);

		log.debug("Zend_Cache_Core: load item \"{}\"", id);

		Object data = _backend.load(id, doNotTestCacheValidity);
		if (data == null) {
			// no cache available
			return NULL;
		}

		//		if (!doNotUnserialize && MapUtils.getBoolean(_options, "automatic_serialization", true)) {
		//			// we need to unserialize before sending the result
		//			return unserialize($data);
		//		}

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
	public boolean clean(String mode, List<String> tags) {
		if (mode == null) {
			mode = Zend_Cache.CLEANING_MODE_ALL;
		}

		if (tags == null) {
			tags = Collections.emptyList();
		}

		if (!MapUtils.getBoolean(_options, "caching", false)) {
			return true;
		}

		if (!Arrays.asList(Zend_Cache.CLEANING_MODE_ALL,
			Zend_Cache.CLEANING_MODE_OLD,
			Zend_Cache.CLEANING_MODE_MATCHING_TAG,
			Zend_Cache.CLEANING_MODE_NOT_MATCHING_TAG,
			Zend_Cache.CLEANING_MODE_MATCHING_ANY_TAG).contains(mode)) {
			throw new Zend_Cache_Exception("Invalid cleaning mode");
		}

		_validateTagsArray(tags);

		return _backend.clean(mode, tags);
	}

	/**
	 * Remove a cache
	 *
	 * @param  string $id Cache id to remove
	 * @return boolean True if ok
	 */
	public boolean remove(String id) {
		if (!MapUtils.getBoolean(_options, "caching", true)) {
			return true;
		}

		id = _id(id); // cache id may need prefix
		_validateIdOrTag(id);

		log.debug("Zend_Cache_Core: remove item '{}'", id);

		return _backend.remove(id);
	}

	/**
	 * Validate a cache id or a tag (security, reliable filenames, reserved prefixes...)
	 *
	 * Throw an exception if a problem is found
	 *
	 * @throws Zend_Cache_Exception
	 */
	protected static void _validateIdOrTag(String string) {
		if (string.startsWith("internal-")) {
			throw new Zend_Cache_Exception("\"internal-*\" ids or tags are reserved");
		}

		if (NameUtils.isInvalidJavaIdentifier(string)) {
			throw new Zend_Cache_Exception("Invalid id or tag \"$string\" : must use only [a-zA-Z0-9_]");
		}
	}

	/**
	 * Validate a tags array (security, reliable filenames, reserved prefixes...)
	 *
	 * Throw an exception if a problem is found
	 *
	 * @param  array $tags Array of tags
	 * @throws Zend_Cache_Exception
	 * @return void
	 */
	protected static void _validateTagsArray(List<String> tags) {
		for (String tag : tags) {
			_validateIdOrTag(tag);
		}
	}

	/**
	 * Make and return a cache id
	 *
	 * Checks "cache_id_prefix" and returns new id with prefix or simply the id if null
	 *
	 * @param  string $id Cache id
	 * @return string Cache id (with or without prefix)
	 */
	protected String _id(String id) {
		if (id == null) {
			return id;
		}

		String cacheIdPrefix = (String)_options.get("cache_id_prefix");
		if (cacheIdPrefix != null) {
			return cacheIdPrefix + id; // return with prefix
		}

		return id; // no prefix, just return the $id passed
	}
}
