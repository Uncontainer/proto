package com.yeon.monitor.resource;

/**
 * 
 * @author pulsarang
 */
public class SystemMonitoringPropertyInfo implements Comparable<SystemMonitoringPropertyInfo> {
	private final String canonicalName;
	private String beanName;
	private String propertyName;

	public SystemMonitoringPropertyInfo(String beanName, String propertyName) {
		super();
		this.beanName = beanName;
		this.propertyName = propertyName;

		this.canonicalName = beanName + "." + propertyName;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getCanonicalName() {
		return canonicalName;
	}

	@Override
	public int hashCode() {
		return canonicalName.hashCode();
	}

	@Override
	public int compareTo(SystemMonitoringPropertyInfo other) {
		if (other == null) {
			return -1;
		}

		return this.canonicalName.compareTo(other.canonicalName);
	}

	@Override
	public String toString() {
		return canonicalName;
	}
}
