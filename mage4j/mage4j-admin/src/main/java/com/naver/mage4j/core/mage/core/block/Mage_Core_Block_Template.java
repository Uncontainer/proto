package com.naver.mage4j.core.mage.core.block;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Layout;
import com.naver.mage4j.core.mage.core.model.app.Mage_Core_Model_App_Area.AreaType;
import com.naver.mage4j.core.mage.core.model.design.Mage_Core_Model_Design_Package;
import com.naver.mage4j.core.mage.core.model.resource.store.Store;

/**
 * Base html block
 */
public class Mage_Core_Block_Template extends Mage_Core_Block_Abstract {
	private final Logger log = LoggerFactory.getLogger(Mage_Core_Block_Template.class);

	/**
	 * Base html block
	 */
	public static final String XML_PATH_DEBUG_TEMPLATE_HINTS = "dev/debug/template_hints";

	/**
	 * Base html block
	 */
	public static final String XML_PATH_DEBUG_TEMPLATE_HINTS_BLOCKS = "dev/debug/template_hints_blocks";

	/**
	 * Base html block
	 */
	public static final String XML_PATH_TEMPLATE_ALLOW_SYMLINK = "dev/template/allow_symlink";

	/**
	 * View scripts directory
	 */
	protected String _viewDir = "";

	/**
	 * Assigned variables for view
	 */
	protected Map _viewVars = Collections.emptyMap();

	/**
	 * Assigned variables for view
	 */
	protected String _baseUrl;

	/**
	 * Assigned variables for view
	 */
	protected String _jsUrl;

	/**
	 * Is allowed symlinks flag
	 */
	protected Boolean _allowSymlinks = null;

	/**
	 * Is allowed symlinks flag
	 */
	protected static Boolean _showTemplateHints;

	/**
	 * Is allowed symlinks flag
	 */
	protected static boolean _showTemplateHintsBlocks;

	/**
	 * Path to template file in theme.
	 */
	protected String _template;

	/**
	 * Internal constructor, that is called from real constructor
	 * 
	 */
	protected Mage_Core_Block_Template(Map<String, Object> args) {
		super(args);
		if (this.hasData("template")) {
			this.setTemplate((String)this.getData("template"));
		}

	}

	/**
	 * Get relevant path to template
	 * 
	 * @return string
	 */
	public String getTemplate() {
		return this._template;
	}

	/**
	 * Set path to template used for generating block's output.
	 * 
	 * @param template
	 * @return Mage_Core_Block_Template
	 */
	@Override
	public Mage_Core_Block_Template setTemplate(String template) {
		this._template = template;
		return this;
	}

	/**
	 * Get absolute path to template
	 * 
	 * @return string
	 */
	public String getTemplateFile() {
		Mage_Core_Model_Design_Package.Param param = new Mage_Core_Model_Design_Package.Param();
		param.setRelative(true);
		String area = this.getArea();
		if (area != null) {
			param.setArea(AreaType.fromCode(area));
		}

		return AppContext.getCurrent().getDesignPackage().getTemplateFilename(this.getTemplate(), param);
	}

	/**
	 * Get design area
	 * 
	 * @return string
	 */
	public String getArea() {
		return (String)this._getData("area");
	}

	/**
	 * Assign variable
	 * 
	 * @param key
	 * @param value
	 * @return Mage_Core_Block_Template
	 */
	public Mage_Core_Block_Template assign(String key, Object value/* = null */) {
		this._viewVars.put(key, value);

		return this;
	}

	public Mage_Core_Block_Template assign(Map<String, Object> value) {
		for (Map.Entry<String, Object> each : value.entrySet()) {
			this.assign(each.getKey(), each.getValue());
		}

		return this;
	}

	/**
	 * Set template location directory
	 * 
	 * @param dir
	 * @return Mage_Core_Block_Template
	 */
	//	public Mage_Core_Block_Template setScriptPath(String dir) {
	//		String scriptPath = realpath(dir);
	//		String designDir = AppContext.getCurrent().getConfig().getBaseDir("design");
	//		if (scriptPath.indexOf(realpath(designDir)) == 0 || this._getAllowSymlinks()) {
	//			this._viewDir = dir;
	//		} else {
	//			log.error("Not valid script path: {}", dir);
	//		}
	//
	//		return this;
	//	}

	/**
	 * Check if direct output is allowed for block
	 * 
	 * @return bool
	 */
	public boolean getDirectOutput() {
		Mage_Core_Model_Layout layout = this.getLayout();
		if (layout != null) {
			return layout.getDirectOutput();
		}

		return false;
	}

	/**
	 * Check if direct output is allowed for block
	 * 
	 * @return bool
	 */
	public boolean getShowTemplateHints() {
		if (_showTemplateHints == null) {
			boolean devAllowed = AppContext.getCurrent().getHelperData().isDevAllowed((Store)null);
			_showTemplateHints = AppContext.getCurrent().getStoreConfigAsBoolean(XML_PATH_DEBUG_TEMPLATE_HINTS) && devAllowed;
			_showTemplateHintsBlocks = AppContext.getCurrent().getStoreConfigAsBoolean(XML_PATH_DEBUG_TEMPLATE_HINTS_BLOCKS) && devAllowed;
		}

		return _showTemplateHints;
	}

	/**
	 * Retrieve block view from file (template)
	 * 
	 * @param fileName
	 * @return string
	 */
	//	public String fetchView(String fileName) {
	//		Varien_Profiler.start(fileName);
	//		extract(this._viewVars, Flags.EXTR_SKIP);
	//		boolean _do = this.getDirectOutput();
	//		if (!(_do)) {
	//			EchoBuffer.init();
	//		}
	//
	//		if (this.getShowTemplateHints()) {
	//			EchoBuffer.append("<div style=\"position:relative; border:1px dotted red; margin:6px 2px; padding:18px 2px 2px 2px; zoom:1;\">" +
	//				"<div style=\"position:absolute; left:0; top:0; padding:2px 5px; background:red; color:white; font:normal 11px Arial;" +
	//				"text-align:left !important; z-index:998;\" onmouseover=\"this.style.zIndex='999'\"" +
	//				"onmouseout=\"this.style.zIndex='998'\" title=\"" + fileName + "\">" + fileName + "</div>");
	//
	//			if (_showTemplateHintsBlocks) {
	//				String thisClass = this.getClass().getSimpleName();
	//				EchoBuffer.append("<div style=\"position:absolute; right:0; top:0; padding:2px 5px; background:red; color:blue; font:normal 11px Arial;" +
	//					"text-align:left !important; z-index:998;\" onmouseover=\"this.style.zIndex='999'\" onmouseout=\"this.style.zIndex='998'\"" +
	//					"title=\"" + thisClass + "\">" + thisClass + "</div>");
	//				;
	//			}
	//		}
	//
	//		try {
	//			String includeFilePath = realpath(this._viewDir + DS + fileName);
	//			if ((includeFilePath.indexOf(realpath(this._viewDir)) == 0) || this._getAllowSymlinks()) {
	//				require $includeFilePath;
	//			} else {
	//				log.error("Not valid template file:{}", fileName);
	//			}
	//
	//		} catch (Exception e) {
	//			EchoBuffer.getAndClean();
	//			throw e;
	//		}
	//
	//		if (this.getShowTemplateHints()) {
	//			EchoBuffer.append("</div>");
	//		}
	//
	//		String html;
	//		if (!_do) {
	//			html = EchoBuffer.getAndClean();
	//		} else {
	//			html = "";
	//		}
	//
	//		Varien_Profiler.stop(fileName);
	//		return html;
	//	}

	/**
	 * Render block
	 * 
	 * @return string
	 */
	//	public String renderView() {
	//		this.setScriptPath(AppContext.getCurrent().getConfig().getBaseDir("design"));
	//		return this.fetchView(this.getTemplateFile());
	//	}

	/**
	 * Render block HTML
	 * 
	 * @return string
	 */
	//	@Override
	//	protected String _toHtml() {
	//		if (this.getTemplate() == null) {
	//			return "";
	//		}
	//
	//		return this.renderView();
	//	}

	/**
	 * Get base url of the application
	 * 
	 * @return string
	 */
	public String getBaseUrl() {
		if (this._baseUrl == null) {
			this._baseUrl = AppContext.getCurrent().getStore().getHelper().getBaseUrl();
		}

		return this._baseUrl;
	}

	/**
	 * Get url of base javascript fileTo get url of skin javascript file use getSkinUrl()
	 * 
	 * @param fileName
	 * @return string
	 */
	public String getJsUrl(String fileName/* = "" */) {
		if (this._jsUrl == null) {
			this._jsUrl = AppContext.getCurrent().getStore().getHelper().getBaseUrl();
		}

		return this._jsUrl + fileName;
	}

	/**
	 * Get data from specified object
	 * 
	 * @param object
	 * @param key
	 * @return mixed
	 */
	//	public Object getObjectData(Varien_Object object, String key) {
	//		return object.getDataUsingMethod(key);
	//	}

	/**
	 * Get cache key informative items
	 * 
	 * @return array
	 */
	@Override
	public List<String> getCacheKeyInfo() {
		return Arrays.asList("BLOCK_TPL", AppContext.getCurrent().getStore().getCode(), this.getTemplateFile(), this.getTemplate()/* 'template' => $this->getTemplate() */);
	}

	/**
	 * Get is allowed symliks flag
	 * 
	 * @return bool
	 */
	protected boolean _getAllowSymlinks() {
		if (this._allowSymlinks == null) {
			this._allowSymlinks = AppContext.getCurrent().getStoreConfigFlag(XML_PATH_TEMPLATE_ALLOW_SYMLINK);
		}

		return this._allowSymlinks;
	}

}