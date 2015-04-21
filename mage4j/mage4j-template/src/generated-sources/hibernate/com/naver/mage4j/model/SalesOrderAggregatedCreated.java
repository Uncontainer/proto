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
 * SalesOrderAggregatedCreated generated by hbm2java
 */
@Entity
@Table(name = "sales_order_aggregated_created"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = {"period", "store_id", "order_status"}))
public class SalesOrderAggregatedCreated implements java.io.Serializable {

	private Integer id;
	private CoreStore coreStore;
	private Date period;
	private String orderStatus;
	private int ordersCount;
	private BigDecimal totalQtyOrdered;
	private BigDecimal totalQtyInvoiced;
	private BigDecimal totalIncomeAmount;
	private BigDecimal totalRevenueAmount;
	private BigDecimal totalProfitAmount;
	private BigDecimal totalInvoicedAmount;
	private BigDecimal totalCanceledAmount;
	private BigDecimal totalPaidAmount;
	private BigDecimal totalRefundedAmount;
	private BigDecimal totalTaxAmount;
	private BigDecimal totalTaxAmountActual;
	private BigDecimal totalShippingAmount;
	private BigDecimal totalShippingAmountActual;
	private BigDecimal totalDiscountAmount;
	private BigDecimal totalDiscountAmountActual;

	public SalesOrderAggregatedCreated() {
	}

	public SalesOrderAggregatedCreated(String orderStatus, int ordersCount, BigDecimal totalQtyOrdered, BigDecimal totalQtyInvoiced, BigDecimal totalIncomeAmount, BigDecimal totalRevenueAmount, BigDecimal totalProfitAmount, BigDecimal totalInvoicedAmount, BigDecimal totalCanceledAmount, BigDecimal totalPaidAmount, BigDecimal totalRefundedAmount, BigDecimal totalTaxAmount, BigDecimal totalTaxAmountActual, BigDecimal totalShippingAmount, BigDecimal totalShippingAmountActual,
		BigDecimal totalDiscountAmount, BigDecimal totalDiscountAmountActual) {
		this.orderStatus = orderStatus;
		this.ordersCount = ordersCount;
		this.totalQtyOrdered = totalQtyOrdered;
		this.totalQtyInvoiced = totalQtyInvoiced;
		this.totalIncomeAmount = totalIncomeAmount;
		this.totalRevenueAmount = totalRevenueAmount;
		this.totalProfitAmount = totalProfitAmount;
		this.totalInvoicedAmount = totalInvoicedAmount;
		this.totalCanceledAmount = totalCanceledAmount;
		this.totalPaidAmount = totalPaidAmount;
		this.totalRefundedAmount = totalRefundedAmount;
		this.totalTaxAmount = totalTaxAmount;
		this.totalTaxAmountActual = totalTaxAmountActual;
		this.totalShippingAmount = totalShippingAmount;
		this.totalShippingAmountActual = totalShippingAmountActual;
		this.totalDiscountAmount = totalDiscountAmount;
		this.totalDiscountAmountActual = totalDiscountAmountActual;
	}

	public SalesOrderAggregatedCreated(CoreStore coreStore, Date period, String orderStatus, int ordersCount, BigDecimal totalQtyOrdered, BigDecimal totalQtyInvoiced, BigDecimal totalIncomeAmount, BigDecimal totalRevenueAmount, BigDecimal totalProfitAmount, BigDecimal totalInvoicedAmount, BigDecimal totalCanceledAmount, BigDecimal totalPaidAmount, BigDecimal totalRefundedAmount, BigDecimal totalTaxAmount, BigDecimal totalTaxAmountActual, BigDecimal totalShippingAmount,
		BigDecimal totalShippingAmountActual, BigDecimal totalDiscountAmount, BigDecimal totalDiscountAmountActual) {
		this.coreStore = coreStore;
		this.period = period;
		this.orderStatus = orderStatus;
		this.ordersCount = ordersCount;
		this.totalQtyOrdered = totalQtyOrdered;
		this.totalQtyInvoiced = totalQtyInvoiced;
		this.totalIncomeAmount = totalIncomeAmount;
		this.totalRevenueAmount = totalRevenueAmount;
		this.totalProfitAmount = totalProfitAmount;
		this.totalInvoicedAmount = totalInvoicedAmount;
		this.totalCanceledAmount = totalCanceledAmount;
		this.totalPaidAmount = totalPaidAmount;
		this.totalRefundedAmount = totalRefundedAmount;
		this.totalTaxAmount = totalTaxAmount;
		this.totalTaxAmountActual = totalTaxAmountActual;
		this.totalShippingAmount = totalShippingAmount;
		this.totalShippingAmountActual = totalShippingAmountActual;
		this.totalDiscountAmount = totalDiscountAmount;
		this.totalDiscountAmountActual = totalDiscountAmountActual;
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

	@Column(name = "order_status", nullable = false, length = 50)
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

	@Column(name = "total_qty_ordered", nullable = false, precision = 12, scale = 4)
	public BigDecimal getTotalQtyOrdered() {
		return this.totalQtyOrdered;
	}

	public void setTotalQtyOrdered(BigDecimal totalQtyOrdered) {
		this.totalQtyOrdered = totalQtyOrdered;
	}

	@Column(name = "total_qty_invoiced", nullable = false, precision = 12, scale = 4)
	public BigDecimal getTotalQtyInvoiced() {
		return this.totalQtyInvoiced;
	}

	public void setTotalQtyInvoiced(BigDecimal totalQtyInvoiced) {
		this.totalQtyInvoiced = totalQtyInvoiced;
	}

	@Column(name = "total_income_amount", nullable = false, precision = 12, scale = 4)
	public BigDecimal getTotalIncomeAmount() {
		return this.totalIncomeAmount;
	}

	public void setTotalIncomeAmount(BigDecimal totalIncomeAmount) {
		this.totalIncomeAmount = totalIncomeAmount;
	}

	@Column(name = "total_revenue_amount", nullable = false, precision = 12, scale = 4)
	public BigDecimal getTotalRevenueAmount() {
		return this.totalRevenueAmount;
	}

	public void setTotalRevenueAmount(BigDecimal totalRevenueAmount) {
		this.totalRevenueAmount = totalRevenueAmount;
	}

	@Column(name = "total_profit_amount", nullable = false, precision = 12, scale = 4)
	public BigDecimal getTotalProfitAmount() {
		return this.totalProfitAmount;
	}

	public void setTotalProfitAmount(BigDecimal totalProfitAmount) {
		this.totalProfitAmount = totalProfitAmount;
	}

	@Column(name = "total_invoiced_amount", nullable = false, precision = 12, scale = 4)
	public BigDecimal getTotalInvoicedAmount() {
		return this.totalInvoicedAmount;
	}

	public void setTotalInvoicedAmount(BigDecimal totalInvoicedAmount) {
		this.totalInvoicedAmount = totalInvoicedAmount;
	}

	@Column(name = "total_canceled_amount", nullable = false, precision = 12, scale = 4)
	public BigDecimal getTotalCanceledAmount() {
		return this.totalCanceledAmount;
	}

	public void setTotalCanceledAmount(BigDecimal totalCanceledAmount) {
		this.totalCanceledAmount = totalCanceledAmount;
	}

	@Column(name = "total_paid_amount", nullable = false, precision = 12, scale = 4)
	public BigDecimal getTotalPaidAmount() {
		return this.totalPaidAmount;
	}

	public void setTotalPaidAmount(BigDecimal totalPaidAmount) {
		this.totalPaidAmount = totalPaidAmount;
	}

	@Column(name = "total_refunded_amount", nullable = false, precision = 12, scale = 4)
	public BigDecimal getTotalRefundedAmount() {
		return this.totalRefundedAmount;
	}

	public void setTotalRefundedAmount(BigDecimal totalRefundedAmount) {
		this.totalRefundedAmount = totalRefundedAmount;
	}

	@Column(name = "total_tax_amount", nullable = false, precision = 12, scale = 4)
	public BigDecimal getTotalTaxAmount() {
		return this.totalTaxAmount;
	}

	public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	@Column(name = "total_tax_amount_actual", nullable = false, precision = 12, scale = 4)
	public BigDecimal getTotalTaxAmountActual() {
		return this.totalTaxAmountActual;
	}

	public void setTotalTaxAmountActual(BigDecimal totalTaxAmountActual) {
		this.totalTaxAmountActual = totalTaxAmountActual;
	}

	@Column(name = "total_shipping_amount", nullable = false, precision = 12, scale = 4)
	public BigDecimal getTotalShippingAmount() {
		return this.totalShippingAmount;
	}

	public void setTotalShippingAmount(BigDecimal totalShippingAmount) {
		this.totalShippingAmount = totalShippingAmount;
	}

	@Column(name = "total_shipping_amount_actual", nullable = false, precision = 12, scale = 4)
	public BigDecimal getTotalShippingAmountActual() {
		return this.totalShippingAmountActual;
	}

	public void setTotalShippingAmountActual(BigDecimal totalShippingAmountActual) {
		this.totalShippingAmountActual = totalShippingAmountActual;
	}

	@Column(name = "total_discount_amount", nullable = false, precision = 12, scale = 4)
	public BigDecimal getTotalDiscountAmount() {
		return this.totalDiscountAmount;
	}

	public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}

	@Column(name = "total_discount_amount_actual", nullable = false, precision = 12, scale = 4)
	public BigDecimal getTotalDiscountAmountActual() {
		return this.totalDiscountAmountActual;
	}

	public void setTotalDiscountAmountActual(BigDecimal totalDiscountAmountActual) {
		this.totalDiscountAmountActual = totalDiscountAmountActual;
	}

}
