package com.naver.mage4j.external.varien;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.naver.mage4j.external.php.Standard;

public class Varien_Object {
	/**
	* Object attributes
	*/
	protected Map<String, Object> _data = new HashMap<String, Object>();

	/**
	 * Data changes flag (true after setData|unsetData call)
	 */
	protected boolean _hasDataChanges = false;

	/**
	* Original data that was loaded
	*/
	protected Map<String, Object> _origData;

	/**
	* Setter/Getter underscore transformation cache
	*/
	protected Map<String, Object> _underscoreCache = new HashMap<String, Object>();

	/**
	 * Object delete flag
	 */
	protected boolean _isDeleted = false;

	/**
	 * Map short fields names to its full names
	 */
	protected Map<String, String> _oldFieldsMap = new HashMap<String, String>();

	/**
	* Name of object id field
	*/
	protected String _idFieldName = null;

	/**
	 * Map of fields to sync to other fields upon changing their data
	 */
	protected Map<String, String> _syncFieldsMap = new HashMap<String, String>();

	/**
	 * Constructor
	 *
	 * By default is looking for first argument as array and assignes it as object attributes
	 * This behaviour may change in child classes
	 *
	 */
	public Varien_Object(Map<String, Object> args) {
		_initOldFieldsMap();
		if (!_oldFieldsMap.isEmpty()) {
			_prepareSyncFieldsMap();
		}

		_data = args;
		if (_data == null) {
			_data = new HashMap<String, Object>();
		}

		_addFullNames();
	}

	public Varien_Object(Varien_Object options) {
		this(options != null ? new HashMap<String, Object>(options._data) : null);
	}

	public Varien_Object() {
		this((Map<String, Object>)null);
	}

	protected void _addFullNames() {
		for (Entry<String, Object> each : this._data.entrySet()) {
			String fullFieldName = this._syncFieldsMap.get(each.getKey());
			if (fullFieldName == null) {
				continue;
			}

			this._data.put(fullFieldName, each.getValue());
		}
	}

	/**
	* Inits mapping array of object's previously used fields to new fields.
	* Must be overloaded by descendants to set concrete fields map.
	*/
	protected Varien_Object _initOldFieldsMap() {
		return this;
	}

	/**
	 * Called after old fields are inited. Forms synchronization map to sync old fields and new fields
	 * between each other.
	 *
	 * @return Varien_Object
	 */
	protected Varien_Object _prepareSyncFieldsMap() {
		Map<String, String> old2New = _oldFieldsMap;

		Map<String, String> new2Old = Standard.array_flip(_oldFieldsMap);
		_syncFieldsMap = Standard.array_merge(old2New, new2Old);

		return this;
	}

	/**
	 * Set _isDeleted flag value (if $isDeleted param is defined) and return current flag value
	 *
	 * @param boolean $isDeleted
	 * @return boolean
	 */
	public boolean isDeleted(Boolean isDeleted) {
		boolean reuslt = _isDeleted;
		if (isDeleted != null) {
			_isDeleted = isDeleted;
		}

		return reuslt;
	}

	/**
	 * Get data change status
	 */
	public boolean hasDataChanges() {
		return _hasDataChanges;
	}

	/**
	 * set name of object id field
	 */
	public Varien_Object setIdFieldName(String name) {
		_idFieldName = name;
		return this;
	}

	/**
	 * Retrieve name of object id field
	 */
	public String getIdFieldName() {
		return _idFieldName;
	}

	/**
	 * Retrieve object id
	 */
	public Object getId() {
		String idFieldName = getIdFieldName();
		if (idFieldName == null) {
			idFieldName = "id";
		}

		return _getData(idFieldName);
	}

	/**
	 * Set object id field value
	 */
	public Varien_Object setId(Object value) {
		String idFieldName = getIdFieldName();
		if (idFieldName == null) {
			idFieldName = "id";
		}

		setData(idFieldName, value);

		return this;
	}

	/**
	* Add data to the object.
	*
	* Retains previous data in the object.
	*
	* @param array $arr
	* @return Varien_Object
	*/
	public Varien_Object addData(Map<String, Object> data) {
		if (data != null) {
			for (Entry<String, Object> each : data.entrySet()) {
				this.setData(each.getKey(), each.getValue());
			}
		}

		return this;
	}

	/**
	 * Overwrite data in the object.
	 *
	 * $key can be string or array.
	 * If $key is string, the attribute value will be overwritten by $value
	 *
	 * If $key is an array, it will overwrite all the data in the object.
	 *
	 * @param string|array $key
	 * @param mixed $value
	 * @return Varien_Object
	 */
	public Varien_Object setData(String key, Object value)
	{
		this._hasDataChanges = true;
		String fullFieldName = this._syncFieldsMap.get(key);
		if (fullFieldName != null) {
			key = fullFieldName;
		}

		this._data.put(key, value);

		return this;
	}

	public Varien_Object setData(Map<String, Object> data) {
		if (data == null) {
			throw new IllegalArgumentException();
		}

		this._hasDataChanges = true;
		this._data = data;
		this._addFullNames();

		return this;
	}

	/**
	 * Unset data from the object.
	 *
	 * $key can be a string only. Array will be ignored.
	 *
	 * @param string $key
	 * @return Varien_Object
	 */
	public Varien_Object unsetData(String key) {
		_hasDataChanges = true;
		if (key == null) {
			_data = new HashMap<String, Object>();
		} else {
			_data.remove(key);
			if (_syncFieldsMap.containsKey(key)) {
				String fullFieldName = _syncFieldsMap.get(key);
				_data.remove(fullFieldName);
			}
		}

		return this;
	}

	/**
	 * Unset old fields data from the object.
	 *
	 * $key can be a string only. Array will be ignored.
	 *
	 * @param string $key
	 * @return Varien_Object
	 */
	public Varien_Object unsetOldData(String key) {
		if (key == null) {
			for (String eachKey : _oldFieldsMap.keySet()) {
				_data.remove(eachKey);
			}
		} else {
			_data.remove(key);
		}

		return this;
	}

	/**
	 * Retrieves data from the object
	 *
	 * If $key is empty will return all the data as an array
	 * Otherwise it will return value of the attribute specified by $key
	 *
	 * If $index is specified it will assume that attribute data is an array
	 * and retrieve corresponding member.
	 *
	 * @param string $key
	 * @param string|int $index
	 * @return mixed
	 */
	public Object getData(String key/*, $index=null*/) {
		if (StringUtils.isBlank(key)) {
			return _data;
		}

		Object defaultValue = null;

		// accept a/b/c as ['a']['b']['c']
		if (key.indexOf('/') >= 0) {
			String[] keyArr = key.split("/");
			Object data = _data;
			for (int i = 0; i < keyArr.length; i++) {
				String k = keyArr[i];
				if ("".equals(k)) {
					return defaultValue;
				}

				if (data instanceof Map) {
					if (!((Map)data).containsKey(k)) {
						return defaultValue;
					}

					data = ((Map)data).get(key);
				} else if (data instanceof Varien_Object) {
					data = ((Varien_Object)data).getData(k);
				} else {
					return defaultValue;
				}
			}

			return data;
		}

		// legacy functionality for $index
		if (_data.containsKey(key)) {
			//            if (is_null($index)) {
			return _data.get(key);
			//            }

			//            $value = $this->_data[$key];
			//            if (is_array($value)) {
			//                //if (isset($value[$index]) && (!empty($value[$index]) || strlen($value[$index]) > 0)) {
			//                /**
			//                 * If we have any data, even if it empty - we should use it, anyway
			//                 */
			//                if (isset($value[$index])) {
			//                    return $value[$index];
			//                }
			//                return null;
			//            } elseif (is_string($value)) {
			//                $arr = explode("\n", $value);
			//                return (isset($arr[$index]) && (!empty($arr[$index]) || strlen($arr[$index]) > 0))
			//                    ? $arr[$index] : null;
			//            } elseif ($value instanceof Varien_Object) {
			//                return $value->getData($index);
			//            }
			//            return defaultValue;
		}

		return defaultValue;
	}

	/**
	 * Get value from _data array without parse key
	 *
	 * @param   string $key
	 * @return  mixed
	 */
	protected Object _getData(String key) {
		return _data.get(key);
	}

	/**
	 * Fast get data or set default if value is not available
	 *
	 * @param string $key
	 * @param mixed $default
	 * @return mixed
	 */
	public Object getDataSetDefault(String key, Object defaultValue) {
		if (!_data.containsKey(key)) {
			_data.put(key, defaultValue);
			return defaultValue;
		} else {
			return _data.get(key);
		}
	}

	/**
	 * If $key is empty, checks whether there's any data in the object
	 * Otherwise checks if the specified attribute is set.
	 *
	 * @param string $key
	 * @return boolean
	 */
	public boolean hasData(String key) {
		if (StringUtils.isEmpty(key)) {
			return _data != null;
		}

		return _data.containsKey(key);
	}

	/**
	 * Convert object attributes to array
	 *
	 * @param  array $arrAttributes array of required attributes
	 * @return array
	 */
	public Map<String, Object> toArray(List<String> arrAttributes) {
		if (CollectionUtils.isEmpty(arrAttributes)) {
			return _data;
		}

		Map<String, Object> arrRes = new HashMap<String, Object>();

		for (String attribute : arrAttributes) {
			arrRes.put(attribute, _data.get(attribute));
		}

		return arrRes;
	}

	/**
	 * Set required array elements
	 *
	 * @param   array $arr
	 * @param   array $elements
	 * @return  array
	 */
	protected Map<String, Object> _prepareArray(Map<String, Object> arr, List<String> elements) {
		for (String element : elements) {
			if (!arr.containsKey(element)) {
				arr.put(element, null);
			}
		}

		return arr;
	}

	/**
	 * Convert object attributes to XML
	 *
	 * @param  array $arrAttributes array of required attributes
	 * @param string $rootName name of the root element
	 * @return string
	 */
	protected String toXml(List<String> arrAttributes, String rootName, boolean addOpenTag, boolean addCdata) {
		StringBuilder xml = new StringBuilder();
		if (addOpenTag) {
			xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\n");
		}
		if (!StringUtils.isEmpty(rootName)) {
			xml.append("<").append(rootName).append(">").append("\n");
		}

		//		Varien_Simplexml_Element xmlModel = new Varien_Simplexml_Element("<node></node>");
		Map<String, Object> arrData = toArray(arrAttributes);
		for (Entry<String, Object> each : arrData.entrySet()) {
			xml.append("<").append(each.getKey()).append(">").append("\n");
			if (addCdata) {
				xml.append("<![CDATA[").append(each.getValue()).append("]]>");
			} else {
				xml.append(StringEscapeUtils.escapeXml(each.getValue().toString()));
				//                $fieldValue = $xmlModel->xmlentities($fieldValue);
			}
			xml.append("</").append(each.getKey()).append(">").append("\n");
		}
		if (!StringUtils.isEmpty(rootName)) {
			xml.append("</").append(rootName).append(">").append("\n");
		}
		return xml.toString();
	}

	/**
	 * checks whether the object is empty
	 *
	 * @return boolean
	 */
	public boolean isEmpty() {
		return _data.isEmpty();
	}
}
