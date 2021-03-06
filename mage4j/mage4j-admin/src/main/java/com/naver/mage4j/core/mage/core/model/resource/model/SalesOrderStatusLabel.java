package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.naver.mage4j.core.mage.core.model.resource.store.Store;

/**
 * SalesOrderStatusLabel generated by hbm2java
 */
@Entity
@Table(name = "sales_order_status_label"
	, catalog = "magento")
public class SalesOrderStatusLabel implements java.io.Serializable {

	private SalesOrderStatusLabelId id;
	private SalesOrderStatus salesOrderStatus;
	private Store coreStore;
	private String label;

	public SalesOrderStatusLabel() {
	}

	public SalesOrderStatusLabel(SalesOrderStatusLabelId id, SalesOrderStatus salesOrderStatus, Store coreStore, String label) {
		this.id = id;
		this.salesOrderStatus = salesOrderStatus;
		this.coreStore = coreStore;
		this.label = label;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "status", column = @Column(name = "status", nullable = false, length = 32)),
		@AttributeOverride(name = "storeId", column = @Column(name = "store_id", nullable = false))})
	public SalesOrderStatusLabelId getId() {
		return this.id;
	}

	public void setId(SalesOrderStatusLabelId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status", nullable = false, insertable = false, updatable = false)
	public SalesOrderStatus getSalesOrderStatus() {
		return this.salesOrderStatus;
	}

	public void setSalesOrderStatus(SalesOrderStatus salesOrderStatus) {
		this.salesOrderStatus = salesOrderStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false, insertable = false, updatable = false)
	public Store getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(Store coreStore) {
		this.coreStore = coreStore;
	}

	@Column(name = "label", nullable = false, length = 128)
	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
