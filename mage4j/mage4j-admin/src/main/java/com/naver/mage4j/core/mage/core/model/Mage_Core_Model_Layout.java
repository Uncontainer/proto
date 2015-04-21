package com.naver.mage4j.core.mage.core.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.naver.mage4j.core.mage.MageInstanceLoader;
import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.mage.core.Mage_Core_Exception;
import com.naver.mage4j.core.mage.core.block.Mage_Core_Block_Abstract;
import com.naver.mage4j.core.mage.core.block.Mage_Core_Block_Messages;
import com.naver.mage4j.core.mage.core.helper.Mage_Core_Helper_Abstract;
import com.naver.mage4j.core.mage.core.layout.Mage_Core_Model_Layout_Element;
import com.naver.mage4j.core.mage.core.layout.Mage_Core_Model_Layout_Update;
import com.naver.mage4j.core.mage.core.model.app.Mage_Core_Model_App_Area.AreaType;
import com.naver.mage4j.core.mage.core.model.event.Callback;
import com.naver.mage4j.core.util.JacksonUtil;
import com.naver.mage4j.external.varien.Varien_Profiler;
import com.naver.mage4j.external.varien.simplexml.SimplexmlUtils;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element;
import com.naver.mage4j.external.varien.simplexml.Simplexml_Element_JDom;
import com.naver.mage4j.external.varien.simplexml.Varien_Simplexml_Config;

public class Mage_Core_Model_Layout extends Varien_Simplexml_Config {
	private final Logger log = LoggerFactory.getLogger(Mage_Core_Model_Layout.class);

	/**
	 * Layout Update module
	 */
	protected Mage_Core_Model_Layout_Update _update;

	/**
	 * Blocks registry
	 *
	 * @var array
	 */
	protected final Map<String, Mage_Core_Block_Abstract> _blocks = new HashMap<String, Mage_Core_Block_Abstract>();

	/**
	 * Cache of block callbacks to output during rendering
	 *
	 * @var array
	 */
	protected final Map<String, Callback> _output = new HashMap<String, Callback>();

	/**
	 * Layout area (f.e. admin, frontend)
	 *
	 * @var string
	 */
	protected AreaType _area;

	/**
	 * Helper blocks cache for this layout
	 *
	 * @var array
	 */
	protected final Map<String, Object> _helpers = new HashMap<String, Object>();

	/**
	 * Flag to have blocks" output go directly to browser as oppose to return result
	 *
	 * @var boolean
	 */
	protected boolean _directOutput = false;

	@Autowired
	private ModelInstanceLoader modelInstanceLoader;

	@Autowired
	private ModelLoader modelLoader;

	/**
	 * Class constructor
	 *
	 * @param array $data
	 */
	public Mage_Core_Model_Layout(/*Map<String, Object> data*/) {
		super(Mage_Core_Model_Layout_Element.class);
		setXml(SimplexmlUtils.build("<layout/>", _elementClass));
		_update = AppContext.getCurrent().getLayoutUpdate();
		//        parent::__construct($data);
	}

	/**
	 * Layout update instance
	 *
	 */
	public Mage_Core_Model_Layout_Update getUpdate() {
		return _update;
	}

	/**
	 * Set layout area
	 */
	public void setArea(AreaType area) {
		_area = area;
	}

	/**
	 * Retrieve layout area
	 */
	public AreaType getArea() {
		return _area;
	}

	/**
	 * Declaring layout direct output flag
	 *
	 * @param   bool $flag
	 * @return  Mage_Core_Model_Layout
	 */
	public void setDirectOutput(boolean flag) {
		_directOutput = flag;
	}

	/**
	 * Retrieve derect output flag
	 */
	public boolean getDirectOutput() {
		return _directOutput;
	}

	/**
	 * Loyout xml generation
	 *
	 * @return Mage_Core_Model_Layout
	 */
	public Mage_Core_Model_Layout generateXml() {
		Mage_Core_Model_Session session = AppContext.getCurrent().getSession(null);
		Simplexml_Element_JDom xml = getUpdate().asSimplexml();
		List<Simplexml_Element> removeInstructions = xml.selectDescendants("//remove");
		for (Simplexml_Element infoNode : removeInstructions) {
			String blockName = infoNode.getAttribute("name");
			if (StringUtils.isNotEmpty(blockName)) {
				List<Simplexml_Element> ignoreNodes = xml.selectDescendants("//block[@name='" + blockName + "']");
				if (ignoreNodes.isEmpty()) {
					continue;
				}

				List<Simplexml_Element> ignoreReferences = xml.selectDescendants("//reference[@name='" + blockName + "']");
				ignoreNodes.addAll(ignoreReferences);

				String acl = infoNode.getAttribute("acl");
				for (Simplexml_Element block : ignoreNodes) {
					if (block.getAttribute("ignore") != null) {
						continue;
					}

					if (acl != null && session.isAllowed(acl)) {
						continue;
					}

					// TODO 별도의 속성 복사본을 가지고 있어야 하는지 확인..
					//                    if (!isset($block->attributes()->ignore)) {
					block.setAttribute("ignore", "true");
					//                    }
				}
			}
		}

		setXml(xml);

		return this;
	}

	/**
	 * Create layout blocks hierarchy from layout xml configuration
	 *
	 * @param Mage_Core_Layout_Element|null $parent
	 */
	public void generateBlocks(Mage_Core_Model_Layout_Element parent) {
		if (SimplexmlUtils.isNull(parent)) {
			Simplexml_Element node = getNode(null);
			if (SimplexmlUtils.isNull(node)) {
				return;
			}

			parent = (Mage_Core_Model_Layout_Element)node;
		}

		for (Simplexml_Element node : parent.children()) {
			if (StringUtils.isNotBlank(node.getAttribute("ignore"))) {
				continue;
			}

			String nodeName = node.getName();
			if ("block".equals(nodeName)) {
				_generateBlock(node, parent);
				generateBlocks((Mage_Core_Model_Layout_Element)node);
			} else if ("reference".equals(nodeName)) {
				generateBlocks((Mage_Core_Model_Layout_Element)node);
			} else if ("action".equals(nodeName)) {
				_generateAction(node, parent);
			}
		}
	}

	/**
	 * Add block object to layout based on xml node data
	 */
	protected void _generateBlock(Simplexml_Element node, Mage_Core_Model_Layout_Element parent) {
		String className = node.getString("class");
		if (StringUtils.isEmpty(className)) {
			className = node.getString("type");
		}

		String blockName = node.getString("name");
		String _profilerKey = "BLOCK: " + blockName;
		Varien_Profiler.start(_profilerKey);

		Mage_Core_Block_Abstract block = addBlock(className, blockName);
		if (block == null) {
			return;
		}

		Mage_Core_Block_Abstract parentBlock = null;
		String parentName = node.getString("parent");
		if (StringUtils.isNotEmpty(parentName)) {
			parentBlock = getBlock(parentName);
		} else {
			parentName = parent.getBlockName();
			if (StringUtils.isNotEmpty(parentName)) {
				parentBlock = getBlock(parentName);
			}
		}

		if (parentBlock != null) {
			String alias = node.getString("as");
			if (alias == null) {
				alias = "";
			}

			String before = node.getString("before");
			String after = node.getString("after");

			String sibling;
			if (StringUtils.isNotBlank(before)) {
				sibling = before;
				if ("-".equals(sibling)) {
					sibling = "";
				}
				parentBlock.insert(block, sibling, false, alias);
			} else if (StringUtils.isNotBlank(after)) {
				sibling = after;
				if ("-".equals(sibling)) {
					sibling = "";
				}
				parentBlock.insert(block, sibling, false, alias);
			} else {
				parentBlock.append(block, alias);
			}
		}

		String template = node.getString("template");
		if (StringUtils.isNotBlank(template)) {
			block.setTemplate(template);
		}

		String output = node.getString("output");
		if (StringUtils.isNotBlank(output)) {
			addOutputBlock(blockName, output);
		}

		Varien_Profiler.stop(_profilerKey);
	}

	/**
	 * Enter description here...
	 */
	protected void _generateAction(Simplexml_Element node, Mage_Core_Model_Layout_Element parent) {
		String ifconfig = node.getString("ifconfig");
		if (StringUtils.isNotEmpty(ifconfig)) {
			if (!AppContext.getCurrent().getStoreConfigFlag(ifconfig)) {
				return;
			}
		}

		String method = node.getString("method");
		String blockName = node.getString("block");
		String parentName;
		if (StringUtils.isNotEmpty(blockName)) {
			parentName = blockName;
		} else {
			parentName = parent.getBlockName();
		}

		String _profilerKey = "BLOCK ACTION: " + parentName + " -> " + method;
		Varien_Profiler.start(_profilerKey);

		Map<String, Object> parsedArgs = new LinkedHashMap<>();

		Mage_Core_Block_Abstract block = null;
		if (StringUtils.isNotEmpty(parentName)) {
			block = getBlock(parentName);
		}
		if (block != null) {
			List<Simplexml_Element> args = node.children();
			//            unset($args["@attributes"]);

			for (Simplexml_Element arg : args) {
				//                if (($arg instanceof Mage_Core_Model_Layout_Element)) {
				String helper = arg.getString("helper");
				if (helper != null) {
					int pos = helper.lastIndexOf('/');
					String helperName = helper.substring(0, pos);
					String helperMethod = helper.substring(pos + 1);
					Map<String, Object> callbackParamMap = arg.toMap();
					//                    	unset($callbackParamMap["@"]);

					parsedArgs.put(arg.getName(), new Callback(modelLoader.helper(helperName), helperMethod).call(callbackParamMap));
				} else {
					/**
					 * if there is no helper we hope that this is assoc array
					 */
					Map<String, Object> arr = new HashMap<String, Object>();
					for (Simplexml_Element child : arg.children()) {
						arr.put(child.getName(), child.toMap());
					}
					if (!arr.isEmpty()) {
						parsedArgs.put(arg.getName(), arr);
					}
				}
				//                }
			}

			String json = node.getString("json");
			if (json != null) {
				String[] jsonKeys = StringUtils.split(json, " ");
				for (String jsonKey : jsonKeys) {
					parsedArgs.put(jsonKey, JacksonUtil.toObject((String)parsedArgs.get(jsonKey)));
				}
			}

			Object blockArgs = _translateLayoutNode(node, parsedArgs);
			new Callback(block, method).call(blockArgs);
		}

		Varien_Profiler.stop(_profilerKey);
	}

	/**
	 * Translate layout node
	 **/
	protected Object _translateLayoutNode(Simplexml_Element node, Map<String, Object> args) {
		String translate = node.getString("translate");
		if (translate != null) {
			// Translate value by core module if module attribute was not set
			String moduleName = SimplexmlUtils.getString(node, "module", "code");

			// Handle translations in arrays if needed
			String[] translatableArguments = StringUtils.split(translate, " ");
			for (String translatableArgumentName : translatableArguments) {
				/*
				 * .(dot) character is used as a path separator in nodes hierarchy
				 * e.g. info.title means that Magento needs to translate value of <title> node
				 * that is a child of <info> node
				 */
				// @var $argumentHierarhy array - path to translatable item in $args array
				String[] argumentHierarchy = StringUtils.split(translatableArgumentName, ".");
				Map<String, Object> argumentStack = args;
				boolean canTranslate = true;

				int index = 0;
				Object value = null;
				while (!argumentStack.isEmpty()) {
					String argumentName = argumentHierarchy[index++];
					if (argumentStack.containsKey(argumentName)) {
						/*
						 * Move to the next element in arguments hieracrhy
						 * in order to find target translatable argument
						 */
						Object obj = argumentStack.get(argumentName);
						if (obj instanceof Map) {
							argumentStack = (Map<String, Object>)obj;
						} else {
							break;
						}
					} else {
						// Target argument cannot be found
						canTranslate = false;
						break;
					}
				}

				if (canTranslate && value instanceof String) {
					// $argumentStack is now a reference to target translatable argument so it can be translated
					return new Callback(modelLoader.helper(moduleName), "__").call(value);
				}
			}
		}

		return args;
	}

	/**
	 * Save block in blocks registry
	 */
	public void setBlock(String name, Mage_Core_Block_Abstract block) {
		_blocks.put(name, block);
	}

	/**
	 * Remove block from registry
	 *
	 * @param string $name
	 */
	public void unsetBlock(String name) {
		_blocks.remove(name);
	}

	/**
	 * Block Factory
	 *
	 * @param     string $type
	 * @param     string $name
	 * @param     array $attributes
	 * @return    Mage_Core_Block_Abstract
	 */
	public Mage_Core_Block_Abstract createBlock(String type, String name, Map<String, Object> attributes) {
		Mage_Core_Block_Abstract block;
		try {
			block = _getBlockInstance(type, attributes);
		} catch (Exception e) {
			log.error("", e);
			return null;
		}

		if (StringUtils.isEmpty(name) || name.charAt(0) == '.') {
			block.setIsAnonymous(true);
			if (!StringUtils.isEmpty(name)) {
				block.setAnonSuffix(name.substring(1));
			}
			name = "ANONYMOUS_" + _blocks.size();
		} else if (_blocks.containsKey(name) && AppContext.getCurrent().getConfig().getIsDeveloperMode()) {
			//Mage::throwException(AppContext.getCurrent().getHelperData().__("Block with name "%s" already exists", $name));
		}

		block.setType(type);
		block.setNameInLayout(name);
		block.addData(attributes);
		block.setLayout(this);

		_blocks.put(name, block);
		AppContext.getCurrent().getEventDispatcher().dispatchEvent("core_layout_block_create_after", Collections.singletonMap("block", (Object)block));

		return block;
	}

	public Mage_Core_Block_Abstract createBlock(String type) {
		return createBlock(type, "", Collections.<String, Object> emptyMap());
	}

	/**
	 * Add a block to registry, create new object if needed
	 *
	 * @param string|Mage_Core_Block_Abstract $blockClass
	 * @param string $blockName
	 * @return Mage_Core_Block_Abstract
	 */
	public Mage_Core_Block_Abstract addBlock(String block, String blockName) {
		return createBlock(block, blockName, null);
	}

	/**
	 * Create block object instance based on block type
	 *
	 * @param string $block
	 * @param array $attributes
	 * @return Mage_Core_Block_Abstract
	 */
	protected Mage_Core_Block_Abstract _getBlockInstance(String blockName, Map<String, Object> attributes) {
		if (blockName.contains("/")) {
			blockName = modelInstanceLoader.getBlockClassName(blockName);
			if (blockName == null) {
				throw new Mage_Core_Exception("Invalid block type: " + blockName);
			}
		}

		Object block = MageInstanceLoader.get().getInstanceIfExist(blockName);

		if (!(block instanceof Mage_Core_Block_Abstract)) {
			throw new Mage_Core_Exception("Invalid block type: " + blockName);
		}

		return (Mage_Core_Block_Abstract)block;
	}

	/**
	 * Retrieve all blocks from registry as array
	 * @return 
	 */
	public Map<String, Mage_Core_Block_Abstract> getAllBlocks() {
		return _blocks;
	}

	/**
	 * Get block object by name
	 *
	 * @param string $name
	 * @return Mage_Core_Block_Abstract
	 */
	public Mage_Core_Block_Abstract getBlock(String name) {
		return _blocks.get(name);
	}

	/**
	 * Add a block to output
	 */
	public void addOutputBlock(String blockName, String method/*="toHtml"*/) {
		_output.put(blockName, new Callback(blockName, method));
	}

	public void removeOutputBlock(String blockName) {
		_output.remove(blockName);
	}

	/**
	 * Get all blocks marked for output
	 *
	 * @return string
	 */
	public String getOutput() {
		StringBuilder out = new StringBuilder();

		for (Callback callback : _output.values()) {
			out.append(getBlock((String)callback.call()));
		}

		return out.toString();
	}

	/**
	 * Retrieve messages block
	 *
	 * @return Mage_Core_Block_Messages
	 */
	public Mage_Core_Block_Messages getMessagesBlock() {
		Mage_Core_Block_Messages block = (Mage_Core_Block_Messages)getBlock("messages");
		if (block != null) {
			return block;
		}

		return (Mage_Core_Block_Messages)createBlock("core/messages", "messages", null);
	}

	/**
	 * Enter description here...
	 *
	 * @param string $type
	 * @return Mage_Core_Helper_Abstract
	 */
	public Object getBlockSingleton(String type) {
		Object helper = _helpers.get(type);
		if (helper == null) {
			String className = modelInstanceLoader.getBlockClassName(type);
			if (className == null) {
				throw new Mage_Core_Exception("Invalid block type: " + type);
			}

			helper = MageInstanceLoader.get().getInstance(className);
			if (helper != null) {
				if (helper instanceof Mage_Core_Block_Abstract) {
					((Mage_Core_Block_Abstract)helper).setLayout(this);
				}
				_helpers.put(type, helper);
			}
		}
		return helper;
	}

	/**
	 * Retrieve helper object
	 *
	 * @param   string $name
	 * @return  Mage_Core_Helper_Abstract
	 */
	public Mage_Core_Helper_Abstract helper(String name) {
		Mage_Core_Helper_Abstract helper = modelLoader.helper(name);
		if (helper != null) {
			helper.setLayout(this);
		}

		return helper;
	}

	/**
	 * Lookup module name for translation from current specified layout node
	 *
	 * Priorities:
	 * 1) "module" attribute in the element
	 * 2) "module" attribute in any ancestor element
	 * 3) layout handle name - first 1 or 2 parts (namespace is determined automatically)
	 *
	 * @param Varien_Simplexml_Element $node
	 * @return string
	 */
	public static String findTranslationModuleName(Simplexml_Element node) {
		String result = node.getAttribute("module");
		if (result != null) {
			return result;
		}

		for (Simplexml_Element element : node.selectDescendants("ancestor::*[@module]")) {
			result = element.getAttribute("module");
			if (result != null) {
				return result;
			}
		}

		for (Simplexml_Element handle : node.selectDescendants("ancestor-or-self::*[last()-1]")) {
			String name = AppContext.getCurrent().getConfig().determineOmittedNamespace(handle.getName(), false);
			if (name != null) {
				return name;
			}
		}

		return "core";
	}
}
