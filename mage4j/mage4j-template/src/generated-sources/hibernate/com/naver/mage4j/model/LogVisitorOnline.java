package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * LogVisitorOnline generated by hbm2java
 */
@Entity
@Table(name = "log_visitor_online"
	, catalog = "magento")
public class LogVisitorOnline implements java.io.Serializable {

	private Long visitorId;
	private String visitorType;
	private long remoteAddr;
	private Date firstVisitAt;
	private Date lastVisitAt;
	private Integer customerId;
	private String lastUrl;

	public LogVisitorOnline() {
	}

	public LogVisitorOnline(String visitorType, long remoteAddr) {
		this.visitorType = visitorType;
		this.remoteAddr = remoteAddr;
	}

	public LogVisitorOnline(String visitorType, long remoteAddr, Date firstVisitAt, Date lastVisitAt, Integer customerId, String lastUrl) {
		this.visitorType = visitorType;
		this.remoteAddr = remoteAddr;
		this.firstVisitAt = firstVisitAt;
		this.lastVisitAt = lastVisitAt;
		this.customerId = customerId;
		this.lastUrl = lastUrl;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "visitor_id", unique = true, nullable = false)
	public Long getVisitorId() {
		return this.visitorId;
	}

	public void setVisitorId(Long visitorId) {
		this.visitorId = visitorId;
	}

	@Column(name = "visitor_type", nullable = false, length = 1)
	public String getVisitorType() {
		return this.visitorType;
	}

	public void setVisitorType(String visitorType) {
		this.visitorType = visitorType;
	}

	@Column(name = "remote_addr", nullable = false)
	public long getRemoteAddr() {
		return this.remoteAddr;
	}

	public void setRemoteAddr(long remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "first_visit_at", length = 19)
	public Date getFirstVisitAt() {
		return this.firstVisitAt;
	}

	public void setFirstVisitAt(Date firstVisitAt) {
		this.firstVisitAt = firstVisitAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_visit_at", length = 19)
	public Date getLastVisitAt() {
		return this.lastVisitAt;
	}

	public void setLastVisitAt(Date lastVisitAt) {
		this.lastVisitAt = lastVisitAt;
	}

	@Column(name = "customer_id")
	public Integer getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	@Column(name = "last_url")
	public String getLastUrl() {
		return this.lastUrl;
	}

	public void setLastUrl(String lastUrl) {
		this.lastUrl = lastUrl;
	}

}