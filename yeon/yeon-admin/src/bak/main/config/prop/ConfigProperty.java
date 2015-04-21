package com.pulsarang.infra.config.prop;

import com.pulsarang.infra.config.propinfo.ConfigPropertyInfo;

import javax.persistence.*;

@Entity
@Table(name = "tkt_prop")
public class ConfigProperty {
	@Id
	ConfigPropertyId id;

	@Column(name = "tkt_prop_val_cont", length = 1000, nullable = false)
	String propertyValue;

	@Transient
	ConfigPropertyInfo configPropertyInfo;

	@Transient
	String cacheValue;

	public ConfigProperty() {
	}

	public ConfigProperty(ConfigPropertyId id, String propertyValue) {
		super();
		this.id = id;
		this.propertyValue = propertyValue;
	}

	public ConfigPropertyId getId() {
		return id;
	}

	public void setId(ConfigPropertyId id) {
		this.id = id;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public ConfigPropertyInfo getConfigPropertyInfo() {
		return configPropertyInfo;
	}

	public void setConfigPropertyInfo(ConfigPropertyInfo configPropertyInfo) {
		this.configPropertyInfo = configPropertyInfo;
	}

	public String getCacheValue() {
		return cacheValue;
	}

	public void setCacheValue(String cacheValue) {
		this.cacheValue = cacheValue;
	}
}
