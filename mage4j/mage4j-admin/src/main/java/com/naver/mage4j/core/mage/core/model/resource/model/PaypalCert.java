package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.naver.mage4j.core.mage.core.model.resource.website.Website;

/**
 * PaypalCert generated by hbm2java
 */
@Entity
@Table(name = "paypal_cert"
	, catalog = "magento")
public class PaypalCert implements java.io.Serializable {

	private Short certId;
	private Website coreWebsite;
	private String content;
	private Date updatedAt;

	public PaypalCert() {
	}

	public PaypalCert(Website coreWebsite) {
		this.coreWebsite = coreWebsite;
	}

	public PaypalCert(Website coreWebsite, String content, Date updatedAt) {
		this.coreWebsite = coreWebsite;
		this.content = content;
		this.updatedAt = updatedAt;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "cert_id", unique = true, nullable = false)
	public Short getCertId() {
		return this.certId;
	}

	public void setCertId(Short certId) {
		this.certId = certId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "website_id", nullable = false)
	public Website getCoreWebsite() {
		return this.coreWebsite;
	}

	public void setCoreWebsite(Website coreWebsite) {
		this.coreWebsite = coreWebsite;
	}

	@Column(name = "content", length = 65535, columnDefinition = "TEXT")
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", length = 19)
	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

}
