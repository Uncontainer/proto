//package com.naver.mage4j;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.naver.mage4j.core.Mage_Core_Model_App;
//import com.naver.mage4j.core.model.Mage_Core_Model_Config;
//import com.naver.mage4j.core.model.app.Mage_Core_Model_App_Area;
//import com.naver.mage4j.core.model.session.Mage_Core_Model_Session_Exception;
//import com.naver.mage4j.core.model.store.Mage_Core_Model_Store_Exception;
//import com.naver.mage4j.external.varien.Varien_Profiler;
//
//public class Mage {
//	/**
//	 * Application model
//	 */
//	private Mage_Core_Model_App _app;
//
//	private Mage_Core_Model_Config modelConfig;
//
//	/**
//	 * Magento edition constants
//	 */
//	static final String EDITION_COMMUNITY = "Community";
//	public static final String EDITION_ENTERPRISE = "Enterprise";
//	public static final String EDITION_PROFESSIONAL = "Professional";
//	public static final String EDITION_GO = "Go";
//
//	static private String _currentEdition = EDITION_COMMUNITY;
//
//	/**
//	* Is allow throw Exception about headers already sent
//	*/
//	public static boolean headersSentThrowsException = true;
//
//	public Mage() {
//	}
//
//	/**
//	* Front end main entry point
//	*
//	* @param string $code
//	* @param string $type
//	* @param string|array $options
//	*/
//	public void run(HttpServletRequest request, HttpServletResponse response, String code, String type, Map<String, Object> options) {
//		try {
//			Varien_Profiler.start("mage");
//			if (options.containsKey("edition")) {
//				_currentEdition = (String)options.get("edition");
//			}
//
//			_app = new Mage_Core_Model_App(request, response);
//
//			_setIsInstalled(options);
//			_setConfigModel(options);
//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("scope_code", code);
//			params.put("scope_type", type);
//			params.put("options", options);
//			_app.run(params);
//			Varien_Profiler.stop("mage");
//		} catch (Mage_Core_Model_Session_Exception e) {
//			throw e;
//			//            header("Location: " . self::getBaseUrl());
//			//            die();
//		} catch (Mage_Core_Model_Store_Exception e) {
//			throw e;
//			//            require_once(self::getBaseDir() . DS . "errors" . DS . "404.php");
//			//            die();
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//			//            if (self::isInstalled() || self::$_isDownloader) {
//			//                self::printException($e);
//			//                exit();
//			//            }
//			//            try {
//			//                self::dispatchEvent("mage_run_exception", array("exception" => $e));
//			//                if (!headers_sent() && self::isInstalled()) {
//			//                    header("Location:" . self::getUrl("install"));
//			//                } else {
//			//                    self::printException($e);
//			//                }
//			//            } catch (Exception $ne) {
//			//                self::printException($ne, $e->getMessage());
//			//            }
//		}
//	}
//
//	public void run(HttpServletRequest request, HttpServletResponse response) {
//		run(request, response, "", "store", new HashMap<String, Object>());
//	}
//
//	/**
//	 * Set application isInstalled flag based on given options
//	 *
//	 * @param array $options
//	 */
//	private void _setIsInstalled(Map<String, Object> options) {
//		modelConfig.setIsInstalled(options);
//	}
//
//	/**
//	 * Set application Config model
//	 *
//	 * @param array $options
//	 */
//	private void _setConfigModel(Map<String, Object> options) {
//		String configModelClassName = (String)options.get("config_model");
//		Object alternativeConfigModel = null;
//		if (configModelClassName != null) {
//			options.remove("config_model");
//			try {
//				alternativeConfigModel = Class.forName(configModelClassName).getConstructor(Map.class).newInstance(options);
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//		} else {
//			alternativeConfigModel = null;
//		}
//
//		//		if (alternativeConfigModel instanceof Mage_Core_Model_Config) {
//		//			_config = (Mage_Core_Model_Config)alternativeConfigModel;
//		//		} else {
//		//			_config = new Mage_Core_Model_Config(options);
//		//		}
//	}
//
//	public Mage_Core_Model_App app() {
//		return app("", "store", new HashMap<String, Object>());
//	}
//
//	/**
//	 * Get initialized application object.
//	 *
//	 * @param string $code
//	 * @param string $type
//	 * @param string|array $options
//	 * @return Mage_Core_Model_App
//	 */
//	public Mage_Core_Model_App app(String code, String type, Map<String, Object> options) {
//		if (_app == null) {
//			_app = new Mage_Core_Model_App(null, null);
//			//            self::setRoot();
//			_setIsInstalled(options);
//			_setConfigModel(options);
//
//			Varien_Profiler.start("self::app::init");
//			_app.init(code, type, options);
//			Varien_Profiler.stop("self::app::init");
//			_app.loadAreaPart(Mage_Core_Model_App_Area.AreaType.GLOBAL, Mage_Core_Model_App_Area.PartType.EVENTS);
//		}
//
//		return _app;
//	}
//}
