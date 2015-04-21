package com.naver.mage4j.core.mage.core.model.design;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.Mage_Core_Exception;
import com.naver.mage4j.core.mage.core.model.app.Mage_Core_Model_App_Area.AreaType;
import com.naver.mage4j.core.mage.core.model.resource.store.Store;

@Component
public class Mage_Core_Model_Design_Fallback {
	@Autowired
	private Mage_Core_Model_Design_Config _config;

	protected Store _store = null;

	protected Map<String, List<Scheme>> _cachedSchemes = new HashMap<String, List<Scheme>>();

	/**
	 * Used to find circular dependencies
	 *
	 * @var array
	 */
	protected Set<String> _visited = new HashSet<String>();

	public Mage_Core_Model_Design_Fallback(/*Mage_Core_Model_Design_Config config*/) {
		//		if (config == null) {
		//			throw new IllegalArgumentException();
		//		}
		//
		//		this._config = config;
	}

	/**
	 * Retrieve store
	 *
	 * @return Mage_Core_Model_Store
	 */
	public Store getStore() {
		if (_store == null) {
			return AppContext.getCurrent().getStore();
		}
		return _store;
	}

	/**
	 * @param $store string|integer|Mage_Core_Model_Store
	 * @return $this
	 */
	public void setStore(Store store) {
		if (store == null) {
			store = AppContext.getCurrent().getStore();
		}
		_store = store;
		_cachedSchemes = new HashMap<String, List<Scheme>>();
	}

	/**
	 * Get fallback scheme
	 *
	 * @param string $area
	 * @param string packageName
	 * @param string $theme
	 * @return array
	 */
	public List<Scheme> getFallbackScheme(AreaType area, String packageName, String theme) {
		String cacheKey = area.getCode() + "/" + packageName + "/" + theme;

		List<Scheme> result = _cachedSchemes.get(cacheKey);
		if (result == null) {
			if (_isInheritanceDefined(area, packageName, theme)) {
				result = _getFallbackScheme(area, packageName, theme);
			} else {
				result = _getLegacyFallbackScheme();
			}

			_cachedSchemes.put(cacheKey, result);
		}

		return result;
	}

	/**
	 * Get fallback scheme according to theme config
	 *
	 * @param string $area
	 * @param string packageName
	 * @param string $theme
	 * @return array
	 * @throws Mage_Core_Exception
	 */
	protected List<Scheme> _getFallbackScheme(AreaType area, String packageName, String theme) {
		List<Scheme> result = new ArrayList<Mage_Core_Model_Design_Fallback.Scheme>();
		_visited = new HashSet<String>();
		String parent;
		while ((parent = _config.getNode(area.getCode() + "/" + packageName + "/" + theme + "/parent").getText()) != null) {
			_checkVisited(area, packageName, theme);

			String[] parts = StringUtils.split(parent, "/");
			if (parts.length != 2) {
				throw new Mage_Core_Exception("Parent node should be defined as \"packageName/theme\"");
			}

			result.add(new Scheme(parts[0], parts[1]));
		}

		return result;
	}

	public static class Scheme {
		String packageName;
		String theme;

		public Scheme(String packageName, String theme) {
			super();
			this.packageName = packageName;
			this.theme = theme;
		}
	}

	/**
	 * Prevent circular inheritance
	 *
	 * @param string $area
	 * @param string packageName
	 * @param string $theme
	 * @throws Mage_Core_Exception
	 * @return array
	 */
	protected void _checkVisited(AreaType area, String packageName, String theme) {
		String path = area.getCode() + "/" + packageName + "/" + theme;
		if (_visited.contains(path)) {
			throw new Mage_Core_Exception("Circular inheritance in theme " + packageName + "/" + theme);
		}
		_visited.add(path);
	}

	/**
	 * Get fallback scheme when inheritance is not defined (backward compatibility)
	 *
	 * @return array
	 */
	protected List<Scheme> _getLegacyFallbackScheme() {
		return Arrays.asList(new Scheme(null, null),
			new Scheme(null, _getFallbackTheme()),
			new Scheme(null, Mage_Core_Model_Design_Package.DEFAULT_THEME));
	}

	/**
	 * Default theme getter
	 */
	protected String _getFallbackTheme() {
		return getStore().getHelper().getConfigAsString("design/theme/default", null);
	}

	/**
	 * Check if inheritance defined in theme config
	 *
	 * @param $area
	 * @param packageName
	 * @param $theme
	 * @return bool
	 */
	protected boolean _isInheritanceDefined(AreaType area, String packageName, String theme) {
		String path = area.getCode() + "/" + packageName + "/" + theme + "/parent";
		return !_config.getNode(path).isNull();
	}
}
