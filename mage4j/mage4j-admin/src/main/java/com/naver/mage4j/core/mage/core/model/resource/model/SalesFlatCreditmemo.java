package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.naver.mage4j.core.mage.core.model.resource.store.Store;

/**
 * SalesFlatCreditmemo generated by hbm2java
 */
@Entity
@Table(name = "sales_flat_creditmemo"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = "increment_id"))
public class SalesFlatCreditmemo implements java.io.Serializable {

	private Integer entityId;
	private SalesFlatOrder salesFlatOrder;
	private Store coreStore;
	private BigDecimal adjustmentPositive;
	private BigDecimal baseShippingTaxAmount;
	private BigDecimal storeToOrderRate;
	private BigDecimal baseDiscountAmount;
	private BigDecimal baseToOrderRate;
	private BigDecimal grandTotal;
	private BigDecimal baseAdjustmentNegative;
	private BigDecimal baseSubtotalInclTax;
	private BigDecimal shippingAmount;
	private BigDecimal subtotalInclTax;
	private BigDecimal adjustmentNegative;
	private BigDecimal baseShippingAmount;
	private BigDecimal storeToBaseRate;
	private BigDecimal baseToGlobalRate;
	private BigDecimal baseAdjustment;
	private BigDecimal baseSubtotal;
	private BigDecimal discountAmount;
	private BigDecimal subtotal;
	private BigDecimal adjustment;
	private BigDecimal baseGrandTotal;
	private BigDecimal baseAdjustmentPositive;
	private BigDecimal baseTaxAmount;
	private BigDecimal shippingTaxAmount;
	private BigDecimal taxAmount;
	private Short emailSent;
	private Integer creditmemoStatus;
	private Integer state;
	private Integer shippingAddressId;
	private Integer billingAddressId;
	private Integer invoiceId;
	private String storeCurrencyCode;
	private String orderCurrencyCode;
	private String baseCurrencyCode;
	private String globalCurrencyCode;
	private String transactionId;
	private String incrementId;
	private Date createdAt;
	private Date updatedAt;
	private BigDecimal hiddenTaxAmount;
	private BigDecimal baseHiddenTaxAmount;
	private BigDecimal shippingHiddenTaxAmount;
	private BigDecimal baseShippingHiddenTaxAmnt;
	private BigDecimal shippingInclTax;
	private BigDecimal baseShippingInclTax;
	private String discountDescription;
	private SalesFlatCreditmemoGrid salesFlatCreditmemoGrid;
	private Set<SalesFlatCreditmemoComment> salesFlatCreditmemoComments = new HashSet<SalesFlatCreditmemoComment>(0);
	private Set<SalesFlatCreditmemoItem> salesFlatCreditmemoItems = new HashSet<SalesFlatCreditmemoItem>(0);

	public SalesFlatCreditmemo() {
	}

	public SalesFlatCreditmemo(SalesFlatOrder salesFlatOrder) {
		this.salesFlatOrder = salesFlatOrder;
	}

	public SalesFlatCreditmemo(SalesFlatOrder salesFlatOrder, Store coreStore, BigDecimal adjustmentPositive, BigDecimal baseShippingTaxAmount, BigDecimal storeToOrderRate, BigDecimal baseDiscountAmount, BigDecimal baseToOrderRate, BigDecimal grandTotal, BigDecimal baseAdjustmentNegative, BigDecimal baseSubtotalInclTax, BigDecimal shippingAmount, BigDecimal subtotalInclTax, BigDecimal adjustmentNegative, BigDecimal baseShippingAmount, BigDecimal storeToBaseRate, BigDecimal baseToGlobalRate,
		BigDecimal baseAdjustment, BigDecimal baseSubtotal, BigDecimal discountAmount, BigDecimal subtotal, BigDecimal adjustment, BigDecimal baseGrandTotal, BigDecimal baseAdjustmentPositive, BigDecimal baseTaxAmount, BigDecimal shippingTaxAmount, BigDecimal taxAmount, Short emailSent, Integer creditmemoStatus, Integer state, Integer shippingAddressId, Integer billingAddressId, Integer invoiceId, String storeCurrencyCode, String orderCurrencyCode, String baseCurrencyCode,
		String globalCurrencyCode, String transactionId, String incrementId, Date createdAt, Date updatedAt, BigDecimal hiddenTaxAmount, BigDecimal baseHiddenTaxAmount, BigDecimal shippingHiddenTaxAmount, BigDecimal baseShippingHiddenTaxAmnt, BigDecimal shippingInclTax, BigDecimal baseShippingInclTax, String discountDescription, SalesFlatCreditmemoGrid salesFlatCreditmemoGrid, Set<SalesFlatCreditmemoComment> salesFlatCreditmemoComments, Set<SalesFlatCreditmemoItem> salesFlatCreditmemoItems) {
		this.salesFlatOrder = salesFlatOrder;
		this.coreStore = coreStore;
		this.adjustmentPositive = adjustmentPositive;
		this.baseShippingTaxAmount = baseShippingTaxAmount;
		this.storeToOrderRate = storeToOrderRate;
		this.baseDiscountAmount = baseDiscountAmount;
		this.baseToOrderRate = baseToOrderRate;
		this.grandTotal = grandTotal;
		this.baseAdjustmentNegative = baseAdjustmentNegative;
		this.baseSubtotalInclTax = baseSubtotalInclTax;
		this.shippingAmount = shippingAmount;
		this.subtotalInclTax = subtotalInclTax;
		this.adjustmentNegative = adjustmentNegative;
		this.baseShippingAmount = baseShippingAmount;
		this.storeToBaseRate = storeToBaseRate;
		this.baseToGlobalRate = baseToGlobalRate;
		this.baseAdjustment = baseAdjustment;
		this.baseSubtotal = baseSubtotal;
		this.discountAmount = discountAmount;
		this.subtotal = subtotal;
		this.adjustment = adjustment;
		this.baseGrandTotal = baseGrandTotal;
		this.baseAdjustmentPositive = baseAdjustmentPositive;
		this.baseTaxAmount = baseTaxAmount;
		this.shippingTaxAmount = shippingTaxAmount;
		this.taxAmount = taxAmount;
		this.emailSent = emailSent;
		this.creditmemoStatus = creditmemoStatus;
		this.state = state;
		this.shippingAddressId = shippingAddressId;
		this.billingAddressId = billingAddressId;
		this.invoiceId = invoiceId;
		this.storeCurrencyCode = storeCurrencyCode;
		this.orderCurrencyCode = orderCurrencyCode;
		this.baseCurrencyCode = baseCurrencyCode;
		this.globalCurrencyCode = globalCurrencyCode;
		this.transactionId = transactionId;
		this.incrementId = incrementId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.hiddenTaxAmount = hiddenTaxAmount;
		this.baseHiddenTaxAmount = baseHiddenTaxAmount;
		this.shippingHiddenTaxAmount = shippingHiddenTaxAmount;
		this.baseShippingHiddenTaxAmnt = baseShippingHiddenTaxAmnt;
		this.shippingInclTax = shippingInclTax;
		this.baseShippingInclTax = baseShippingInclTax;
		this.discountDescription = discountDescription;
		this.salesFlatCreditmemoGrid = salesFlatCreditmemoGrid;
		this.salesFlatCreditmemoComments = salesFlatCreditmemoComments;
		this.salesFlatCreditmemoItems = salesFlatCreditmemoItems;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "entity_id", unique = true, nullable = false)
	public Integer getEntityId() {
		return this.entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	public SalesFlatOrder getSalesFlatOrder() {
		return this.salesFlatOrder;
	}

	public void setSalesFlatOrder(SalesFlatOrder salesFlatOrder) {
		this.salesFlatOrder = salesFlatOrder;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	public Store getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(Store coreStore) {
		this.coreStore = coreStore;
	}

	@Column(name = "adjustment_positive", precision = 12, scale = 4)
	public BigDecimal getAdjustmentPositive() {
		return this.adjustmentPositive;
	}

	public void setAdjustmentPositive(BigDecimal adjustmentPositive) {
		this.adjustmentPositive = adjustmentPositive;
	}

	@Column(name = "base_shipping_tax_amount", precision = 12, scale = 4)
	public BigDecimal getBaseShippingTaxAmount() {
		return this.baseShippingTaxAmount;
	}

	public void setBaseShippingTaxAmount(BigDecimal baseShippingTaxAmount) {
		this.baseShippingTaxAmount = baseShippingTaxAmount;
	}

	@Column(name = "store_to_order_rate", precision = 12, scale = 4)
	public BigDecimal getStoreToOrderRate() {
		return this.storeToOrderRate;
	}

	public void setStoreToOrderRate(BigDecimal storeToOrderRate) {
		this.storeToOrderRate = storeToOrderRate;
	}

	@Column(name = "base_discount_amount", precision = 12, scale = 4)
	public BigDecimal getBaseDiscountAmount() {
		return this.baseDiscountAmount;
	}

	public void setBaseDiscountAmount(BigDecimal baseDiscountAmount) {
		this.baseDiscountAmount = baseDiscountAmount;
	}

	@Column(name = "base_to_order_rate", precision = 12, scale = 4)
	public BigDecimal getBaseToOrderRate() {
		return this.baseToOrderRate;
	}

	public void setBaseToOrderRate(BigDecimal baseToOrderRate) {
		this.baseToOrderRate = baseToOrderRate;
	}

	@Column(name = "grand_total", precision = 12, scale = 4)
	public BigDecimal getGrandTotal() {
		return this.grandTotal;
	}

	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}

	@Column(name = "base_adjustment_negative", precision = 12, scale = 4)
	public BigDecimal getBaseAdjustmentNegative() {
		return this.baseAdjustmentNegative;
	}

	public void setBaseAdjustmentNegative(BigDecimal baseAdjustmentNegative) {
		this.baseAdjustmentNegative = baseAdjustmentNegative;
	}

	@Column(name = "base_subtotal_incl_tax", precision = 12, scale = 4)
	public BigDecimal getBaseSubtotalInclTax() {
		return this.baseSubtotalInclTax;
	}

	public void setBaseSubtotalInclTax(BigDecimal baseSubtotalInclTax) {
		this.baseSubtotalInclTax = baseSubtotalInclTax;
	}

	@Column(name = "shipping_amount", precision = 12, scale = 4)
	public BigDecimal getShippingAmount() {
		return this.shippingAmount;
	}

	public void setShippingAmount(BigDecimal shippingAmount) {
		this.shippingAmount = shippingAmount;
	}

	@Column(name = "subtotal_incl_tax", precision = 12, scale = 4)
	public BigDecimal getSubtotalInclTax() {
		return this.subtotalInclTax;
	}

	public void setSubtotalInclTax(BigDecimal subtotalInclTax) {
		this.subtotalInclTax = subtotalInclTax;
	}

	@Column(name = "adjustment_negative", precision = 12, scale = 4)
	public BigDecimal getAdjustmentNegative() {
		return this.adjustmentNegative;
	}

	public void setAdjustmentNegative(BigDecimal adjustmentNegative) {
		this.adjustmentNegative = adjustmentNegative;
	}

	@Column(name = "base_shipping_amount", precision = 12, scale = 4)
	public BigDecimal getBaseShippingAmount() {
		return this.baseShippingAmount;
	}

	public void setBaseShippingAmount(BigDecimal baseShippingAmount) {
		this.baseShippingAmount = baseShippingAmount;
	}

	@Column(name = "store_to_base_rate", precision = 12, scale = 4)
	public BigDecimal getStoreToBaseRate() {
		return this.storeToBaseRate;
	}

	public void setStoreToBaseRate(BigDecimal storeToBaseRate) {
		this.storeToBaseRate = storeToBaseRate;
	}

	@Column(name = "base_to_global_rate", precision = 12, scale = 4)
	public BigDecimal getBaseToGlobalRate() {
		return this.baseToGlobalRate;
	}

	public void setBaseToGlobalRate(BigDecimal baseToGlobalRate) {
		this.baseToGlobalRate = baseToGlobalRate;
	}

	@Column(name = "base_adjustment", precision = 12, scale = 4)
	public BigDecimal getBaseAdjustment() {
		return this.baseAdjustment;
	}

	public void setBaseAdjustment(BigDecimal baseAdjustment) {
		this.baseAdjustment = baseAdjustment;
	}

	@Column(name = "base_subtotal", precision = 12, scale = 4)
	public BigDecimal getBaseSubtotal() {
		return this.baseSubtotal;
	}

	public void setBaseSubtotal(BigDecimal baseSubtotal) {
		this.baseSubtotal = baseSubtotal;
	}

	@Column(name = "discount_amount", precision = 12, scale = 4)
	public BigDecimal getDiscountAmount() {
		return this.discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	@Column(name = "subtotal", precision = 12, scale = 4)
	public BigDecimal getSubtotal() {
		return this.subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	@Column(name = "adjustment", precision = 12, scale = 4)
	public BigDecimal getAdjustment() {
		return this.adjustment;
	}

	public void setAdjustment(BigDecimal adjustment) {
		this.adjustment = adjustment;
	}

	@Column(name = "base_grand_total", precision = 12, scale = 4)
	public BigDecimal getBaseGrandTotal() {
		return this.baseGrandTotal;
	}

	public void setBaseGrandTotal(BigDecimal baseGrandTotal) {
		this.baseGrandTotal = baseGrandTotal;
	}

	@Column(name = "base_adjustment_positive", precision = 12, scale = 4)
	public BigDecimal getBaseAdjustmentPositive() {
		return this.baseAdjustmentPositive;
	}

	public void setBaseAdjustmentPositive(BigDecimal baseAdjustmentPositive) {
		this.baseAdjustmentPositive = baseAdjustmentPositive;
	}

	@Column(name = "base_tax_amount", precision = 12, scale = 4)
	public BigDecimal getBaseTaxAmount() {
		return this.baseTaxAmount;
	}

	public void setBaseTaxAmount(BigDecimal baseTaxAmount) {
		this.baseTaxAmount = baseTaxAmount;
	}

	@Column(name = "shipping_tax_amount", precision = 12, scale = 4)
	public BigDecimal getShippingTaxAmount() {
		return this.shippingTaxAmount;
	}

	public void setShippingTaxAmount(BigDecimal shippingTaxAmount) {
		this.shippingTaxAmount = shippingTaxAmount;
	}

	@Column(name = "tax_amount", precision = 12, scale = 4)
	public BigDecimal getTaxAmount() {
		return this.taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	@Column(name = "email_sent")
	public Short getEmailSent() {
		return this.emailSent;
	}

	public void setEmailSent(Short emailSent) {
		this.emailSent = emailSent;
	}

	@Column(name = "creditmemo_status")
	public Integer getCreditmemoStatus() {
		return this.creditmemoStatus;
	}

	public void setCreditmemoStatus(Integer creditmemoStatus) {
		this.creditmemoStatus = creditmemoStatus;
	}

	@Column(name = "state")
	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Column(name = "shipping_address_id")
	public Integer getShippingAddressId() {
		return this.shippingAddressId;
	}

	public void setShippingAddressId(Integer shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
	}

	@Column(name = "billing_address_id")
	public Integer getBillingAddressId() {
		return this.billingAddressId;
	}

	public void setBillingAddressId(Integer billingAddressId) {
		this.billingAddressId = billingAddressId;
	}

	@Column(name = "invoice_id")
	public Integer getInvoiceId() {
		return this.invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	@Column(name = "store_currency_code", length = 3)
	public String getStoreCurrencyCode() {
		return this.storeCurrencyCode;
	}

	public void setStoreCurrencyCode(String storeCurrencyCode) {
		this.storeCurrencyCode = storeCurrencyCode;
	}

	@Column(name = "order_currency_code", length = 3)
	public String getOrderCurrencyCode() {
		return this.orderCurrencyCode;
	}

	public void setOrderCurrencyCode(String orderCurrencyCode) {
		this.orderCurrencyCode = orderCurrencyCode;
	}

	@Column(name = "base_currency_code", length = 3)
	public String getBaseCurrencyCode() {
		return this.baseCurrencyCode;
	}

	public void setBaseCurrencyCode(String baseCurrencyCode) {
		this.baseCurrencyCode = baseCurrencyCode;
	}

	@Column(name = "global_currency_code", length = 3)
	public String getGlobalCurrencyCode() {
		return this.globalCurrencyCode;
	}

	public void setGlobalCurrencyCode(String globalCurrencyCode) {
		this.globalCurrencyCode = globalCurrencyCode;
	}

	@Column(name = "transaction_id")
	public String getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	@Column(name = "increment_id", unique = true, length = 50)
	public String getIncrementId() {
		return this.incrementId;
	}

	public void setIncrementId(String incrementId) {
		this.incrementId = incrementId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", length = 19)
	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", length = 19)
	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Column(name = "hidden_tax_amount", precision = 12, scale = 4)
	public BigDecimal getHiddenTaxAmount() {
		return this.hiddenTaxAmount;
	}

	public void setHiddenTaxAmount(BigDecimal hiddenTaxAmount) {
		this.hiddenTaxAmount = hiddenTaxAmount;
	}

	@Column(name = "base_hidden_tax_amount", precision = 12, scale = 4)
	public BigDecimal getBaseHiddenTaxAmount() {
		return this.baseHiddenTaxAmount;
	}

	public void setBaseHiddenTaxAmount(BigDecimal baseHiddenTaxAmount) {
		this.baseHiddenTaxAmount = baseHiddenTaxAmount;
	}

	@Column(name = "shipping_hidden_tax_amount", precision = 12, scale = 4)
	public BigDecimal getShippingHiddenTaxAmount() {
		return this.shippingHiddenTaxAmount;
	}

	public void setShippingHiddenTaxAmount(BigDecimal shippingHiddenTaxAmount) {
		this.shippingHiddenTaxAmount = shippingHiddenTaxAmount;
	}

	@Column(name = "base_shipping_hidden_tax_amnt", precision = 12, scale = 4)
	public BigDecimal getBaseShippingHiddenTaxAmnt() {
		return this.baseShippingHiddenTaxAmnt;
	}

	public void setBaseShippingHiddenTaxAmnt(BigDecimal baseShippingHiddenTaxAmnt) {
		this.baseShippingHiddenTaxAmnt = baseShippingHiddenTaxAmnt;
	}

	@Column(name = "shipping_incl_tax", precision = 12, scale = 4)
	public BigDecimal getShippingInclTax() {
		return this.shippingInclTax;
	}

	public void setShippingInclTax(BigDecimal shippingInclTax) {
		this.shippingInclTax = shippingInclTax;
	}

	@Column(name = "base_shipping_incl_tax", precision = 12, scale = 4)
	public BigDecimal getBaseShippingInclTax() {
		return this.baseShippingInclTax;
	}

	public void setBaseShippingInclTax(BigDecimal baseShippingInclTax) {
		this.baseShippingInclTax = baseShippingInclTax;
	}

	@Column(name = "discount_description")
	public String getDiscountDescription() {
		return this.discountDescription;
	}

	public void setDiscountDescription(String discountDescription) {
		this.discountDescription = discountDescription;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "salesFlatCreditmemo")
	public SalesFlatCreditmemoGrid getSalesFlatCreditmemoGrid() {
		return this.salesFlatCreditmemoGrid;
	}

	public void setSalesFlatCreditmemoGrid(SalesFlatCreditmemoGrid salesFlatCreditmemoGrid) {
		this.salesFlatCreditmemoGrid = salesFlatCreditmemoGrid;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salesFlatCreditmemo")
	public Set<SalesFlatCreditmemoComment> getSalesFlatCreditmemoComments() {
		return this.salesFlatCreditmemoComments;
	}

	public void setSalesFlatCreditmemoComments(Set<SalesFlatCreditmemoComment> salesFlatCreditmemoComments) {
		this.salesFlatCreditmemoComments = salesFlatCreditmemoComments;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salesFlatCreditmemo")
	public Set<SalesFlatCreditmemoItem> getSalesFlatCreditmemoItems() {
		return this.salesFlatCreditmemoItems;
	}

	public void setSalesFlatCreditmemoItems(Set<SalesFlatCreditmemoItem> salesFlatCreditmemoItems) {
		this.salesFlatCreditmemoItems = salesFlatCreditmemoItems;
	}

}
