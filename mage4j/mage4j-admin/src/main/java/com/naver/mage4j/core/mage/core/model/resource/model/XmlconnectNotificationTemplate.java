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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * XmlconnectNotificationTemplate generated by hbm2java
 */
@Entity
@Table(name = "xmlconnect_notification_template"
	, catalog = "magento")
public class XmlconnectNotificationTemplate implements java.io.Serializable {

	private Integer templateId;
	private XmlconnectApplication xmlconnectApplication;
	private String name;
	private String pushTitle;
	private String messageTitle;
	private String content;
	private Date createdAt;
	private Date modifiedAt;
	private Set<XmlconnectQueue> xmlconnectQueues = new HashSet<XmlconnectQueue>(0);

	public XmlconnectNotificationTemplate() {
	}

	public XmlconnectNotificationTemplate(XmlconnectApplication xmlconnectApplication, String name, String pushTitle, String messageTitle, String content) {
		this.xmlconnectApplication = xmlconnectApplication;
		this.name = name;
		this.pushTitle = pushTitle;
		this.messageTitle = messageTitle;
		this.content = content;
	}

	public XmlconnectNotificationTemplate(XmlconnectApplication xmlconnectApplication, String name, String pushTitle, String messageTitle, String content, Date createdAt, Date modifiedAt, Set<XmlconnectQueue> xmlconnectQueues) {
		this.xmlconnectApplication = xmlconnectApplication;
		this.name = name;
		this.pushTitle = pushTitle;
		this.messageTitle = messageTitle;
		this.content = content;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.xmlconnectQueues = xmlconnectQueues;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "template_id", unique = true, nullable = false)
	public Integer getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "application_id", nullable = false)
	public XmlconnectApplication getXmlconnectApplication() {
		return this.xmlconnectApplication;
	}

	public void setXmlconnectApplication(XmlconnectApplication xmlconnectApplication) {
		this.xmlconnectApplication = xmlconnectApplication;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "push_title", nullable = false, length = 140)
	public String getPushTitle() {
		return this.pushTitle;
	}

	public void setPushTitle(String pushTitle) {
		this.pushTitle = pushTitle;
	}

	@Column(name = "message_title", nullable = false)
	public String getMessageTitle() {
		return this.messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	@Column(name = "content", nullable = false, length = 65535, columnDefinition = "TEXT")
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", length = 19)
	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_at", length = 19)
	public Date getModifiedAt() {
		return this.modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "xmlconnectNotificationTemplate")
	public Set<XmlconnectQueue> getXmlconnectQueues() {
		return this.xmlconnectQueues;
	}

	public void setXmlconnectQueues(Set<XmlconnectQueue> xmlconnectQueues) {
		this.xmlconnectQueues = xmlconnectQueues;
	}

}
