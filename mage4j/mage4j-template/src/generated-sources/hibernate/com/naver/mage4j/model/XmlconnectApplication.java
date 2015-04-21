package com.naver.mage4j.model;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * XmlconnectApplication generated by hbm2java
 */
@Entity
@Table(name = "xmlconnect_application"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class XmlconnectApplication implements java.io.Serializable {

	private Short applicationId;
	private CoreStore coreStore;
	private String name;
	private String code;
	private String type;
	private Date activeFrom;
	private Date activeTo;
	private Date updatedAt;
	private short status;
	private Short browsingMode;
	private Set<XmlconnectNotificationTemplate> xmlconnectNotificationTemplates = new HashSet<XmlconnectNotificationTemplate>(0);
	private Set<XmlconnectHistory> xmlconnectHistories = new HashSet<XmlconnectHistory>(0);
	private Set<XmlconnectImages> xmlconnectImageses = new HashSet<XmlconnectImages>(0);
	private Set<XmlconnectConfigData> xmlconnectConfigDatas = new HashSet<XmlconnectConfigData>(0);

	public XmlconnectApplication() {
	}

	public XmlconnectApplication(String name, String code, String type, short status) {
		this.name = name;
		this.code = code;
		this.type = type;
		this.status = status;
	}

	public XmlconnectApplication(CoreStore coreStore, String name, String code, String type, Date activeFrom, Date activeTo, Date updatedAt, short status, Short browsingMode, Set<XmlconnectNotificationTemplate> xmlconnectNotificationTemplates, Set<XmlconnectHistory> xmlconnectHistories, Set<XmlconnectImages> xmlconnectImageses, Set<XmlconnectConfigData> xmlconnectConfigDatas) {
		this.coreStore = coreStore;
		this.name = name;
		this.code = code;
		this.type = type;
		this.activeFrom = activeFrom;
		this.activeTo = activeTo;
		this.updatedAt = updatedAt;
		this.status = status;
		this.browsingMode = browsingMode;
		this.xmlconnectNotificationTemplates = xmlconnectNotificationTemplates;
		this.xmlconnectHistories = xmlconnectHistories;
		this.xmlconnectImageses = xmlconnectImageses;
		this.xmlconnectConfigDatas = xmlconnectConfigDatas;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "application_id", unique = true, nullable = false)
	public Short getApplicationId() {
		return this.applicationId;
	}

	public void setApplicationId(Short applicationId) {
		this.applicationId = applicationId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	public CoreStore getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(CoreStore coreStore) {
		this.coreStore = coreStore;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "code", unique = true, nullable = false, length = 32)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "type", nullable = false, length = 32)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "active_from", length = 10)
	public Date getActiveFrom() {
		return this.activeFrom;
	}

	public void setActiveFrom(Date activeFrom) {
		this.activeFrom = activeFrom;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "active_to", length = 10)
	public Date getActiveTo() {
		return this.activeTo;
	}

	public void setActiveTo(Date activeTo) {
		this.activeTo = activeTo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", length = 19)
	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Column(name = "status", nullable = false)
	public short getStatus() {
		return this.status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	@Column(name = "browsing_mode")
	public Short getBrowsingMode() {
		return this.browsingMode;
	}

	public void setBrowsingMode(Short browsingMode) {
		this.browsingMode = browsingMode;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "xmlconnectApplication")
	public Set<XmlconnectNotificationTemplate> getXmlconnectNotificationTemplates() {
		return this.xmlconnectNotificationTemplates;
	}

	public void setXmlconnectNotificationTemplates(Set<XmlconnectNotificationTemplate> xmlconnectNotificationTemplates) {
		this.xmlconnectNotificationTemplates = xmlconnectNotificationTemplates;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "xmlconnectApplication")
	public Set<XmlconnectHistory> getXmlconnectHistories() {
		return this.xmlconnectHistories;
	}

	public void setXmlconnectHistories(Set<XmlconnectHistory> xmlconnectHistories) {
		this.xmlconnectHistories = xmlconnectHistories;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "xmlconnectApplication")
	public Set<XmlconnectImages> getXmlconnectImageses() {
		return this.xmlconnectImageses;
	}

	public void setXmlconnectImageses(Set<XmlconnectImages> xmlconnectImageses) {
		this.xmlconnectImageses = xmlconnectImageses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "xmlconnectApplication")
	public Set<XmlconnectConfigData> getXmlconnectConfigDatas() {
		return this.xmlconnectConfigDatas;
	}

	public void setXmlconnectConfigDatas(Set<XmlconnectConfigData> xmlconnectConfigDatas) {
		this.xmlconnectConfigDatas = xmlconnectConfigDatas;
	}

}
