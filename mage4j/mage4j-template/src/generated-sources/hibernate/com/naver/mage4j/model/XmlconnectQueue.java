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
 * XmlconnectQueue generated by hbm2java
 */
@Entity
@Table(name = "xmlconnect_queue"
	, catalog = "magento")
public class XmlconnectQueue implements java.io.Serializable {

	private Integer queueId;
	private XmlconnectNotificationTemplate xmlconnectNotificationTemplate;
	private Date createTime;
	private Date execTime;
	private String pushTitle;
	private String messageTitle;
	private String content;
	private short status;
	private String type;

	public XmlconnectQueue() {
	}

	public XmlconnectQueue(XmlconnectNotificationTemplate xmlconnectNotificationTemplate, String pushTitle, short status, String type) {
		this.xmlconnectNotificationTemplate = xmlconnectNotificationTemplate;
		this.pushTitle = pushTitle;
		this.status = status;
		this.type = type;
	}

	public XmlconnectQueue(XmlconnectNotificationTemplate xmlconnectNotificationTemplate, Date createTime, Date execTime, String pushTitle, String messageTitle, String content, short status, String type) {
		this.xmlconnectNotificationTemplate = xmlconnectNotificationTemplate;
		this.createTime = createTime;
		this.execTime = execTime;
		this.pushTitle = pushTitle;
		this.messageTitle = messageTitle;
		this.content = content;
		this.status = status;
		this.type = type;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "queue_id", unique = true, nullable = false)
	public Integer getQueueId() {
		return this.queueId;
	}

	public void setQueueId(Integer queueId) {
		this.queueId = queueId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "template_id", nullable = false)
	public XmlconnectNotificationTemplate getXmlconnectNotificationTemplate() {
		return this.xmlconnectNotificationTemplate;
	}

	public void setXmlconnectNotificationTemplate(XmlconnectNotificationTemplate xmlconnectNotificationTemplate) {
		this.xmlconnectNotificationTemplate = xmlconnectNotificationTemplate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "exec_time", length = 19)
	public Date getExecTime() {
		return this.execTime;
	}

	public void setExecTime(Date execTime) {
		this.execTime = execTime;
	}

	@Column(name = "push_title", nullable = false, length = 140)
	public String getPushTitle() {
		return this.pushTitle;
	}

	public void setPushTitle(String pushTitle) {
		this.pushTitle = pushTitle;
	}

	@Column(name = "message_title")
	public String getMessageTitle() {
		return this.messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	@Column(name = "content", length = 65535)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "status", nullable = false)
	public short getStatus() {
		return this.status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	@Column(name = "type", nullable = false, length = 12)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
