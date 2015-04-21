package com.naver.mage4j.core.mage.core.model.design;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.mage4j.core.mage.core.model.resource.design.DesignChangeRepository;

@Component
public class DesignContext {
	@Autowired
	private DesignChangeRepository designChangeRepository;

	@Autowired
	private Mage_Core_Model_Design_Package designPackage;

	public Mage_Core_Model_Design_Package getDesign() {
		return designPackage;
	}

	private static Map<String, Map<String, Boolean>> _regexMatchCache = new HashMap<String, Map<String, Boolean>>();
	private static Map<String, Object> _customThemeTypeCache = new HashMap<String, Object>();

	/**
	 * Return package name based on design exception rules
	 *
	 * @param array $rules - design exception rules
	 * @param string $regexpsConfigPath
	 * @return bool|string
	 */
	public static String getPackageByUserAgent(List<Map<String, Object>> rules, String userAgent, String regexpsConfigPath/*="path_mock"*/) {
		for (Map<String, Object> rule : rules) {
			String regexp = (String)rule.get("regexp");
			Map<String, Boolean> map = _regexMatchCache.get(regexp);
			if (map != null) {
				if (map.get(userAgent) != null) {
					String value = (String)rule.get("value");
					_customThemeTypeCache.put(regexpsConfigPath, value);
					return value;
				}
			}

			String regexpNom = regexp;
			if (!regexpNom.startsWith("/")) {
				regexpNom = "/" + regexpNom;
			}
			if (!regexpNom.endsWith("/")) {
				regexpNom = regexpNom + "/";
			}

			if (Pattern.compile(regexpNom).matcher(userAgent).matches()) {
				map = _regexMatchCache.get(regexp);
				if (map == null) {
					map = new HashMap<String, Boolean>();
					_regexMatchCache.put(regexp, map);
				}
				map.put(userAgent, true);
				_customThemeTypeCache.put(regexpsConfigPath, rule.get("value"));

				return (String)rule.get("value");
			}
		}

		return null;
	}
}
