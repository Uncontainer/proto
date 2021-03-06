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
 * ProductAlertStock generated by hbm2java
 */
@Entity
@Table(name = "product_alert_stock"
	, catalog = "magento")
public class ProductAlertStock implements java.io.Serializable {

	private Integer alertStockId;
	private Website coreWebsite;
	private CustomerEntity customerEntity;
	private CatalogProductEntity catalogProductEntity;
	private Date addDate;
	private Date sendDate;
	private short sendCount;
	private short status;

	public ProductAlertStock() {
	}

	public ProductAlertStock(Website coreWebsite, CustomerEntity customerEntity, CatalogProductEntity catalogProductEntity, Date addDate, short sendCount, short status) {
		this.coreWebsite = coreWebsite;
		this.customerEntity = customerEntity;
		this.catalogProductEntity = catalogProductEntity;
		this.addDate = addDate;
		this.sendCount = sendCount;
		this.status = status;
	}

	public ProductAlertStock(Website coreWebsite, CustomerEntity customerEntity, CatalogProductEntity catalogProductEntity, Date addDate, Date sendDate, short sendCount, short status) {
		this.coreWebsite = coreWebsite;
		this.customerEntity = customerEntity;
		this.catalogProductEntity = catalogProductEntity;
		this.addDate = addDate;
		this.sendDate = sendDate;
		this.sendCount = sendCount;
		this.status = status;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "alert_stock_id", unique = true, nullable = false)
	public Integer getAlertStockId() {
		return this.alertStockId;
	}

	public void setAlertStockId(Integer alertStockId) {
		this.alertStockId = alertStockId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "website_id", nullable = false)
	public Website getCoreWebsite() {
		return this.coreWebsite;
	}

	public void setCoreWebsite(Website coreWebsite) {
		this.coreWebsite = coreWebsite;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	public CustomerEntity getCustomerEntity() {
		return this.customerEntity;
	}

	public void setCustomerEntity(CustomerEntity customerEntity) {
		this.customerEntity = customerEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	public CatalogProductEntity getCatalogProductEntity() {
		return this.catalogProductEntity;
	}

	public void setCatalogProductEntity(CatalogProductEntity catalogProductEntity) {
		this.catalogProductEntity = catalogProductEntity;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "add_date", nullable = false, length = 19)
	public Date getAddDate() {
		return this.addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "send_date", length = 19)
	public Date getSendDate() {
		return this.sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	@Column(name = "send_count", nullable = false)
	public short getSendCount() {
		return this.sendCount;
	}

	public void setSendCount(short sendCount) {
		this.sendCount = sendCount;
	}

	@Column(name = "status", nullable = false)
	public short getStatus() {
		return this.status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

}
