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
import javax.persistence.UniqueConstraint;

/**
 * OauthConsumer generated by hbm2java
 */
@Entity
@Table(name = "oauth_consumer"
	, catalog = "magento"
	, uniqueConstraints = {@UniqueConstraint(columnNames = "key"), @UniqueConstraint(columnNames = "secret")})
public class OauthConsumer implements java.io.Serializable {

	private Integer entityId;
	private Date createdAt;
	private Date updatedAt;
	private String name;
	private String key;
	private String secret;
	private String callbackUrl;
	private String rejectedCallbackUrl;
	private Set<OauthToken> oauthTokens = new HashSet<OauthToken>(0);

	public OauthConsumer() {
	}

	public OauthConsumer(Date createdAt, String name, String key, String secret, String rejectedCallbackUrl) {
		this.createdAt = createdAt;
		this.name = name;
		this.key = key;
		this.secret = secret;
		this.rejectedCallbackUrl = rejectedCallbackUrl;
	}

	public OauthConsumer(Date createdAt, Date updatedAt, String name, String key, String secret, String callbackUrl, String rejectedCallbackUrl, Set<OauthToken> oauthTokens) {
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.name = name;
		this.key = key;
		this.secret = secret;
		this.callbackUrl = callbackUrl;
		this.rejectedCallbackUrl = rejectedCallbackUrl;
		this.oauthTokens = oauthTokens;
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

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "key", unique = true, nullable = false, length = 32)
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "secret", unique = true, nullable = false, length = 32)
	public String getSecret() {
		return this.secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Column(name = "callback_url")
	public String getCallbackUrl() {
		return this.callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	@Column(name = "rejected_callback_url", nullable = false)
	public String getRejectedCallbackUrl() {
		return this.rejectedCallbackUrl;
	}

	public void setRejectedCallbackUrl(String rejectedCallbackUrl) {
		this.rejectedCallbackUrl = rejectedCallbackUrl;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "oauthConsumer")
	public Set<OauthToken> getOauthTokens() {
		return this.oauthTokens;
	}

	public void setOauthTokens(Set<OauthToken> oauthTokens) {
		this.oauthTokens = oauthTokens;
	}

}
