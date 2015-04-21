package com.pulsarang.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class MapModel implements Map<String, Object> {
	private boolean readOnly = false;
	protected Map<String, Object> properties;

	public MapModel() {
		this(new HashMap<String, Object>());
	}

	public MapModel(int size) {
		this(new HashMap<String, Object>(size));
	}

	public MapModel(MapModel model) {
		if (model == null) {
			setProperties(null);
		} else {
			setProperties(model.getProperties());
		}
	}

	public MapModel(Map<String, Object> properties) {
		setProperties(properties);
	}

	public void setProperties(Map<String, Object> properties) {
		if (readOnly) {
			throw new UnsupportedOperationException("It's unmodifiable.");
		}

		if (properties == null) {
			this.properties = new HashMap<String, Object>();
		} else {
			this.properties = properties;
		}
	}

	public void unmodifiable() {
		if (!readOnly) {
			properties = Collections.unmodifiableMap(properties);
			readOnly = true;
		}
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	@SuppressWarnings("unchecked")
	public <T extends MapModel> T dup() {
		return (T) fromMap(new HashMap<String, Object>(getProperties()), this.getClass());
	}

	public <T extends MapModel> T dup(Class<T> clazz) {
		return (T) fromMap(new HashMap<String, Object>(getProperties()), clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> T getProperty(String name) {
		return (T) properties.get(name);
	}

	@SuppressWarnings("unchecked")
	public <T> T getProperty(String name, T defaultValue) {
		Object value = properties.get(name);
		if (value == null) {
			return defaultValue;
		} else {
			return (T) value;
		}
	}

	public void setProperty(String name, Object value) {
		properties.put(name, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T removeProperty(String name) {
		return (T) properties.remove(name);
	}

	public Boolean getBoolean(String name) {
		return getProperty(name);
	}

	public Boolean getBoolean(String name, Boolean defaultValue) {
		return getProperty(name, defaultValue);
	}

	public void setBoolean(String name, Boolean value) {
		setProperty(name, value);
	}

	public Byte getByte(String name) {
		Number value = getProperty(name);
		return value != null ? value.byteValue() : null;
	}

	public Byte getByte(String name, Byte defaultValue) {
		Byte value = getByte(name);
		return value != null ? value : defaultValue;
	}

	public void setByte(String name, Byte value) {
		setProperty(name, value);
	}

	public Double getDouble(String name) {
		return getProperty(name);
	}

	public Double getDouble(String name, Double defaultValue) {
		return getProperty(name, defaultValue);
	}

	public void setDouble(String name, Double value) {
		setProperty(name, value);
	}

	public Float getFloat(String name) {
		Number value = getProperty(name);
		return value != null ? value.floatValue() : null;
	}

	public Float getFloat(String name, Float defaultValue) {
		Float value = getFloat(name);
		return value != null ? value : defaultValue;
	}

	public void setFloat(String name, Float value) {
		setProperty(name, value);
	}

	public Integer getInteger(String name) {
		return getProperty(name);
	}

	public Integer getInteger(String name, Integer defaultValue) {
		return getProperty(name, defaultValue);
	}

	public void setInteger(String name, Integer value) {
		setProperty(name, value);
	}

	public Short getShort(String name) {
		Number value = getProperty(name);
		return value != null ? value.shortValue() : null;
	}

	public Short getShort(String name, Short defaultValue) {
		Short value = getShort(name);
		return value != null ? value : defaultValue;
	}

	public void setShort(String name, Short value) {
		setProperty(name, value);
	}

	public String getString(String name) {
		return getString(name, null);
	}

	public String getString(String name, String defaultValue) {
		return getProperty(name, defaultValue);
	}

	public void setString(String name, String value) {
		setProperty(name, value);
	}

	public Long getLong(String name) {
		Number value = getProperty(name);
		return value != null ? value.longValue() : null;
	}

	public Long getLong(String name, Long defaultValue) {
		Long value = getLong(name);
		return value != null ? value : defaultValue;
	}

	public void setLong(String name, long value) {
		setProperty(name, value);
	}

	public Date getDate(String name) {
		Object value = getProperty(name);
		if (value == null) {
			return null;
		}

		if (value instanceof Date) {
			return (Date) value;
		} else if (value instanceof Number) {
			return new Date(((Number) value).longValue());
		} else {
			throw new IllegalArgumentException("'" + name + "=" + value + "' is not a date value.");
		}
	}

	public Date getDate(String name, Date defaultValue) {
		Date date = getDate(name);
		return date != null ? date : defaultValue;
	}

	public void setDate(String name, Date date) {
		if (date == null) {
			setProperty(name, null);
		} else {
			setProperty(name, date.getTime());
		}
	}

	public BigDecimal getBigDecimal(String key) {
		return getBigDecimal(key, null);
	}

	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		return getProperty(key, defaultValue);
	}

	public BigInteger getBigInteger(String key) {
		return getBigInteger(key, null);
	}

	public BigInteger getBigInteger(String key, BigInteger defaultValue) {
		return getProperty(key, defaultValue);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getList(String name) {
		return (List<T>) getProperty(name);
	}

	public <T> void setList(String name, List<T> value) {
		setProperty(name, value);
	}

	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> getMap(String name) {
		return (Map<K, V>) getProperty(name);
	}

	public <K, V> void setMap(String name, Map<K, V> value) {
		setProperty(name, value);
	}

	public <T extends MapModel> T getMapModel(String name, Class<T> clazz) {
		Map<String, Object> map = getMap(name);
		return MapModel.fromMap(map, clazz);
	}

	public <T extends MapModel> void setMapModel(String name, T value) {
		setMap(name, value);
	}

	/*-----------------------------------------
	 * 기타
	 -----------------------------------------*/

	@Override
	public String toString() {
		return properties.toString();
	}

	@Override
	public int hashCode() {
		return properties.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		return this.properties.equals(((MapModel) obj).getProperties());
	}

	/*-----------------------------------------
	 * json 관련
	 -----------------------------------------*/

	public String toJson() {
		return CollectionJsonMapper.getInstance().toJson(getProperties());
	}

	public void fromJson(String json) {
		if (StringUtils.isEmpty(json)) {
			return;
		}

		setProperties(CollectionJsonMapper.getInstance().toMap(json));
	}

	@SuppressWarnings("unchecked")
	public <T extends MapModel> T convert(Class<T> clazz) {
		if (this.getClass() == clazz) {
			return (T) this;
		}

		return fromMap(getProperties(), clazz);
	}

	public static <T extends MapModel> T fromJson(String json, Class<T> clazz) {
		if (json == null) {
			return null;
		}

		return new MapModelReader<T>(json, clazz).get();
	}

	public static <T extends MapModel> T fromMap(Map<String, Object> map, Class<T> clazz) {
		T instance;
		try {
			instance = clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		if (map != null) {
			instance.setProperties(map);
		}

		return instance;
	}

	@SuppressWarnings("unchecked")
	public static final <T extends MapModel> T convert(MapModel model, Class<T> clazz) {
		if (model == null) {
			return null;
		}

		if (model.getClass() == clazz) {
			return (T) model;
		}

		return fromMap(model.getProperties(), clazz);
	}

	/*-----------------------------------------
	 * Map interface 구현
	 -----------------------------------------*/

	@Override
	public int size() {
		return properties.size();
	}

	@Override
	public boolean isEmpty() {
		return properties.isEmpty();
	}

	@Override
	public boolean containsKey(Object paramObject) {
		return properties.containsKey(paramObject);
	}

	@Override
	public boolean containsValue(Object paramObject) {
		return properties.containsValue(paramObject);
	}

	@Override
	public Object get(Object paramObject) {
		return properties.get(paramObject);
	}

	@Override
	public Object put(String paramK, Object paramV) {
		return properties.put(paramK, paramV);
	}

	@Override
	public Object remove(Object paramObject) {
		return properties.remove(paramObject);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> paramMap) {
		properties.putAll(paramMap);
	}

	@Override
	public void clear() {
		properties.clear();
	}

	@Override
	public Set<String> keySet() {
		return properties.keySet();
	}

	@Override
	public Collection<Object> values() {
		return properties.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return properties.entrySet();
	}
}
