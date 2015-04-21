package com.naver.mage4j.model;

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

/**
 * XmlconnectHistory generated by hbm2java
 */
@Entity
@Table(name = "xmlconnect_history"
	, catalog = "magento")
public class XmlconnectHistory implements java.io.Serializable {

	private Integer historyId;
	private XmlconnectApplication xmlconnectApplication;
	private Date createdAt;
	private Short storeId;
	private byte[] params;
	private String title;
	private String activationKey;
	private String name;
	private String code;

	public XmlconnectHistory() {
	}

	public XmlconnectHistory(XmlconnectApplication xmlconnectApplication, String title, String activationKey, String name, String code) {
		this.xmlconnectApplication = xmlconnectApplication;
		this.title = title;
		this.activationKey = activationKey;
		this.name = name;
		this.code = code;
	}

	public XmlconnectHistory(XmlconnectApplication xmlconnectApplication, Date createdAt, Short storeId, byte[] params, String title, String activationKey, String name, String code) {
		this.xmlconnectApplication = xmlconnectApplication;
		this.createdAt = createdAt;
		this.storeId = storeId;
		this.params = params;
		this.title = title;
		this.activationKey = activationKey;
		this.name = name;
		this.code = code;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "history_id", unique = true, nullable = false)
	public Integer getHistoryId() {
		return this.historyId;
	}

	public void setHistoryId(Integer historyId) {
		this.historyId = historyId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "application_id", nullable = false)
	public XmlconnectApplication getXmlconnectApplication() {
		return this.xmlconnectApplication;
	}

	public void setXmlconnectApplication(XmlconnectApplication xmlconnectApplication) {
		this.xmlconnectApplication = xmlconnectApplication;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", length = 19)
	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Column(name = "store_id")
	public Short getStoreId() {
		return this.storeId;
	}

	public void setStoreId(Short storeId) {
		this.storeId = storeId;
	}

	@Column(name = "params")
	public byte[] getParams() {
		return this.params;
	}

	public void setParams(byte[] params) {
		this.params = params;
	}

	@Column(name = "title", nullable = false, length = 200)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "activation_key", nullable = false)
	public String getActivationKey() {
		return this.activationKey;
	}

	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "code", nullable = false, length = 32)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
