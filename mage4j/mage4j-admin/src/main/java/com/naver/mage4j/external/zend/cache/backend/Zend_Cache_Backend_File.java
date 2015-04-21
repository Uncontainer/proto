package com.naver.mage4j.external.zend.cache.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naver.mage4j.core.util.JacksonUtil;
import com.naver.mage4j.external.php.Standard;
import com.naver.mage4j.external.zend.Zend_Cache;
import com.naver.mage4j.external.zend.cache.Zend_Cache_Backend;
import com.naver.mage4j.external.zend.cache.Zend_Cache_Exception;

public class Zend_Cache_Backend_File extends Zend_Cache_Backend implements Zend_Cache_Backend_ExtendedInterface {
	private final Logger log = LoggerFactory.getLogger(Zend_Cache_Backend_File.class);
	/**
	 * Available options
	 *
	 * =====> (string) cache_dir :
	 * - Directory where to put the cache files
	 *
	 * =====> (boolean) file_locking :
	 * - Enable / disable file_locking
	 * - Can avoid cache corruption under bad circumstances but it doesn"t work on multithread
	 * webservers and on NFS filesystems for example
	 *
	 * =====> (boolean) read_control :
	 * - Enable / disable read control
	 * - If enabled, a control key is embeded in cache file and this key is compared with the one
	 * calculated after the reading.
	 *
	 * =====> (string) read_control_type :
	 * - Type of read control (only if read control is enabled). Available values are :
	 *   "md5" for a md5 hash control (best but slowest)
	 *   "crc32" for a crc32 hash control (lightly less safe but faster, better choice)
	 *   "adler32" for an adler32 hash control (excellent choice too, faster than crc32)
	 *   "strlen" for a length only test (fastest)
	 *
	 * =====> (int) hashed_directory_level :
	 * - Hashed directory level
	 * - Set the hashed directory structure level. 0 means "no hashed directory
	 * structure", 1 means "one level of directory", 2 means "two levels"...
	 * This option can speed up the cache only when you have many thousands of
	 * cache file. Only specific benchs can help you to choose the perfect value
	 * for you. Maybe, 1 or 2 is a good start.
	 *
	 * =====> (int) hashed_directory_umask :
	 * - deprecated
	 * - Permissions for hashed directory structure
	 *
	 * =====> (int) hashed_directory_perm :
	 * - Permissions for hashed directory structure
	 *
	 * =====> (string) file_name_prefix :
	 * - prefix for cache files
	 * - be really carefull with this option because a too generic value in a system cache dir
	 *   (like /tmp) can cause disasters when cleaning the cache
	 *
	 * =====> (int) cache_file_umask :
	 * - deprecated
	 * - Permissions for cache files
	 *
	 * =====> (int) cache_file_perm :
	 * - Permissions for cache files
	 *
	 * =====> (int) metatadatas_array_max_size :
	 * - max size for the metadatas array (don"t change this value unless you
	 *   know what you are doing)
	 *
	 * @var array available options
	 */
	private Map<String, Object> _options = new HashMap<String, Object>();

	/**
	 * Array of metadatas (each item is an associative array)
	 *
	 * @var array
	 */
	private Map<String, Map<String, Object>> _metadatasArray;

	/**
	 * Constructor
	 *
	 * @param  array $options associative array of options
	 * @throws Zend_Cache_Exception
	 * @return void
	 */
	public Zend_Cache_Backend_File(Map<String, Object> options) {
		super(options);
		_options.put("cache_dir", null);
		_options.put("file_locking", true);
		_options.put("read_control", true);
		_options.put("read_control_type", "crc32");
		_options.put("hashed_directory_level", 0);
		_options.put("hashed_directory_perm", 0700);
		_options.put("file_name_prefix", "zend_cache");
		_options.put("cache_file_perm", 0600);
		_options.put("metadatas_array_max_size", 100);

		_cleanMetadatas();

		String cacheDir = (String)_options.get("cache_dir");
		if (cacheDir != null) { // particular case for this option					
			setCacheDir(cacheDir, true);
		} else {
			setCacheDir(getTmpDir() + "/", false);
		}

		String fileNamePrefix = (String)_options.get("file_name_prefix");
		if (fileNamePrefix != null) { // particular case for this option
			if (!Pattern.compile("^[a-zA-Z0-9_]+$").matcher(fileNamePrefix).find()) {
				throw new Zend_Cache_Exception("Invalid file_name_prefix : must use only [a-zA-Z0-9_]");
			}
		}

		if (MapUtils.getIntValue(_options, "metadatas_array_max_size", 0) < 10) {
			throw new Zend_Cache_Exception("Invalid metadatas_array_max_size, must be > 10");
		}

		String hashedDirectoryUmask = (String)options.get("hashed_directory_umask");
		if (hashedDirectoryUmask != null) {
			// See #ZF-12047
			log.error("'hashed_directory_umask' is deprecated -> please use 'hashed_directory_perm' instead");
			if (options.get("hashed_directory_perm") == null) {
				options.put("hashed_directory_perm", options.get(hashedDirectoryUmask));
			}
		}

		Object hashedDirectoryPerm = options.get("hashed_directory_perm");
		if (hashedDirectoryPerm instanceof String) {
			// See #ZF-4422
			_options.put("hashed_directory_perm", Integer.parseInt((String)hashedDirectoryPerm, 8));
		}

		String cacheFileUmask = (String)options.get("cache_file_umask");
		if (cacheFileUmask != null) {
			// See #ZF-12047
			log.error("'cache_file_umask' is deprecated -> please use 'cache_file_perm' instead");
			if (options.get("cache_file_perm") == null) {
				options.put("cache_file_perm", cacheFileUmask);
			}
		}

		Object cacheFilePerm = options.get("cache_file_perm");
		if (hashedDirectoryPerm instanceof String) {
			// See #ZF-4422
			_options.put("cache_file_perm", Integer.parseInt((String)cacheFilePerm, 8));
		}
	}

	@Override
	public void setDirectives(Map<String, Object> directives) {
		super.setDirectives(directives);
	}

	/**
	 * Set the cache_dir (particular case of setOption() method)
	 *
	 * @param  string  $value
	 * @param  boolean $trailingSeparator If true, add a trailing separator is necessary
	 * @throws Zend_Cache_Exception
	 * @return void
	 */
	public void setCacheDir(String value, boolean trailingSeparator) {
		File file = new File(value);
		if (!file.isDirectory()) {
			throw new Zend_Cache_Exception("cache_dir '" + value + "' must be a directory");
		}

		if (!file.canWrite()) {
			throw new Zend_Cache_Exception("cache_dir '" + value + "' is not writable");
		}

		if (trailingSeparator) {
			// add a trailing DIRECTORY_SEPARATOR if necessary
			value = file.getAbsolutePath() + "/";
		}

		_options.put("cache_dir", value);
	}

	/**
	 * Test if a cache is available for the given id and (if yes) return it (false else)
	 *
	 * @param string $id cache id
	 * @param boolean $doNotTestCacheValidity if set to true, the cache validity won"t be tested
	 * @return string|false cached datas
	 */
	@Override
	public Object load(String id, boolean doNotTestCacheValidity) {
		if (_test(id, doNotTestCacheValidity) == null) {
			// The cache is not hit !
			return null;
		}

		Map<String, Object> metadatas = _getMetadatas(id);
		File file = _file(id);
		Object data;
		try {
			data = _fileGetContents(file);
		} catch (Exception e) {
			log.warn("Fail to load file.({})", new Object[] {file, e});
			return null;
		}

		if (MapUtils.getBoolean(_options, "read_control", false)) {
			// TODO hash 값을 계산해서 가져오도록 구현 추가..
			//			String hashData = _hash(data, (String)_options.get("read_control_type"));
			String hashData = "1";
			String hashControl = (String)metadatas.get("hash");
			if (!hashData.equals(hashControl)) {
				// Problem detected by the read control !
				log.info("Zend_Cache_Backend_File::load() / read_control : stored hash and computed hash do not match");
				remove(id);
				return null;
			}
		}

		return data;
	}

	/**
	 * Test if a cache is available or not (for the given id)
	 *
	 * @param string $id cache id
	 * @return mixed false (a cache is not available) or "last modified" timestamp (int) of the available cache record
	 */
	@Override
	public Long test(String id) {
		//        clearstatcache();
		return _test(id, false);
	}

	/**
	 * Save some string datas into a cache record
	 *
	 * Note : $data is always "string" (serialization is done by the
	 * core not by the backend)
	 *
	 * @param  string $data             Datas to cache
	 * @param  string $id               Cache id
	 * @param  array  $tags             Array of strings, the cache record will be tagged by each string entry
	 * @param  int    $specificLifetime If != false, set a specific lifetime for this cache record (null => infinite lifetime)
	 * @return boolean true if no problem
	 */
	@Override
	public boolean save(Object data, String id, List<String> tags, int specificLifetimeInSeconds) {
		//        clearstatcache();
		File file = _file(id);
		String path = _path(id);
		File pathFile = new File(path);
		if (MapUtils.getInteger(_options, "hashed_directory_level", 0) > 0) {
			if (!pathFile.canWrite()) {
				// maybe, we just have to build the directory structure
				_recursiveMkdirAndChmod(id);
			}
			if (!pathFile.canWrite()) {
				return false;
			}
		}
		String hash;
		if (MapUtils.getBooleanValue(_options, "read_control", false)) {
			// TODO hash 값을 계산해서 가져오도록 구현 추가..
			//			hash = _hash(data, (String)_options.get("read_control_type"));
			hash = "1";
		} else {
			hash = "";
		}

		Map<String, Object> metadatas = new HashMap<String, Object>();
		metadatas.put("hash", hash);
		metadatas.put("mtime", System.currentTimeMillis());
		metadatas.put("expire", _expireTime(getLifetime(specificLifetimeInSeconds)));
		metadatas.put("tags", tags);

		boolean res = _setMetadatas(id, metadatas);
		if (!res) {
			log.warn("Zend_Cache_Backend_File::save() / error on saving metadata");
			return false;
		}

		return _filePutContents(file, data);
	}

	/**
	 * Remove a cache record
	 *
	 * @param  string $id cache id
	 * @return boolean true if no problem
	 */
	@Override
	public boolean remove(String id) {
		File file = _file(id);
		boolean boolRemove = _remove(file);
		boolean boolMetadata = _delMetadatas(id);
		return boolMetadata && boolRemove;
	}

	/**
	 * Clean some cache records
	 *
	 * Available modes are :
	 *
	 * Zend_Cache::CLEANING_MODE_ALL (default)    => remove all cache entries ($tags is not used)
	 * Zend_Cache::CLEANING_MODE_OLD              => remove too old cache entries ($tags is not used)
	 * Zend_Cache::CLEANING_MODE_MATCHING_TAG     => remove cache entries matching all given tags
	 *                                               ($tags can be an array of strings or a single string)
	 * Zend_Cache::CLEANING_MODE_NOT_MATCHING_TAG => remove cache entries not {matching one of the given tags}
	 *                                               ($tags can be an array of strings or a single string)
	 * Zend_Cache::CLEANING_MODE_MATCHING_ANY_TAG => remove cache entries matching any given tags
	 *                                               ($tags can be an array of strings or a single string)
	 *
	 * @param string $mode clean mode
	 * @param tags array $tags array of tags
	 * @return boolean true if no problem
	 */
	@Override
	public boolean clean(String mode, List<String> tags) {
		// We use this protected method to hide the recursive stuff
		//        clearstatcache();
		return _clean((String)_options.get("cache_dir"), mode, tags);
	}

	public boolean clean() {
		return clean(Zend_Cache.CLEANING_MODE_ALL, null);
	}

	/**
	 * Return an array of stored cache ids
	 *
	 * @return array array of stored cache ids (string)
	 */
	@Override
	public List<String> getIds() {
		return _get((String)_options.get("cache_dir"), "ids", Collections.<String> emptyList());
	}

	/**
	 * Return an array of stored tags
	 *
	 * @return array array of stored tags (string)
	 */
	@Override
	public List<String> getTags() {
		return _get((String)_options.get("cache_dir"), "tags", Collections.<String> emptyList());
	}

	/**
	 * Return an array of stored cache ids which match given tags
	 *
	 * In case of multiple tags, a logical AND is made between tags
	 *
	 * @param array $tags array of tags
	 * @return array array of matching cache ids (string)
	 */
	@Override
	public List<String> getIdsMatchingTags(List<String> tags) {
		return _get((String)_options.get("cache_dir"), "matching", tags);
	}

	/**
	 * Return an array of stored cache ids which don"t match given tags
	 *
	 * In case of multiple tags, a logical OR is made between tags
	 *
	 * @param array $tags array of tags
	 * @return array array of not matching cache ids (string)
	 */
	@Override
	public List<String> getIdsNotMatchingTags(List<String> tags) {
		return _get((String)_options.get("cache_dir"), "notMatching", tags);
	}

	/**
	 * Return an array of stored cache ids which match any given tags
	 *
	 * In case of multiple tags, a logical AND is made between tags
	 *
	 * @param array $tags array of tags
	 * @return array array of any matching cache ids (string)
	 */
	@Override
	public List<String> getIdsMatchingAnyTags(List<String> tags) {
		return _get((String)_options.get("cache_dir"), "matchingAny", tags);
	}

	/**
	 * Return the filling percentage of the backend storage
	 *
	 * @throws Zend_Cache_Exception
	 * @return int integer between 0 and 100
	 */
	@Override
	public int getFillingPercentage() {
		return 50;
		// TODO 디스트 여유 공간 확인 기능 추가.
		//        $free = disk_free_space((String)_options.get("cache_dir"));
		//        $total = disk_total_space((String)_options.get("cache_dir"));
		//        if ($total == 0) {
		//            Zend_Cache::throwException("can\"t get disk_total_space");
		//        } else {
		//            if ($free >= $total) {
		//                return 100;
		//            }
		//            return ((int) (100. * ($total - $free) / $total));
		//        }
	}

	/**
	 * Return an array of metadatas for the given cache id
	 *
	 * The array must include these keys :
	 * - expire : the expire timestamp
	 * - tags : a string array of tags
	 * - mtime : timestamp of last modification time
	 *
	 * @param string $id cache id
	 * @return array array of metadatas (false if the cache id is not found)
	 */
	@Override
	public Map<String, Object> getMetadatas(String id) {
		Map<String, Object> metadatas = _getMetadatas(id);
		if (isExpired(metadatas)) {
			return null;
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("expire", metadatas.get("expire"));
		result.put("tags", metadatas.get("tags"));
		result.put("mtime", metadatas.get("mtime"));

		return result;
	}

	/**
	 * Give (if possible) an extra lifetime to the given cache id
	 *
	 * @param string $id cache id
	 * @param int $extraLifetime
	 * @return boolean true if ok
	 */
	@Override
	public boolean touch(String id, int extraLifetime) {
		Map<String, Object> metadatas = _getMetadatas(id);
		if (isExpired(metadatas)) {
			return false;
		}

		Map<String, Object> newMetadatas = new HashMap<String, Object>();
		newMetadatas.put("hash", metadatas.get("hash"));
		newMetadatas.put("mtime", System.currentTimeMillis());
		newMetadatas.put("expire", (Long)metadatas.get("expire") + extraLifetime);
		newMetadatas.put("tags", metadatas.get("tags"));

		return _setMetadatas(id, newMetadatas);
	}

	/**
	 * Return an associative array of capabilities (booleans) of the backend
	 *
	 * The array must include these keys :
	 * - automatic_cleaning (is automating cleaning necessary)
	 * - tags (are tags supported)
	 * - expired_read (is it possible to read expired cache records
	 *                 (for doNotTestCacheValidity option for example))
	 * - priority does the backend deal with priority when saving
	 * - infinite_lifetime (is infinite lifetime can work with this backend)
	 * - get_list (is it possible to get the list of cache ids and the complete list of tags)
	 *
	 * @return array associative of with capabilities
	 */
	@Override
	public Map<String, Object> getCapabilities() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("automatic_cleaning", true);
		result.put("tags", true);
		result.put("expired_read", true);
		result.put("priority", false);
		result.put("infinite_lifetime", true);
		result.put("get_list", true);

		return result;
	}

	/**
	 * PUBLIC METHOD FOR UNIT TESTING ONLY !
	 *
	 * Force a cache record to expire
	 */
	void ___expire(String id) {
		Map<String, Object> metadatas = _getMetadatas(id);
		if (metadatas != null) {
			metadatas.put("expire", 1);
			_setMetadatas(id, metadatas);
		}
	}

	/**
	 * Get a metadatas record
	 *
	 * @param  string $id  Cache id
	 * @return array|false Associative array of metadatas
	 */
	private Map<String, Object> _getMetadatas(String id) {
		Map<String, Object> metadatas = _metadatasArray.get(id);
		if (metadatas != null) {
			return metadatas;
		} else {
			metadatas = _loadMetadatas(id);
			if (metadatas == null) {
				return null;
			}

			_setMetadatas(id, metadatas, false);
			return metadatas;
		}
	}

	/**
	 * Set a metadatas record
	 *
	 * @param  string $id        Cache id
	 * @param  array  $metadatas Associative array of metadatas
	 * @param  boolean $save     optional pass false to disable saving to file
	 * @return boolean True if no problem
	 */
	private boolean _setMetadatas(String id, Map<String, Object> metadatas, boolean save) {
		if (save) {
			boolean result = _saveMetadatas(id, metadatas);
			if (!result) {
				return false;
			}
		}

		_metadatasArray.put(id, metadatas);
		return true;
	}

	private boolean _setMetadatas(String id, Map<String, Object> metadatas) {
		return _setMetadatas(id, metadatas, true);
	}

	/**
	 * Drop a metadata record
	 *
	 * @param  string $id Cache id
	 * @return boolean True if no problem
	 */
	private boolean _delMetadatas(String id) {
		_metadatasArray.remove(id);

		File file = _metadatasFile(id);
		return _remove(file);
	}

	/**
	 * Clear the metadatas array
	 *
	 * @return void
	 */
	private void _cleanMetadatas() {
		final int metadatasArrayMaxSize = MapUtils.getInteger(_options, "metadatas_array_max_size", 100);
		_metadatasArray = new LinkedHashMap<String, Map<String, Object>>(metadatasArrayMaxSize, 0.75f, true) {
			@Override
			protected boolean removeEldestEntry(Map.Entry<String, Map<String, Object>> eldest) {
				return size() > metadatasArrayMaxSize;
			}
		};
	}

	/**
	 * Load metadatas from disk
	 *
	 * @param  string $id Cache id
	 * @return array|false Metadatas associative array
	 */
	private Map<String, Object> _loadMetadatas(String id) {
		File file = _metadatasFile(id);
		try {
			String result = (String)_fileGetContents(file);
			return JacksonUtil.toObject(result, Map.class);
		} catch (IOException e) {
			log.info("Fail to read file.({})", new Object[] {file, e});
			return null;
		}
	}

	/**
	 * Save metadatas to disk
	 *
	 * @param  string $id        Cache id
	 * @param  array  $metadatas Associative array
	 * @return boolean True if no problem
	 */
	private boolean _saveMetadatas(String id, Map<String, Object> metadatas) {
		File file = _metadatasFile(id);
		return _filePutContents(file, JacksonUtil.toJson(metadatas));
	}

	/**
	 * Make and return a file name (with path) for metadatas
	 *
	 * @param  string $id Cache id
	 * @return string Metadatas file name (with path)
	 */
	private File _metadatasFile(String id) {
		String path = _path(id);
		String fileName = _idToFileName("internal-metadatas---" + id);
		return new File(path, fileName);
	}

	/**
	 * Check if the given filename is a metadatas one
	 *
	 * @param  string $fileName File name
	 * @return boolean True if it"s a metadatas one
	 */
	private boolean _isMetadatasFile(String fileName) {
		String id = _fileNameToId(fileName);
		return id.startsWith("internal-metadatas---");
	}

	/**
	 * Remove a file
	 *
	 * If we can"t remove the file (because of locks or any problem), we will touch
	 * the file to invalidate it
	 *
	 * @param  string $file Complete file path
	 * @return boolean True if ok
	 */
	private boolean _remove(File file) {
		if (!file.isFile()) {
			return false;
		}

		return file.delete();
		//            $this->_log("Zend_Cache_Backend_File::_remove() : we can"t remove $file");
	}

	/**
	 * Clean some cache records (protected method used for recursive stuff)
	 *
	 * Available modes are :
	 * Zend_Cache::CLEANING_MODE_ALL (default)    => remove all cache entries ($tags is not used)
	 * Zend_Cache::CLEANING_MODE_OLD              => remove too old cache entries ($tags is not used)
	 * Zend_Cache::CLEANING_MODE_MATCHING_TAG     => remove cache entries matching all given tags
	 *                                               ($tags can be an array of strings or a single string)
	 * Zend_Cache::CLEANING_MODE_NOT_MATCHING_TAG => remove cache entries not {matching one of the given tags}
	 *                                               ($tags can be an array of strings or a single string)
	 * Zend_Cache::CLEANING_MODE_MATCHING_ANY_TAG => remove cache entries matching any given tags
	 *                                               ($tags can be an array of strings or a single string)
	 *
	 * @param  string $dir  Directory to clean
	 * @param  string $mode Clean mode
	 * @param  array  $tags Array of tags
	 * @throws Zend_Cache_Exception
	 * @return boolean True if no problem
	 */
	private boolean _clean(String dirPath, String mode, List<String> tags) {
		File dir = new File(dirPath);
		if (!dir.isDirectory()) {
			return false;
		}

		if (tags == null) {
			tags = Collections.emptyList();
		}

		boolean result = true;
		final String prefix = (String)_options.get("file_name_prefix") + "--";
		File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(prefix);
			}
		});

		if (files == null || files.length == 0) {
			return true;
		}

		for (File file : files) {
			if (file.isFile()) {
				if (_isMetadatasFile(file.getName())) {
					// in CLEANING_MODE_ALL, we drop anything, even remainings old metadatas files
					if (!mode.equals(Zend_Cache.CLEANING_MODE_ALL)) {
						continue;
					}
				}

				String id = _fileNameToId(file.getName());
				Map<String, Object> metadatas = _getMetadatas(id);
				if (metadatas == null) {
					metadatas = new HashMap<String, Object>();
					metadatas.put("expire", 1);
					metadatas.put("tags", Collections.emptyList());
				}

				if (Zend_Cache.CLEANING_MODE_ALL.equals(mode)) {
					if (!remove(id) && !_remove(file)) {
						result = false;
					}
				} else if (Zend_Cache.CLEANING_MODE_OLD.equals(mode)) {
					if (isExpired(metadatas)) {
						if (!remove(id)) {
							result = false;
						}
					}
				} else if (Zend_Cache.CLEANING_MODE_MATCHING_TAG.equals(mode)) {
					boolean matching = true;
					List<String> tagsInMeta = (List<String>)metadatas.get("tags");
					for (String tag : tags) {
						if (!tagsInMeta.contains(tag)) {
							matching = false;
							break;
						}
					}

					if (matching) {
						if (!remove(id)) {
							result = false;
						}
					}
				} else if (Zend_Cache.CLEANING_MODE_NOT_MATCHING_TAG.equals(mode)) {
					boolean matching = false;
					List<String> tagsInMeta = (List<String>)metadatas.get("tags");
					for (String tag : tags) {
						if (tagsInMeta.contains(tag)) {
							matching = true;
							break;
						}
					}
					if (!matching) {
						if (!remove(id)) {
							result = false;
						}
					}
				} else if (Zend_Cache.CLEANING_MODE_MATCHING_ANY_TAG.equals(mode)) {
					boolean matching = false;
					List<String> tagsInMeta = (List<String>)metadatas.get("tags");
					for (String tag : tags) {
						if (tagsInMeta.contains(tag)) {
							matching = true;
							break;
						}
					}

					if (matching) {
						if (!remove(id)) {
							result = false;
						}
					}
				} else {
					throw new Zend_Cache_Exception("Invalid mode for clean() method");
				}
			}

			if (file.isDirectory() && MapUtils.getInteger(_options, "hashed_directory_level", 0) > 0) {
				// Recursive call
				result = _clean(file.getAbsolutePath(), mode, tags) && result;
				if (Zend_Cache.CLEANING_MODE_ALL.equals(mode)) {
					file.delete();
				}
			}
		}

		return result;
	}

	private List<String> _get(String dirPath, String mode, List<String> tags) {
		File dir = new File(dirPath);
		if (!dir.isDirectory()) {
			return null;
		}

		final String prefix = (String)_options.get("file_name_prefix") + "--";
		File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(prefix);
			}
		});

		if (files == null || files.length == 0) {
			return Collections.emptyList();
		}

		Set<String> result = new HashSet<String>();
		for (File file : files) {
			if (file.isFile()) {
				String id = _fileNameToId(file.getName());
				Map<String, Object> metadatas = _getMetadatas(id);
				if (metadatas == null) {
					continue;
				}

				if (isExpired(metadatas)) {
					continue;
				}

				if ("ids".equals(mode)) {
					result.add(id);
				} else if ("tags".equals(mode)) {
					result.addAll((List<String>)metadatas.get("tags"));
				} else if ("matching".equals(mode)) {
					boolean matching = true;
					List<String> tagsInMeta = (List<String>)metadatas.get("tags");
					for (String tag : tags) {
						if (!tagsInMeta.contains(tag)) {
							matching = false;
							break;
						}
					}

					if (matching) {
						result.add(id);
					}
				}
				else if ("notMatching".equals(mode)) {
					boolean matching = false;
					List<String> tagsInMeta = (List<String>)metadatas.get("tags");
					for (String tag : tags) {
						if (!tagsInMeta.contains(tag)) {
							matching = true;
							break;
						}
					}

					if (!matching) {
						result.add(id);
					}
				}
				else if ("matchingAny".equals(mode)) {
					boolean matching = false;
					List<String> tagsInMeta = (List<String>)metadatas.get("tags");
					for (String tag : tags) {
						if (!tagsInMeta.contains(tag)) {
							matching = true;
							break;
						}
					}

					if (matching) {
						result.add(id);
					}
				} else {
					throw new Zend_Cache_Exception("Invalid mode for _get() method");
				}
			}

			if (file.isDirectory() && (MapUtils.getInteger(_options, "hashed_directory_level", 0) > 0)) {
				// Recursive call
				List<String> recursiveRs = _get(file.getAbsolutePath(), mode, tags);
				if (recursiveRs == null) {
					log.warn("Zend_Cache_Backend_File::_get() / recursive call : can\"t list entries of " + file.getAbsolutePath() + "'");
				} else {
					result.addAll(recursiveRs);
				}
			}
		}

		return new ArrayList<String>(result);
	}

	/**
	 * Compute & return the expire time
	 *
	 * @return int expire time (unix timestamp)
	 */
	private long _expireTime(int lifetimeInSeconds) {
		if (lifetimeInSeconds < 0) {
			return Long.MAX_VALUE;
		}

		return System.currentTimeMillis() + lifetimeInSeconds * 1000;
	}

	private boolean isExpired(Map<String, Object> metadatas) {
		return metadatas == null || System.currentTimeMillis() > MapUtils.getLong(metadatas, "expire", 0L);
	}

	/**
	 * Make a control key with the string containing datas
	 *
	 * @param  string $data        Data
	 * @param  string $controlType Type of control "md5", "crc32" or "strlen"
	 * @throws Zend_Cache_Exception
	 * @return string Control key
	 */
	private String _hash(String data, String controlType)
	{
		// TODO controlType에 hash 값 계산 구현.
		//        switch ($controlType) {
		//        case "md5":
		return Standard.md5(data);
		//        case "crc32":
		//            return crc32($data);
		//        case "strlen":
		//            return strlen($data);
		//        case "adler32":
		//            return hash("adler32", $data);
		//        default:
		//            Zend_Cache::throwException("Incorrect hash function : $controlType");
		//        }
	}

	/**
	 * Return the complete directory path of a filename (including hashedDirectoryStructure)
	 *
	 * @param  string $id Cache id
	 * @param  boolean $parts if true, returns array of directory parts instead of single string
	 * @return string Complete directory path
	 */
	private List<String> _path(String id, boolean parts) {
		String root = (String)_options.get("cache_dir");
		String prefix = (String)_options.get("file_name_prefix");
		int hashedDirectoryLevel = MapUtils.getIntValue(_options, "hashed_directory_level", 0);
		if (hashedDirectoryLevel > 0) {
			List<String> partsArray = new ArrayList<String>(hashedDirectoryLevel);
			String hash = _hash("adler32", id);
			for (int i = 0; i < hashedDirectoryLevel; i++) {
				root = root + prefix + "--" + hash.substring(0, i + 1) + "/";
				partsArray.add(root);
			}

			if (parts) {
				return partsArray;
			} else {
				return Collections.singletonList(root);
			}
		} else {
			return Collections.singletonList(root);
		}
	}

	/**
	 * Make the directory strucuture for the given id
	 *
	 * @param string $id cache id
	 * @return boolean true
	 */
	private boolean _recursiveMkdirAndChmod(String id) {
		int hashedDirectoryLevel = MapUtils.getIntValue(_options, "hashed_directory_level", 0);
		if (hashedDirectoryLevel <= 0) {
			return true;
		}

		List<String> partsArray = _path(id, true);
		for (String part : partsArray) {
			File file = new File(part);
			file.mkdirs();
			// TODO 디렉토리 모드 설정 기능 구현
			//            if (!new File(part).isDirectory()) {
			//                @mkdir($part, $this->_options["hashed_directory_perm"]);
			//                @chmod($part, $this->_options["hashed_directory_perm"]); // see #ZF-320 (this line is required in some configurations)
			//            }
		}
		return true;
	}

	/**
	 * Test if the given cache id is available (and still valid as a cache record)
	 *
	 * @param  string  $id                     Cache id
	 * @param  boolean $doNotTestCacheValidity If set to true, the cache validity won"t be tested
	 * @return boolean|mixed false (a cache is not available) or "last modified" timestamp (int) of the available cache record
	 */
	private Long _test(String id, boolean doNotTestCacheValidity) {
		Map<String, Object> metadatas = _getMetadatas(id);
		if (metadatas == null) {
			return null;
		}
		if (doNotTestCacheValidity || !isExpired(metadatas)) {
			return (Long)metadatas.get("mtime");
		}
		return null;
	}

	/**
	 * Return the file content of the given file
	 *
	 * @param  string $file File complete path
	 * @return string File content (or false if problem)
	 * @throws IOException 
	 */
	private Object _fileGetContents(File file) throws IOException {
		if (!file.isFile()) {
			return null;
		}

		try {
			return new ObjectInputStream(new FileInputStream(file)).readObject();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		// TODO lock 추가.
		//    	
		//        String result = false;
		//        $f = @fopen($file, "rb");
		//        if ($f) {
		//            if ($this->_options["file_locking"]) @flock($f, LOCK_SH);
		//            $result = stream_get_contents($f);
		//            if ($this->_options["file_locking"]) @flock($f, LOCK_UN);
		//            @fclose($f);
		//        }
		//        return $result;
	}

	/**
	 * Put the given string into the given file
	 *
	 * @param  string $file   File complete path
	 * @param  string $string String to put in file
	 * @return boolean true if no problem
	 */
	private boolean _filePutContents(File file, Object data) {
		if (data == null) {
			throw new IllegalArgumentException();
		}

		try {
			new ObjectOutputStream(new FileOutputStream(file)).writeObject(data);
			return true;
		} catch (IOException e) {
			return false;
		}
		// TODO 파일 잠금 처리 추가.
		//        $result = false;
		//        $f = @fopen($file, "ab+");
		//        if ($f) {
		//            if ($this->_options["file_locking"]) @flock($f, LOCK_EX);
		//            fseek($f, 0);
		//            ftruncate($f, 0);
		//            $tmp = @fwrite($f, $string);
		//            if (!($tmp === FALSE)) {
		//                $result = true;
		//            }
		//            @fclose($f);
		//        }
		//        @chmod($file, $this->_options["cache_file_perm"]);
		//        return $result;
	}

	private static final String ID_SEP = "---";

	/**
	 * Transform a cache id into a file name and return it
	 */
	private String _idToFileName(String id) {
		String prefix = (String)_options.get("file_name_prefix");
		return prefix + ID_SEP + id;
	}

	/**
	 * Transform a file name into cache id and return it
	 */
	private String _fileNameToId(String fileName) {
		String prefix = (String)_options.get("file_name_prefix");
		return fileName.substring(prefix.length() + ID_SEP.length());
	}

	/**
	 * Make and return a file name (with path)
	 *
	 * @param  string $id Cache id
	 * @return string File name (with path)
	 */
	private File _file(String id) {
		String path = _path(id);
		String fileName = _idToFileName(id);
		return new File(path, fileName);
	}

	private String _path(String id) {
		return _path(id, false).get(0);
	}
}
