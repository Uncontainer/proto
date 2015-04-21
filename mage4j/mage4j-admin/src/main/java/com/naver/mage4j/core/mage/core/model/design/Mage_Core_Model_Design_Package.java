package com.naver.mage4j.core.mage.core.model.design;

import java.io.File;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Config;
import com.naver.mage4j.core.mage.core.model.app.Mage_Core_Model_App_Area.AreaType;
import com.naver.mage4j.core.mage.core.model.design.Mage_Core_Model_Design_Fallback.Scheme;
import com.naver.mage4j.core.mage.core.model.resource.store.Store;
import com.naver.mage4j.core.mage.core.model.resource.store.UrlType;
import com.naver.mage4j.external.varien.Varien_Profiler;

import de.ailis.pherialize.Mixed;
import de.ailis.pherialize.Pherialize;

@Component
public class Mage_Core_Model_Design_Package {
	public static final AreaType DEFAULT_AREA = AreaType.FRONTEND;
	public static final String DEFAULT_PACKAGE = "default";
	public static final String DEFAULT_THEME = "default";
	public static final String BASE_PACKAGE = "base";

	/**
	 * Package name
	 */
	protected String _name;

	private AreaType _area;

	private Map<ThemeType, String> _theme = new EnumMap<ThemeType, String>(ThemeType.class);

	@Autowired
	private Mage_Core_Model_Config modelConfig;

	@Autowired
	private Mage_Core_Model_Design_Config _config;

	@Autowired
	private Mage_Core_Model_Design_Fallback _fallback = null;

	private boolean _shouldFallback = true;

	protected Store _store = null;

	public Mage_Core_Model_Design_Package() {
		//		_fallback = new Mage_Core_Model_Design_Fallback(_config);
	}

	/**
	 * Set store
	 *
	 * @param  string|integer|Mage_Core_Model_Store $store
	 * @return Mage_Core_Model_Design_Package
	 */
	public void setStore(Store store) {
		if (_fallback != null) {
			_fallback.setStore(store);
		}
		_store = store;
	}

	/**
	 * Retrieve store
	 *
	 * @return string|integer|Mage_Core_Model_Store
	 */
	public Store getStore() {
		if (_store == null) {
			return AppContext.getCurrent().getStore();
		}

		return _store;
	}

	public AreaType getArea() {
		if (_area == null) {
			_area = DEFAULT_AREA;
		}

		return _area;
	}

	/**
	 * Set package area
	 *
	 * @param  string $area
	 * @return Mage_Core_Model_Design_Package
	 */
	public void setArea(AreaType area) {
		_area = area;
	}

	/**
	 * Set package name
	 * In case of any problem, the default will be set.
	 *
	 * @param  string $name
	 * @return Mage_Core_Model_Design_Package
	 */
	public void setPackageName(String name) {
		if (StringUtils.isEmpty(name)) {
			// see, if exceptions for user-agents defined in config
			String customPackage = _checkUserAgentAgainstRegexps("design/package/ua_regexp");
			if (customPackage != null) {
				_name = customPackage;
			} else {
				_name = AppContext.getCurrent().getStoreConfigAsString("design/package/name");
			}
		} else {
			_name = name;
		}
		// make sure not to crash, if wrong package specified
		if (!designPackageExists(_name, getArea())) {
			_name = DEFAULT_PACKAGE;
		}
	}

	/**
	 * Get regex rules from config and check user-agent against them
	 *
	 * Rules must be stored in config as a serialized array(["regexp"]=>"...", ["value"] => "...")
	 * Will return false or found string.
	 *
	 * @param string $regexpsConfigPath
	 * @return mixed
	 */
	protected String _checkUserAgentAgainstRegexps(String regexpsConfigPath) {
		String userAgent = AppContext.getCurrent().getRequest().getUserAgent();
		if (StringUtils.isBlank(userAgent)) {
			return null;
		}

		//        if (!empty(self::$_customThemeTypeCache[$regexpsConfigPath])) {
		//            return self::$_customThemeTypeCache[$regexpsConfigPath];
		//        }

		String configValueSerialized = AppContext.getCurrent().getStoreConfigAsString(regexpsConfigPath);
		if (configValueSerialized == null) {
			return null;
		}

		// TODO serialzed 된 데이터 포맷 확인 이후 처리
		Mixed unserialize = Pherialize.unserialize(configValueSerialized);
		List<Map<String, Object>> regexps = (List)unserialize.toArray().values();
		if (CollectionUtils.isEmpty(regexps)) {
			return null;
		}

		return DesignContext.getPackageByUserAgent(regexps, userAgent, regexpsConfigPath);
	}

	/**
	 * Retrieve package name
	 *
	 * @return string
	 */
	public String getPackageName() {
		if (_name == null) {
			setPackageName(null);
		}

		return _name;
	}

	public boolean designPackageExists(String packageName, AreaType area) {
		String path = modelConfig.getOptions().getBaseDir() + "/" + area + "/" + packageName;
		return new File(path).isDirectory();
	}

	public void setTheme(String theme) {
		for (ThemeType type : ThemeType.values()) {
			_theme.put(type, theme);
		}
	}

	public void setTheme(ThemeType type, String theme) {
		_theme.put(type, theme);
	}

	public String getTheme(ThemeType type) {
		String theme = _theme.get(type);
		if (theme == null) {
			theme = AppContext.getCurrent().getStoreConfigAsString("design/theme/" + type.name().toLowerCase());
			if (theme == null && type != ThemeType.DEFAULT) {
				theme = getTheme(ThemeType.DEFAULT);
				if (theme == null) {
					theme = DEFAULT_THEME;
				}
			}
		}
		_theme.put(type, theme);

		// set exception value for theme, if defined in config
		String customTheme = _checkUserAgentAgainstRegexps("design/theme/" + type.name().toLowerCase() + "_ua_regexp");
		if (customTheme != null) {
			_theme.put(type, customTheme);
		}

		return _theme.get(type);
	}

	public String getDefaultTheme() {
		return DEFAULT_THEME;
	}

	public static class Param {
		Store store;
		AreaType area;
		String packageName;
		ThemeType designType;
		String theme;
		Boolean isDefault;
		Boolean secure;
		boolean relative = true;

		public void merge(Scheme scheme) {
			this.packageName = scheme.packageName;
			this.theme = scheme.theme;
		}

		public Store getStore() {
			return store;
		}

		public void setStore(Store store) {
			this.store = store;
		}

		public AreaType getArea() {
			return area;
		}

		public void setArea(AreaType area) {
			this.area = area;
		}

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		public ThemeType getDesignType() {
			return designType;
		}

		public void setDesignType(ThemeType designType) {
			this.designType = designType;
		}

		public String getTheme() {
			return theme;
		}

		public void setTheme(String theme) {
			this.theme = theme;
		}

		public Boolean getIsDefault() {
			return isDefault;
		}

		public void setIsDefault(Boolean isDefault) {
			this.isDefault = isDefault;
		}

		public Boolean getSecure() {
			return secure;
		}

		public void setSecure(Boolean secure) {
			this.secure = secure;
		}

		public boolean isRelative() {
			return relative;
		}

		public void setRelative(boolean relative) {
			this.relative = relative;
		}
	}

	public void updateParamDefaults(Param params) {
		if (params.store == null) {
			params.store = getStore();
		}
		if (params.area == null) {
			params.area = getArea();
		}
		if (params.packageName == null) {
			params.packageName = getPackageName();
		}
		if (params.theme == null) {
			params.theme = getTheme(params.designType);
		}
		if (params.isDefault == null) {
			params.isDefault = false;
		}
	}

	private String getDesignBaseDir() {
		return AppContext.getCurrent().getConfig().getBaseDir("design");
	}

	public String getBaseDir(Param params) {
		updateParamDefaults(params);
		return (params.relative ? "" : getDesignBaseDir()) +
			params.area.getCode() + "/" + params.packageName + "/" + params.theme + "/" + params.designType.getCode();
	}

	public String getSkinBaseDir(Param params) {
		if (params == null) {
			params = new Param();
		}
		params.designType = ThemeType.SKIN;
		updateParamDefaults(params);

		return (params.relative ? "" : getDesignBaseDir()) +
			params.area.getCode() + "/" + params.packageName + "/" + params.theme;
	}

	public String getLocaleBaseDir(Param params) {
		if (params == null) {
			params = new Param();
		}
		params.designType = ThemeType.LOCALE;
		updateParamDefaults(params);

		return (params.relative ? "" : getDesignBaseDir()) +
			params.area.getCode() + "/" + params.packageName + "/" + params.theme + "/locale/" +
			AppContext.getCurrent().getLocale().getLocaleCode();
	}

	public String getSkinBaseUrl(Param params) {
		params.setDesignType(ThemeType.SKIN);
		this.updateParamDefaults(params);

		String baseUrl = AppContext.getCurrent().getStore().getHelper().getBaseUrl(UrlType.SKIN, params.secure);
		baseUrl = baseUrl + params.getArea().getCode() + '/' + params.getPackageName() + '/' + params.getTheme() + '/';

		return baseUrl;
	}

	/**
	 * Use this one to get existing file name with fallback to default
	 *
	 * $params['_type'] is required
	 *
	 * @param string $file
	 * @param array $params
	 * @return string
	 */
	public String getFilename(String file, ThemeType type, Param params) {
		Varien_Profiler.start("getFilename");
		updateParamDefaults(params);
		String result = _fallback(
			file,
			params,
			_fallback.getFallbackScheme(params.area, params.packageName, params.theme)
			);
		Varien_Profiler.stop("getFilename");
		return result;
	}

	/**
	 * Check whether requested file exists in specified theme params
	 *
	 * Possible params:
	 * - _type: layout|template|skin|locale
	 * - _package: design package, if not set = default
	 * - _theme: if not set = default
	 * - _file: path relative to theme root
	 *
	 * @see Mage_Core_Model_Config::getBaseDir
	 * @param string $file
	 * @param array $params
	 * @return string|false
	 */
	public String validateFile(String file, Param params)
	{
		String fileName = _renderFilename(file, params);
		String testFile = (params.relative ? "" : getDesignBaseDir() + "/") + fileName;
		if (!new File(testFile).exists()) {
			return null;
		}

		return fileName;
	}

	/**
	 * Get filename by specified theme parameters
	 *
	 * @param array $file
	 * @param $params
	 * @return string
	 */
	protected String _renderFilename(String file, Param params) {
		String dir;
		switch (params.designType) {
			case SKIN:
				dir = getSkinBaseDir(params);
				break;
			case LOCALE:
				dir = getLocaleBaseDir(params);
				break;
			default:
				dir = getBaseDir(params);
				break;
		}
		return dir + "/" + file;
	}

	/**
	 * Check for files existence by specified scheme
	 *
	 * If fallback enabled, the first found file will be returned. Otherwise the base package / default theme file,
	 *   regardless of found or not.
	 * If disabled, the lookup won't be performed to spare filesystem calls.
	 *
	 * @param string $file
	 * @param array &$params
	 * @param array $fallbackScheme
	 * @return string
	 */
	protected String _fallback(String file, Param params, List<Scheme> fallbackScheme) {
		if (_shouldFallback) {
			for (Scheme each : fallbackScheme) {
				params.merge(each);
				String filename = validateFile(file, params);
				if (filename != null) {
					return filename;
				}
			}

			params.packageName = BASE_PACKAGE;
			params.theme = DEFAULT_THEME;
		}

		return _renderFilename(file, params);
	}

	public String getLayoutFilename(String file, Param params) {
		return getFilename(file, ThemeType.LAYOUT, params);
	}

	public String getTemplateFilename(String file, Param params) {
		return getFilename(file, ThemeType.TEMPLATE, params);
	}

	public String getLocaleFileName(String file, Param params) {
		return getFilename(file, ThemeType.LOCALE, params);
	}

	/**
	 * Get skin file url
	 *
	 * @param string $file
	 * @param array $params
	 * @return string
	 */
	public String getSkinUrl(String file/* = null*/, Param params/* = array()*/)
	{
		Varien_Profiler.start("getSkinUrl");
		if (params.getDesignType() == null) {
			params.setDesignType(ThemeType.SKIN);
		}
		if (params.getIsDefault() == null) {
			params.setIsDefault(false);
		}

		this.updateParamDefaults(params);
		String result;
		if (StringUtils.isNotEmpty(file)) {
			result = this._fallback(file, params, this._fallback.getFallbackScheme(params.getArea(), params.getPackageName(), params.getTheme()));
		}
		result = this.getSkinBaseUrl(params) + file == null ? "" : file;
		Varien_Profiler.stop("getSkinUrl");
		return result;
	}
}
