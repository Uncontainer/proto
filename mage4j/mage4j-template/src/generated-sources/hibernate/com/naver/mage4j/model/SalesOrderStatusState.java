package com.naver.mage4j.model;

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

/**
 * SalesOrderStatusState generated by hbm2java
 */
@Entity
@Table(name = "sales_order_status_state"
	, catalog = "magento")
public class SalesOrderStatusState implements java.io.Serializable {

	private SalesOrderStatusStateId id;
	private SalesOrderStatus salesOrderStatus;
	private short isDefault;

	public SalesOrderStatusState() {
	}

	public SalesOrderStatusState(SalesOrderStatusStateId id, SalesOrderStatus salesOrderStatus, short isDefault) {
		this.id = id;
		this.salesOrderStatus = salesOrderStatus;
		this.isDefault = isDefault;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "status", column = @Column(name = "status", nullable = false, length = 32)),
		@AttributeOverride(name = "state", column = @Column(name = "state", nullable = false, length = 32))})
	public SalesOrderStatusStateId getId() {
		return this.id;
	}

	public void setId(SalesOrderStatusStateId id) {
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

	@Column(name = "is_default", nullable = false)
	public short getIsDefault() {
		return this.isDefault;
	}

	public void setIsDefault(short isDefault) {
		this.isDefault = isDefault;
	}

}