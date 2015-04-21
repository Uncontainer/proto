package com.naver.mage4j.core.mage.core.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naver.mage4j.external.php.Functions;
import com.naver.mage4j.external.varien.Varien_Profiler;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;

@Component
public class ModelInstanceLoader {
	private final Logger log = LoggerFactory.getLogger(Mage_Core_Model_Config.class);

	@Autowired
	private Mage_Core_Model_Config modelConfig;

	/**
	 * Storage for generated class names
	 */
	private Map<String, Map<String, Map<String, String>>> _classNameCache = new HashMap<String, Map<String, Map<String, String>>>();

	/**
	 * Get resource model object by alias
	 */
	public Object getResourceModelInstance(String modelClass, Map<String, Object> constructArguments) {
		String factoryName = _getResourceModelFactoryClassName(modelClass);
		if (factoryName == null) {
			return null;
		}

		return getModelInstance(factoryName, constructArguments);
	}

	/**
	 * Get model class instance.
	 * 
	 * Example: $config->getModelInstance("catalog/product")
	 * 
	 * Will instantiate Mage_Catalog_Model_Mysql4_Product
	 */
	public Object getModelInstance(String modelClass, Object constructArguments) {
		String className = getModelClassName(modelClass);
		Varien_Profiler.start("CORE::create_object_of::" + className);
		try {
			return Class.forName(className).getConstructor(constructArguments.getClass()).newInstance(constructArguments);
		} catch (Exception e) {
			log.info("Fail to create instace.({})", new Object[] {modelClass, e});
			return null;
		} finally {
			Varien_Profiler.stop("CORE::create_object_of::" + className);
		}
	}

	/**
	 * Get factory class name for a resource
	 */
	private String _getResourceModelFactoryClassName(String modelClass) {
		String[] classArray = modelClass.split("/");
		if (classArray.length != 2) {
			return null;
		}

		String module = classArray[0];
		String model = classArray[1];

		Simplexml_Element moduleNode = modelConfig.selectDescendant("global/models/" + module);
		if (moduleNode.isNull()) {
			return null;
		}

		String resourceModel = moduleNode.getString("resourceModel");
		if (resourceModel == null) {
			return null;
		}

		return resourceModel + "/" + model;
	}

	/**
	 * Retrieve helper class name
	 */
	public String getHelperClassName(String helperName) {
		if (!helperName.contains("/")) {
			helperName += "/data";
		}

		return getGroupedClassName("helper", helperName, null);
	}

	/**
	 * Retrieve block class name
	 */
	public String getBlockClassName(String blockType) {
		if (!blockType.contains("/")) {
			return blockType;
		}

		return getGroupedClassName("block", blockType, null);
	}

	public String getClassName(Simplexml_Element config) {
		String modelClass = config.getString("class");
		if (modelClass == null) {
			modelClass = config.getString("model");
		}

		if (modelClass == null) {
			return null;
		}

		return getModelClassName(modelClass);
	}

	/**
	 * Retrieve module class name
	 */
	public String getModelClassName(String modelClass) {
		modelClass = modelClass.trim();
		if (!modelClass.contains("/")) {
			return modelClass;
		}

		return getGroupedClassName("model", modelClass, null);
	}

	/**
	 * Retrieve class name by class group
	 */
	private String getGroupedClassName(String groupType, String classId, String groupRootNodeName) {
		if (groupRootNodeName == null) {
			groupRootNodeName = "global/" + groupType + "s";
		}

		String[] classArr = classId.trim().split("/");
		String groupNodeName = classArr[0];

		String classNodeName = classArr.length > 1 ? classArr[1] : null;

		Map<String, Map<String, String>> groupMap = _classNameCache.get(groupRootNodeName);
		if (groupMap != null) {
			Map<String, String> classNameMap = groupMap.get(groupNodeName);
			if (classNameMap != null) {
				String result = classNameMap.get(classNodeName);
				if (result != null) {
					return result;
				}
			}
		}

		Simplexml_Element config = modelConfig.selectDescendant("global/" + groupType + "s/" + groupNodeName);

		// First - check maybe the entity class was rewritten
		// TODO getSingle에 대한 null 처리 추가.
		String className = config.selectSingle("rewrite").getString(classNodeName);
		if (className == null) {
			/**
			 * Backwards compatibility for pre-MMDB extensions. In MMDB release resource nodes <..._mysql4> were renamed to <..._resource>. So <deprecatedNode> is left to keep name of previously used
			 * nodes, that still may be used by non-updated extensions.
			 */
			String deprecatedNode = config.getString("deprecatedNode");
			if (deprecatedNode != null) {
				// TODO getSingle에 대한 null 처리 추가.
				Simplexml_Element configOld = config.selectDescendant("global/" + groupType + "s/" + deprecatedNode);
				className = configOld.selectSingle("rewrite").getString(classNodeName);
			}
		}

		// Second - if entity is not rewritten then use class prefix to form class name
		if (StringUtils.isBlank(className)) {
			if (!config.isNull()) {
				className = getClassName(config);
			}

			if (StringUtils.isBlank(className)) {
				className = "mage_" + groupNodeName + "_" + groupType;
			}

			if (StringUtils.isNotBlank(classNodeName)) {
				className += "_" + classNodeName;
			}

			// TODO 실제 class로 변환 로직 추가.
			className = Functions.uc_words(className);
		}

		groupMap = _classNameCache.get(groupRootNodeName);
		if (groupMap == null) {
			groupMap = new HashMap<String, Map<String, String>>();
			_classNameCache.put(groupRootNodeName, groupMap);
		}

		Map<String, String> classNameMap = groupMap.get(groupNodeName);
		if (classNameMap == null) {
			classNameMap = new HashMap<String, String>();
			groupMap.put(groupNodeName, classNameMap);
		}

		classNameMap.put(classNodeName, className);

		return className;
	}
}
