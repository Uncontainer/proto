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
 * LogCustomer generated by hbm2java
 */
@Entity
@Table(name = "log_customer"
	, catalog = "magento")
public class LogCustomer implements java.io.Serializable {

	private Integer logId;
	private Long visitorId;
	private int customerId;
	private Date loginAt;
	private Date logoutAt;
	private short storeId;

	public LogCustomer() {
	}

	public LogCustomer(int customerId, Date loginAt, short storeId) {
		this.customerId = customerId;
		this.loginAt = loginAt;
		this.storeId = storeId;
	}

	public LogCustomer(Long visitorId, int customerId, Date loginAt, Date logoutAt, short storeId) {
		this.visitorId = visitorId;
		this.customerId = customerId;
		this.loginAt = loginAt;
		this.logoutAt = logoutAt;
		this.storeId = storeId;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "log_id", unique = true, nullable = false)
	public Integer getLogId() {
		return this.logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	@Column(name = "visitor_id")
	public Long getVisitorId() {
		return this.visitorId;
	}

	public void setVisitorId(Long visitorId) {
		this.visitorId = visitorId;
	}

	@Column(name = "customer_id", nullable = false)
	public int getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "login_at", nullable = false, length = 19)
	public Date getLoginAt() {
		return this.loginAt;
	}

	public void setLoginAt(Date loginAt) {
		this.loginAt = loginAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "logout_at", length = 19)
	public Date getLogoutAt() {
		return this.logoutAt;
	}

	public void setLogoutAt(Date logoutAt) {
		this.logoutAt = logoutAt;
	}

	@Column(name = "store_id", nullable = false)
	public short getStoreId() {
		return this.storeId;
	}

	public void setStoreId(short storeId) {
		this.storeId = storeId;
	}

}
