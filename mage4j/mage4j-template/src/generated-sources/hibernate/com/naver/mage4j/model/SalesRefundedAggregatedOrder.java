package com.naver.mage4j.model;

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

/**
 * SalesRefundedAggregatedOrder generated by hbm2java
 */
@Entity
@Table(name = "sales_refunded_aggregated_order"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = {"period", "store_id", "order_status"}))
public class SalesRefundedAggregatedOrder implements java.io.Serializable {

	private Integer id;
	private CoreStore coreStore;
	private Date period;
	private String orderStatus;
	private int ordersCount;
	private BigDecimal refunded;
	private BigDecimal onlineRefunded;
	private BigDecimal offlineRefunded;

	public SalesRefundedAggregatedOrder() {
	}

	public SalesRefundedAggregatedOrder(int ordersCount) {
		this.ordersCount = ordersCount;
	}

	public SalesRefundedAggregatedOrder(CoreStore coreStore, Date period, String orderStatus, int ordersCount, BigDecimal refunded, BigDecimal onlineRefunded, BigDecimal offlineRefunded) {
		this.coreStore = coreStore;
		this.period = period;
		this.orderStatus = orderStatus;
		this.ordersCount = ordersCount;
		this.refunded = refunded;
		this.onlineRefunded = onlineRefunded;
		this.offlineRefunded = offlineRefunded;
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
	public CoreStore getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(CoreStore coreStore) {
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

	@Column(name = "orders_count", nullable = false)
	public int getOrdersCount() {
		return this.ordersCount;
	}

	public void setOrdersCount(int ordersCount) {
		this.ordersCount = ordersCount;
	}

	@Column(name = "refunded", precision = 12, scale = 4)
	public BigDecimal getRefunded() {
		return this.refunded;
	}

	public void setRefunded(BigDecimal refunded) {
		this.refunded = refunded;
	}

	@Column(name = "online_refunded", precision = 12, scale = 4)
	public BigDecimal getOnlineRefunded() {
		return this.onlineRefunded;
	}

	public void setOnlineRefunded(BigDecimal onlineRefunded) {
		this.onlineRefunded = onlineRefunded;
	}

	@Column(name = "offline_refunded", precision = 12, scale = 4)
	public BigDecimal getOfflineRefunded() {
		return this.offlineRefunded;
	}

	public void setOfflineRefunded(BigDecimal offlineRefunded) {
		this.offlineRefunded = offlineRefunded;
	}

}
