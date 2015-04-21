package com.naver.mage4j.core.mage.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.mage4j.core.mage.core.Mage_Core_Model_App;
import com.naver.mage4j.core.mage.core.model.resource.Mage_Core_Model_Resource_Cache;
import com.naver.mage4j.external.php.Standard;
import com.naver.mage4j.external.varien.cache.Varien_Cache_Core;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;
import com.naver.mage4j.external.zend.Zend_Cache;
import com.naver.mage4j.external.zend.cache.CacheBackendInterface;

@Component
public class Mage_Core_Model_Cache implements CacheBackendInterface, InitializingBean {
	/**
	 * Cache settings
	 */
	public static final int DEFAULT_LIFETIME = 7200;
	public static final String OPTIONS_CACHE_ID = "core_cache_options";
	public static final String INVALIDATED_TYPES = "core_cache_invalidate";
	public static final String XML_PATH_TYPES = "global/cache/types";

	@Autowired
	private Mage_Core_Model_Resource_Cache mageCoreModelResourceCache;

	@Autowired
	private Mage_Core_Model_Config coreModelConfig;

	/**
	* List of available request processors
	*/
	private List<Object> _requestProcessors;

	/**
	 * Default cache backend type
	 */
	private final String _defaultBackend = "File";

	/**
	 * Default options for default backend
	 */
	private Map<String, Object> _defaultBackendOptions;

	private String _idPrefix = "";

	/**
	 * Cache frontend API
	 */
	private Varien_Cache_Core _frontend;

	/**
	 * List of allowed cache options
	 */
	private Map<String, Short> _allowedCacheOptions;

	/**
	 * Disallow cache saving
	 */
	private boolean _disallowSave = false;

	/**
	* Class constructor. Initialize cache instance based on options
	*/
	public Mage_Core_Model_Cache() {
		// TODO 용도 및 생성자 호출 부분 비교하여 처리.
		_defaultBackendOptions = new HashMap<String, Object>();
		_defaultBackendOptions.put("hashed_directory_level", 1);
		_defaultBackendOptions.put("hashed_directory_perm", 0777);
		_defaultBackendOptions.put("file_name_prefix", "mage");
	}

	@Override
	public void afterPropertiesSet(/*Map<String, Object> cacheInitOptions*/) throws Exception {
		Simplexml_Element config = coreModelConfig.selectDescendant("global/cache");
		Map<String, Object> options = config.isNull() ? new HashMap<String, Object>() : config.toMap();

		String cacheDir = (String)options.get("cache_dir");
		_defaultBackendOptions.put("cache_dir", cacheDir != null ? cacheDir : coreModelConfig.getBaseDir("cache"));
		/**
		 * Initialize id prefix
		 */
		_idPrefix = MapUtils.getString(options, "id_prefix", "");
		if (_idPrefix.isEmpty()) {
			_idPrefix = MapUtils.getString(options, "prefix", "");
		}

		if (_idPrefix.isEmpty()) {
			_idPrefix = Standard.md5(coreModelConfig.getOptions().getEtcDir()).substring(0, 3) + "_";
		}

		Map<String, Object> backend = _getBackendOptions(options);
		Map<String, Object> frontend = _getFrontendOptions(options);
		_frontend = Zend_Cache.factory("Varien_Cache_Core", (String)backend.get("type"), frontend, (Map)backend.get("options"), true, true, true);

		_requestProcessors = (List)options.get("request_processors");
		if (_requestProcessors == null) {
			_requestProcessors = new ArrayList<Object>();
		}
		_disallowSave = MapUtils.getBooleanValue(options, "disallow_save", false);
	}

	/**
	 * Get cache backend options. Result array contain backend type ('type' key) and backend options ('options')
	 *
	 * @param   array $cacheOptions
	 * @return  array
	 */
	private Map<String, Object> _getBackendOptions(Map<String, Object> cacheOptions) {
		//        boolean enable2levels = false;
		//        String type = MapUtils.getString(cacheOptions, "backend", _defaultBackend);
		//        
		//        Object temp = cacheOptions.get("backend_options");
		//        Map<String, Object> option = temp instanceof Map ? (Map)temp : new HashMap<String, Object>();
		//
		//        String backendType = null;
		//        type = type.toLowerCase();
		//        if("sqlite".equals(type)) {
		//            if (extension_loaded("sqlite") && isset($options["cache_db_complete_path"])) {
		//                $backendType = "Sqlite";
		//            }
		//        } else if("memcached".equals(type)) {
		//        	if (extension_loaded("memcached")) {
		//        		if (isset($cacheOptions["memcached"])) {
		//        			$options = $cacheOptions["memcached"];
		//        		}
		//        		$enable2levels = true;
		//        		$backendType = "Libmemcached";
		//        	} elseif (extension_loaded("memcache")) {
		//        		if (isset($cacheOptions["memcached"])) {
		//        			$options = $cacheOptions["memcached"];
		//        		}
		//        		$enable2levels = true;
		//        		$backendType = "Memcached";
		//        	}
		//        } else if("apc".equals(type)) {
		//        	if (extension_loaded("apc") && ini_get("apc.enabled")) {
		//        		$enable2levels = true;
		//        		$backendType = "Apc";
		//        	}
		//        } else if("xcache".equals(type)) {
		//        	if (extension_loaded("xcache")) {
		//        		$enable2levels = true;
		//        		$backendType = "Xcache";
		//        	}
		//        }else if("eaccelerator".equals(type) || "varien_cache_backend_eaccelerator".equals(type)) {
		//        	if (extension_loaded("eaccelerator") && ini_get("eaccelerator.enable")) {
		//        		$enable2levels = true;
		//        		$backendType = "Varien_Cache_Backend_Eaccelerator";
		//        	}
		//        }else if("varien_cache_backend_database".equals(type) || "database".equals(type)) {
		//        	$backendType = "Varien_Cache_Backend_Database";
		//        	$options = $this->getDbAdapterOptions($options);
		//        } else {
		//            if (!type.equals(_defaultBackend)) {
		//                try {
		//                    if (class_exists($type, true)) {
		//                        $implements = class_implements($type, true);
		//                        if (in_array("Zend_Cache_Backend_Interface", $implements)) {
		//                            $backendType = $type;
		//                            if (isset($options["enable_two_levels"])) {
		//                                $enable2levels = true;
		//                            }
		//                        }
		//                    }
		//                } catch (Exception $e) {
		//                }
		//            }
		//        }
		//
		//        if (backendType == null) {
		//            backendType = _defaultBackend;
		//            foreach ($this->_defaultBackendOptions as $option => $value) {
		//                if (!array_key_exists($option, $options)) {
		//                    $options[$option] = $value;
		//                }
		//            }
		//        }
		//
		//        $backendOptions = array("type" => $backendType, "options" => $options);
		//        if (enable2levels) {
		//            $backendOptions = $this->_getTwoLevelsBackendOptions($backendOptions, $cacheOptions);
		//        }
		//        return $backendOptions;
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("options", new HashMap<String, Object>());
		return result;
	}

	/**
	 * Get options of cache frontend (options of Zend_Cache_Core)
	 *
	 * @param   array $cacheOptions
	 * @return  array
	 */
	private Map<String, Object> _getFrontendOptions(Map<String, Object> cacheOptions) {
		Map<String, Object> options = (Map)cacheOptions.get("frontend_options");
		if (options == null) {
			options = new HashMap<String, Object>();
		}

		if (options.containsKey("caching")) {
			options.put("caching", true);
		}

		if (!options.containsKey("lifetime")) {
			options.put("lifetime", MapUtils.getInteger(cacheOptions, "lifetime", DEFAULT_LIFETIME));
		}

		if (!options.containsKey("automatic_cleaning_factor")) {
			options.put("automatic_cleaning_factor", 0);
		}

		options.put("cache_id_prefix", _idPrefix);

		return options;
	}

	/**
	 * Try to get response body from cache storage with predefined processors
	 *
	 * @return bool
	 */
	public boolean processRequest() {
		if (_requestProcessors.isEmpty()) {
			return false;
		}

		//        $content = false;
		//        foreach ($this->_requestProcessors as $processor) {
		//            $processor = $this->_getProcessor($processor);
		//            if ($processor) {
		//                $content = $processor->extractContent($content);
		//            }
		//        }
		//
		//        if ($content) {
		//            Mage::app()->getResponse()->appendBody($content);
		//            return true;
		//        }
		return false;
	}

	/**
	 * Check if cache can be used for specific data type
	 *
	 * @param string $typeCode
	 * @return bool
	 */
	public boolean canUse(String typeCode) {
		if (_allowedCacheOptions == null) {
			_initOptions();
		}

		if (StringUtils.isBlank(typeCode)) {
			return !_allowedCacheOptions.isEmpty();
		}

		return MapUtils.getBoolean(_allowedCacheOptions, typeCode, false);
	}

	/**
	 * Initialize cache types options
	 *
	 * @return Mage_Core_Model_Cache
	 */
	private Mage_Core_Model_Cache _initOptions() {
		Map<String, Short> options = (Map)load(OPTIONS_CACHE_ID);
		if (options == null/* || options == Boolean.FALSE*/) {
			options = mageCoreModelResourceCache.getAllOptions();
			if (options == null) {
				options = new HashMap<String, Short>();
			}
		}

		if ("true".equalsIgnoreCase((String)coreModelConfig.getOptions().getData("global_ban_use_cache"))) {
			for (Entry<String, Short> each : options.entrySet()) {
				options.put(each.getKey(), null);
			}
		}

		_allowedCacheOptions = options;

		return this;
	}

	/**
	 * Save data
	 *
	 * @param string $data
	 * @param string $id
	 * @param array $tags
	 * @param int $lifeTime
	 * @return bool
	 */
	@Override
	public boolean save(Object data, String id, List<String> tags, int lifeTimeInSeconds) {
		if (_disallowSave) {
			return true;
		}

		/**
		 * Add global magento cache tag to all cached data exclude config cache
		 */
		if (!tags.contains(Mage_Core_Model_Config.CACHE_TAG)) {
			tags.add(Mage_Core_Model_App.CACHE_TAG);
		}

		return getFrontend().save(data, _id(id), _tags(tags), lifeTimeInSeconds, 8);
	}

	public boolean save(Object data, String id) {
		return save(data, id, Collections.<String> emptyList(), -1);
	}

	/**
	 * Remove cached data by identifier
	 *
	 * @param   string $id
	 * @return  bool
	 */
	@Override
	public boolean remove(String id) {
		return getFrontend().remove(_id(id));
	}

	/**
	 * Clean cached data by specific tag
	 *
	 * @param   array $tags
	 * @return  bool
	 */
	public boolean clean(List<String> tags) {
		String mode = Zend_Cache.CLEANING_MODE_MATCHING_ANY_TAG;
		boolean res;
		if (tags != null && tags.isEmpty()) {
			res = getFrontend().clean(mode, _tags(tags));
		} else {
			res = getFrontend().clean(mode, Arrays.asList(Mage_Core_Model_App.CACHE_TAG));
			res = res && getFrontend().clean(mode, Arrays.asList(Mage_Core_Model_Config.CACHE_TAG));
		}

		return res;
	}

	/**
	 * Clean cached data by specific tag
	 *
	 * @return  bool
	 */
	public boolean flush() {
		return getFrontend().clean(null, null);
	}

	/**
	 * Prepare cache tags.
	 *
	 * @param   array $tags
	 * @return  array
	 */
	private List<String> _tags(List<String> tags) {
		List<String> result = new ArrayList<String>(tags.size());

		for (String tag : tags) {
			result.add(_id(tag));
		}

		return result;
	}

	/**
	 * Load data from cache by id
	 *
	 * @param   string $id
	 * @return  string
	 */
	public Object load(String id) {
		return load(id, false);
	}

	@Override
	public Object load(String id, boolean doNotTestCacheValidity) {
		return getFrontend().load(_id(id), doNotTestCacheValidity, false);
	}

	/**
	 * Get cache frontend API object
	 *
	 * @return Varien_Cache_Core
	 */
	public Varien_Cache_Core getFrontend() {
		return _frontend;
	}

	/**
	 * Prepare unified valid identifier with prefix
	 */
	private String _id(String id) {
		if (id != null) {
			id = id.toUpperCase();
		}

		return id;
	}
}
