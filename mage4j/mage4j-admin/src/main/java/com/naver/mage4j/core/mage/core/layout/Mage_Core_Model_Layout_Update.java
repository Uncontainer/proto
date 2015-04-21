package com.naver.mage4j.core.mage.core.layout;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.Mage_Core_Model_App;
import com.naver.mage4j.core.mage.core.model.ModelInstanceLoader;
import com.naver.mage4j.core.mage.core.model.app.Mage_Core_Model_App_Area.AreaType;
import com.naver.mage4j.core.mage.core.model.design.Mage_Core_Model_Design_Package;
import com.naver.mage4j.core.mage.core.model.design.ThemeType;
import com.naver.mage4j.core.mage.core.model.resource.store.StoreContext;
import com.naver.mage4j.external.php.Standard;
import com.naver.mage4j.external.varien.Varien_Profiler;
import com.naver.mage4j.external.varien.simplexml.SimplexmlUtils;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element_JDom;

public class Mage_Core_Model_Layout_Update {
	/**
	 * Additional tag for cleaning layout cache convenience
	 */
	public static final String LAYOUT_GENERAL_CACHE_TAG = "LAYOUT_GENERAL_CACHE_TAG";

	/**
	 * Layout Update Simplexml Element Class Name
	 *
	 * @var String
	 */
	protected String _elementClass;

	/**
	 * @var Simplexml_Element
	 */
	protected Simplexml_Element _packageLayout;

	/**
	 * Cache key
	 */
	protected String _cacheId;

	/**
	 * Cache prefix
	 */
	protected String _cachePrefix;

	/**
	 * Cumulative array of update XML Strings
	 */
	protected List<String> _updates = new ArrayList<String>();

	/**
	 * Handles used in this update
	 */
	protected Map<String, Integer> _handles = new HashMap<String, Integer>();

	/**
	 * Substitution values in structure array("from"=>array(), "to"=>array())
	 */
	protected Map<String, List<String>> _subst = new HashMap<String, List<String>>();

	@Autowired
	private ModelInstanceLoader modelInstanceLoader;

	public Mage_Core_Model_Layout_Update() {
		Map<String, String> subst = AppContext.getCurrent().getConfig().getPathVars();
		for (Entry<String, String> each : subst.entrySet()) {
			List<String> from = _subst.get("from");
			if (from == null) {
				from = new ArrayList<String>();
				_subst.put("from", from);
			}
			from.add("{{" + each.getKey() + "}}");

			List<String> to = _subst.get("to");
			if (to == null) {
				to = new ArrayList<String>();
				_subst.put("to", to);
			}
			to.add(each.getValue());
		}
	}

	public String getElementClass() {
		if (_elementClass == null) {
			_elementClass = modelInstanceLoader.getModelClassName("core/layout_element");
		}

		return _elementClass;
	}

	public void resetUpdates() {
		_updates = new ArrayList<String>();
	}

	public void addUpdate(String update) {
		_updates.add(update);
	}

	public List<String> asArray() {
		return _updates;
	}

	public String asString() {
		return StringUtils.join(_updates, "");
	}

	public void resetHandles() {
		_handles = new HashMap<String, Integer>();
	}

	public void addHandle(String handle) {
		_handles.put(handle, 1);
	}

	public void addHandles(List<String> handles) {
		for (String handle : handles) {
			addHandle(handle);
		}
	}

	public void removeHandle(String handle) {
		_handles.remove(handle);
	}

	public List<String> getHandles() {
		return new ArrayList<String>(_handles.keySet());
	}

	/**
	 * Get cache id
	 *
	 * @return String
	 */
	public String getCacheId() {
		if (_cacheId == null) {
			_cacheId = "LAYOUT_" + AppContext.getCurrent().getStore().getStoreId() + Standard.md5(StringUtils.join(getHandles(), "__"));
		}
		return _cacheId;
	}

	/**
	 * Set cache id
	 */
	public void setCacheId(String cacheId) {
		_cacheId = cacheId;
	}

	public boolean loadCache() {
		//        if (!app.useCache("layout")) {
		//            return false;
		//        }
		//
		//        if (!$result = app.loadCache($this->getCacheId())) {
		//            return false;
		//        }
		//
		//        $this->addUpdate($result);
		//
		//        return true;
		return false;
	}

	public boolean saveCache() {
		//        if (!app.useCache("layout")) {
		//            return false;
		//        }
		//        $str = $this->asString();
		//        $tags = $this->getHandles();
		//        $tags[] = self::LAYOUT_GENERAL_CACHE_TAG;
		//        return app.saveCache($str, $this->getCacheId(), $tags, null);
		return false;
	}

	/**
	 * Load layout updates by handles
	 * @param array|string $handles
	 */
	public void load(List<String> handles) {
		if (handles != null) {
			for (String handle : handles) {
				addHandle(handle);
			}
		}

		if (loadCache()) {
			return;
		}

		for (String handle : getHandles()) {
			merge(handle);
		}

		saveCache();
	}

	public Simplexml_Element_JDom asSimplexml() {
		String xml = "<?xml version=\"1.0\"?><layout>" + asString() + "</layout>";
		return SimplexmlUtils.build(xml, getElementClass());
	}

	/**
	 * Merge layout update by handle
	 */
	public void merge(String handle) {
		fetchPackageLayoutUpdates(handle);
		if (AppContext.getCurrent().getConfig().isInstalled()) {
			fetchDbLayoutUpdates(handle);
		}
	}

	public void fetchFileLayoutUpdates() {
		Mage_Core_Model_App app = AppContext.getCurrent();
		Short storeId = app.getStore().getStoreId();
		String elementClass = getElementClass();
		Mage_Core_Model_Design_Package design = app.getDesignPackage();
		//		String cacheKey = "LAYOUT_" + design.getArea().getCode() + "_STORE" + storeId + "_" + design.getPackageName() + "_" + design.getTheme(ThemeType.LAYOUT);
		//
		//		List<String> cacheTags = Arrays.asList(LAYOUT_GENERAL_CACHE_TAG);
		String layoutStr = null;
		//		if (app.useCache("layout") && (layoutStr = (String)app.loadCache(cacheKey)) != null) {
		//			_packageLayout = SimplexmlUtils.build(layoutStr, elementClass);
		//		}
		if (layoutStr == null) {
			_packageLayout = getFileLayoutUpdatesXml(design.getArea(), design.getPackageName(), design.getTheme(ThemeType.LAYOUT), storeId);
			//			if (app.useCache("layout")) {
			//				app.saveCache(_packageLayout.asXml(), cacheKey, cacheTags, null);
			//			}
		}
	}

	public boolean fetchPackageLayoutUpdates(String handle) {
		String _profilerKey = "layout/package_update: " + handle;
		Varien_Profiler.start(_profilerKey);
		if (_packageLayout == null) {
			fetchFileLayoutUpdates();
		}

		for (Simplexml_Element updateXml : _packageLayout.selectAll(handle)) {
			fetchRecursiveUpdates(updateXml);
			addUpdate(SimplexmlUtils.innerHtml(updateXml));
		}
		Varien_Profiler.stop(_profilerKey);

		return true;
	}

	public boolean fetchDbLayoutUpdates(String handle)
	{
		String _profilerKey = "layout/db_update: " + handle;
		Varien_Profiler.start(_profilerKey);
		String updateStr = _getUpdateString(handle);
		if (updateStr == null) {
			return false;
		}
		updateStr = "<update_xml>" + updateStr + "</update_xml>";
		updateStr = Standard.str_replace(_subst.get("from"), _subst.get("to"), updateStr);
		Simplexml_Element updateXml = SimplexmlUtils.build(updateStr, getElementClass());
		fetchRecursiveUpdates(updateXml);
		addUpdate(SimplexmlUtils.innerHtml(updateXml));

		Varien_Profiler.stop(_profilerKey);
		return true;
	}

	/**
	 * Get update String
	 *
	 * @param String $handle
	 * @return mixed
	 */
	protected String _getUpdateString(String handle) {
		//        return Mage::getResourceModel("core/layout")->fetchUpdatesByHandle(handle); /*Mage_Core_Model_Resource_Layout::fetchUpdatesByHandle*/
		throw new UnsupportedOperationException();
	}

	public void fetchRecursiveUpdates(Simplexml_Element updateXml) {
		for (Simplexml_Element child : updateXml.children()) {
			if ("update".equals(child.getName())) {
				String handle = child.getString("handle");
				if (handle == null) {
					continue;
				}

				merge(handle);
				// Adding merged layout handle to the list of applied hanles
				addHandle(handle);
			}
		}
	}

	/**
	 * Collect and merge layout updates from file
	 *
	 * @param String $area
	 * @param String $package
	 * @param String $theme
	 * @param integer|null $storeId
	 * @return Mage_Core_Model_Layout_Element
	 */
	public Simplexml_Element getFileLayoutUpdatesXml(AreaType area, String packageName, String theme, Short storeId) {
		Mage_Core_Model_App app = AppContext.getCurrent();
		if (null == storeId) {
			storeId = app.getStore().getStoreId();
		}

		/* @var $design Mage_Core_Model_Design_Package */
		Mage_Core_Model_Design_Package design = app.getDesignPackage();
		String elementClass = getElementClass();
		Simplexml_Element updatesRoot = app.getConfig().getNode(area.getCode() + "/layout/updates");
		app.getEventDispatcher().dispatchEvent("core_layout_update_updates_get_after", Collections.singletonMap("updates", (Object)updatesRoot));
		List<Simplexml_Element> updates = updatesRoot.children();
		Simplexml_Element themeUpdates = app.getDesignConfig().getNode(area.getCode() + "/" + packageName + "/" + theme + "/layout/updates");
		updates.addAll(themeUpdates.children());
		List<String> updateFiles = new ArrayList<String>();
		for (Simplexml_Element updateNode : updates) {
			String file = updateNode.getString("file");
			if (StringUtils.isNotEmpty(file)) {
				String module = updateNode.getAttribute("module");
				if (StringUtils.isNotEmpty(module) && StoreContext.getContext().getById(storeId).getHelper().getConfigFlag("advanced/modules_disable_output/" + module)) {
					continue;
				}
				updateFiles.add(file);
			}
		}
		// custom local layout updates file - load always last
		updateFiles.add("local.xml");
		String layoutStr = "";
		for (String updateFile : updateFiles) {
			Mage_Core_Model_Design_Package.Param params = new Mage_Core_Model_Design_Package.Param();
			params.setArea(area);
			params.setPackageName(packageName);
			params.setTheme(theme);
			String filename = design.getLayoutFilename(updateFile, params);
			File file = new File(filename);
			if (!file.exists() || !file.canRead()) {
				continue;
			}

			String fileStr;
			try {
				fileStr = IOUtils.toString(new FileInputStream(file));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			fileStr = Standard.str_replace(_subst.get("from"), _subst.get("to"), fileStr);
			Simplexml_Element fileXml = SimplexmlUtils.build(fileStr, elementClass);
			//			if (!(fileXml instanceof Simplexml_Element)) {
			//				continue;
			//			}
			layoutStr += SimplexmlUtils.innerHtml(fileXml);
		}

		return SimplexmlUtils.build("<layouts>" + layoutStr + "</layouts>", elementClass);
	}
}
