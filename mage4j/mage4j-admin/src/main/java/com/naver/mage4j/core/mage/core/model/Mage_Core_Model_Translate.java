package com.naver.mage4j.core.mage.core.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.model.app.Mage_Core_Model_App_Area.AreaType;
import com.naver.mage4j.core.mage.core.model.design.DesignContext;
import com.naver.mage4j.core.mage.core.model.design.ThemeType;
import com.naver.mage4j.core.mage.core.model.resource.translate.CoreTranslate;
import com.naver.mage4j.core.mage.core.model.resource.translate.TranslateContext;
import com.naver.mage4j.core.mage.core.model.translate.Mage_Core_Model_Translate_Expr;
import com.naver.mage4j.core.util.JacksonUtil;
import com.naver.mage4j.external.varien.simplexml.SimplexmlUtils;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;
import com.naver.mage4j.external.zend.Zend_Locale;

/**
 * Translate model
 */
@Component
public class Mage_Core_Model_Translate {
	/**
	 * Translate model
	 */
	public static final String CSV_SEPARATOR = ",";
	public static final String SCOPE_SEPARATOR = "::";
	public static final String CACHE_TAG = "translate";
	public static final String CONFIG_KEY_AREA = "area";
	public static final String CONFIG_KEY_LOCALE = "locale";
	public static final String CONFIG_KEY_STORE = "store";
	public static final String CONFIG_KEY_DESIGN_PACKAGE = "package";
	public static final String CONFIG_KEY_DESIGN_THEME = "theme";

	/**
	 * Default translation string
	 */
	public static final String DEFAULT_STRING = "Translate String";

	/**
	 * Locale name
	 */
	protected String _locale;

	//	/**
	//	 * Translation object
	//	 */
	//	protected Zend_Translate_Adapter _translate;

	/**
	 * Translator configuration array
	 */
	private Map<String, Object> _config = new HashMap<String, Object>();

	protected boolean _useCache = true;

	/**
	 * Cache identifier
	 */
	protected String _cacheId;

	/**
	 * Translation data
	 */
	protected Map<String, String> _data = Collections.emptyMap();

	/**
	 * Translation data for data scope (per module)
	 */
	protected Map<String, Object> _dataScope;

	/**
	 * Configuration flag to enable inline translations
	 */
	protected boolean _translateInline;

	protected boolean _canUseInline = true;

	@Autowired
	private DesignContext designContext;

	/**
	 * Configuration flag to local enable inline translations
	 * 
	 */
	public Mage_Core_Model_Translate() {

	}

	/**
	 * Initialization translation data
	 * 
	 * @param area
	 * @return Mage_Core_Model_Translate
	 */
	public void init(AreaType areaType, boolean forceReload) {
		Map<String, Object> config = new HashMap<String, Object>();
		config.put(CONFIG_KEY_AREA, areaType);
		setConfig(config);

		//        $this->_translateInline = Mage::getSingleton("core/translate_inline")
		//            ->isAllowed($area=="adminhtml" ? "admin" : null);
		//
		//        if (!$forceReload) {
		//            if ($this->_canUseCache()) {
		//                $this->_data = $this->_loadCache();
		//                if ($this->_data !== false) {
		//                    return $this;
		//                }
		//            }
		//            Mage::app()->removeCache($this->getCacheId());
		//        }
		//
		//        $this->_data = array();
		//
		//        foreach ($this->getModulesConfig() as $moduleName=>$info) {
		//            $info = $info->asArray();
		//            $this->_loadModuleTranslation($moduleName, $info["files"], $forceReload);
		//        }
		//
		//        $this->_loadThemeTranslation($forceReload);
		//        $this->_loadDbTranslation($forceReload);
		//
		//        if (!$forceReload && $this->_canUseCache()) {
		//            $this->_saveCache();
		//        }
	}

	/**
	 * Retrieve modules configuration by translation
	 * 
	 * @return Mage_Core_Model_Config_Element
	 */
	public Simplexml_Element getModulesConfig() {
		Simplexml_Element config = AppContext.getCurrent().getConfig().getNode(this.getConfig(CONFIG_KEY_AREA) + "/translate/modules");
		if (SimplexmlUtils.isNull(config)) {
			return Simplexml_Element.NULL;
		}

		config = config.selectSingle(this.getConfig(CONFIG_KEY_AREA) + "/translate/modules");
		if (SimplexmlUtils.isNull(config)) {
			return Simplexml_Element.NULL;
		}

		return config;
	}

	/**
	 * Initialize configuration
	 * 
	 * @param config
	 * @return Mage_Core_Model_Translate
	 */
	public void setConfig(Map<String, Object> config) {
		_config = config;
		if (!config.containsKey(CONFIG_KEY_LOCALE)) {
			_config.put(CONFIG_KEY_LOCALE, getLocale());
		}
		if (!config.containsKey(CONFIG_KEY_STORE)) {
			_config.put(CONFIG_KEY_STORE, AppContext.getCurrent().getStore().getStoreId());
		}
		if (!config.containsKey(CONFIG_KEY_DESIGN_PACKAGE)) {
			_config.put(CONFIG_KEY_DESIGN_PACKAGE, designContext.getDesign().getPackageName());
		}
		if (!config.containsKey(CONFIG_KEY_DESIGN_THEME)) {
			_config.put(CONFIG_KEY_DESIGN_THEME, designContext.getDesign().getTheme(ThemeType.LOCALE));
		}
	}

	/**
	 * Retrieve config value by key
	 * 
	 * @param key
	 * @return mixed
	 */
	public Object getConfig(String key) {
		if (this._config.get(key) != null) {
			return this._config.get(key);
		}

		return null;
	}

	/**
	 * Loading data from module translation files
	 * 
	 * @param moduleName
	 * @param files
	 * @return Mage_Core_Model_Translate
	 * @throws IOException 
	 */
	protected Mage_Core_Model_Translate _loadModuleTranslation(String moduleName, List<String> files, boolean forceReload/* = false */) throws IOException {
		for (String file : files) {
			file = this._getModuleFilePath(moduleName, file);
			this._addData(this._getFileData(file), moduleName, forceReload);
		}

		return this;
	}

	/**
	 * Adding translation data
	 * 
	 * @param data
	 * @param scope
	 * @return Mage_Core_Model_Translate
	 */
	protected Mage_Core_Model_Translate _addData(Map<String, String> data, Object scope, boolean forceReload/* = false */) {
		for (Map.Entry<String, String> each : data.entrySet()) {
			String key = each.getKey();
			String value = each.getValue();
			if (key.equals(value)) {
				continue;
			}

			key = this._prepareDataString(key);
			value = this._prepareDataString(value);
			if (scope != null && this._dataScope.get(key) != null && !forceReload) {
				String scopeKey = this._dataScope.get(key) + SCOPE_SEPARATOR + key;
				if (this._data.get(scopeKey) == null) {
					if (this._data.get(key) != null) {
						this._data.put(scopeKey, this._data.get(key));
						if (AppContext.getCurrent().getConfig().getIsDeveloperMode()) {
							this._data.remove(key);
						}

					}

				}

				scopeKey = (scope + SCOPE_SEPARATOR + key);
				this._data.put(scopeKey, value);
			} else {
				this._data.put(key, value);
				this._dataScope.put(key, scope);
			}

		}

		return this;
	}

	/**
	 * Adding translation data
	 * 
	 * @param data
	 * @param scope
	 * @return Mage_Core_Model_Translate
	 */
	protected String _prepareDataString(String string) {
		return string.replace("\"\"", "\"");
	}

	/**
	 * Loading current theme translation
	 * 
	 * @return Mage_Core_Model_Translate
	 * @throws IOException 
	 */
	protected Mage_Core_Model_Translate _loadThemeTranslation(boolean forceReload/* = false */) throws IOException {
		String file = AppContext.getCurrent().getDesignPackage().getLocaleFileName("translate.csv", null);
		this._addData(this._getFileData(file), false, forceReload);
		return this;
	}

	/**
	 * Loading current store translation from DB
	 * 
	 * @return Mage_Core_Model_Translate
	 */
	protected Mage_Core_Model_Translate _loadDbTranslation(boolean forceReload/* = false */) {
		Map<String, String> arr = TranslateContext.getContext().getTranslationArray(null, this.getLocale());
		this._addData(arr, this.getConfig(CONFIG_KEY_STORE), forceReload);
		return this;
	}

	/**
	 * Retrieve translation file for module
	 * 
	 * @param module
	 * @return string
	 */
	protected String _getModuleFilePath(String module, String fileName) {
		String file = AppContext.getCurrent().getConfig().getBaseDir("locale");
		file += ("/" + this.getLocale() + "/" + fileName);
		return file;
	}

	/**
	 * Retrieve data from file
	 * 
	 * @param file
	 * @return array
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	protected Map<String, String> _getFileData(String filePath) throws IOException {
		Map<String, String> data = new HashMap<>();
		File file = new File(filePath);
		if (file.exists()) {
			CSVParser parser = CSVFormat.DEFAULT.parse(new FileReader(file));
			for (CSVRecord record : parser.getRecords()) {
				data.put(record.get(0), record.get(1));
			}
		}

		return data;
	}

	/**
	 * Retrieve translation data
	 * 
	 * @return array
	 */
	public Map<String, String> getData() {
		if (this._data == null) {
			return Collections.emptyMap();
		}

		return this._data;
	}

	/**
	 * Retrieve locale
	 * 
	 * @return string
	 */
	public String getLocale() {
		if (this._locale == null) {
			this._locale = AppContext.getCurrent().getLocale().getLocaleCode();
		}

		return this._locale;
	}

	/**
	 * Retrieve locale
	 * 
	 * @return string
	 */
	public Mage_Core_Model_Translate setLocale(String locale) {
		this._locale = locale;
		return this;
	}

	public Mage_Core_Model_Translate setLocale(Zend_Locale locale) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Retrieve DB resource model
	 * 
	 * @return unknown
	 */
	public CoreTranslate getResource() {
		return TranslateContext.getContext().get();
	}

	//	/**
	//	 * Retrieve translation object
	//	 * 
	//	 * @return Zend_Translate_Adapter
	//	 */
	//	public Zend_Translate_Adapter getTranslate() {
	//		if (this._translate == null) {
	//			this._translate = new Zend_Translate("array", this.getData(), this.getLocale());
	//		}
	//
	//		return this._translate;
	//	}

	public String translate(Mage_Core_Model_Translate_Expr expr, List<Object> args) {
		String translated;
		String code = expr.getCode(SCOPE_SEPARATOR);
		String module = expr.getModule();
		String text = expr.getText();
		translated = this._getTranslatedString(text, code);

		String result = String.format(translated, args);
		//		if (result == false) {
		//			result = translated;
		//		}

		if (this._translateInline && this.getTranslateInline()) {
			if (!result.contains("{{{") || !result.contains("}}}") || !result.contains("}}{{")) {
				result = ("{{{" + result + "}}{{" + translated + "}}{{" + text + "}}{{" + module + "}}}");
			}
		}

		return result;
	}

	/**
	 * Translate
	 * 
	 * @param args
	 * @return string
	 */
	public String translate(String text, List<Object> args) {
		if (StringUtils.isEmpty(text)) {
			return "";
		}

		String module;
		String theme = AppContext.getCurrent().getRequest().getRequestParam("theme");
		if (StringUtils.isNotEmpty(theme)) {
			module = "frontend/default/" + theme;
		} else {
			module = "frontend/default/default";
		}

		return translate(new Mage_Core_Model_Translate_Expr(text, module), args);
	}

	/**
	 * Set Translate inline mode
	 * 
	 * @param flag
	 * @return Mage_Core_Model_Translate
	 */
	public Mage_Core_Model_Translate setTranslateInline(boolean flag/* = null */) {
		this._canUseInline = flag;
		return this;
	}

	/**
	 * Retrieve active translate mode
	 * 
	 * @return bool
	 */
	public boolean getTranslateInline() {
		return this._canUseInline;
	}

	/**
	 * Retrive translated template file
	 * 
	 * @param file
	 * @param type
	 * @param localeCode
	 * @return string
	 * @throws IOException 
	 */
	public String getTemplateFile(String file, String type, String localeCode/* = null */) throws IOException {
		String filePath;
		if ((localeCode == null) || localeCode.matches("[^a-zA-Z_]")) {
			localeCode = this.getLocale();
		}

		String localeBaseDir = AppContext.getCurrent().getConfig().getBaseDir("locale");
		filePath = localeBaseDir + "/" + localeCode + "/" + "template" + "/" + type + "/" + file;
		if (!new File(filePath).exists()) {
			filePath = localeBaseDir + "/" + AppContext.getCurrent().getLocale().getDefaultLocale() + "/" + "template" + "/" + type + "/" + file;
		}

		if (!new File(filePath).exists()) {
			filePath = (localeBaseDir + "/" + Mage_Core_Model_Locale.DEFAULT_LOCALE + "/" + "template" + "/" + type + "/" + file);
		}

		return FileUtils.readFileToString(new File(filePath));
	}

	/**
	 * Retrieve cache identifier
	 * 
	 * @return string
	 */
	public String getCacheId() {
		if (this._cacheId == null) {
			this._cacheId = "translate";
			if (this._config.get(CONFIG_KEY_LOCALE) != null) {
				this._cacheId += ("_" + this._config.get(CONFIG_KEY_LOCALE));
			}

			if (this._config.get(CONFIG_KEY_AREA) != null) {
				this._cacheId += ("_" + this._config.get(CONFIG_KEY_AREA));
			}

			if (this._config.get(CONFIG_KEY_STORE) != null) {
				this._cacheId += ("_" + this._config.get(CONFIG_KEY_STORE));
			}

			if (this._config.get(CONFIG_KEY_DESIGN_PACKAGE) != null) {
				this._cacheId += ("_" + this._config.get(CONFIG_KEY_DESIGN_PACKAGE));
			}

			if (this._config.get(CONFIG_KEY_DESIGN_THEME) != null) {
				this._cacheId += ("_" + this._config.get(CONFIG_KEY_DESIGN_THEME));
			}

		}

		return this._cacheId;
	}

	/**
	 * Loading data cache
	 * 
	 * @param area
	 * @return array|false
	 */
	protected Object _loadCache() {
		if (!(this._canUseCache())) {
			return null;
		}

		String jsonData = (String)AppContext.getCurrent().loadCache(this.getCacheId());
		if (jsonData == null) {
			return null;
		}

		return JacksonUtil.toObject(jsonData, Map.class);
	}

	/**
	 * Saving data cache
	 * 
	 * @param area
	 * @return Mage_Core_Model_Translate
	 */
	protected Mage_Core_Model_Translate _saveCache() {
		if (!(this._canUseCache())) {
			return this;
		}

		AppContext.getCurrent().saveCache(JacksonUtil.toJson(this.getData()), this.getCacheId(), Collections.singletonList(CACHE_TAG));
		return this;
	}

	/**
	 * Check cache usage availability
	 * 
	 * @return bool
	 */
	protected boolean _canUseCache() {
		return AppContext.getCurrent().useCache("translate");
	}

	/**
	 * Return translated string from text.
	 * 
	 * @param text
	 * @param code
	 * @return string
	 */
	protected String _getTranslatedString(String text, String code) {
		String translated;
		if (this.getData().containsKey(code)) {
			translated = this._data.get(code);
		} else if (this.getData().containsKey(text)) {
			translated = this._data.get(text);
		} else {
			translated = text;
		}

		return translated;
	}
}