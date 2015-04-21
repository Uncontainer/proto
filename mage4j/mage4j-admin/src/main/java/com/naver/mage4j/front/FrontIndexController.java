package com.naver.mage4j.front;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.Mage_Core_Model_App;
import com.naver.mage4j.core.mage.core.RedirectAndExitException;
import com.naver.mage4j.core.mage.core.model.ScopeType;
import com.naver.mage4j.core.mage.core.model.session.Mage_Core_Model_Session_Exception;
import com.naver.mage4j.core.mage.core.model.store.Mage_Core_Model_Store_Exception;
import com.naver.mage4j.external.varien.Varien_Profiler;

@Controller
@RequestMapping("")
public class FrontIndexController {
	@Autowired
	private AppContext appContext;

	@RequestMapping(method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response/*, String code, String type*/, Map<String, Object> options) {
		try {
			String code = null;
			ScopeType type = null;
			Varien_Profiler.start("mage");
			Mage_Core_Model_App _app = appContext.getApp(request, response, code, type, options);
			_app.run();
			Varien_Profiler.stop("mage");
		} catch (Mage_Core_Model_Session_Exception e) {
			throw e;
			//            header("Location: " . self::getBaseUrl());
			//            die();
		} catch (Mage_Core_Model_Store_Exception e) {
			throw e;
			//            require_once(self::getBaseDir() . DS . "errors" . DS . "404.php");
			//            die();
		} catch (RedirectAndExitException e) {
			return null;
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException)e;
			}

			throw new RuntimeException(e);
			//            if (self::isInstalled() || self::$_isDownloader) {
			//                self::printException($e);
			//                exit();
			//            }
			//            try {
			//                self::dispatchEvent("mage_run_exception", array("exception" => $e));
			//                if (!headers_sent() && self::isInstalled()) {
			//                    header("Location:" . self::getUrl("install"));
			//                } else {
			//                    self::printException($e);
			//                }
			//            } catch (Exception $ne) {
			//                self::printException($ne, $e->getMessage());
			//            }
		}

		return "index";
	}
}
