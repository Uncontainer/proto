package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
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
 * SalesShippingAggregatedOrder generated by hbm2java
 */
@Entity
@Table(name = "sales_shipping_aggregated_order"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = {"period", "store_id", "order_status", "shipping_description"}))
public class SalesShippingAggregatedOrder implements java.io.Serializable {

	private Integer id;
	private Store coreStore;
	private Date period;
	private String orderStatus;
	private String shippingDescription;
	private int ordersCount;
	private BigDecimal totalShipping;
	private BigDecimal totalShippingActual;

	public SalesShippingAggregatedOrder() {
	}

	public SalesShippingAggregatedOrder(int ordersCount) {
		this.ordersCount = ordersCount;
	}

	public SalesShippingAggregatedOrder(Store coreStore, Date period, String orderStatus, String shippingDescription, int ordersCount, BigDecimal totalShipping, BigDecimal totalShippingActual) {
		this.coreStore = coreStore;
		this.period = period;
		this.orderStatus = orderStatus;
		this.shippingDescription = shippingDescription;
		this.ordersCount = ordersCount;
		this.totalShipping = totalShipping;
		this.totalShippingActual = totalShippingActual;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	public Store getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(Store coreStore) {
		this.coreStore = coreStore;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "period", length = 10)
	public Date getPeriod() {
		return this.period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}

	@Column(name = "order_status", length = 50)
	public String getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Column(name = "shipping_description")
	public String getShippingDescription() {
		return this.shippingDescription;
	}

	public void setShippingDescription(String shippingDescription) {
		this.shippingDescription = shippingDescription;
	}

	@Column(name = "orders_count", nullable = false)
	public int getOrdersCount() {
		return this.ordersCount;
	}

	public void setOrdersCount(int ordersCount) {
		this.ordersCount = ordersCount;
	}

	@Column(name = "total_shipping", precision = 12, scale = 4)
	public BigDecimal getTotalShipping() {
		return this.totalShipping;
	}

	public void setTotalShipping(BigDecimal totalShipping) {
		this.totalShipping = totalShipping;
	}

	@Column(name = "total_shipping_actual", precision = 12, scale = 4)
	public BigDecimal getTotalShippingActual() {
		return this.totalShippingActual;
	}

	public void setTotalShippingActual(BigDecimal totalShippingActual) {
		this.totalShippingActual = totalShippingActual;
	}

}
