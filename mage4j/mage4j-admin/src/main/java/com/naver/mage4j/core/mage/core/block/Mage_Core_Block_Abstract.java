package com.naver.mage4j.core.mage.core.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.core.mage.MageInstanceLoader;
import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.Mage_Core_Exception;
import com.naver.mage4j.core.mage.core.Mage_Core_Model_App;
import com.naver.mage4j.core.mage.core.controller.request.Mage_Core_Controller_Request_Http;
import com.naver.mage4j.core.mage.core.controller.varien.Mage_Core_Controller_Varien_Action;
import com.naver.mage4j.core.mage.core.helper.Mage_Core_Helper_Abstract;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Factory;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Layout;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Session;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Translate;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Url;
import com.naver.mage4j.core.mage.core.model.Mage_Core_Model_Url.RouteParams;
import com.naver.mage4j.core.mage.core.model.ModelLoader;
import com.naver.mage4j.core.mage.core.model.design.Mage_Core_Model_Design_Package.Param;
import com.naver.mage4j.core.mage.core.model.event.Callback;
import com.naver.mage4j.core.mage.core.model.event.EventDispatcher;
import com.naver.mage4j.core.mage.core.model.translate.Mage_Core_Model_Translate_Expr;
import com.naver.mage4j.core.util.JacksonUtil;
import com.naver.mage4j.external.php.Standard;
import com.naver.mage4j.external.varien.Varien_Object;
import com.naver.mage4j.external.varien.Varien_Profiler;

/**
 * Base Content Block class
 *
 * For block generation you must define Data source class, data source class method,
 * parameters array and block template
 *
 * @category   Mage
 * @package    Mage_Core
 * @author     Magento Core Team <core@magentocommerce.com>
 */
public abstract class Mage_Core_Block_Abstract extends Varien_Object {
	/**
	 * Cache group Tag
	 */
	public static final String CACHE_GROUP = "block_html";

	/**
	 * Cache tags data key
	 */
	public static final String CACHE_TAGS_DATA_KEY = "cache_tags";

	/**
	 * Block name in layout
	 */
	protected String _nameInLayout;

	/**
	 * Parent layout of the block
	 */
	protected Mage_Core_Model_Layout _layout;

	/**
	 * Parent block
	 */
	protected Mage_Core_Block_Abstract _parent;

	/**
	 * Short alias of this block that was refered from parent
	 */
	protected String _alias;

	/**
	 * Suffix for name of anonymous block
	 */
	protected String _anonSuffix;

	/**
	 * Contains references to child block objects
	 *
	 * @var array
	 */
	protected Map<String, Mage_Core_Block_Abstract> _children = new HashMap<>();

	/**
	 * Sorted children list
	 *
	 * @var array
	 */
	protected List<String> _sortedChildren = new ArrayList<>();

	/**
	 * Children blocks HTML cache array
	 *
	 * @var array
	 */
	protected Map<String, String> _childrenHtmlCache = new HashMap<>();

	/**
	 * Arbitrary groups of child blocks
	 *
	 * @var array
	 */
	protected Map<String, List<String>> _childGroups = new HashMap<>();

	/**
	 * Request object
	 *
	 * @var Zend_Controller_Request_Http
	 */
	protected Mage_Core_Controller_Request_Http _request;

	/**
	 * Messages block instance
	 *
	 * @var Mage_Core_Block_Messages
	 */
	protected Mage_Core_Block_Messages _messagesBlock = null;

	/**
	 * Whether this block was not explicitly named
	 *
	 * @var boolean
	 */
	protected boolean _isAnonymous = false;

	/**
	 * Parent block
	 */
	protected Mage_Core_Block_Abstract _parentBlock;

	/**
	 * Block html frame open tag
	 */
	protected String _frameOpenTag;

	/**
	 * Block html frame close tag
	 * @var string
	 */
	protected String _frameCloseTag;

	/**
	 * Url object
	 *
	 * @var Mage_Core_Model_Url
	 */
	protected static Mage_Core_Model_Url _urlModel;

	/**
	 * @var Varien_Object
	 */
	private static Varien_Object _transportObject;

	/**
	 * Array of block sort priority instructions
	 *
	 * @var array
	 */
	protected Map<String, Instruction> _sortInstructions = new HashMap<>();

	/**
	 * Factory instance
	 *
	 * @var Mage_Core_Model_Factory
	 */
	protected Mage_Core_Model_Factory _factory;

	/**
	 * Application instance
	 *
	 * @var Mage_Core_Model_App
	 */
	protected Mage_Core_Model_App _app;

	static class Instruction {
		String siblingName;
		boolean after;
		boolean exists;

		public Instruction(String siblingName, boolean after, boolean exists) {
			super();
			this.siblingName = siblingName;
			this.after = after;
			this.exists = exists;
		}
	}

	/**
	 * Initialize factory instance
	 */
	public Mage_Core_Block_Abstract(Map<String, Object> args) {
		super(args);
		_factory = (Mage_Core_Model_Factory)args.get("core_factory");
		_app = (Mage_Core_Model_App)args.get("app");
	}

	/**
	 * Retrieve factory instance
	 */
//	protected Mage_Core_Model_Factory _getFactory()    {
//        return _factory == null ? Mage::getSingleton("core/factory") : _factory;
//    }

	/**
	 * Retrieve application instance
	 */
	protected Mage_Core_Model_App _getApp() {
		return _app == null ? AppContext.getCurrent() : _app;
	}

	/**
	 * Retrieve request object
	 *
	 * @return Mage_Core_Controller_Request_Http
	 * @throws Exception
	 */
	public Mage_Core_Controller_Request_Http getRequest() {
		_request = AppContext.getCurrent().getRequest();
		if (_request == null) {
			throw new Mage_Core_Exception("Can't retrieve request object");
		}

		return _request;
	}

	/**
	 * Retrieve parent block
	 */
	public Mage_Core_Block_Abstract getParentBlock() {
		return _parentBlock;
	}

	/**
	 * Set parent block
	 */
	public void setParentBlock(Mage_Core_Block_Abstract block) {
		_parentBlock = block;
	}

	/**
	 * Retrieve current action object
	 */
	public Mage_Core_Controller_Varien_Action getAction() {
		return _getApp().getFrontController().getAction();
	}

	/**
	 * Set layout object
	 */
	public void setLayout(Mage_Core_Model_Layout layout) {
		_layout = layout;
		EventDispatcher eventDispatcher = AppContext.getCurrent().getEventDispatcher();
		eventDispatcher.dispatchEvent("core_block_abstract_prepare_layout_before", Collections.singletonMap("block", this));
		_prepareLayout();
		eventDispatcher.dispatchEvent("core_block_abstract_prepare_layout_after", Collections.singletonMap("block", this));
	}

	/**
	 * Preparing global layout
	 *
	 * You can redefine this method in child classes for changing layout
	 */
	protected void _prepareLayout() {
	}

	/**
	 * Retrieve layout object
	 */
	public Mage_Core_Model_Layout getLayout() {
		return _layout;
	}

	/**
	 * Check if block is using auto generated (Anonymous) name
	 */
	public boolean getIsAnonymous()
	{
		return _isAnonymous;
	}

	/**
	 * Set the anonymous flag
	 */
	public void setIsAnonymous(boolean flag) {
		_isAnonymous = flag;
	}

	/**
	 * Returns anonymous block suffix
	 *
	 * @return string
	 */
	public String getAnonSuffix() {
		return _anonSuffix;
	}

	/**
	 * Set anonymous suffix for current block
	 */
	public void setAnonSuffix(String suffix) {
		_anonSuffix = suffix;
	}

	/**
	 * Returns block alias
	 */
	public String getBlockAlias()
	{
		return _alias;
	}

	/**
	 * Set block alias
	 */
	public void setBlockAlias(String alias) {
		_alias = alias;
	}

	/**
	 * Set block's name in layout and unsets previous link if such exists.
	 */
	public void setNameInLayout(String name) {
		if (StringUtils.isNotEmpty(_nameInLayout) && getLayout() != null) {
			getLayout().unsetBlock(_nameInLayout);
			getLayout().setBlock(name, this);
		}
		_nameInLayout = name;
	}

	/**
	 * Retrieve sorted list of children.
	 *
	 * @return array
	 */
	public List<String> getSortedChildren() {
		sortChildren(false);
		return _sortedChildren;
	}

	/**
	 * Set block attribute value
	 *
	 * Wrapper for method "setData"
	 */
	public void setAttribute(String name, String value/* = null*/) {
		setData(name, value);
	}

	public void setChild(String alias, String blockName) {
		Mage_Core_Block_Abstract block = getLayout().getBlock(blockName);
		if (block == null) {
			return;
		}

		setChild(alias, block);
	}

	/**
	 * Set child block
	 */
	public void setChild(String alias, Mage_Core_Block_Abstract block) {
		if (block.getIsAnonymous()) {
			String suffix = getAnonSuffix();
			if (StringUtils.isEmpty(suffix)) {
				suffix = "child" + _children.size();
			}
			String blockName = getNameInLayout() + "." + suffix;

			Mage_Core_Model_Layout layout = getLayout();
			if (layout != null) {
				layout.unsetBlock(block.getNameInLayout());
				layout.setBlock(blockName, block);
			}

			block.setNameInLayout(blockName);
			block.setIsAnonymous(false);

			if (StringUtils.isEmpty(alias)) {
				alias = blockName;
			}
		}

		block.setParentBlock(this);
		block.setBlockAlias(alias);
		_children.put(alias, block);
	}

	/**
	 * Unset child block
	 */
	public void unsetChild(String alias) {
		Mage_Core_Block_Abstract block = _children.remove(alias);
		if (block != null) {
			/** @var Mage_Core_Block_Abstract $block */
			String name = block.getNameInLayout();
			int key = _sortedChildren.indexOf(name);
			if (key >= 0) {
				_sortedChildren.remove(key);
			}
		}
	}

	/**
	 * Call a child and unset it, if callback matched result
	 *
	 * $params will pass to child callback
	 * $params may be array, if called from layout with elements with same name, for example:
	 * ...<foo>value_1</foo><foo>value_2</foo><foo>value_3</foo>
	 *
	 * Or, if called like this:
	 * ...<foo>value_1</foo><bar>value_2</bar><baz>value_3</baz>
	 * - then it will be $params1, $params2, $params3
	 *
	 * It is no difference anyway, because they will be transformed in appropriate way.
	 *
	 * @param string $alias
	 * @param string $callback
	 * @param mixed $result
	 * @param array $params
	 * @return Mage_Core_Block_Abstract
	 */
	public void unsetCallChild(String alias, String callback, Object result, Object params)
	{
		Mage_Core_Block_Abstract child = getChild(alias);
		if (child != null) {
			// TODO 언제 호출되는지 확인.
			//            $args = func_get_args();
			//            $alias = array_shift($args);
			//            $callback = array_shift($args);
			//            $result = (string)array_shift($args);
			//            if (!is_array($params)) {
			//                $params = $args;
			//            }
			//
			//            if ($result == call_user_func_array(array(&$child, $callback), $params)) {
			//                unsetChild(alias);
			//            }
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Unset all children blocks
	 *
	 * @return Mage_Core_Block_Abstract
	 */
	public void unsetChildren()
	{
		_children = new HashMap<>();
		_sortedChildren = new ArrayList<>();
	}

	/**
	 * Retrieve child block by name
	 */
	public Mage_Core_Block_Abstract getChild(String name) {
		return _children.get(name);
	}

	public Map<String, Mage_Core_Block_Abstract> getChildren() {
		return _children;
	}

	/**
	 * Retrieve child block HTML
	 */
	public String getChildHtml(String name/* = ""*/, boolean useCache/* = true*/, boolean sorted/* = false*/) {
		if (StringUtils.isBlank(name)) {
			Map<String, Mage_Core_Block_Abstract> children;
			if (sorted) {
				children = new HashMap<String, Mage_Core_Block_Abstract>();
				for (String childName : getSortedChildren()) {
					children.put(childName, getLayout().getBlock(childName));
				}
			} else {
				children = getChildren();
			}

			StringBuilder out = new StringBuilder();
			for (Mage_Core_Block_Abstract child : children.values()) {
				out.append(_getChildHtml(child.getBlockAlias(), useCache));
			}
			return out.toString();
		} else {
			return _getChildHtml(name, useCache);
		}
	}

	/**
	 * @param string $name          Parent block name
	 * @param string $childName     OPTIONAL Child block name
	 * @param bool $useCache        OPTIONAL Use cache flag
	 * @param bool $sorted          OPTIONAL @see getChildHtml()
	 * @return string
	 */
	public String getChildChildHtml(String name, String childName/* = ""*/, boolean useCache/* = true*/, boolean sorted/* = false*/) {
		if (StringUtils.isEmpty(name)) {
			return "";
		}

		Mage_Core_Block_Abstract child = getChild(name);
		if (child == null) {
			return "";
		}
		return getChildHtml(childName, useCache, sorted);
	}

	/**
	 * Obtain sorted child blocks
	 *
	 * @return array
	 */
	public Map<String, Mage_Core_Block_Abstract> getSortedChildBlocks() {
		Map<String, Mage_Core_Block_Abstract> children = new LinkedHashMap<>();
		for (String childName : getSortedChildren()) {
			children.put(childName, getLayout().getBlock(childName));
		}

		return children;
	}

	/**
	 * Retrieve child block HTML
	 *
	 * @param   string $name
	 * @param   boolean $useCache
	 * @return  string
	 */
	protected String _getChildHtml(String name, boolean useCache/* = true*/) {
		if (useCache) {
			String html = _childrenHtmlCache.get(name);
			if (html != null) {
				return html;
			}
		}

		Mage_Core_Block_Abstract child = getChild(name);

		String html;
		if (child == null) {
			html = "";
		} else {
			_beforeChildToHtml(name, child);
			html = child.toHtml();
		}

		_childrenHtmlCache.put(name, html);
		return html;
	}

	/**
	 * Prepare child block before generate html
	 */
	protected void _beforeChildToHtml(String name, Mage_Core_Block_Abstract child) {
	}

	/**
	 * Retrieve block html
	 *
	 * @param   string $name
	 * @return  string
	 */
	public String getBlockHtml(String name) {
		Mage_Core_Model_Layout layout = getLayout();
		if (layout == null) {
			layout = getAction().getLayout();
			if (layout == null) {
				return "";
			}
		}

		Mage_Core_Block_Abstract block = layout.getBlock(name);
		if (block == null) {
			return "";
		}

		return block.toHtml();
	}

	/**
	 * Insert child block
	 *
	 * @param   Mage_Core_Block_Abstract|string $block
	 * @param   string $siblingName
	 * @param   boolean $after
	 * @param   string $alias
	 * @return  object $this
	 */
	public void insert(String blockName, String siblingName/* = ""*/, boolean after/* = false*/, String alias/* = ""*/) {
		insert(getLayout().getBlock(blockName), siblingName, after, alias);
	}

	public void insert(Mage_Core_Block_Abstract block, String siblingName/* = ""*/, boolean after/* = false*/, String alias/* = ""*/) {
		if (block == null) {
			/*
			 * if we don't have block - don't throw exception because
			 * block can simply removed using layout method remove
			 */
			//Mage::throwException(Mage::helper('core')
			// ->__('Invalid block name to set child %s: %s', $alias, $block));
			return;
		}

		String name;
		if (block.getIsAnonymous()) {
			setChild("", block);
			name = block.getNameInLayout();
		} else if (StringUtils.isNotEmpty(alias)) {
			setChild(alias, block);
			name = getNameInLayout();
		} else {
			name = getNameInLayout();
			setChild(name, block);
		}

		if (StringUtils.isBlank(siblingName)) {
			if (after) {
				_sortedChildren.add(name);
			} else {
				_sortedChildren.add(0, name);
			}
		} else {
			int key = _sortedChildren.indexOf(siblingName);
			if (key >= 0) {
				if (after) {
					key++;
				}
				_sortedChildren.add(key, name);
			} else {
				if (after) {
					_sortedChildren.add(name);
				} else {
					_sortedChildren.add(0, name);
				}
			}

			_sortInstructions.put(name, new Instruction(siblingName, after, key >= 0));
		}
	}

	/**
	 * Sort block's children
	 *
	 * @param boolean $force force re-sort all children
	 * @return Mage_Core_Block_Abstract
	 */
	public void sortChildren(boolean force/* = false*/) {
		for (Entry<String, Instruction> each : _sortInstructions.entrySet()) {
			String name = each.getKey();
			Instruction instruction = each.getValue();
			//            list($siblingName, $after, $exists) = $list;

			if (instruction.exists && !force) {
				continue;
			}

			instruction.exists = true;

			int index = _sortedChildren.indexOf(name);
			int siblingKey = _sortedChildren.indexOf(instruction.siblingName);

			if (index < 0 || siblingKey < 0) {
				continue;
			}

			if (instruction.after) {
				// insert after block
				if (index == siblingKey + 1) {
					continue;
				}

				// remove sibling from array
				_sortedChildren.remove(index);
				// insert sibling after
				_sortedChildren.add(siblingKey + 1, name);
			} else {
				// insert before block
				if (index == siblingKey - 1) {
					continue;
				}

				// remove sibling from array
				_sortedChildren.remove(index);
				// insert sibling after
				_sortedChildren.add(siblingKey, name);
			}
		}
	}

	/**
	 * Append child block
	 *
	 * @param   Mage_Core_Block_Abstract|string $block
	 * @param   string $alias
	 * @return  Mage_Core_Block_Abstract
	 */
	public void append(Mage_Core_Block_Abstract block, String alias/* = ""*/) {
		insert(block, "", true, alias);
	}

	/**
	 * Make sure specified block will be registered in the specified child groups
	 *
	 * @param string $groupName
	 * @param Mage_Core_Block_Abstract $child
	 */
	public void addToChildGroup(String groupName, Mage_Core_Block_Abstract child) {
		List<String> childGroup = _childGroups.get(groupName);
		if (childGroup == null) {
			childGroup = new ArrayList<>();
			_childGroups.put(groupName, childGroup);
		} else {
			if (childGroup.contains(child.getBlockAlias())) {
				return;
			}
		}

		childGroup.add(child.getBlockAlias());
	}

	/**
	 * Add self to the specified group of parent block
	 */
	public void addToParentGroup(String groupName) {
		getParentBlock().addToChildGroup(groupName, this);
	}

	/**
	 * Get a group of child blocks
	 *
	 * Returns an array of <alias> => <block>
	 * or an array of <alias> => <callback_result>
	 * The callback currently supports only $this methods and passes the alias as parameter
	 *
	 * @param string $groupName
	 * @param string $callback
	 * @param bool $skipEmptyResults
	 * @return array
	 */
	public Map<String, Mage_Core_Block_Abstract> getChildGroup(String groupName, String callback/* = null*/, boolean skipEmptyResults/* = true*/) {
		if (!_childGroups.containsKey(groupName)) {
			return Collections.emptyMap();
		}

		Map<String, Mage_Core_Block_Abstract> result = new HashMap<>();
		for (Mage_Core_Block_Abstract block : getSortedChildBlocks().values()) {
			String alias = block.getBlockAlias();
			List<String> childGroup = _childGroups.get(groupName);
			if (childGroup.contains(alias)) {
				if (callback != null) {
					Mage_Core_Block_Abstract row = (Mage_Core_Block_Abstract)new Callback(this, callback).call(alias);
					if (!skipEmptyResults || row != null) {
						result.put(alias, row);
					}
				} else {
					result.put(alias, block);
				}
			}
		}
		return result;
	}

	/**
	 * Get a value from child block by specified key
	 *
	 * @param string $alias
	 * @param string $key
	 * @return mixed
	 */
	public Object getChildData(String alias, String key/* = ""*/) {
		Mage_Core_Block_Abstract child = getChild(alias);
		if (child != null) {
			return child.getData(key);
		}

		return null;
	}

	/**
	 * Before rendering html, but after trying to load cache
	 */
	protected void _beforeToHtml() {
	}

	/**
	 * Specify block output frame tags
	 *
	 * @param $openTag
	 * @param $closeTag
	 * @return Mage_Core_Block_Abstract
	 */
	public void setFrameTags(String openTag, String closeTag/* = null*/) {
		_frameOpenTag = openTag;
		if (closeTag != null) {
			_frameCloseTag = closeTag;
		} else {
			_frameCloseTag = "/" + openTag;
		}
	}

	/**
	 * Produce and return block's html output
	 *
	 * It is a final method, but you can override _toHtml() method in descendants if needed.
	 *
	 * @return string
	 */
	final public String toHtml() {
		Mage_Core_Model_App app = AppContext.getCurrent();
		EventDispatcher eventDispatcher = app.getEventDispatcher();
		eventDispatcher.dispatchEvent("core_block_abstract_to_html_before", Collections.singletonMap("block", this));
		if (app.getStoreConfigAsString("advanced/modules_disable_output/" + getModuleName()) == null) {
			return "";
		}

		String html = _loadCache();
		if (html == null) {
			Mage_Core_Model_Translate translator = app.getTranslator();
			/** @var $translate Mage_Core_Model_Translate */
			if (hasData("translate_inline")) {
				translator.setTranslateInline((Boolean)getData("translate_inline"));
			}

			_beforeToHtml();
			html = _toHtml();
			_saveCache(html);

			if (hasData("translate_inline")) {
				translator.setTranslateInline(true);
			}
		}
		html = _afterToHtml(html);

		/**
		 * Check framing options
		 */
		if (_frameOpenTag != null) {
			html = "<" + _frameOpenTag + ">" + html + "<" + _frameCloseTag + ">";
		}

		/**
		 * Use single transport object instance for all blocks
		 */
		if (_transportObject == null) {
			_transportObject = new Varien_Object();
		}

		_transportObject.setData("html", html);
		Map<String, Object> eventParams = new HashMap<>();
		eventParams.put("block", this);
		eventParams.put("transport", _transportObject);
		eventDispatcher.dispatchEvent("core_block_abstract_to_html_after", eventParams);
		html = (String)_transportObject.getData("html");

		return html;
	}

	/**
	 * Processing block html after rendering
	 */
	protected String _afterToHtml(String html) {
		return html;
	}

	/**
	 * Override this method in descendants to produce html
	 */
	protected String _toHtml() {
		return "";
	}

	/**
	 * Returns url model class name
	 */
	protected String _getUrlModelClass() {
		return "core/url";
	}

	/**
	 * Create and return url object
	 *
	 * @return Mage_Core_Model_Url
	 */
	protected Mage_Core_Model_Url _getUrlModel() {
		return MageInstanceLoader.get().getInstance(_getUrlModelClass());
	}

	/**
	 * Generate url by route and parameters
	 *
	 * @param   string $route
	 * @param   array $params
	 * @return  string
	 */
	public String getUrl(String route/* = ""*/, RouteParams params) {
		return _getUrlModel().getUrl(route, params);
	}

	/**
	 * Generate base64-encoded url by route and parameters
	 *
	 * @param   string $route
	 * @param   array $params
	 * @return  string
	 */
	public String getUrlBase64(String route/* = ""*/, RouteParams params) {
		return AppContext.getCurrent().getHelperData().urlEncode(getUrl(route, params));
	}

	/**
	 * Generate url-encoded url by route and parameters
	 *
	 * @param   string $route
	 * @param   array $params
	 * @return  string
	 */
	public String getUrlEncoded(String route/* = ""*/, RouteParams params) {
		return AppContext.getCurrent().getHelperData().urlEncode(getUrl(route, params));
	}

	/**
	 * Retrieve url of skins file
	 */
	public String getSkinUrl(String file/* = null*/, Param params) {
		return _getApp().getDesignPackage().getSkinUrl(file, params);
	}

	/**
	 * Retrieve messages block
	 *
	 * @return Mage_Core_Block_Messages
	 */
	public Mage_Core_Block_Messages getMessagesBlock() {
		if (_messagesBlock == null) {
			return getLayout().getMessagesBlock();
		}

		return _messagesBlock;
	}

	/**
	 * Set messages block
	 *
	 * @param   Mage_Core_Block_Messages $block
	 * @return  Mage_Core_Block_Abstract
	 */
	public void setMessagesBlock(Mage_Core_Block_Messages block) {
		_messagesBlock = block;
	}

	/**
	 * Return block helper
	 *
	 * @param string $type
	 * @return Mage_Core_Block_Abstract
	 */
	public Mage_Core_Block_Abstract getHelper(String type) {
		return (Mage_Core_Block_Abstract)getLayout().getBlockSingleton(type);
	}

	/**
	 * Returns helper object
	 *
	 * @param string $name
	 * @return Mage_Core_Block_Abstract
	 */
	public Mage_Core_Helper_Abstract helper(String name) {
		Mage_Core_Model_Layout layout = getLayout();
		if (layout != null) {
			return layout.helper(name);
		}

		return ModelLoader.get().helper(name);
	}

	/**
	 * Retrieve formatting date
	 *
	 * @param   string $date
	 * @param   string $format
	 * @param   bool $showTime
	 * @return  string
	 */
	//	public String formatDate(String date/* = null*/, String format/* = Mage_Core_Model_Locale::FORMAT_TYPE_SHORT*/, boolean showTime/* = false*/) {
	//		return AppContext.getCurrent().getHelperData().formatDate(date, format, showTime);
	//	}

	/**
	 * Retrieve formatting time
	 *
	 * @param   string $time
	 * @param   string $format
	 * @param   bool $showDate
	 * @return  string
	 */
	//	public String formatTime(String time/* = null*/, String format/* = Mage_Core_Model_Locale::FORMAT_TYPE_SHORT*/, boolean showDate/* = false*/) {
	//		return AppContext.getCurrent().getHelperData().formatTime(time, format, showDate);
	//	}

	/**
	 * Retrieve module name of block
	 *
	 * @return string
	 */
	public String getModuleName() {
		String module = (String)getData("module_name");
		if (module == null) {
			String clazz = getClass().getName();
			module = clazz.substring(0, clazz.indexOf("_Block"));
			setData("module_name", module);
		}

		return module;
	}

	/**
	 * Translate block sentence
	 *
	 * @return string
	 */
	public String __(Object... args) {
		Mage_Core_Model_Translate_Expr expr = new Mage_Core_Model_Translate_Expr((String)args[0], getModuleName());

		List<Object> translateArgs = new ArrayList<Object>(args.length);
		for (int i = 1; i < args.length; i++) {
			translateArgs.add(args[i]);
		}

		return AppContext.getCurrent().getTranslator().translate(expr, translateArgs);
	}

	/**
	 * Escape html entities
	 *
	 * @param   mixed $data
	 * @param   array $allowedTags
	 * @return  string
	 */
	public String escapeHtml(String data, List<String> allowedTags/* = null*/) {
		return AppContext.getCurrent().getHelperData().escapeHtml(data, allowedTags);
	}

	/**
	 * Wrapper for standard strip_tags() function with extra functionality for html entities
	 *
	 * @param string $data
	 * @param string $allowableTags
	 * @param bool $allowHtmlEntities
	 * @return string
	 */
	public String stripTags(String data, List<String> allowedTags/* = null*/, boolean allowHtmlEntities/* = false*/)
	{
		return AppContext.getCurrent().getHelperData().stripTags(data, allowedTags, allowHtmlEntities);
	}

	/**
	 * Escape html entities in url
	 *
	 * @param string $data
	 * @return string
	 */
	public String escapeUrl(String data)
	{
		return AppContext.getCurrent().getHelperData().escapeUrl(data);
	}

	/**
	 * Escape quotes inside html attributes
	 * Use $addSlashes = false for escaping js that inside html attribute (onClick, onSubmit etc)
	 *
	 * @param  string $data
	 * @param  bool $addSlashes
	 * @return string
	 */
	public String quoteEscape(String data, boolean addSlashes/* = false*/)
	{
		return AppContext.getCurrent().getHelperData().quoteEscape(data, addSlashes);
	}

	/**
	 * Escape quotes in java scripts
	 *
	 * @param mixed $data
	 * @param string $quote
	 * @return mixed
	 */
	public String jsQuoteEscape(String data, String quote/* = "'"*/) {
		return AppContext.getCurrent().getHelperData().jsQuoteEscape(data, quote);
	}

	/**
	 * Alias for getName method.
	 *
	 * @return string
	 */
	public String getNameInLayout() {
		return _nameInLayout;
	}

	/**
	 * Get chilren blocks count
	 * @return int
	 */
	public int countChildren() {
		return _children.size();
	}

	/**
	 * Prepare url for save to cache
	 *
	 * @return Mage_Core_Block_Abstract
	 */
	protected void _beforeCacheUrl() {
		Mage_Core_Model_App app = AppContext.getCurrent();
		if (app.useCache(CACHE_GROUP)) {
			app.setUseSessionVar(true);
		}
	}

	/**
	 * Replace URLs from cache
	 *
	 * @param string $html
	 * @return string
	 */
	protected String _afterCacheUrl(String html) {
		Mage_Core_Model_App app = AppContext.getCurrent();
		if (app.useCache(CACHE_GROUP)) {
			app.setUseSessionVar(false);
			Varien_Profiler.start("CACHE_URL");
			html = _getUrlModel().sessionUrlVar(html);
			Varien_Profiler.stop("CACHE_URL");
		}

		return html;
	}

	/**
	 * Get cache key informative items
	 * Provide string array key to share specific info item with FPC placeholder
	 *
	 * @return array
	 */
	public List<String> getCacheKeyInfo() {
		return Arrays.asList(getNameInLayout());
	}

	/**
	 * Get Key for caching block content
	 *
	 * @return string
	 */
	public String getCacheKey() {
		if (hasData("cache_key")) {
			return (String)getData("cache_key");
		}
		/**
		 * don't prevent recalculation by saving generated cache key
		 * because of ability to render single block instance with different data
		 */
		List<String> keys = getCacheKeyInfo();
		String key = StringUtils.join(keys, "|");
		key = Standard.sha1(key);

		return key;
	}

	/**
	 * Get tags array for saving cache
	 *
	 * @return array
	 */
	public List<String> getCacheTags() {
		List<String> tags = null;
		String tagsCache = (String)AppContext.getCurrent().loadCache(_getTagsCacheKey(null));
		if (tagsCache != null) {
			tags = JacksonUtil.toObject(tagsCache, List.class);
		}
		if (tags == null || tags.isEmpty()) {
			tags = (List<String>)getData(CACHE_TAGS_DATA_KEY);
			if (tags == null) {
				tags = new ArrayList<String>();
			} else {
				tags = new ArrayList<String>(new TreeSet<String>(tags));
			}

			if (!tags.contains(CACHE_GROUP)) {
				tags.add(CACHE_GROUP);
			}
		}

		return tags;

	}

	/**
	 * Add tag to block
	 *
	 * @param string|array $tag
	 * @return Mage_Core_Block_Abstract
	 */
	public Mage_Core_Block_Abstract addCacheTag(List<String> tags) {
		List<String> cachingTags = (List<String>)getData(CACHE_TAGS_DATA_KEY);
		if (cachingTags != null) {
			cachingTags.addAll(tags);
		} else {
			cachingTags = tags;
		}

		setData(CACHE_TAGS_DATA_KEY, cachingTags);
		return this;
	}

	/**
	 * Add tags from specified model to current block
	 *
	 * @param Mage_Core_Model_Abstract $model
	 * @return Mage_Core_Block_Abstract
	 */
	//	public Mage_Core_Block_Abstract addModelTags(Mage_Core_Model_Abstract model)
	//	{
	//		List<String> cacheTags = model.getCacheIdTags();
	//		if (cacheTags != null && !cacheTags.isEmpty()) {
	//			addCacheTag(cacheTags);
	//		}
	//
	//		return this;
	//	}

	/**
	 * Get block cache life time
	 *
	 * @return int
	 */
	public Integer getCacheLifetime() {
		if (!this.hasData("cache_lifetime")) {
			return null;
		}
		return (Integer)getData("cache_lifetime");
	}

	/**
	 * Load block html from cache storage
	 *
	 * @return string | false
	 */
	protected String _loadCache() {
		Integer cacheLifetime = getCacheLifetime();
		if (cacheLifetime == null || !AppContext.getCurrent().useCache(CACHE_GROUP)) {
			return null;
		}

		String cacheKey = getCacheKey();
		/** @var $session Mage_Core_Model_Session */
		Mage_Core_Model_Session session = AppContext.getCurrent().getSession(null);
		String cacheData = (String)AppContext.getCurrent().loadCache(cacheKey);
		if (cacheData != null) {
			cacheData = cacheData.replace(_getSidPlaceholder(cacheKey), session.getSessionIdQueryParam() + "=" + session.getEncryptedSessionId());
		}
		return cacheData;
	}

	/**
	 * Save block content to cache storage
	 *
	 * @param string $data
	 * @return Mage_Core_Block_Abstract
	 */
	protected Mage_Core_Block_Abstract _saveCache(String data) {
		Integer cacheLifetime = getCacheLifetime();
		if (cacheLifetime == null || !AppContext.getCurrent().useCache(CACHE_GROUP)) {
			return null;
		}

		String cacheKey = getCacheKey();
		/** @var $session Mage_Core_Model_Session */
		Mage_Core_Model_Session session = AppContext.getCurrent().getSession(null);
		data = data.replace(session.getSessionIdQueryParam() + "=" + session.getEncryptedSessionId(), _getSidPlaceholder(cacheKey));

		List<String> tags = getCacheTags();

		AppContext.getCurrent().saveCache(data, cacheKey, tags, cacheLifetime);
		AppContext.getCurrent().saveCache(JacksonUtil.toJson(tags), _getTagsCacheKey(cacheKey), tags, cacheLifetime);

		return this;
	}

	/**
	 * Get cache key for tags
	 *
	 * @param string $cacheKey
	 * @return string
	 */
	protected String _getTagsCacheKey(String cacheKey/* = null*/) {
		if (StringUtils.isNotEmpty(cacheKey)) {
			cacheKey = this.getCacheKey();
		}

		cacheKey = Standard.md5(cacheKey + "_tags");
		return cacheKey;
	}

	/**
	 * Get SID placeholder for cache
	 *
	 * @param null|string $cacheKey
	 * @return string
	 */
	protected String _getSidPlaceholder(String cacheKey/* = null*/) {
		if (cacheKey == null) {
			cacheKey = this.getCacheKey();
		}

		return "<!--SID=" + cacheKey + "-->";
	}

	/**
	 * Collect and retrieve items tags.
	 * Item should implements Mage_Core_Model_Abstract::getCacheIdTags method
	 *
	 * @param array|Varien_Data_Collection $items
	 * @return array
	 */
	//	public List<String> getItemsTags(List<Object> items)    {
	//    	List<String> tags = new ArrayList<String>();
	//        /** @var $item Mage_Core_Model_Abstract */
	//        for ($items as $item) {
	//            List<String> itemTags = $item->getCacheIdTags();
	//            
	//            if (itemTags == null || itemTags.isEmpty()) {
	//                continue;
	//            }
	//            tags.addAll(itemTags);
	//        }
	//        
	//        return tags;
	//    }

	public abstract Mage_Core_Block_Abstract setTemplate(String template);

	public void setType(String type) {
		setData("type", type);
	}

	public String getTitle() {
		return (String)getData("Title");
	}

	public void setTitle(String title) {
		setData("Title", title);
	}
}
