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

/**
 * ApiSession generated by hbm2java
 */
@Entity
@Table(name = "api_session"
	, catalog = "magento")
public class ApiSession implements java.io.Serializable {

	private ApiSessionId id;
	private ApiUser apiUser;

	public ApiSession() {
	}

	public ApiSession(ApiSessionId id, ApiUser apiUser) {
		this.id = id;
		this.apiUser = apiUser;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false)),
		@AttributeOverride(name = "logdate", column = @Column(name = "logdate", nullable = false, length = 19)),
		@AttributeOverride(name = "sessid", column = @Column(name = "sessid", length = 40))})
	public ApiSessionId getId() {
		return this.id;
	}

	public void setId(ApiSessionId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
	public ApiUser getApiUser() {
		return this.apiUser;
	}

	public void setApiUser(ApiUser apiUser) {
		this.apiUser = apiUser;
	}

}
