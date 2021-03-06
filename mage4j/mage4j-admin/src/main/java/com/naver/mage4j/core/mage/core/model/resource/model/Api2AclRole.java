package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Api2AclRole generated by hbm2java
 */
@Entity
@Table(name = "api2_acl_role"
	, catalog = "magento")
public class Api2AclRole implements java.io.Serializable {

	private Integer entityId;
	private Date createdAt;
	private Date updatedAt;
	private String roleName;
	private Set<Api2AclUser> api2AclUsers = new HashSet<Api2AclUser>(0);
	private Set<Api2AclRule> api2AclRules = new HashSet<Api2AclRule>(0);

	public Api2AclRole() {
	}

	public Api2AclRole(Date createdAt, String roleName) {
		this.createdAt = createdAt;
		this.roleName = roleName;
	}

	public Api2AclRole(Date createdAt, Date updatedAt, String roleName, Set<Api2AclUser> api2AclUsers, Set<Api2AclRule> api2AclRules) {
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.roleName = roleName;
		this.api2AclUsers = api2AclUsers;
		this.api2AclRules = api2AclRules;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "entity_id", unique = true, nullable = false)
	public Integer getEntityId() {
		return this.entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false, length = 19)
	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", length = 19)
	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Column(name = "role_name", nullable = false)
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "api2AclRole")
	public Set<Api2AclUser> getApi2AclUsers() {
		return this.api2AclUsers;
	}

	public void setApi2AclUsers(Set<Api2AclUser> api2AclUsers) {
		this.api2AclUsers = api2AclUsers;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "api2AclRole")
	public Set<Api2AclRule> getApi2AclRules() {
		return this.api2AclRules;
	}

	public void setApi2AclRules(Set<Api2AclRule> api2AclRules) {
		this.api2AclRules = api2AclRules;
	}

}
