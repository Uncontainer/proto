package com.naver.mage4j.external.zend;

import java.util.Map;

import com.naver.mage4j.external.varien.cache.Varien_Cache_Core;
import com.naver.mage4j.external.zend.cache.Zend_Cache_Exception;
import com.naver.mage4j.external.zend.cache.backend.Zend_Cache_Backend_File;
import com.naver.mage4j.external.zend.cache.backend.Zend_Cache_Backend_Interface;

public class Zend_Cache {
	/**
	 * Consts for clean() method
	 */
	public static final String CLEANING_MODE_ALL = "all";
	public static final String CLEANING_MODE_OLD = "old";
	public static final String CLEANING_MODE_MATCHING_TAG = "matchingTag";
	public static final String CLEANING_MODE_NOT_MATCHING_TAG = "notMatchingTag";
	public static final String CLEANING_MODE_MATCHING_ANY_TAG = "matchingAnyTag";

	/**
	* Factory
	*
	* @param mixed  $frontend        frontend name (string) or Zend_Cache_Frontend_ object
	* @param mixed  $backend         backend name (string) or Zend_Cache_Backend_ object
	* @param array  $frontendOptions associative array of options for the corresponding frontend constructor
	* @param array  $backendOptions  associative array of options for the corresponding backend constructor
	* @param boolean $customFrontendNaming if true, the frontend argument is used as a complete class name ; if false, the frontend argument is used as the end of "Zend_Cache_Frontend_[...]" class name
	* @param boolean $customBackendNaming if true, the backend argument is used as a complete class name ; if false, the backend argument is used as the end of "Zend_Cache_Backend_[...]" class name
	* @param boolean $autoload if true, there will no #require_once for backend and frontend (useful only for custom backends/frontends)
	* @throws Zend_Cache_Exception
	* @return Zend_Cache_Core|Zend_Cache_Frontend
	*/
	public static Varien_Cache_Core factory(String frontend, String backend, Map<String, Object> frontendOptions, Map<String, Object> backendOptions, boolean customFrontendNaming, boolean customBackendNaming, boolean autoload) {
		Zend_Cache_Backend_Interface backendObject = _makeBackend(backend, backendOptions, customBackendNaming, autoload);

		Varien_Cache_Core frontendObject = _makeFrontend(frontend, frontendOptions, customFrontendNaming, autoload);

		frontendObject.setBackend(backendObject);

		return frontendObject;
	}

	/**
	 * Backend Constructor
	 *
	 * @param string  $backend
	 * @param array   $backendOptions
	 * @param boolean $customBackendNaming
	 * @param boolean $autoload
	 * @return Zend_Cache_Backend
	 */
	public static Zend_Cache_Backend_Interface _makeBackend(String backend, Map<String, Object> backendOptions, boolean customBackendNaming, boolean autoload) {
		if (!customBackendNaming) {
			backend = _normalizeName(backend);
		}

		// TODO 캐시 타입에 따른 처리 추가.
		return new Zend_Cache_Backend_File(backendOptions);
	}

	/**
	 * Frontend Constructor
	 *
	 * @param string  $frontend
	 * @param array   $frontendOptions
	 * @param boolean $customFrontendNaming
	 * @param boolean $autoload
	 * @return Zend_Cache_Core|Zend_Cache_Frontend
	 */
	public static Varien_Cache_Core _makeFrontend(String frontend, Map<String, Object> frontendOptions, boolean customFrontendNaming, boolean autoload)
	{
		if (!customFrontendNaming) {
			frontend = _normalizeName(frontend);
		}

		return new Varien_Cache_Core(frontendOptions);
		// TODO 캐시 타입에 따른 처리 추가.
		//        if (in_array($frontend, self::$standardFrontends)) {
		//            // we use a standard frontend
		//            // For perfs reasons, with frontend == 'Core', we can interact with the Core itself
		//            $frontendClass = 'Zend_Cache_' . ($frontend != 'Core' ? 'Frontend_' : '') . $frontend;
		//            // security controls are explicit
		//            #require_once str_replace('_', DIRECTORY_SEPARATOR, $frontendClass) . '.php';
		//        } else {
		//            // we use a custom frontend
		//            if (!preg_match('~^[\w\\\\]+$~D', $frontend)) {
		//                Zend_Cache::throwException("Invalid frontend name [$frontend]");
		//            }
		//            if (!$customFrontendNaming) {
		//                // we use this boolean to avoid an API break
		//                $frontendClass = 'Zend_Cache_Frontend_' . $frontend;
		//            } else {
		//                $frontendClass = $frontend;
		//            }
		//            if (!$autoload) {
		//                $file = str_replace('_', DIRECTORY_SEPARATOR, $frontendClass) . '.php';
		//                if (!(self::_isReadable($file))) {
		//                    self::throwException("file $file not found in include_path");
		//                }
		//                #require_once $file;
		//            }
		//        }
		//        return new $frontendClass($frontendOptions);
	}

	/**
	 * Normalize frontend and backend names to allow multiple words TitleCased
	 *
	 * @param  string $name  Name to normalize
	 * @return string
	 */
	private static String _normalizeName(String name) {
		return name;
		// TODO 구현 추가.
		//        $name = ucfirst(strtolower($name));
		//        $name = str_replace(array('-', '_', '.'), ' ', $name);
		//        $name = ucwords($name);
		//        $name = str_replace(' ', '', $name);
		//        if (stripos($name, 'ZendServer') === 0) {
		//            $name = 'ZendServer_' . substr($name, strlen('ZendServer'));
		//        }
		//
		//        return $name;
	}
}
