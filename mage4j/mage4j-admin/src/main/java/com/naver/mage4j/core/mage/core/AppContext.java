package com.naver.mage4j.core.mage.core;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.naver.mage4j.core.mage.core.model.ScopeType;

@Component
public class AppContext {
	@Autowired
	private ApplicationContext applicationContext;

	public static final String DEFAULT_ENCODING = "UTF-8";

	private static ThreadLocal<Mage_Core_Model_App> APP_HOLDER = new ThreadLocal<Mage_Core_Model_App>();

	//	public static class AppHolder {
	//		private static final ThreadLocal<Mage_Core_Model_App> APP_HOLDER = new ThreadLocal<Mage_Core_Model_App>();
	//
	//		public static Mage_Core_Model_App getApp() {
	//			return APP_HOLDER.get();
	//		}
	//
	//		static void setApp(Mage_Core_Model_App app) {
	//			APP_HOLDER.set(app);
	//		}
	//
	//		static void clearServletObjects() {
	//			APP_HOLDER.remove();
	//		}
	//	}

	public Mage_Core_Model_App getApp(HttpServletRequest request, HttpServletResponse response, String code, ScopeType type, Map<String, Object> options) {
		Mage_Core_Model_App app = applicationContext.getBean(Mage_Core_Model_App.class);
		// app 초기화 코드 내부에서 app를 이용하는 경우가 있으므로 먼저 설정해준다.
		APP_HOLDER.set(app);

		app.init(request, response, code, type, options);

		//		_setConfigModel(options);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("scope_code", code);
		params.put("scope_type", type);
		params.put("options", options);

		return app;
	}

	public static boolean isCurrentSet() {
		return APP_HOLDER.get() != null;
	}

	public static Mage_Core_Model_App getCurrent() {
		Mage_Core_Model_App app = APP_HOLDER.get();
		if (app == null) {
			throw new IllegalStateException("App has not set.");
		}

		return app;
	}

	public static void resetCurrent() {
		APP_HOLDER.remove();
	}

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
	//		if (alternativeConfigModel instanceof Mage_Core_Model_Config) {
	//			_config = (Mage_Core_Model_Config)alternativeConfigModel;
	//		} else {
	//			_config = new Mage_Core_Model_Config(options);
	//		}
	//	}
}
