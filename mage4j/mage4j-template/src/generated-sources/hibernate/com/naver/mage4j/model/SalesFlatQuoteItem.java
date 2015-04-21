package com.naver.mage4j.model;

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

/**
 * SalesFlatQuoteItem generated by hbm2java
 */
@Entity
@Table(name = "sales_flat_quote_item"
	, catalog = "magento")
public class SalesFlatQuoteItem implements java.io.Serializable {

	private Integer itemId;
	private SalesFlatQuoteItem salesFlatQuoteItem;
	private SalesFlatQuote salesFlatQuote;
	private CoreStore coreStore;
	private CatalogProductEntity catalogProductEntity;
	private Date createdAt;
	private Date updatedAt;
	private Short isVirtual;
	private String sku;
	private String name;
	private String description;
	private String appliedRuleIds;
	private String additionalData;
	private short freeShipping;
	private Short isQtyDecimal;
	private Short noDiscount;
	private BigDecimal weight;
	private BigDecimal qty;
	private BigDecimal price;
	private BigDecimal basePrice;
	private BigDecimal customPrice;
	private BigDecimal discountPercent;
	private BigDecimal discountAmount;
	private BigDecimal baseDiscountAmount;
	private BigDecimal taxPercent;
	private BigDecimal taxAmount;
	private BigDecimal baseTaxAmount;
	private BigDecimal rowTotal;
	private BigDecimal baseRowTotal;
	private BigDecimal rowTotalWithDiscount;
	private BigDecimal rowWeight;
	private String productType;
	private BigDecimal baseTaxBeforeDiscount;
	private BigDecimal taxBeforeDiscount;
	private BigDecimal originalCustomPrice;
	private String redirectUrl;
	private BigDecimal baseCost;
	private BigDecimal priceInclTax;
	private BigDecimal basePriceInclTax;
	private BigDecimal rowTotalInclTax;
	private BigDecimal baseRowTotalInclTax;
	private BigDecimal hiddenTaxAmount;
	private BigDecimal baseHiddenTaxAmount;
	private Integer giftMessageId;
	private BigDecimal weeeTaxDisposition;
	private BigDecimal weeeTaxRowDisposition;
	private BigDecimal baseWeeeTaxDisposition;
	private BigDecimal baseWeeeTaxRowDisposition;
	private String weeeTaxApplied;
	private BigDecimal weeeTaxAppliedAmount;
	private BigDecimal weeeTaxAppliedRowAmount;
	private BigDecimal baseWeeeTaxAppliedAmount;
	private BigDecimal baseWeeeTaxAppliedRowAmnt;
	private Set<SalesFlatQuoteItem> salesFlatQuoteItems = new HashSet<SalesFlatQuoteItem>(0);
	private Set<SalesFlatQuoteItemOption> salesFlatQuoteItemOptions = new HashSet<SalesFlatQuoteItemOption>(0);
	private Set<SalesFlatQuoteAddressItem> salesFlatQuoteAddressItems = new HashSet<SalesFlatQuoteAddressItem>(0);

	public SalesFlatQuoteItem() {
	}

	public SalesFlatQuoteItem(SalesFlatQuote salesFlatQuote, Date createdAt, Date updatedAt, short freeShipping, BigDecimal qty, BigDecimal price, BigDecimal basePrice, BigDecimal rowTotal, BigDecimal baseRowTotal) {
		this.salesFlatQuote = salesFlatQuote;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.freeShipping = freeShipping;
		this.qty = qty;
		this.price = price;
		this.basePrice = basePrice;
		this.rowTotal = rowTotal;
		this.baseRowTotal = baseRowTotal;
	}

	public SalesFlatQuoteItem(SalesFlatQuoteItem salesFlatQuoteItem, SalesFlatQuote salesFlatQuote, CoreStore coreStore, CatalogProductEntity catalogProductEntity, Date createdAt, Date updatedAt, Short isVirtual, String sku, String name, String description, String appliedRuleIds, String additionalData, short freeShipping, Short isQtyDecimal, Short noDiscount, BigDecimal weight, BigDecimal qty, BigDecimal price, BigDecimal basePrice, BigDecimal customPrice, BigDecimal discountPercent,
		BigDecimal discountAmount, BigDecimal baseDiscountAmount, BigDecimal taxPercent, BigDecimal taxAmount, BigDecimal baseTaxAmount, BigDecimal rowTotal, BigDecimal baseRowTotal, BigDecimal rowTotalWithDiscount, BigDecimal rowWeight, String productType, BigDecimal baseTaxBeforeDiscount, BigDecimal taxBeforeDiscount, BigDecimal originalCustomPrice, String redirectUrl, BigDecimal baseCost, BigDecimal priceInclTax, BigDecimal basePriceInclTax, BigDecimal rowTotalInclTax,
		BigDecimal baseRowTotalInclTax, BigDecimal hiddenTaxAmount, BigDecimal baseHiddenTaxAmount, Integer giftMessageId, BigDecimal weeeTaxDisposition, BigDecimal weeeTaxRowDisposition, BigDecimal baseWeeeTaxDisposition, BigDecimal baseWeeeTaxRowDisposition, String weeeTaxApplied, BigDecimal weeeTaxAppliedAmount, BigDecimal weeeTaxAppliedRowAmount, BigDecimal baseWeeeTaxAppliedAmount, BigDecimal baseWeeeTaxAppliedRowAmnt, Set<SalesFlatQuoteItem> salesFlatQuoteItems,
		Set<SalesFlatQuoteItemOption> salesFlatQuoteItemOptions, Set<SalesFlatQuoteAddressItem> salesFlatQuoteAddressItems) {
		this.salesFlatQuoteItem = salesFlatQuoteItem;
		this.salesFlatQuote = salesFlatQuote;
		this.coreStore = coreStore;
		this.catalogProductEntity = catalogProductEntity;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.isVirtual = isVirtual;
		this.sku = sku;
		this.name = name;
		this.description = description;
		this.appliedRuleIds = appliedRuleIds;
		this.additionalData = additionalData;
		this.freeShipping = freeShipping;
		this.isQtyDecimal = isQtyDecimal;
		this.noDiscount = noDiscount;
		this.weight = weight;
		this.qty = qty;
		this.price = price;
		this.basePrice = basePrice;
		this.customPrice = customPrice;
		this.discountPercent = discountPercent;
		this.discountAmount = discountAmount;
		this.baseDiscountAmount = baseDiscountAmount;
		this.taxPercent = taxPercent;
		this.taxAmount = taxAmount;
		this.baseTaxAmount = baseTaxAmount;
		this.rowTotal = rowTotal;
		this.baseRowTotal = baseRowTotal;
		this.rowTotalWithDiscount = rowTotalWithDiscount;
		this.rowWeight = rowWeight;
		this.productType = productType;
		this.baseTaxBeforeDiscount = baseTaxBeforeDiscount;
		this.taxBeforeDiscount = taxBeforeDiscount;
		this.originalCustomPrice = originalCustomPrice;
		this.redirectUrl = redirectUrl;
		this.baseCost = baseCost;
		this.priceInclTax = priceInclTax;
		this.basePriceInclTax = basePriceInclTax;
		this.rowTotalInclTax = rowTotalInclTax;
		this.baseRowTotalInclTax = baseRowTotalInclTax;
		this.hiddenTaxAmount = hiddenTaxAmount;
		this.baseHiddenTaxAmount = baseHiddenTaxAmount;
		this.giftMessageId = giftMessageId;
		this.weeeTaxDisposition = weeeTaxDisposition;
		this.weeeTaxRowDisposition = weeeTaxRowDisposition;
		this.baseWeeeTaxDisposition = baseWeeeTaxDisposition;
		this.baseWeeeTaxRowDisposition = baseWeeeTaxRowDisposition;
		this.weeeTaxApplied = weeeTaxApplied;
		this.weeeTaxAppliedAmount = weeeTaxAppliedAmount;
		this.weeeTaxAppliedRowAmount = weeeTaxAppliedRowAmount;
		this.baseWeeeTaxAppliedAmount = baseWeeeTaxAppliedAmount;
		this.baseWeeeTaxAppliedRowAmnt = baseWeeeTaxAppliedRowAmnt;
		this.salesFlatQuoteItems = salesFlatQuoteItems;
		this.salesFlatQuoteItemOptions = salesFlatQuoteItemOptions;
		this.salesFlatQuoteAddressItems = salesFlatQuoteAddressItems;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "item_id", unique = true, nullable = false)
	public Integer getItemId() {
		return this.itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_item_id")
	public SalesFlatQuoteItem getSalesFlatQuoteItem() {
		return this.salesFlatQuoteItem;
	}

	public void setSalesFlatQuoteItem(SalesFlatQuoteItem salesFlatQuoteItem) {
		this.salesFlatQuoteItem = salesFlatQuoteItem;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quote_id", nullable = false)
	public SalesFlatQuote getSalesFlatQuote() {
		return this.salesFlatQuote;
	}

	public void setSalesFlatQuote(SalesFlatQuote salesFlatQuote) {
		this.salesFlatQuote = salesFlatQuote;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	public CoreStore getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(CoreStore coreStore) {
		this.coreStore = coreStore;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	public CatalogProductEntity getCatalogProductEntity() {
		return this.catalogProductEntity;
	}

	public void setCatalogProductEntity(CatalogProductEntity catalogProductEntity) {
		this.catalogProductEntity = catalogProductEntity;
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

	@Column(name = "is_virtual")
	public Short getIsVirtual() {
		return this.isVirtual;
	}

	public void setIsVirtual(Short isVirtual) {
		this.isVirtual = isVirtual;
	}

	@Column(name = "sku")
	public String getSku() {
		return this.sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", length = 65535)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "applied_rule_ids", length = 65535)
	public String getAppliedRuleIds() {
		return this.appliedRuleIds;
	}

	public void setAppliedRuleIds(String appliedRuleIds) {
		this.appliedRuleIds = appliedRuleIds;
	}

	@Column(name = "additional_data", length = 65535)
	public String getAdditionalData() {
		return this.additionalData;
	}

	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}

	@Column(name = "free_shipping", nullable = false)
	public short getFreeShipping() {
		return this.freeShipping;
	}

	public void setFreeShipping(short freeShipping) {
		this.freeShipping = freeShipping;
	}

	@Column(name = "is_qty_decimal")
	public Short getIsQtyDecimal() {
		return this.isQtyDecimal;
	}

	public void setIsQtyDecimal(Short isQtyDecimal) {
		this.isQtyDecimal = isQtyDecimal;
	}

	@Column(name = "no_discount")
	public Short getNoDiscount() {
		return this.noDiscount;
	}

	public void setNoDiscount(Short noDiscount) {
		this.noDiscount = noDiscount;
	}

	@Column(name = "weight", precision = 12, scale = 4)
	public BigDecimal getWeight() {
		return this.weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	@Column(name = "qty", nullable = false, precision = 12, scale = 4)
	public BigDecimal getQty() {
		return this.qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	@Column(name = "price", nullable = false, precision = 12, scale = 4)
	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Column(name = "base_price", nullable = false, precision = 12, scale = 4)
	public BigDecimal getBasePrice() {
		return this.basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	@Column(name = "custom_price", precision = 12, scale = 4)
	public BigDecimal getCustomPrice() {
		return this.customPrice;
	}

	public void setCustomPrice(BigDecimal customPrice) {
		this.customPrice = customPrice;
	}

	@Column(name = "discount_percent", precision = 12, scale = 4)
	public BigDecimal getDiscountPercent() {
		return this.discountPercent;
	}

	public void setDiscountPercent(BigDecimal discountPercent) {
		this.discountPercent = discountPercent;
	}

	@Column(name = "discount_amount", precision = 12, scale = 4)
	public BigDecimal getDiscountAmount() {
		return this.discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	@Column(name = "base_discount_amount", precision = 12, scale = 4)
	public BigDecimal getBaseDiscountAmount() {
		return this.baseDiscountAmount;
	}

	public void setBaseDiscountAmount(BigDecimal baseDiscountAmount) {
		this.baseDiscountAmount = baseDiscountAmount;
	}

	@Column(name = "tax_percent", precision = 12, scale = 4)
	public BigDecimal getTaxPercent() {
		return this.taxPercent;
	}

	public void setTaxPercent(BigDecimal taxPercent) {
		this.taxPercent = taxPercent;
	}

	@Column(name = "tax_amount", precision = 12, scale = 4)
	public BigDecimal getTaxAmount() {
		return this.taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	@Column(name = "base_tax_amount", precision = 12, scale = 4)
	public BigDecimal getBaseTaxAmount() {
		return this.baseTaxAmount;
	}

	public void setBaseTaxAmount(BigDecimal baseTaxAmount) {
		this.baseTaxAmount = baseTaxAmount;
	}

	@Column(name = "row_total", nullable = false, precision = 12, scale = 4)
	public BigDecimal getRowTotal() {
		return this.rowTotal;
	}

	public void setRowTotal(BigDecimal rowTotal) {
		this.rowTotal = rowTotal;
	}

	@Column(name = "base_row_total", nullable = false, precision = 12, scale = 4)
	public BigDecimal getBaseRowTotal() {
		return this.baseRowTotal;
	}

	public void setBaseRowTotal(BigDecimal baseRowTotal) {
		this.baseRowTotal = baseRowTotal;
	}

	@Column(name = "row_total_with_discount", precision = 12, scale = 4)
	public BigDecimal getRowTotalWithDiscount() {
		return this.rowTotalWithDiscount;
	}

	public void setRowTotalWithDiscount(BigDecimal rowTotalWithDiscount) {
		this.rowTotalWithDiscount = rowTotalWithDiscount;
	}

	@Column(name = "row_weight", precision = 12, scale = 4)
	public BigDecimal getRowWeight() {
		return this.rowWeight;
	}

	public void setRowWeight(BigDecimal rowWeight) {
		this.rowWeight = rowWeight;
	}

	@Column(name = "product_type")
	public String getProductType() {
		return this.productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	@Column(name = "base_tax_before_discount", precision = 12, scale = 4)
	public BigDecimal getBaseTaxBeforeDiscount() {
		return this.baseTaxBeforeDiscount;
	}

	public void setBaseTaxBeforeDiscount(BigDecimal baseTaxBeforeDiscount) {
		this.baseTaxBeforeDiscount = baseTaxBeforeDiscount;
	}

	@Column(name = "tax_before_discount", precision = 12, scale = 4)
	public BigDecimal getTaxBeforeDiscount() {
		return this.taxBeforeDiscount;
	}

	public void setTaxBeforeDiscount(BigDecimal taxBeforeDiscount) {
		this.taxBeforeDiscount = taxBeforeDiscount;
	}

	@Column(name = "original_custom_price", precision = 12, scale = 4)
	public BigDecimal getOriginalCustomPrice() {
		return this.originalCustomPrice;
	}

	public void setOriginalCustomPrice(BigDecimal originalCustomPrice) {
		this.originalCustomPrice = originalCustomPrice;
	}

	@Column(name = "redirect_url")
	public String getRedirectUrl() {
		return this.redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	@Column(name = "base_cost", precision = 12, scale = 4)
	public BigDecimal getBaseCost() {
		return this.baseCost;
	}

	public void setBaseCost(BigDecimal baseCost) {
		this.baseCost = baseCost;
	}

	@Column(name = "price_incl_tax", precision = 12, scale = 4)
	public BigDecimal getPriceInclTax() {
		return this.priceInclTax;
	}

	public void setPriceInclTax(BigDecimal priceInclTax) {
		this.priceInclTax = priceInclTax;
	}

	@Column(name = "base_price_incl_tax", precision = 12, scale = 4)
	public BigDecimal getBasePriceInclTax() {
		return this.basePriceInclTax;
	}

	public void setBasePriceInclTax(BigDecimal basePriceInclTax) {
		this.basePriceInclTax = basePriceInclTax;
	}

	@Column(name = "row_total_incl_tax", precision = 12, scale = 4)
	public BigDecimal getRowTotalInclTax() {
		return this.rowTotalInclTax;
	}

	public void setRowTotalInclTax(BigDecimal rowTotalInclTax) {
		this.rowTotalInclTax = rowTotalInclTax;
	}

	@Column(name = "base_row_total_incl_tax", precision = 12, scale = 4)
	public BigDecimal getBaseRowTotalInclTax() {
		return this.baseRowTotalInclTax;
	}

	public void setBaseRowTotalInclTax(BigDecimal baseRowTotalInclTax) {
		this.baseRowTotalInclTax = baseRowTotalInclTax;
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

	@Column(name = "gift_message_id")
	public Integer getGiftMessageId() {
		return this.giftMessageId;
	}

	public void setGiftMessageId(Integer giftMessageId) {
		this.giftMessageId = giftMessageId;
	}

	@Column(name = "weee_tax_disposition", precision = 12, scale = 4)
	public BigDecimal getWeeeTaxDisposition() {
		return this.weeeTaxDisposition;
	}

	public void setWeeeTaxDisposition(BigDecimal weeeTaxDisposition) {
		this.weeeTaxDisposition = weeeTaxDisposition;
	}

	@Column(name = "weee_tax_row_disposition", precision = 12, scale = 4)
	public BigDecimal getWeeeTaxRowDisposition() {
		return this.weeeTaxRowDisposition;
	}

	public void setWeeeTaxRowDisposition(BigDecimal weeeTaxRowDisposition) {
		this.weeeTaxRowDisposition = weeeTaxRowDisposition;
	}

	@Column(name = "base_weee_tax_disposition", precision = 12, scale = 4)
	public BigDecimal getBaseWeeeTaxDisposition() {
		return this.baseWeeeTaxDisposition;
	}

	public void setBaseWeeeTaxDisposition(BigDecimal baseWeeeTaxDisposition) {
		this.baseWeeeTaxDisposition = baseWeeeTaxDisposition;
	}

	@Column(name = "base_weee_tax_row_disposition", precision = 12, scale = 4)
	public BigDecimal getBaseWeeeTaxRowDisposition() {
		return this.baseWeeeTaxRowDisposition;
	}

	public void setBaseWeeeTaxRowDisposition(BigDecimal baseWeeeTaxRowDisposition) {
		this.baseWeeeTaxRowDisposition = baseWeeeTaxRowDisposition;
	}

	@Column(name = "weee_tax_applied", length = 65535)
	public String getWeeeTaxApplied() {
		return this.weeeTaxApplied;
	}

	public void setWeeeTaxApplied(String weeeTaxApplied) {
		this.weeeTaxApplied = weeeTaxApplied;
	}

	@Column(name = "weee_tax_applied_amount", precision = 12, scale = 4)
	public BigDecimal getWeeeTaxAppliedAmount() {
		return this.weeeTaxAppliedAmount;
	}

	public void setWeeeTaxAppliedAmount(BigDecimal weeeTaxAppliedAmount) {
		this.weeeTaxAppliedAmount = weeeTaxAppliedAmount;
	}

	@Column(name = "weee_tax_applied_row_amount", precision = 12, scale = 4)
	public BigDecimal getWeeeTaxAppliedRowAmount() {
		return this.weeeTaxAppliedRowAmount;
	}

	public void setWeeeTaxAppliedRowAmount(BigDecimal weeeTaxAppliedRowAmount) {
		this.weeeTaxAppliedRowAmount = weeeTaxAppliedRowAmount;
	}

	@Column(name = "base_weee_tax_applied_amount", precision = 12, scale = 4)
	public BigDecimal getBaseWeeeTaxAppliedAmount() {
		return this.baseWeeeTaxAppliedAmount;
	}

	public void setBaseWeeeTaxAppliedAmount(BigDecimal baseWeeeTaxAppliedAmount) {
		this.baseWeeeTaxAppliedAmount = baseWeeeTaxAppliedAmount;
	}

	@Column(name = "base_weee_tax_applied_row_amnt", precision = 12, scale = 4)
	public BigDecimal getBaseWeeeTaxAppliedRowAmnt() {
		return this.baseWeeeTaxAppliedRowAmnt;
	}

	public void setBaseWeeeTaxAppliedRowAmnt(BigDecimal baseWeeeTaxAppliedRowAmnt) {
		this.baseWeeeTaxAppliedRowAmnt = baseWeeeTaxAppliedRowAmnt;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salesFlatQuoteItem")
	public Set<SalesFlatQuoteItem> getSalesFlatQuoteItems() {
		return this.salesFlatQuoteItems;
	}

	public void setSalesFlatQuoteItems(Set<SalesFlatQuoteItem> salesFlatQuoteItems) {
		this.salesFlatQuoteItems = salesFlatQuoteItems;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salesFlatQuoteItem")
	public Set<SalesFlatQuoteItemOption> getSalesFlatQuoteItemOptions() {
		return this.salesFlatQuoteItemOptions;
	}

	public void setSalesFlatQuoteItemOptions(Set<SalesFlatQuoteItemOption> salesFlatQuoteItemOptions) {
		this.salesFlatQuoteItemOptions = salesFlatQuoteItemOptions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salesFlatQuoteItem")
	public Set<SalesFlatQuoteAddressItem> getSalesFlatQuoteAddressItems() {
		return this.salesFlatQuoteAddressItems;
	}

	public void setSalesFlatQuoteAddressItems(Set<SalesFlatQuoteAddressItem> salesFlatQuoteAddressItems) {
		this.salesFlatQuoteAddressItems = salesFlatQuoteAddressItems;
	}

}