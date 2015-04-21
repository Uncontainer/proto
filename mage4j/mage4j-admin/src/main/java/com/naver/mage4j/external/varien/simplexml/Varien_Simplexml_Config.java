package com.naver.mage4j.external.varien.simplexml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naver.mage4j.external.php.Standard;
import com.naver.mage4j.external.zend.cache.CacheBackendInterface;

public class Varien_Simplexml_Config {
	private final Logger log = LoggerFactory.getLogger(Varien_Simplexml_Config.class);

	/**
	 * Xpath describing nodes in configuration that need to be extended
	 * 
	 * @example <allResources extends="/config/modules//resource"/>
	 */
	private static final String _xpathExtends = "//*[@extends]";

	protected final Class<? extends Simplexml_Element_JDom> _elementClass;

	/**
	 * Configuration xml
	 */
	protected Simplexml_Element_JDom _xml = null;

	public Varien_Simplexml_Config(Class<? extends Simplexml_Element_JDom> elementClass) {
		this._elementClass = elementClass;
	}

	public Varien_Simplexml_Config(Varien_Simplexml_Config other) {
		//		this._cacheId = other._cacheId;
		this._elementClass = other._elementClass;
		if (other._xml != null) {
			Constructor<? extends Simplexml_Element_JDom> constructor;
			try {
				constructor = this._elementClass.getConstructor(this._elementClass);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(e);
			}

			try {
				this._xml = constructor.newInstance(other._xml);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Constructor
	 * 
	 * Initializes XML for this configuration
	 * 
	 * @see self::setXml
	 * @param string
	 *            |Varien_Simplexml_Element $sourceData
	 * @param string
	 *            $sourceType
	 */
	public Varien_Simplexml_Config(Class<? extends Simplexml_Element_JDom> elementClass, Map<String, Object> sourceData) {
		this._elementClass = elementClass;
		// TODO sourceData 처리 추가.
	}

	public Varien_Simplexml_Config(Simplexml_Element_JDom sourceData) {
		if (sourceData == null) {
			throw new IllegalArgumentException();
		}

		this._xml = sourceData;
		this._elementClass = sourceData.getClass();
	}

	public Varien_Simplexml_Config(Class<? extends Simplexml_Element_JDom> elementClass, String sourceData) {
		this._elementClass = elementClass;
		this.loadString(sourceData);
	}

	public Varien_Simplexml_Config(Class<? extends Simplexml_Element_JDom> elementClass, File sourceData) {
		this._elementClass = elementClass;
		this.loadFile(sourceData);
	}

	/**
	 * Process configuration xml
	 * 
	 * @return Varien_Simplexml_Config
	 */
	public Varien_Simplexml_Config applyExtends() {
		List<Simplexml_Element> targets = selectDescendants(_xpathExtends);
		if (targets.isEmpty()) {
			return this;
		}

		for (Simplexml_Element target : targets) {
			String extendsString = target.getString("extends");
			List<Simplexml_Element> sources = selectDescendants(extendsString);
			if (!sources.isEmpty()) {
				for (Simplexml_Element source : sources) {
					target.extend(source);
				}
			} else {
				log.warn("Not found extend: {}", extendsString);
			}
		}

		return this;
	}

	/**
	 * Sets xml for this configuration
	 */
	public void setXml(Simplexml_Element_JDom node) {
		_xml = node;
	}

	public Simplexml_Element getNode(String name) {
		if (_xml == null) {
			return Simplexml_Element.NULL;
		}

		if (name == null) {
			return _xml;
		}

		return _xml.selectDescendant(name);
	}

	public Simplexml_Element selectDescendant(String path) {
		if (_xml == null) {
			return Simplexml_Element.NULL;
		}

		return _xml.selectDescendant(path);
	}

	public List<Simplexml_Element> selectDescendants(String path) {
		if (_xml == null) {
			return Collections.emptyList();
		}

		return _xml.selectDescendants(path);
	}

	/**
	 * Stub method for processing file data right after loading the file text
	 */
	public String processFileData(String text) {
		return text;
	}

	/**
	 * Imports XML string
	 * 
	 * @param string
	 *            $string
	 * @return boolean
	 */
	public boolean loadString(String xmlString) {
		if (Simplexml_Element_JDom.class.isAssignableFrom(this._elementClass)) {
			this._xml = SimplexmlUtils.build(xmlString, this._elementClass);
			return true;
		}

		return false;
	}

	/**
	 * Imports XML file
	 * 
	 * @param string
	 *            $filePath
	 * @throws IOException
	 */
	public boolean loadFile(File xmlFile) {
		if (xmlFile.exists() && Simplexml_Element_JDom.class.isAssignableFrom(this._elementClass)) {
			this._xml = SimplexmlUtils.build(xmlFile, this._elementClass);
			return true;
		}

		return false;
	}

	public Varien_Simplexml_Config extend(Varien_Simplexml_Config config) {
		return extend(config, true);
	}

	/**
	 * Enter description here...
	 * 
	 * @param Varien_Simplexml_Config
	 *            $config
	 * @param boolean $overwrite
	 * @return Varien_Simplexml_Config
	 */
	public Varien_Simplexml_Config extend(Varien_Simplexml_Config config, boolean overwrite) {
		this._xml.extend(config._xml, overwrite);

		return this;
	}

	/**
	 * Create node by $path and set its value.
	 *
	 * @param string $path separated by slashes
	 * @param string $value
	 * @param boolean $overwrite
	 * @return Varien_Simplexml_Config
	 */
	public Varien_Simplexml_Config setNode(String path, String value, boolean overwrite) {
		_xml.setNode(path, value, overwrite);

		return this;
	}

	public Varien_Simplexml_Config setNode(String path, String value) {
		return setNode(path, value, true);
	}

	/**
	 * Return Xml of node as string
	 *
	 * @return string
	 */
	public String getXmlString() {
		return _xml.asNiceXml(false);
	}

	private static final String FALSE_CHECKSUM = new String();

	public class XmlConfigCache {
		private final String _cacheId;
		private boolean _cacheSaved = false;
		private String _cacheChecksum = null;
		private int _cacheLifetime = -1;
		private List<String> _cacheTags = new ArrayList<String>();

		private final CacheBackendInterface _cache;

		public XmlConfigCache(CacheBackendInterface cache, String cacheId) {
			if (cache == null || StringUtils.isBlank(cacheId)) {
				throw new IllegalArgumentException();
			}

			this._cache = cache;
			this._cacheId = cacheId;
		}

		public String getCacheId() {
			return _cacheId;
		}

		/**
		 * @return Varien_Simplexml_Config_Cache_Abstract
		 */
		public CacheBackendInterface getCache() {
			return _cache;
		}

		public boolean loadCache() {
			if (!validateCacheChecksum()) {
				return false;
			}

			String xmlString = (String)getCache().load(getCacheId(), false);
			if (StringUtils.isEmpty(xmlString)) {
				return false;
			}

			_xml = SimplexmlUtils.build(xmlString, _elementClass);
			return true;
		}

		public Varien_Simplexml_Config removeCache() {
			_cache.remove(getCacheId());
			_cache.remove(getCacheChecksumId());
			return Varien_Simplexml_Config.this;
		}

		public Varien_Simplexml_Config saveCache(List<String> tags) {
			if (_cacheSaved) {
				return Varien_Simplexml_Config.this;
			}

			String cacheChecksum = getCacheChecksum();
			if (FALSE_CHECKSUM == cacheChecksum) {
				return Varien_Simplexml_Config.this;
			}

			if (tags == null) {
				tags = _cacheTags;
			}

			if (cacheChecksum != null) {
				_cache.save(cacheChecksum, getCacheChecksumId(), tags, _cacheLifetime);
			}

			String xmlString = getXmlString();
			_cache.save(xmlString, getCacheId(), tags, _cacheLifetime);

			_cacheSaved = true;

			return Varien_Simplexml_Config.this;
		}

		private boolean validateCacheChecksum() {
			String newChecksum = getCacheChecksum();
			if (newChecksum == FALSE_CHECKSUM) {
				return false;
			}

			if (newChecksum == null) {
				return true;
			}

			String cachedChecksum = (String)getCache().load(getCacheChecksumId(), false);
			return (cachedChecksum == null || newChecksum.equals(cachedChecksum));
		}

		/**
		 * Enter description here...
		 * 
		 * @param string
		 *            $data
		 * @return Varien_Simplexml_Config
		 */
		public Varien_Simplexml_Config setCacheChecksum(String data) {
			if (StringUtils.isBlank(data)) {
				_cacheChecksum = null;
				// } else if (false===$data || 0===$data) {
				// $this->_cacheChecksum = false;
			} else {
				_cacheChecksum = Standard.md5(data);
			}

			return Varien_Simplexml_Config.this;
		}

		/**
		 * Enter description here...
		 * 
		 * @return string
		 */
		public String getCacheChecksum() {
			return _cacheChecksum;
		}

		/**
		 * Enter description here...
		 *
		 * @return string
		 */
		private String getCacheChecksumId() {
			return getCacheId() + "__CHECKSUM";
		}
	}
}
