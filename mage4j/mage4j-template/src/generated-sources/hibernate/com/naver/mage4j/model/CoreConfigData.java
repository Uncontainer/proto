package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * CoreConfigData generated by hbm2java
 */
@Entity
@Table(name = "core_config_data"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = {"scope", "scope_id", "path"}))
public class CoreConfigData implements java.io.Serializable {

	private Integer configId;
	private String scope;
	private int scopeId;
	private String path;
	private String value;

	public CoreConfigData() {
	}

	public CoreConfigData(String scope, int scopeId, String path) {
		this.scope = scope;
		this.scopeId = scopeId;
		this.path = path;
	}

	public CoreConfigData(String scope, int scopeId, String path, String value) {
		this.scope = scope;
		this.scopeId = scopeId;
		this.path = path;
		this.value = value;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "config_id", unique = true, nullable = false)
	public Integer getConfigId() {
		return this.configId;
	}

	public void setConfigId(Integer configId) {
		this.configId = configId;
	}

	@Column(name = "scope", nullable = false, length = 8)
	public String getScope() {
		return this.scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@Column(name = "scope_id", nullable = false)
	public int getScopeId() {
		return this.scopeId;
	}

	public void setScopeId(int scopeId) {
		this.scopeId = scopeId;
	}

	@Column(name = "path", nullable = false)
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(name = "value", length = 65535)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
