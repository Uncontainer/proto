package com.pulsarang.infra.config;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ConfigEntityId implements Serializable, Comparable<ConfigEntityId> {
	private static final long serialVersionUID = -4862748768682236500L;

	@Column(name = "tkt_nm", length = 32, nullable = false)
	String name;

	// 티켓 카테고리
	@Column(name = "tkt_catg", length = 20, nullable = false)
	String category;

	public ConfigEntityId() {
		super();
	}

	public ConfigEntityId(String category, String name) {
		super();
		this.category = category;
		this.name = name;
	}

	public ConfigEntityId(ConfigId configId) {
		this.category = configId.getCategoryName();
		this.name = configId.getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return name + "@" + category;
	}

	@Override
	public int compareTo(ConfigEntityId o) {
		if (o == null) {
			return 1;
		}

		return toString().compareTo(o.toString());
	}
}
