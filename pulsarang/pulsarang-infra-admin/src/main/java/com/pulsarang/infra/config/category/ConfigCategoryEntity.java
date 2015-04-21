package com.pulsarang.infra.config.category;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pulsarang.infra.config.scope.ConfigScopeType;

@Entity
@Table(name = "cnfg_ctgr")
public class ConfigCategoryEntity {
	@Id
	@Column(name = "ctgr_nm", length = 30)
	String name;

	@Column(name = "ctgr_desc", length = 1000, nullable = true)
	String description;

	@Column(name = "scope_tp_cd", length = 10, nullable = false)
	String scopeTypeCode;

	@Column(name = "super_ctgr_nms", length = 1000)
	String superCategoryNames;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScopeTypeCode() {
		return scopeTypeCode;
	}

	public void setScopeTypeCode(String scopeTypeCode) {
		this.scopeTypeCode = scopeTypeCode;
	}

	public String getSuperCategoryNames() {
		return superCategoryNames;
	}

	public void setSuperCategoryNames(String superCategoryNames) {
		this.superCategoryNames = superCategoryNames;
	}

	public ConfigScopeType getScopeType() {
		if (scopeTypeCode == null) {
			return null;
		}

		return ConfigScopeType.valueOf(scopeTypeCode);
	}

}
