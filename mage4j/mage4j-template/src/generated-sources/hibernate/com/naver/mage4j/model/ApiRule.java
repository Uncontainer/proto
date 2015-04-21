package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * ApiRule generated by hbm2java
 */
@Entity
@Table(name = "api_rule"
	, catalog = "magento")
public class ApiRule implements java.io.Serializable {

	private Integer ruleId;
	private ApiRole apiRole;
	private String resourceId;
	private String apiPrivileges;
	private int assertId;
	private String roleType;
	private String apiPermission;

	public ApiRule() {
	}

	public ApiRule(ApiRole apiRole, int assertId) {
		this.apiRole = apiRole;
		this.assertId = assertId;
	}

	public ApiRule(ApiRole apiRole, String resourceId, String apiPrivileges, int assertId, String roleType, String apiPermission) {
		this.apiRole = apiRole;
		this.resourceId = resourceId;
		this.apiPrivileges = apiPrivileges;
		this.assertId = assertId;
		this.roleType = roleType;
		this.apiPermission = apiPermission;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "rule_id", unique = true, nullable = false)
	public Integer getRuleId() {
		return this.ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	public ApiRole getApiRole() {
		return this.apiRole;
	}

	public void setApiRole(ApiRole apiRole) {
		this.apiRole = apiRole;
	}

	@Column(name = "resource_id")
	public String getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	@Column(name = "api_privileges", length = 20)
	public String getApiPrivileges() {
		return this.apiPrivileges;
	}

	public void setApiPrivileges(String apiPrivileges) {
		this.apiPrivileges = apiPrivileges;
	}

	@Column(name = "assert_id", nullable = false)
	public int getAssertId() {
		return this.assertId;
	}

	public void setAssertId(int assertId) {
		this.assertId = assertId;
	}

	@Column(name = "role_type", length = 1)
	public String getRoleType() {
		return this.roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	@Column(name = "api_permission", length = 10)
	public String getApiPermission() {
		return this.apiPermission;
	}

	public void setApiPermission(String apiPermission) {
		this.apiPermission = apiPermission;
	}

}