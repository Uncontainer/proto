package com.naver.mage4j.core.mage.core.model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.Mage_Core_Exception;
import com.naver.mage4j.core.mage.core.model.config.Mage_Core_Model_Config_Element;
import com.naver.mage4j.core.mage.core.model.config.Mage_Core_Model_Config_Options;
import com.naver.mage4j.core.mage.core.model.resource.Mage_Core_Model_Resource_Config;
import com.naver.mage4j.core.mage.core.model.resource.store.StoreContext;
import com.naver.mage4j.core.mage.core.model.resource.store.StoreHelper;
import com.naver.mage4j.core.mage.core.model.resource.store.UrlType;
import com.naver.mage4j.core.mage.core.model.resource.website.WebsiteContext;
import com.naver.mage4j.core.util.PhpDateUtils;
import com.naver.mage4j.external.php.DateTime;
import com.naver.mage4j.external.php.Functions;
import com.naver.mage4j.external.php.Standard;
import com.naver.mage4j.external.varien.Varien_Profiler;
import com.naver.mage4j.external.varien.simplexml.SimplexmlUtils;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;
import com.naver.mage4j.external.varien.simplexml.Varien_Simplexml_Config;

@Component
public class Mage_Core_Model_Config extends Varien_Simplexml_Config implements InitializingBean {
	public static final String CACHE_TAG = "CONFIG";

	/**
	 * Flag which allow use cache logic
	 */
	private boolean _useCache = false;

	private boolean _isSingleStoreAllowed = true;

	/**
	 * Configuration options
	 */
	private Mage_Core_Model_Config_Options _options;

	/**
	 * Empty configuration object for loading and megring configuration parts
	 */
	private Varien_Simplexml_Config _prototype;

	/**
	 * Flag which identify what local configuration is loaded
	 */
	private boolean _isLocalConfigLoaded = false;

	/**
	 * System environment server variables
	 */
	protected Map<String, String> _distroServerVars;

	/**
	 * Array which is using for replace placeholders of server variables
	 */
	protected Map<String, String> _substServerVars;

	/**
	* Resource model
	* Used for operations with DB
	*/
	@Autowired
	protected Mage_Core_Model_Resource_Config _resourceModel;

	@Autowired
	private StoreContext storeContext;

	@Autowired
	private WebsiteContext websiteContext;

	private final String cacheId = "config_global";
	private Map<String, Integer> _cacheSections;
	private Map<String, Simplexml_Element> _cacheLoadedSections;

	public Mage_Core_Model_Config() {
		// TODO 외부 파라미터를 받아야할 경우가 있을지 확인 필요
		this((Map<String, Object>)null);
	}

	public Mage_Core_Model_Config(Varien_Simplexml_Config other) {
		super(other);
	}

	public Mage_Core_Model_Config(Map<String, Object> options) {
		super(Mage_Core_Model_Config_Element.class, options);
		this._options = new Mage_Core_Model_Config_Options(options);
		this._prototype = new Varien_Simplexml_Config(Mage_Core_Model_Config_Element.class);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public boolean isSingleStoreAllowed() {
		return _isSingleStoreAllowed;
	}

	/**
	 * Initialization of core configuration
	 */
	public void init(Map<String, Object> options) {
		//		setCacheChecksum(null);
		setOptions(options);

		loadBase();
		loadModules();
		loadDb();
		loadSectionCache();
	}

	/**
	 * Load base system configuration (config.xml and local.xml files)
	 */
	public void loadBase() {
		String etcDir = getOptions().getEtcDir();
		File[] files = new File(etcDir).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".xml");
			}
		});

		this.loadFile(files[0]);
		for (int i = 1; i < files.length; i++) {
			Varien_Simplexml_Config merge = new Varien_Simplexml_Config(this._prototype);
			merge.loadFile(files[i]);
			this.extend(merge);
		}

		for (File file : files) {
			if (file.getName().equals("local.xml")) {
				this._isLocalConfigLoaded = true;
			}
		}
	}

	/**
	 * Load modules configuration
	 */
	private Mage_Core_Model_Config loadModules() {
		Varien_Profiler.start("config/load-modules");
		_loadDeclaredModules();

		// TODO resourceConfig의 내용 확인 이후 로딩 고려.(예 config.mysql.xml)
		String resourceConfig = String.format("config.%s.xml", _getResourceConnectionModel("core"));
		loadModulesConfiguration(Arrays.asList("config.xml", resourceConfig), this);

		/**
		 * Prevent local.xml directives overwriting
		 */
		Varien_Simplexml_Config mergeConfig = new Varien_Simplexml_Config(_prototype);
		_isLocalConfigLoaded = mergeConfig.loadFile(new File(getOptions().getEtcDir() + "/local.xml"));
		if (_isLocalConfigLoaded) {
			extend(mergeConfig);
		}

		applyExtends();
		Varien_Profiler.stop("config/load-modules");
		return this;
	}

	/**
	 * Load config data from DB
	 */
	private void loadDb() {
		if (_isLocalConfigLoaded && isInstalled()) {
			Varien_Profiler.start("config/load-db");
			_resourceModel.loadToXml(this);
			Varien_Profiler.stop("config/load-db");
		}
	}

	private void loadSectionCache() {
		_cacheSections = new HashMap<String, Integer>();
		_cacheSections.put("admin", 0);
		_cacheSections.put("adminhtml", 0);
		_cacheSections.put("crontab", 0);
		_cacheSections.put("install", 0);
		_cacheSections.put("stores", 1);
		_cacheSections.put("websites", 0);

		_cacheLoadedSections = new HashMap<String, Simplexml_Element>();
		for (Map.Entry<String, Integer> each : _cacheSections.entrySet()) {
			String sectionName = each.getKey();
			int level = each.getValue();
			_loadSectionCache(cacheId, sectionName, _xml, level);
		}
	}

	private void _loadSectionCache(String idPrefix, String sectionName, Simplexml_Element source, int recursionLevel) {
		Simplexml_Element section = source.selectSingle(sectionName);
		if (section.isNull()) {
			return;
		}

		String cacheId = idPrefix + "_" + sectionName;
		if (recursionLevel > 0) {
			for (Simplexml_Element child : section.children()) {
				_loadSectionCache(cacheId, child.getName(), child, recursionLevel - 1);
			}
		}

		_cacheLoadedSections.put(cacheId, section);
	}

	public Mage_Core_Model_Config_Options getOptions() {
		return this._options;
	}

	/**
	 * Set configuration options
	 */
	public Mage_Core_Model_Config setOptions(Map<String, Object> options) {
		if (options instanceof Map) {
			this.getOptions().addData(options);
		}

		return this;
	}

	/**
	* Retrieve application root absolute path
	*
	* @param string $type
	* @return string
	*/
	public String getBaseDir(String type) {
		return getOptions().getDir(type);
	}

	/**
	 * Get config resource model
	 *
	 * @return Mage_Core_Store_Mysql4_Config
	 */
	public Mage_Core_Model_Resource_Config getResourceModel() {
		return _resourceModel;
	}

	/**
	 * Check if local configuration (DB connection, etc) is loaded
	 *
	 * @return bool
	 */
	public boolean isLocalConfigLoaded() {
		return _isLocalConfigLoaded;
	}

	/**
	 * Returns node found by the $path and scope info
	 * @param string
	 *            |int $scopeCode
	 * @return Mage_Core_Model_Config_Element
	 */
	public Simplexml_Element getNode(String path, ScopeType scope, Object scopeCode) {
		if (scope != null) {
			String strScope = scope.name().toLowerCase();
			switch (scope) {
				case STORE:
				case WEBSITE:
					strScope += "s";
					break;
				case DEFAULT:
					break;
				default:
					if (scopeCode instanceof Number) {
						short numScopeCode = ((Number)scopeCode).shortValue();
						if ("stores".equals(scope)) {
							scopeCode = storeContext.getById(numScopeCode).getCode();
						} else if ("websites".equals(scope)) {
							scopeCode = websiteContext.getWebsiteById(numScopeCode).getCode();
						} else {
							throw new Mage_Core_Exception("Unknown scope '" + scope + "'.");
						}
					}

			}

			path = strScope + (scopeCode != null ? "/" + scopeCode : "") + (path == null ? "" : "/" + path);
		}

		/**
		 * Check path cache loading
		 */
		if (_useCache && (path != null)) {
			String[] pathes = path.split("/");
			String section = pathes[0];
			if (_cacheSections.containsKey(section)) {
				Simplexml_Element res = getSectionNode(pathes);
				if (res != null) {
					return res;
				}
			}
		}

		return super.getNode(path);
	}

	/**
	 * Get node value from cached section data
	 * 
	 * @param array
	 *            $path
	 * @return Mage_Core_Model_Config
	 */
	public Simplexml_Element getSectionNode(String[] path) {
		String section = path[0];
		Simplexml_Element config = _getSectionConfig(path);
		if (config != null) {
			path = Standard.array_slice(path, _cacheSections.get(section) + 1);
			return config.selectDescendant(StringUtils.join(path, "/"));
		} else {
			return Simplexml_Element.NULL;
		}
	}

	/**
	 * Getter for section configuration object
	 * 
	 * @param array
	 *            $path
	 * @return Mage_Core_Model_Config_Element
	 */
	private Simplexml_Element _getSectionConfig(String[] path) {
		String section = path[0];
		if (!_cacheSections.containsKey(section)) {
			return null;
		}

		String[] sectionPath = Arrays.copyOf(path, _cacheSections.get(section) + 1);
		String sectionKey = StringUtils.join(sectionPath, "_");

		return _cacheLoadedSections.get(sectionKey);
	}

	/**
	 * Create node by $path and set its value.
	 *
	 * @param string $path separated by slashes
	 * @param string $value
	 * @param bool $overwrite
	 * @return Varien_Simplexml_Config
	 */
	@Override
	public Varien_Simplexml_Config setNode(String path, String value, boolean overwrite) {
		if (_useCache && path != null) {
			String[] sectionPath = path.split("/");
			Simplexml_Element config = _getSectionConfig(sectionPath);
			if (config != null) {
				sectionPath = Standard.array_slice(sectionPath, _cacheSections.get(sectionPath[0]) + 1);
				config.setNode(StringUtils.join(sectionPath, "/"), value, overwrite);
			}
		}

		return super.setNode(path, value, overwrite);
	}

	/**
	 * Iterate all active modules "etc" folders and combine data from specidied xml file name to one object
	 */
	private Mage_Core_Model_Config loadModulesConfiguration(List<String> fileNames, Mage_Core_Model_Config mergeToObject) {
		boolean disableLocalModules = _canUseLocalModules();

		if (mergeToObject == null) {
			mergeToObject = new Mage_Core_Model_Config(_prototype);
			mergeToObject.loadString("<config/>");
		}

		Varien_Simplexml_Config mergeModel = new Varien_Simplexml_Config(_prototype);

		for (Simplexml_Element module : getNode("modules").children()) {
			String modName = module.getName();

			if (((Mage_Core_Model_Config_Element)module).is("active")) {
				if (disableLocalModules && "local".equals(module.getString("corePool"))) {
					continue;
				}

				for (String fileName : fileNames) {
					String configFile = getModuleDir("etc", modName) + "/" + fileName;
					if (mergeModel.loadFile(new File(configFile))) {
						mergeToObject.extend(mergeModel, true);
					}
				}
			}
		}

		return mergeToObject;
	}

	/**
	 * Get module directory by directory type
	 */
	public String getModuleDir(String type, String moduleName) {
		String codePool = getModuleConfig(moduleName).getString("codePool");
		String dir = getOptions().getCodeDir() + "/" + codePool + "/" + Functions.uc_words(moduleName, "/");

		if ("etc".equals(type)) {
			dir += "/etc";
		} else if ("controllers".equals(type)) {
			dir += "/controllers";
		} else if ("sql".equals(type)) {
			dir += "/sql";
		} else if ("data".equals(type)) {
			dir += "/data";
		} else if ("locale".equals(type)) {
			dir += "/locale";
		}

		return dir;
	}

	/**
	 * Get module config node
	 */
	Simplexml_Element getModuleConfig(String moduleName) {
		Simplexml_Element modules = getNode("modules");
		if (StringUtils.isBlank(moduleName)) {
			return modules;
		} else {
			return modules.selectSingle(moduleName);
		}
	}

	private Boolean _canUseLocalModules = null;

	/**
	 * Check local modules enable/disable flag If local modules are disbled remove local modules path from include dirs
	 * 
	 * return true if local modules enabled and false if disabled
	 * 
	 * @return bool
	 */
	private boolean _canUseLocalModules() {
		if (_canUseLocalModules != null) {
			return _canUseLocalModules;
		}

		boolean disableLocalModules;
		String disableLocalModulesString = getNode("global").getString("disable_local_modules");
		if (disableLocalModulesString != null) {
			disableLocalModules = "true".equals(disableLocalModulesString) || "1".equals(disableLocalModulesString);
		} else {
			disableLocalModules = false;
		}

		// TODO 처리 필요.
		// if (disableLocalModules && !defined("COMPILER_INCLUDE_PATH")) {
		// set_include_path(
		// // excluded "/app/code/local"
		// BP . DS . "app" . DS . "code" . DS . "community" . PS .
		// BP . DS . "app" . DS . "code" . DS . "core" . PS .
		// BP . DS . "lib" . PS .
		// Mage::registry("original_include_path")
		// );
		// }

		_canUseLocalModules = !disableLocalModules;
		return _canUseLocalModules;
	}

	/**
	 * Retrieve resource connection model name
	 * 
	 * @param string
	 *            $moduleName
	 * @return string
	 */
	private String _getResourceConnectionModel(String moduleName) {
		Simplexml_Element config = null;
		if (moduleName != null) {
			String setupResource = moduleName + "_setup";
			config = getResourceConnectionConfig(setupResource);
		}
		if (config != null) {
			config = getResourceConnectionConfig(Mage_Core_Model_Resource.DEFAULT_SETUP_RESOURCE);
		}

		return config.getString("model");
	}

	/**
	 * Get connection configuration
	 * 
	 * @param string
	 *            $name
	 * @return Varien_Simplexml_Element
	 */
	public Simplexml_Element getResourceConnectionConfig(String name) {
		Simplexml_Element config = getResourceConfig(name);
		if (config != null) {
			Simplexml_Element conn = config.selectSingle("connection");
			if (!conn.isNull()) {
				String use = conn.getString("use");
				if (StringUtils.isNotBlank(use)) {
					return getResourceConnectionConfig(use);
				} else {
					return conn;
				}
			}
		}
		return null;
	}

	/**
	 * Get resource configuration for resource name
	 * 
	 * @param string
	 *            $name
	 * @return Varien_Simplexml_Object
	 */
	public Simplexml_Element getResourceConfig(String name) {
		return _xml.selectSingle("global").selectSingle("resources").selectSingle(name);
	}

	/**
	 * Load declared modules configuration
	 * 
	 * @param null $mergeConfig depricated
	 * @return Mage_Core_Model_Config
	 */
	private Mage_Core_Model_Config _loadDeclaredModules() {
		List<File> moduleFiles = _getDeclaredModuleFiles();
		if (moduleFiles == null) {
			return this;
		}

		Varien_Profiler.start("config/load-modules-declaration");

		Varien_Simplexml_Config unsortedConfig = new Varien_Simplexml_Config(Mage_Core_Model_Config_Element.class);
		unsortedConfig.loadString("<config/>");

		// load modules declarations
		for (File file : moduleFiles) {
			Varien_Simplexml_Config fileConfig = new Varien_Simplexml_Config(Mage_Core_Model_Config_Element.class);
			fileConfig.loadFile(file);
			unsortedConfig.extend(fileConfig);
		}

		Map<String, ModuleDepend> moduleDependMap = new HashMap<String, ModuleDepend>();
		for (Simplexml_Element moduleNode : unsortedConfig.getNode("modules").children()) {
			String moduleName = moduleNode.getName();
			if (!_isAllowedModule(moduleName)) {
				continue;
			}

			Map<String, Boolean> depends = new HashMap<String, Boolean>();
			for (Simplexml_Element depend : moduleNode.selectSingle("depends").children()) {
				depends.put(depend.getName(), true);
			}

			ModuleDepend item = new ModuleDepend(moduleName, depends, "true".equals(moduleNode.getString("active")));

			moduleDependMap.put(moduleName, item);
		}

		// check and sort module dependence
		List<ModuleDepend> moduleDepends = _sortModuleDepends(moduleDependMap);

		// create sorted config
		Varien_Simplexml_Config sortedConfig = new Varien_Simplexml_Config(Mage_Core_Model_Config_Element.class);
		sortedConfig.loadString("<config><modules/></config>");

		for (Simplexml_Element child : unsortedConfig.getNode(null).children()) {
			// TODO 이름 비교를 왜 하는지 확인 필요.
			//			if (!"modules".equals(child.getName())) {
			sortedConfig.getNode(null).appendChild(child);
			//			}
		}

		Simplexml_Element modulesNode = unsortedConfig.getNode("modules");
		for (ModuleDepend moduleProp : moduleDepends) {
			Simplexml_Element node = modulesNode.selectSingle(moduleProp.module);
			if (node.isNull()) {
				continue;
			}
			modulesNode.appendChild(node);
		}

		extend(sortedConfig);

		Varien_Profiler.stop("config/load-modules-declaration");
		return this;
	}

	static class ModuleDepend {
		String module;
		Map<String, Boolean> depends;
		boolean active;

		public ModuleDepend(String module, Map<String, Boolean> depends, boolean active) {
			super();
			this.module = module;
			this.depends = depends;
			this.active = active;
		}
	}

	/**
	 * Sort modules and check depends
	 * 
	 * @param array
	 *            $modules
	 * @return array
	 */
	private List<ModuleDepend> _sortModuleDepends(Map<String, ModuleDepend> moduleMap) {
		for (ModuleDepend each : moduleMap.values()) {
			Map<String, Boolean> newDepends = new HashMap<String, Boolean>(each.depends);
			for (String depend : each.depends.keySet()) {
				if (each.active && ((!moduleMap.containsKey(depend)) || !moduleMap.get(depend).active)) {
					throw new Mage_Core_Exception("Module '" + each.module + "' requires module '" + depend + "'.");
				}
				newDepends.putAll(moduleMap.get(depend).depends);
			}
			each.depends = newDepends;
		}

		List<ModuleDepend> modules = new ArrayList<ModuleDepend>(moduleMap.values());
		for (int i = modules.size() - 1; i >= 0; i--) {
			for (int j = modules.size() - 1; i < j; j--) {
				if (modules.get(i).depends.containsKey(modules.get(j).module)) {
					ModuleDepend value = modules.get(i);
					modules.set(i, modules.get(j));
					modules.set(j, value);
				}
			}
		}

		Set<String> definedModules = new HashSet<String>();
		for (ModuleDepend moduleProp : modules) {
			for (String dependModule : moduleProp.depends.keySet()) {
				if (!definedModules.contains(dependModule)) {
					throw new Mage_Core_Exception("Module '" + moduleProp.module + "' cannot depend on '" + dependModule + "'.");
				}
			}

			definedModules.add(moduleProp.module);
		}

		return modules;
	}

	/**
	 * Modules allowed to load If empty - all modules are allowed
	 * 
	 * @var array
	 */
	private Set<String> _allowedModules = new HashSet<String>();

	/**
	 * Define if module is allowed
	 * 
	 * @param string
	 *            $moduleName
	 * @return bool
	 */
	private boolean _isAllowedModule(String moduleName) {
		if (_allowedModules.isEmpty()) {
			return true;
		} else {
			return _allowedModules.contains(moduleName);
		}
	}

	/**
	 * Retrive Declared Module file list
	 */
	private List<File> _getDeclaredModuleFiles() {
		String etcDir = getOptions().getEtcDir();
		File[] moduleFiles = new File(etcDir + "/modules").listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".xml");
			}
		});

		if (moduleFiles == null || moduleFiles.length == 0) {
			return null;
		}

		List<File> baseFiles = new ArrayList<File>(moduleFiles.length);
		List<File> mageFiles = new ArrayList<File>();
		List<File> customFiles = new ArrayList<File>();

		for (File v : moduleFiles) {
			if (v.getName().startsWith("Mage_All")) {
				baseFiles.add(v);
			} else if (v.getName().startsWith("Mage_")) {
				mageFiles.add(v);
			} else {
				customFiles.add(v);
			}
		}

		baseFiles.addAll(mageFiles);
		baseFiles.addAll(customFiles);

		return baseFiles;
	}

	public String substDistroServerVars(String data) {
		getDistroServerVars();
		for (Entry<String, String> each : _substServerVars.entrySet()) {
			data = data.replace(each.getKey(), each.getValue());
		}

		return data;
	}

	/**
	 * Get default server variables values
	 *
	 * @return array
	 */
	public Map<String, String> getDistroServerVars() {
		if (_distroServerVars == null) {
			// TODO baseUrl 생성 로직 추가.
			//            if (isset($_SERVER['SCRIPT_NAME']) && isset($_SERVER['HTTP_HOST'])) {
			//                $secure = (!empty($_SERVER['HTTPS']) && ($_SERVER['HTTPS']!='off')) || $_SERVER['SERVER_PORT']=='443';
			//                $scheme = ($secure ? 'https' : 'http') . '://' ;
			//
			//                $hostArr = explode(':', $_SERVER['HTTP_HOST']);
			//                $host = $hostArr[0];
			//                $port = isset(
			//                    $hostArr[1]) && (!$secure && $hostArr[1]!=80 || $secure && $hostArr[1]!=443
			//                ) ? ':'.$hostArr[1] : '';
			//                $path = Mage::app()->getRequest()->getBasePath();
			//
			//                $baseUrl = $scheme.$host.$port.rtrim($path, '/').'/';
			//            } else {
			String baseUrl = "http://localhost/";
			//            }

			Mage_Core_Model_Config_Options options = getOptions();
			_distroServerVars = new HashMap<String, String>();
			_distroServerVars.put("root_dir", options.getBaseDir());
			_distroServerVars.put("app_dir", options.getAppDir());
			_distroServerVars.put("var_dir", options.getVarDir());
			_distroServerVars.put("base_url", baseUrl);

			_substServerVars = new HashMap<String, String>();
			for (Entry<String, String> each : _distroServerVars.entrySet()) {
				_substServerVars.put("{{" + each.getKey() + "}}", each.getValue());
			}
		}

		return _distroServerVars;
	}

	/**
	* Is installed flag
	*/
	private Boolean _isInstalled;

	/**
	 * TODO 필요한 method 인지 확인 필요 및 다른 용도로 사용 가능할 지 검토.
	 * Retrieve application installation flag
	 */
	public boolean isInstalled(Map<String, Object> options) {
		if (_isInstalled == null) {
			String etcDir = options != null ? (String)options.get("etc_dir") : null;
			if (StringUtils.isEmpty(etcDir)) {
				etcDir = "etc";
			}
			File localConfigFile = new File(getOptions().getAppRoot() + "/" + etcDir, "local.xml");

			_isInstalled = false;

			if (localConfigFile.exists()) {
				Simplexml_Element localConfig = SimplexmlUtils.build(localConfigFile, this._elementClass);
				DateTime.date_default_timezone_set("UTC");
				String date = localConfig.selectSingle("global").selectSingle("install").getString("date");
				if (date != null) {
					try {
						PhpDateUtils.parseDate(date, false);
						_isInstalled = true;
					} catch (Exception ignore) {
					}
				}
			}
		}

		_isInstalled = true;

		return _isInstalled;
	}

	public void setIsInstalled(Map<String, Object> options) {
		_isInstalled = MapUtils.getBoolean(options, "is_installed", false);
	}

	public boolean isInstalled() {
		return isInstalled((Map<String, Object>)null);
	}

	public boolean isInstalled(String etcDir) {
		return isInstalled(Collections.singletonMap("etc_dir", (Object)etcDir));
	}

	/**
	 * Storage of validated secure urls
	 */
	protected Map<String, Boolean> _secureUrlCache = new HashMap<String, Boolean>();

	public Boolean shouldUrlBeSecure(String url) {
		if (!AppContext.getCurrent().getStoreConfigFlag(StoreHelper.XML_PATH_SECURE_IN_FRONTEND)) {
			return false;
		}

		Boolean secure = _secureUrlCache.get(url);
		if (secure == null) {
			secure = false;
			Simplexml_Element secureUrls = getNode("frontend/secure_url");
			for (Simplexml_Element match : secureUrls.children()) {
				if (url.startsWith(match.getText())) {
					secure = true;
					break;
				}
			}

			_secureUrlCache.put(url, secure);
		}

		return secure;
	}

	/**
	 * Get standard path variables.
	 *
	 * To be used in blocks, templates, etc.
	 *
	 * @param array|string $args Module name if string
	 * @return array
	 */
	public Map<String, String> getPathVars(/*$args=null*/) {
		Map<String, String> path = new HashMap<String, String>();
		StoreHelper helper = AppContext.getCurrent().getStore().getHelper();
		path.put("baseUrl", helper.getBaseUrl());
		path.put("baseSecureUrl", helper.getBaseUrl(UrlType.LINK, true));

		return path;
	}

	public boolean getIsDeveloperMode() {
		// TODO 사용 패턴 확인 및 선언 위치 재조정 필요.
		return false;
	}

	/**
	 * Active modules array per namespace
	 * @var array
	 */
	private Map<String, Map<String, String>> _moduleNamespaces = null;

	/**
	 * Determine whether provided name begins from any available modules, according to namespaces priority
	 * If matched, returns as the matched module "factory" name or a fully qualified module name
	 *
	 * @param string $name
	 * @param bool $asFullModuleName
	 * @return string
	 */
	public String determineOmittedNamespace(String name, boolean asFullModuleName/* = false*/) {
		if (null == _moduleNamespaces) {
			_moduleNamespaces = new HashMap<>();
			for (Simplexml_Element m : _xml.selectDescendants("modules/*")) {
				if ("true".equalsIgnoreCase(m.getString("active"))) {
					String moduleName = m.getName();
					String module = moduleName.toLowerCase();
					String namespaceName = module.substring(0, module.indexOf('_'));
					Map<String, String> namespace = _moduleNamespaces.get(namespaceName);
					if (namespace == null) {
						namespace = new HashMap<>();
						_moduleNamespaces.put(namespaceName, namespace);
					}

					namespace.put(module, moduleName);
				}
			}
		}

		String[] names = StringUtils.split(name.toLowerCase(), "_");
		int partsNum = names.length;
		boolean defaultNamespaceFlag = false;
		for (Entry<String, Map<String, String>> each : _moduleNamespaces.entrySet()) {
			String namespaceName = each.getKey();
			Map<String, String> namespace = each.getValue();

			// assume the namespace is omitted (default namespace only, which comes first)
			if (!defaultNamespaceFlag) {
				defaultNamespaceFlag = true;
				String defaultNS = namespaceName + '_' + names[0];
				String ns = namespace.get(defaultNS);
				if (ns != null) {
					return asFullModuleName ? ns : names[0]; // return omitted as well
				}
			}
			// assume namespace is qualified
			if (names.length > 1) {
				String fullNS = names[0] + '_' + names[1];
				String ns = namespace.get(fullNS);
				if (2 <= partsNum && ns != null) {
					return asFullModuleName ? ns : fullNS;
				}
			}
		}

		return "";
	}
}
