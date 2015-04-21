package com.naver.mage4j.external.zend.cache;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Zend_Cache_Backend {
	/**
	 * Frontend or Core directives
	 *
	 * =====> (int) lifetime :
	 * - Cache lifetime (in seconds)
	 * - If null, the cache is valid forever
	 *
	 * =====> (int) logging :
	 * - if set to true, a logging is activated throw Zend_Log
	 *
	 * @var array directives
	 */
	protected Map<String, Object> _directives;

	/**
	 * Available options
	 *
	 * @var array available options
	 */
	protected Map<String, Object> _options = new HashMap<String, Object>();

	/**
	 * Constructor
	 *
	 * @param  array $options Associative array of options
	 * @throws Zend_Cache_Exception
	 * @return void
	 */
	public Zend_Cache_Backend(Map<String, Object> options) {
		if (options != null) {
			_options.putAll(options);
		}

		_directives = new HashMap<String, Object>();
		_directives.put("lifetime", 3600);
		_directives.put("logging", false);
		_directives.put("logger", null);
	}

	/**
	 * Set the frontend directives
	 *
	 * @param  array $directives Assoc of directives
	 * @throws Zend_Cache_Exception
	 * @return void
	 */
	public void setDirectives(Map<String, Object> directives) {
		for (Entry<String, Object> each : directives.entrySet()) {
			String name = each.getKey().toLowerCase();
			if (_directives.containsKey(name)) {
				_directives.put(name, each.getValue());
			}
		}

		//        _loggerSanity();
	}

	/**
	 * Set an option
	 *
	 * @param  string $name
	 * @param  mixed  $value
	 * @throws Zend_Cache_Exception
	 * @return void
	 */
	public void setOption(String name, Object value) {
		name = name.toLowerCase();
		if (_options.containsKey(name)) {
			_options.put(name, value);
		}
	}

	/**
	 * Returns an option
	 *
	 * @param string $name Optional, the options name to return
	 * @throws Zend_Cache_Exceptions
	 * @return mixed
	 */
	public Object getOption(String name) {
		name = name.toLowerCase();

		if (_options.containsKey(name)) {
			return _options.get(name);
		}

		if (_directives.containsKey(name)) {
			return _directives.get(name);
		}

		throw new Zend_Cache_Exception("Incorrect option name : " + name);
	}

	/**
	 * Get the life time
	 *
	 * if $specificLifetime is not false, the given specific life time is used
	 * else, the global lifetime is used
	 *
	 * @param  int $specificLifetime
	 * @return int Cache life time
	 */
	public int getLifetime(int specificLifetime) {
		if (specificLifetime < 0) {
			return (Integer)_directives.get("lifetime");
		}

		return specificLifetime;
	}

	/**
	 * Determine system TMP directory and detect if we have read access
	 *
	 * inspired from Zend_File_Transfer_Adapter_Abstract
	 *
	 * @return string
	 * @throws Zend_Cache_Exception if unable to determine directory
	 */
	public File getTmpDir() {
		File dir = new File("/temp");
		dir.mkdirs();

		return dir;
		//	        $tmpdir = array();
		//	        foreach (array($_ENV, $_SERVER) as $tab) {
		//	            foreach (array('TMPDIR', 'TEMP', 'TMP', 'windir', 'SystemRoot') as $key) {
		//	                if (isset($tab[$key]) && is_string($tab[$key])) {
		//	                    if (($key == 'windir') or ($key == 'SystemRoot')) {
		//	                        $dir = realpath($tab[$key] . '\\temp');
		//	                    } else {
		//	                        $dir = realpath($tab[$key]);
		//	                    }
		//	                    if ($this->_isGoodTmpDir($dir)) {
		//	                        return $dir;
		//	                    }
		//	                }
		//	            }
		//	        }
		//	        $upload = ini_get('upload_tmp_dir');
		//	        if ($upload) {
		//	            $dir = realpath($upload);
		//	            if ($this->_isGoodTmpDir($dir)) {
		//	                return $dir;
		//	            }
		//	        }
		//	        if (function_exists('sys_get_temp_dir')) {
		//	            $dir = sys_get_temp_dir();
		//	            if ($this->_isGoodTmpDir($dir)) {
		//	                return $dir;
		//	            }
		//	        }
		//	        // Attemp to detect by creating a temporary file
		//	        $tempFile = tempnam(md5(uniqid(rand(), TRUE)), '');
		//	        if ($tempFile) {
		//	            $dir = realpath(dirname($tempFile));
		//	            unlink($tempFile);
		//	            if ($this->_isGoodTmpDir($dir)) {
		//	                return $dir;
		//	            }
		//	        }
		//	        if ($this->_isGoodTmpDir('/tmp')) {
		//	            return '/tmp';
		//	        }
		//	        if ($this->_isGoodTmpDir('\\temp')) {
		//	            return '\\temp';
		//	        }
		//	        throw new Zend_Cache_Exception("Could not determine temp directory, please specify a cache_dir manually");
	}

	/**
	 * Verify if the given temporary directory is readable and writable
	 *
	 * @param string $dir temporary directory
	 * @return boolean true if the directory is ok
	 */
	protected boolean _isGoodTmpDir(File dir) {
		return dir.canRead() && dir.canWrite();
	}

	//
	//    /**
	//     * Make sure if we enable logging that the Zend_Log class
	//     * is available.
	//     * Create a default log object if none is set.
	//     *
	//     * @throws Zend_Cache_Exception
	//     * @return void
	//     */
	//    protected function _loggerSanity()
	//    {
	//        if (!isset($this->_directives['logging']) || !$this->_directives['logging']) {
	//            return;
	//        }
	//
	//        if (isset($this->_directives['logger'])) {
	//            if ($this->_directives['logger'] instanceof Zend_Log) {
	//                return;
	//            }
	//            Zend_Cache::throwException('Logger object is not an instance of Zend_Log class.');
	//        }
	//
	//        // Create a default logger to the standard output stream
	//        #require_once 'Zend/Log.php';
	//        #require_once 'Zend/Log/Writer/Stream.php';
	//        #require_once 'Zend/Log/Filter/Priority.php';
	//        $logger = new Zend_Log(new Zend_Log_Writer_Stream('php://output'));
	//        $logger->addFilter(new Zend_Log_Filter_Priority(Zend_Log::WARN, '<='));
	//        $this->_directives['logger'] = $logger;
	//    }
	//
	//    /**
	//     * Log a message at the WARN (4) priority.
	//     *
	//     * @param  string $message
	//     * @throws Zend_Cache_Exception
	//     * @return void
	//     */
	//    protected function _log($message, $priority = 4)
	//    {
	//        if (!$this->_directives['logging']) {
	//            return;
	//        }
	//
	//        if (!isset($this->_directives['logger'])) {
	//            Zend_Cache::throwException('Logging is enabled but logger is not set.');
	//        }
	//        $logger = $this->_directives['logger'];
	//        if (!$logger instanceof Zend_Log) {
	//            Zend_Cache::throwException('Logger object is not an instance of Zend_Log class.');
	//        }
	//        $logger->log($message, $priority);
	//    }
}
