package com.pulsarang.infra.config.prop;

import com.pulsarang.infra.config.ConfigEntityId;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ConfigPropertyId implements Comparable<ConfigPropertyId>, Serializable {
	private static final long serialVersionUID = 42558431342152263L;

	public ConfigPropertyId() {
		super();
	}

	public ConfigPropertyId(String configName, String configCategory, String propertyName) {
		super();
		this.configName = configName;
		this.configCategory = configCategory;
		this.propertyName = propertyName;
	}

	public ConfigPropertyId(ConfigEntityId id, String propertyName) {
		this(id.getName(), id.getCategory(), propertyName);
	}

	@Column(name = "tkt_nm", length = 32, nullable = false)
	String configName;

	// 티켓 카테고리(설정 적용 범위)
	@Column(name = "tkt_catg", length = 20, nullable = false)
	String configCategory;

	// 설정값 이름
	@Column(name = "tkt_prop_nm", length = 50, nullable = false)
	String propertyName;

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getConfigCategory() {
		return configCategory;
	}

	public void setConfigCategory(String configCategory) {
		this.configCategory = configCategory;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	@Override
	public String toString() {
		return propertyName + "@" + configName + "." + configCategory;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		return toString().equals(obj.toString());
	}

	@Override
	public int compareTo(ConfigPropertyId o) {
		if (o == null) {
			return 1;
		}

		return toString().compareTo(o.toString());
	}

}
