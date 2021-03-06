package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Api2AclUser generated by hbm2java
 */
@Entity
@Table(name = "api2_acl_user"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = "admin_id"))
public class Api2AclUser implements java.io.Serializable {

	private Api2AclUserId id;
	private Api2AclRole api2AclRole;
	private AdminUser adminUser;

	public Api2AclUser() {
	}

	public Api2AclUser(Api2AclUserId id, Api2AclRole api2AclRole, AdminUser adminUser) {
		this.id = id;
		this.api2AclRole = api2AclRole;
		this.adminUser = adminUser;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "adminId", column = @Column(name = "admin_id", unique = true, nullable = false)),
		@AttributeOverride(name = "roleId", column = @Column(name = "role_id", nullable = false))})
	public Api2AclUserId getId() {
		return this.id;
	}

	public void setId(Api2AclUserId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false, insertable = false, updatable = false)
	public Api2AclRole getApi2AclRole() {
		return this.api2AclRole;
	}

	public void setApi2AclRole(Api2AclRole api2AclRole) {
		this.api2AclRole = api2AclRole;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id", unique = true, nullable = false, insertable = false, updatable = false)
	public AdminUser getAdminUser() {
		return this.adminUser;
	}

	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

}
