package com.pulsarang.infra.config.propinfo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ConfigPropertyInfoId implements Serializable {
	private static final long serialVersionUID = 2924085758190240253L;

	// 티켓 카테고리(설정 적용 범위)
	@Column(name = "tkt_catg", length = 20, nullable = false)
	String configCategory;

	// 설정값 이름
	@Column(name = "tkt_prop_nm", length = 50, nullable = false)
	String propertyName;

	public ConfigPropertyInfoId() {
	}

	public ConfigPropertyInfoId(String configCategory, String propertyName) {
		super();
		this.configCategory = configCategory;
		this.propertyName = propertyName;
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
		return propertyName + "@" + configCategory;
	}
}
