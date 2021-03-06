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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.naver.mage4j.core.mage.core.model.resource.store.Store;

/**
 * SalesFlatQuote generated by hbm2java
 */
@Entity
@Table(name = "sales_flat_quote"
	, catalog = "magento")
public class SalesFlatQuote implements java.io.Serializable {

	private Integer entityId;
	private Store coreStore;
	private Date createdAt;
	private Date updatedAt;
	private Date convertedAt;
	private Short isActive;
	private Short isVirtual;
	private Short isMultiShipping;
	private Integer itemsCount;
	private BigDecimal itemsQty;
	private Integer origOrderId;
	private BigDecimal storeToBaseRate;
	private BigDecimal storeToQuoteRate;
	private String baseCurrencyCode;
	private String storeCurrencyCode;
	private String quoteCurrencyCode;
	private BigDecimal grandTotal;
	private BigDecimal baseGrandTotal;
	private String checkoutMethod;
	private Integer customerId;
	private Integer customerTaxClassId;
	private Integer customerGroupId;
	private String customerEmail;
	private String customerPrefix;
	private String customerFirstname;
	private String customerMiddlename;
	private String customerLastname;
	private String customerSuffix;
	private Date customerDob;
	private String customerNote;
	private Short customerNoteNotify;
	private Short customerIsGuest;
	private String remoteIp;
	private String appliedRuleIds;
	private String reservedOrderId;
	private String passwordHash;
	private String couponCode;
	private String globalCurrencyCode;
	private BigDecimal baseToGlobalRate;
	private BigDecimal baseToQuoteRate;
	private String customerTaxvat;
	private String customerGender;
	private BigDecimal subtotal;
	private BigDecimal baseSubtotal;
	private BigDecimal subtotalWithDiscount;
	private BigDecimal baseSubtotalWithDiscount;
	private Integer isChanged;
	private short triggerRecollect;
	private String extShippingInfo;
	private Integer giftMessageId;
	private Short isPersistent;
	private Set<SalesFlatQuoteAddress> salesFlatQuoteAddresses = new HashSet<SalesFlatQuoteAddress>(0);
	private Set<SalesFlatQuotePayment> salesFlatQuotePayments = new HashSet<SalesFlatQuotePayment>(0);
	private Set<SalesFlatQuoteItem> salesFlatQuoteItems = new HashSet<SalesFlatQuoteItem>(0);

	public SalesFlatQuote() {
	}

	public SalesFlatQuote(Store coreStore, Date createdAt, Date updatedAt, short triggerRecollect) {
		this.coreStore = coreStore;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.triggerRecollect = triggerRecollect;
	}

	public SalesFlatQuote(Store coreStore, Date createdAt, Date updatedAt, Date convertedAt, Short isActive, Short isVirtual, Short isMultiShipping, Integer itemsCount, BigDecimal itemsQty, Integer origOrderId, BigDecimal storeToBaseRate, BigDecimal storeToQuoteRate, String baseCurrencyCode, String storeCurrencyCode, String quoteCurrencyCode, BigDecimal grandTotal, BigDecimal baseGrandTotal, String checkoutMethod, Integer customerId, Integer customerTaxClassId, Integer customerGroupId,
		String customerEmail, String customerPrefix, String customerFirstname, String customerMiddlename, String customerLastname, String customerSuffix, Date customerDob, String customerNote, Short customerNoteNotify, Short customerIsGuest, String remoteIp, String appliedRuleIds, String reservedOrderId, String passwordHash, String couponCode, String globalCurrencyCode, BigDecimal baseToGlobalRate, BigDecimal baseToQuoteRate, String customerTaxvat, String customerGender, BigDecimal subtotal,
		BigDecimal baseSubtotal, BigDecimal subtotalWithDiscount, BigDecimal baseSubtotalWithDiscount, Integer isChanged, short triggerRecollect, String extShippingInfo, Integer giftMessageId, Short isPersistent, Set<SalesFlatQuoteAddress> salesFlatQuoteAddresses, Set<SalesFlatQuotePayment> salesFlatQuotePayments, Set<SalesFlatQuoteItem> salesFlatQuoteItems) {
		this.coreStore = coreStore;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.convertedAt = convertedAt;
		this.isActive = isActive;
		this.isVirtual = isVirtual;
		this.isMultiShipping = isMultiShipping;
		this.itemsCount = itemsCount;
		this.itemsQty = itemsQty;
		this.origOrderId = origOrderId;
		this.storeToBaseRate = storeToBaseRate;
		this.storeToQuoteRate = storeToQuoteRate;
		this.baseCurrencyCode = baseCurrencyCode;
		this.storeCurrencyCode = storeCurrencyCode;
		this.quoteCurrencyCode = quoteCurrencyCode;
		this.grandTotal = grandTotal;
		this.baseGrandTotal = baseGrandTotal;
		this.checkoutMethod = checkoutMethod;
		this.customerId = customerId;
		this.customerTaxClassId = customerTaxClassId;
		this.customerGroupId = customerGroupId;
		this.customerEmail = customerEmail;
		this.customerPrefix = customerPrefix;
		this.customerFirstname = customerFirstname;
		this.customerMiddlename = customerMiddlename;
		this.customerLastname = customerLastname;
		this.customerSuffix = customerSuffix;
		this.customerDob = customerDob;
		this.customerNote = customerNote;
		this.customerNoteNotify = customerNoteNotify;
		this.customerIsGuest = customerIsGuest;
		this.remoteIp = remoteIp;
		this.appliedRuleIds = appliedRuleIds;
		this.reservedOrderId = reservedOrderId;
		this.passwordHash = passwordHash;
		this.couponCode = couponCode;
		this.globalCurrencyCode = globalCurrencyCode;
		this.baseToGlobalRate = baseToGlobalRate;
		this.baseToQuoteRate = baseToQuoteRate;
		this.customerTaxvat = customerTaxvat;
		this.customerGender = customerGender;
		this.subtotal = subtotal;
		this.baseSubtotal = baseSubtotal;
		this.subtotalWithDiscount = subtotalWithDiscount;
		this.baseSubtotalWithDiscount = baseSubtotalWithDiscount;
		this.isChanged = isChanged;
		this.triggerRecollect = triggerRecollect;
		this.extShippingInfo = extShippingInfo;
		this.giftMessageId = giftMessageId;
		this.isPersistent = isPersistent;
		this.salesFlatQuoteAddresses = salesFlatQuoteAddresses;
		this.salesFlatQuotePayments = salesFlatQuotePayments;
		this.salesFlatQuoteItems = salesFlatQuoteItems;
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
	@JoinColumn(name = "store_id", nullable = false)
	public Store getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(Store coreStore) {
		this.coreStore = coreStore;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false, length = 19)
	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", nullable = false, length = 19)
	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "converted_at", length = 19)
	public Date getConvertedAt() {
		return this.convertedAt;
	}

	public void setConvertedAt(Date convertedAt) {
		this.convertedAt = convertedAt;
	}

	@Column(name = "is_active")
	public Short getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Short isActive) {
		this.isActive = isActive;
	}

	@Column(name = "is_virtual")
	public Short getIsVirtual() {
		return this.isVirtual;
	}

	public void setIsVirtual(Short isVirtual) {
		this.isVirtual = isVirtual;
	}

	@Column(name = "is_multi_shipping")
	public Short getIsMultiShipping() {
		return this.isMultiShipping;
	}

	public void setIsMultiShipping(Short isMultiShipping) {
		this.isMultiShipping = isMultiShipping;
	}

	@Column(name = "items_count")
	public Integer getItemsCount() {
		return this.itemsCount;
	}

	public void setItemsCount(Integer itemsCount) {
		this.itemsCount = itemsCount;
	}

	@Column(name = "items_qty", precision = 12, scale = 4)
	public BigDecimal getItemsQty() {
		return this.itemsQty;
	}

	public void setItemsQty(BigDecimal itemsQty) {
		this.itemsQty = itemsQty;
	}

	@Column(name = "orig_order_id")
	public Integer getOrigOrderId() {
		return this.origOrderId;
	}

	public void setOrigOrderId(Integer origOrderId) {
		this.origOrderId = origOrderId;
	}

	@Column(name = "store_to_base_rate", precision = 12, scale = 4)
	public BigDecimal getStoreToBaseRate() {
		return this.storeToBaseRate;
	}

	public void setStoreToBaseRate(BigDecimal storeToBaseRate) {
		this.storeToBaseRate = storeToBaseRate;
	}

	@Column(name = "store_to_quote_rate", precision = 12, scale = 4)
	public BigDecimal getStoreToQuoteRate() {
		return this.storeToQuoteRate;
	}

	public void setStoreToQuoteRate(BigDecimal storeToQuoteRate) {
		this.storeToQuoteRate = storeToQuoteRate;
	}

	@Column(name = "base_currency_code")
	public String getBaseCurrencyCode() {
		return this.baseCurrencyCode;
	}

	public void setBaseCurrencyCode(String baseCurrencyCode) {
		this.baseCurrencyCode = baseCurrencyCode;
	}

	@Column(name = "store_currency_code")
	public String getStoreCurrencyCode() {
		return this.storeCurrencyCode;
	}

	public void setStoreCurrencyCode(String storeCurrencyCode) {
		this.storeCurrencyCode = storeCurrencyCode;
	}

	@Column(name = "quote_currency_code")
	public String getQuoteCurrencyCode() {
		return this.quoteCurrencyCode;
	}

	public void setQuoteCurrencyCode(String quoteCurrencyCode) {
		this.quoteCurrencyCode = quoteCurrencyCode;
	}

	@Column(name = "grand_total", precision = 12, scale = 4)
	public BigDecimal getGrandTotal() {
		return this.grandTotal;
	}

	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}

	@Column(name = "base_grand_total", precision = 12, scale = 4)
	public BigDecimal getBaseGrandTotal() {
		return this.baseGrandTotal;
	}

	public void setBaseGrandTotal(BigDecimal baseGrandTotal) {
		this.baseGrandTotal = baseGrandTotal;
	}

	@Column(name = "checkout_method")
	public String getCheckoutMethod() {
		return this.checkoutMethod;
	}

	public void setCheckoutMethod(String checkoutMethod) {
		this.checkoutMethod = checkoutMethod;
	}

	@Column(name = "customer_id")
	public Integer getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	@Column(name = "customer_tax_class_id")
	public Integer getCustomerTaxClassId() {
		return this.customerTaxClassId;
	}

	public void setCustomerTaxClassId(Integer customerTaxClassId) {
		this.customerTaxClassId = customerTaxClassId;
	}

	@Column(name = "customer_group_id")
	public Integer getCustomerGroupId() {
		return this.customerGroupId;
	}

	public void setCustomerGroupId(Integer customerGroupId) {
		this.customerGroupId = customerGroupId;
	}

	@Column(name = "customer_email")
	public String getCustomerEmail() {
		return this.customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	@Column(name = "customer_prefix", length = 40)
	public String getCustomerPrefix() {
		return this.customerPrefix;
	}

	public void setCustomerPrefix(String customerPrefix) {
		this.customerPrefix = customerPrefix;
	}

	@Column(name = "customer_firstname")
	public String getCustomerFirstname() {
		return this.customerFirstname;
	}

	public void setCustomerFirstname(String customerFirstname) {
		this.customerFirstname = customerFirstname;
	}

	@Column(name = "customer_middlename", length = 40)
	public String getCustomerMiddlename() {
		return this.customerMiddlename;
	}

	public void setCustomerMiddlename(String customerMiddlename) {
		this.customerMiddlename = customerMiddlename;
	}

	@Column(name = "customer_lastname")
	public String getCustomerLastname() {
		return this.customerLastname;
	}

	public void setCustomerLastname(String customerLastname) {
		this.customerLastname = customerLastname;
	}

	@Column(name = "customer_suffix", length = 40)
	public String getCustomerSuffix() {
		return this.customerSuffix;
	}

	public void setCustomerSuffix(String customerSuffix) {
		this.customerSuffix = customerSuffix;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "customer_dob", length = 19)
	public Date getCustomerDob() {
		return this.customerDob;
	}

	public void setCustomerDob(Date customerDob) {
		this.customerDob = customerDob;
	}

	@Column(name = "customer_note")
	public String getCustomerNote() {
		return this.customerNote;
	}

	public void setCustomerNote(String customerNote) {
		this.customerNote = customerNote;
	}

	@Column(name = "customer_note_notify")
	public Short getCustomerNoteNotify() {
		return this.customerNoteNotify;
	}

	public void setCustomerNoteNotify(Short customerNoteNotify) {
		this.customerNoteNotify = customerNoteNotify;
	}

	@Column(name = "customer_is_guest")
	public Short getCustomerIsGuest() {
		return this.customerIsGuest;
	}

	public void setCustomerIsGuest(Short customerIsGuest) {
		this.customerIsGuest = customerIsGuest;
	}

	@Column(name = "remote_ip", length = 32)
	public String getRemoteIp() {
		return this.remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	@Column(name = "applied_rule_ids")
	public String getAppliedRuleIds() {
		return this.appliedRuleIds;
	}

	public void setAppliedRuleIds(String appliedRuleIds) {
		this.appliedRuleIds = appliedRuleIds;
	}

	@Column(name = "reserved_order_id", length = 64)
	public String getReservedOrderId() {
		return this.reservedOrderId;
	}

	public void setReservedOrderId(String reservedOrderId) {
		this.reservedOrderId = reservedOrderId;
	}

	@Column(name = "password_hash")
	public String getPasswordHash() {
		return this.passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	@Column(name = "coupon_code")
	public String getCouponCode() {
		return this.couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	@Column(name = "global_currency_code")
	public String getGlobalCurrencyCode() {
		return this.globalCurrencyCode;
	}

	public void setGlobalCurrencyCode(String globalCurrencyCode) {
		this.globalCurrencyCode = globalCurrencyCode;
	}

	@Column(name = "base_to_global_rate", precision = 12, scale = 4)
	public BigDecimal getBaseToGlobalRate() {
		return this.baseToGlobalRate;
	}

	public void setBaseToGlobalRate(BigDecimal baseToGlobalRate) {
		this.baseToGlobalRate = baseToGlobalRate;
	}

	@Column(name = "base_to_quote_rate", precision = 12, scale = 4)
	public BigDecimal getBaseToQuoteRate() {
		return this.baseToQuoteRate;
	}

	public void setBaseToQuoteRate(BigDecimal baseToQuoteRate) {
		this.baseToQuoteRate = baseToQuoteRate;
	}

	@Column(name = "customer_taxvat")
	public String getCustomerTaxvat() {
		return this.customerTaxvat;
	}

	public void setCustomerTaxvat(String customerTaxvat) {
		this.customerTaxvat = customerTaxvat;
	}

	@Column(name = "customer_gender")
	public String getCustomerGender() {
		return this.customerGender;
	}

	public void setCustomerGender(String customerGender) {
		this.customerGender = customerGender;
	}

	@Column(name = "subtotal", precision = 12, scale = 4)
	public BigDecimal getSubtotal() {
		return this.subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	@Column(name = "base_subtotal", precision = 12, scale = 4)
	public BigDecimal getBaseSubtotal() {
		return this.baseSubtotal;
	}

	public void setBaseSubtotal(BigDecimal baseSubtotal) {
		this.baseSubtotal = baseSubtotal;
	}

	@Column(name = "subtotal_with_discount", precision = 12, scale = 4)
	public BigDecimal getSubtotalWithDiscount() {
		return this.subtotalWithDiscount;
	}

	public void setSubtotalWithDiscount(BigDecimal subtotalWithDiscount) {
		this.subtotalWithDiscount = subtotalWithDiscount;
	}

	@Column(name = "base_subtotal_with_discount", precision = 12, scale = 4)
	public BigDecimal getBaseSubtotalWithDiscount() {
		return this.baseSubtotalWithDiscount;
	}

	public void setBaseSubtotalWithDiscount(BigDecimal baseSubtotalWithDiscount) {
		this.baseSubtotalWithDiscount = baseSubtotalWithDiscount;
	}

	@Column(name = "is_changed")
	public Integer getIsChanged() {
		return this.isChanged;
	}

	public void setIsChanged(Integer isChanged) {
		this.isChanged = isChanged;
	}

	@Column(name = "trigger_recollect", nullable = false)
	public short getTriggerRecollect() {
		return this.triggerRecollect;
	}

	public void setTriggerRecollect(short triggerRecollect) {
		this.triggerRecollect = triggerRecollect;
	}

	@Column(name = "ext_shipping_info", length = 65535, columnDefinition = "TEXT")
	public String getExtShippingInfo() {
		return this.extShippingInfo;
	}

	public void setExtShippingInfo(String extShippingInfo) {
		this.extShippingInfo = extShippingInfo;
	}

	@Column(name = "gift_message_id")
	public Integer getGiftMessageId() {
		return this.giftMessageId;
	}

	public void setGiftMessageId(Integer giftMessageId) {
		this.giftMessageId = giftMessageId;
	}

	@Column(name = "is_persistent")
	public Short getIsPersistent() {
		return this.isPersistent;
	}

	public void setIsPersistent(Short isPersistent) {
		this.isPersistent = isPersistent;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salesFlatQuote")
	public Set<SalesFlatQuoteAddress> getSalesFlatQuoteAddresses() {
		return this.salesFlatQuoteAddresses;
	}

	public void setSalesFlatQuoteAddresses(Set<SalesFlatQuoteAddress> salesFlatQuoteAddresses) {
		this.salesFlatQuoteAddresses = salesFlatQuoteAddresses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salesFlatQuote")
	public Set<SalesFlatQuotePayment> getSalesFlatQuotePayments() {
		return this.salesFlatQuotePayments;
	}

	public void setSalesFlatQuotePayments(Set<SalesFlatQuotePayment> salesFlatQuotePayments) {
		this.salesFlatQuotePayments = salesFlatQuotePayments;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salesFlatQuote")
	public Set<SalesFlatQuoteItem> getSalesFlatQuoteItems() {
		return this.salesFlatQuoteItems;
	}

	public void setSalesFlatQuoteItems(Set<SalesFlatQuoteItem> salesFlatQuoteItems) {
		this.salesFlatQuoteItems = salesFlatQuoteItems;
	}

}
