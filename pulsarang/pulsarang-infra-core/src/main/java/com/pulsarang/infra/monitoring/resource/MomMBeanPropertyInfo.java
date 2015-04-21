package com.pulsarang.infra.monitoring.resource;

/**
 * 
 * @author pulsarang
 */
public class MomMBeanPropertyInfo {
	private final String name;
	private final Class<?> javaType;

	public MomMBeanPropertyInfo(String name, Class<?> javaType) {
		super();
		if (name == null || javaType == null) {
			throw new IllegalArgumentException();
		}

		this.name = name;
		this.javaType = javaType;
	}

	public String getName() {
		return name;
	}

	public Class<?> getJavaType() {
		return javaType;
	}

	@Override
	public String toString() {
		return name + "@" + javaType.getCanonicalName();
	}
}
