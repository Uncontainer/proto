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
import javax.persistence.UniqueConstraint;

import com.naver.mage4j.core.mage.core.model.resource.store.Store;

/**
 * ReportViewedProductIndex generated by hbm2java
 */
@Entity
@Table(name = "report_viewed_product_index"
	, catalog = "magento"
	, uniqueConstraints = {@UniqueConstraint(columnNames = {"visitor_id", "product_id"}), @UniqueConstraint(columnNames = {"customer_id", "product_id"})})
public class ReportViewedProductIndex implements java.io.Serializable {

	private Long indexId;
	private CustomerEntity customerEntity;
	private Store coreStore;
	private CatalogProductEntity catalogProductEntity;
	private Integer visitorId;
	private Date addedAt;

	public ReportViewedProductIndex() {
	}

	public ReportViewedProductIndex(CatalogProductEntity catalogProductEntity, Date addedAt) {
		this.catalogProductEntity = catalogProductEntity;
		this.addedAt = addedAt;
	}

	public ReportViewedProductIndex(CustomerEntity customerEntity, Store coreStore, CatalogProductEntity catalogProductEntity, Integer visitorId, Date addedAt) {
		this.customerEntity = customerEntity;
		this.coreStore = coreStore;
		this.catalogProductEntity = catalogProductEntity;
		this.visitorId = visitorId;
		this.addedAt = addedAt;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "index_id", unique = true, nullable = false)
	public Long getIndexId() {
		return this.indexId;
	}

	public void setIndexId(Long indexId) {
		this.indexId = indexId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	public CustomerEntity getCustomerEntity() {
		return this.customerEntity;
	}

	public void setCustomerEntity(CustomerEntity customerEntity) {
		this.customerEntity = customerEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	public Store getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(Store coreStore) {
		this.coreStore = coreStore;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	public CatalogProductEntity getCatalogProductEntity() {
		return this.catalogProductEntity;
	}

	public void setCatalogProductEntity(CatalogProductEntity catalogProductEntity) {
		this.catalogProductEntity = catalogProductEntity;
	}

	@Column(name = "visitor_id")
	public Integer getVisitorId() {
		return this.visitorId;
	}

	public void setVisitorId(Integer visitorId) {
		this.visitorId = visitorId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "added_at", nullable = false, length = 19)
	public Date getAddedAt() {
		return this.addedAt;
	}

	public void setAddedAt(Date addedAt) {
		this.addedAt = addedAt;
	}

}