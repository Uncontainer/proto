package com.pulsarang.infra.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import com.pulsarang.core.util.MapModel;

public class Config extends MapModel {
	protected static final String PARAM_CONFIG_CATEGORY_NAME = "_cnfg_category_name";
	protected static final String PARAM_CONFIG_NAME = "_cnfg_name";
	protected static final String PARAM_NAME = "name";
	protected static final String PARAM_STATUS = "status";
	protected static final String PARAM_MONITORING_LEVEL = "monitoring_level";

	protected static final List<String> EMPTY_LIST = Collections.emptyList();

	public Config() {
	}

	public Config(Map<String, Object> properties) {
		super(properties);
	}

	public boolean isNull() {
		return properties == null;
	}

	public String getConfigName() {
		return getProperty(PARAM_CONFIG_NAME);
	}

	public void setConfigName(String configName) {
		setProperty(PARAM_CONFIG_NAME, configName);
	}

	public String getConfigCategoryName() {
		return getProperty(PARAM_CONFIG_CATEGORY_NAME);
	}

	public void setConfigCategoryName(String configCategoryName) {
		setProperty(PARAM_CONFIG_CATEGORY_NAME, configCategoryName);
	}

	public void setConfigId(ConfigId configId) {
		if (configId == null) {
			removeProperty(PARAM_CONFIG_CATEGORY_NAME);
			removeProperty(PARAM_CONFIG_NAME);
		} else {
			setConfigCategoryName(configId.getCategoryName());
			setConfigName(configId.getName());
		}
	}

	public ConfigId getConfigId() {
		// TODO null 처리 추가.
		return new ConfigId(getConfigCategoryName(), getConfigName());
	}

	@Override
	public void setProperties(Map<String, Object> properties) {
		super.setProperties(properties);
		Class<?> clazz = getClass();
		while (!Config.class.equals(clazz)) {
			initializeFields(clazz, this);
			clazz = clazz.getSuperclass();
		}
	}

	public <T extends Config> T convertTo(Class<T> configClass) {
		return fromMap(properties, configClass);
	}

	void setPropertiesToNull() {
		super.properties = null;
	}

	protected static void initializeFields(Class<?> clazz, Object obj) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
				continue;
			}

			Class<?> type = field.getType();
			if (Object.class.isAssignableFrom(type)) {
				try {
					if (field.isAccessible()) {
						field.set(obj, null);
					} else {
						field.setAccessible(true);
						field.set(obj, null);
						field.setAccessible(false);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			} else if (type.isPrimitive()) {
				// TODO 기본 값으로 초기화할 지 여부 결정.
			}
		}
	}

	protected static <T extends Enum<T>> T getEnum(Class<T> clazz, String name, T defalutValue) {
		if (name == null) {
			return defalutValue;
		}

		for (T item : EnumSet.allOf(clazz)) {
			if (item.name().equals(name)) {
				return item;
			}
		}

		return defalutValue;
	}
}
