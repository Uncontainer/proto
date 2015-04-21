package com.yeon.util;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class MapModel implements Map<String, Object> {
	protected volatile Map<String, Object> data;

	public MapModel() {
		this(new HashMap<String, Object>());
	}

	public MapModel(int size) {
		this(new HashMap<String, Object>(size));
	}

	public MapModel(MapModel model) {
		if (model == null) {
			setValues(null);
		} else {
			setValues(model.getValues());
		}
	}

	public MapModel(MapModel model, boolean clone) {
		if (model == null) {
			setValues(null);
		} else {
			setValues(clone ? new HashMap<String, Object>(model.getValues()) : model.getValues());
		}
	}

	public MapModel(Map<String, Object> properties) {
		setValues(properties);
	}

	public void unmodifiable() {
		if (data instanceof HashMap) {
			return;
		}

		data = Collections.unmodifiableMap(data);
	}

	public void setValues(Map<String, Object> properties) {
		if (data instanceof HashMap) {
			throw new IllegalStateException("It's unmodifiable.");
		}

		if (properties == null) {
			this.data = new HashMap<String, Object>();
		} else {
			this.data = properties;
		}
	}

	public Map<String, Object> getValues() {
		return data;
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(String name) {
		return (T) data.get(name);
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(String name, T defaultValue) {
		Object value = data.get(name);
		if (value == null) {
			return defaultValue;
		} else {
			return (T) value;
		}
	}

	public void setValue(String name, Object value) {
		data.put(name, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T removeValue(String name) {
		return (T) data.remove(name);
	}

	public Boolean getBoolean(String name) {
		return getValue(name);
	}

	public Boolean getBoolean(String name, Boolean defaultValue) {
		return getValue(name, defaultValue);
	}

	public void setBoolean(String name, Boolean value) {
		setValue(name, value);
	}

	public Byte getByte(String name) {
		Number value = getValue(name);
		return value != null ? value.byteValue() : null;
	}

	public Byte getByte(String name, Byte defaultValue) {
		Byte value = getByte(name);
		return value != null ? value : defaultValue;
	}

	public void setByte(String name, Byte value) {
		setValue(name, value);
	}

	public Double getDouble(String name) {
		return getValue(name);
	}

	public Double getDouble(String name, Double defaultValue) {
		return getValue(name, defaultValue);
	}

	public void setDouble(String name, Double value) {
		setValue(name, value);
	}

	public Float getFloat(String name) {
		Number value = getValue(name);
		return value != null ? value.floatValue() : null;
	}

	public Float getFloat(String name, Float defaultValue) {
		Float value = getFloat(name);
		return value != null ? value : defaultValue;
	}

	public void setFloat(String name, Float value) {
		setValue(name, value);
	}

	public Integer getInteger(String name) {
		return getValue(name);
	}

	public Integer getInteger(String name, Integer defaultValue) {
		return getValue(name, defaultValue);
	}

	public void setInteger(String name, Integer value) {
		setValue(name, value);
	}

	public Short getShort(String name) {
		Number value = getValue(name);
		return value != null ? value.shortValue() : null;
	}

	public Short getShort(String name, Short defaultValue) {
		Short value = getShort(name);
		return value != null ? value : defaultValue;
	}

	public void setShort(String name, Short value) {
		setValue(name, value);
	}

	public String getString(String name) {
		return getString(name, null);
	}

	public String getString(String name, String defaultValue) {
		return getValue(name, defaultValue);
	}

	public void setString(String name, String value) {
		setValue(name, value);
	}

	public Long getLong(String name) {
		Number value = getValue(name);
		return value != null ? value.longValue() : null;
	}

	public Long getLong(String name, Long defaultValue) {
		Long value = getLong(name);
		return value != null ? value : defaultValue;
	}

	public void setLong(String name, long value) {
		setValue(name, value);
	}

	public Date getDate(String name) {
		Object value = getValue(name);
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
			setValue(name, null);
		} else {
			setValue(name, date.getTime());
		}
	}

	public BigDecimal getBigDecimal(String key) {
		return getBigDecimal(key, null);
	}

	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		return getValue(key, defaultValue);
	}

	public BigInteger getBigInteger(String key) {
		return getBigInteger(key, null);
	}

	public BigInteger getBigInteger(String key, BigInteger defaultValue) {
		return getValue(key, defaultValue);
	}

	public <T extends Enum<T>> T getEnum(String name, T defalutValue, Class<T> clazz) {
		return toEnum(clazz, getString(name), defalutValue);
	}

	public void setEnum(String name, Enum<?> value) {
		if (value == null) {
			setValue(name, null);
		} else {
			setValue(name, value.name());
		}
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getList(String name) {
		return (List<T>) getValue(name);
	}

	public <T> List<T> getList(String name, List<T> defaultValue) {
		List<T> list = getList(name);
		if (list == null) {
			return defaultValue;
		}

		return list;
	}

	public <T> void setList(String name, List<T> value) {
		setValue(name, value);
	}

	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> getMap(String name) {
		return (Map<K, V>) getValue(name);
	}

	public <K, V> void setMap(String name, Map<K, V> value) {
		setValue(name, value);
	}

	public <T extends MapModel> T getMapModel(String name, Class<T> clazz) {
		Map<String, Object> map = getMap(name);
		return MapModel.fromMap(map, clazz);
	}

	public <T extends MapModel> void setMapModel(String name, T value) {
		setMap(name, value);
	}

	protected static <T extends Enum<T>> T toEnum(Class<T> clazz, String enumName, T defalutValue) {
		if (enumName == null) {
			return defalutValue;
		}

		for (T item : EnumSet.allOf(clazz)) {
			if (item.name().equals(enumName)) {
				return item;
			}
		}

		return defalutValue;
	}

	/*-----------------------------------------
	 * 기타
	 -----------------------------------------*/

	@Override
	public String toString() {
		return data.toString();
	}

	@Override
	public int hashCode() {
		return data.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		return this.data.equals(((MapModel) obj).getValues());
	}

	/*-----------------------------------------
	 * json 관련
	 -----------------------------------------*/

	public String toJson() {
		return CollectionJsonMapper.getInstance().toJson(getValues());
	}

	public void fromJson(String json) {
		if (StringUtils.isEmpty(json)) {
			return;
		}

		setValues(CollectionJsonMapper.getInstance().toMap(json));
	}

	@SuppressWarnings("unchecked")
	public <T extends MapModel> T convert(Class<T> clazz) {
		if (this.getClass() == clazz) {
			return (T) this;
		}

		return fromMap(getValues(), clazz);
	}

	public static <T extends MapModel> T fromJson(String json, Class<T> clazz) {
		if (json == null) {
			return null;
		}

		return new MapModelReader<T>(json, clazz).get();
	}

	public static MapModel fromObject(Object object) {
		// TODO 최적화 고려
		return fromJson(CollectionJsonMapper.getInstance().toJson(object), MapModel.class);
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
			instance.setValues(map);
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

		return fromMap(model.getValues(), clazz);
	}

	/*-----------------------------------------
	 * Map interface 구현
	 -----------------------------------------*/

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public boolean containsKey(Object paramObject) {
		return data.containsKey(paramObject);
	}

	@Override
	public boolean containsValue(Object paramObject) {
		return data.containsValue(paramObject);
	}

	@Override
	public Object get(Object paramObject) {
		return data.get(paramObject);
	}

	@Override
	public Object put(String paramK, Object paramV) {
		return data.put(paramK, paramV);
	}

	@Override
	public Object remove(Object paramObject) {
		return data.remove(paramObject);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> paramMap) {
		data.putAll(paramMap);
	}

	@Override
	public void clear() {
		data.clear();
	}

	@Override
	public Set<String> keySet() {
		return data.keySet();
	}

	@Override
	public Collection<Object> values() {
		return data.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return data.entrySet();
	}
}
