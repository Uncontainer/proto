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
 * SalesFlatOrderItem generated by hbm2java
 */
@Entity
@Table(name = "sales_flat_order_item"
	, catalog = "magento")
public class SalesFlatOrderItem implements java.io.Serializable {

	private Integer itemId;
	private SalesFlatOrder salesFlatOrder;
	private CoreStore coreStore;
	private Integer parentItemId;
	private Integer quoteItemId;
	private Date createdAt;
	private Date updatedAt;
	private Integer productId;
	private String productType;
	private String productOptions;
	private BigDecimal weight;
	private Short isVirtual;
	private String sku;
	private String name;
	private String description;
	private String appliedRuleIds;
	private String additionalData;
	private short freeShipping;
	private Short isQtyDecimal;
	private short noDiscount;
	private BigDecimal qtyBackordered;
	private BigDecimal qtyCanceled;
	private BigDecimal qtyInvoiced;
	private BigDecimal qtyOrdered;
	private BigDecimal qtyRefunded;
	private BigDecimal qtyShipped;
	private BigDecimal baseCost;
	private BigDecimal price;
	private BigDecimal basePrice;
	private BigDecimal originalPrice;
	private BigDecimal baseOriginalPrice;
	private BigDecimal taxPercent;
	private BigDecimal taxAmount;
	private BigDecimal baseTaxAmount;
	private BigDecimal taxInvoiced;
	private BigDecimal baseTaxInvoiced;
	private BigDecimal discountPercent;
	private BigDecimal discountAmount;
	private BigDecimal baseDiscountAmount;
	private BigDecimal discountInvoiced;
	private BigDecimal baseDiscountInvoiced;
	private BigDecimal amountRefunded;
	private BigDecimal baseAmountRefunded;
	private BigDecimal rowTotal;
	private BigDecimal baseRowTotal;
	private BigDecimal rowInvoiced;
	private BigDecimal baseRowInvoiced;
	private BigDecimal rowWeight;
	private BigDecimal baseTaxBeforeDiscount;
	private BigDecimal taxBeforeDiscount;
	private String extOrderItemId;
	private Short lockedDoInvoice;
	private Short lockedDoShip;
	private BigDecimal priceInclTax;
	private BigDecimal basePriceInclTax;
	private BigDecimal rowTotalInclTax;
	private BigDecimal baseRowTotalInclTax;
	private BigDecimal hiddenTaxAmount;
	private BigDecimal baseHiddenTaxAmount;
	private BigDecimal hiddenTaxInvoiced;
	private BigDecimal baseHiddenTaxInvoiced;
	private BigDecimal hiddenTaxRefunded;
	private BigDecimal baseHiddenTaxRefunded;
	private int isNominal;
	private BigDecimal taxCanceled;
	private BigDecimal hiddenTaxCanceled;
	private BigDecimal taxRefunded;
	private BigDecimal baseTaxRefunded;
	private BigDecimal discountRefunded;
	private BigDecimal baseDiscountRefunded;
	private Integer giftMessageId;
	private Integer giftMessageAvailable;
	private BigDecimal baseWeeeTaxAppliedAmount;
	private BigDecimal baseWeeeTaxAppliedRowAmnt;
	private BigDecimal weeeTaxAppliedAmount;
	private BigDecimal weeeTaxAppliedRowAmount;
	private String weeeTaxApplied;
	private BigDecimal weeeTaxDisposition;
	private BigDecimal weeeTaxRowDisposition;
	private BigDecimal baseWeeeTaxDisposition;
	private BigDecimal baseWeeeTaxRowDisposition;
	private Set<SalesOrderTaxItem> salesOrderTaxItems = new HashSet<SalesOrderTaxItem>(0);
	private Set<DownloadableLinkPurchasedItem> downloadableLinkPurchasedItems = new HashSet<DownloadableLinkPurchasedItem>(0);

	public SalesFlatOrderItem() {
	}

	public SalesFlatOrderItem(SalesFlatOrder salesFlatOrder, Date createdAt, Date updatedAt, short freeShipping, short noDiscount, BigDecimal price, BigDecimal basePrice, BigDecimal rowTotal, BigDecimal baseRowTotal, BigDecimal rowInvoiced, BigDecimal baseRowInvoiced, int isNominal) {
		this.salesFlatOrder = salesFlatOrder;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.freeShipping = freeShipping;
		this.noDiscount = noDiscount;
		this.price = price;
		this.basePrice = basePrice;
		this.rowTotal = rowTotal;
		this.baseRowTotal = baseRowTotal;
		this.rowInvoiced = rowInvoiced;
		this.baseRowInvoiced = baseRowInvoiced;
		this.isNominal = isNominal;
	}

	public SalesFlatOrderItem(SalesFlatOrder salesFlatOrder, CoreStore coreStore, Integer parentItemId, Integer quoteItemId, Date createdAt, Date updatedAt, Integer productId, String productType, String productOptions, BigDecimal weight, Short isVirtual, String sku, String name, String description, String appliedRuleIds, String additionalData, short freeShipping, Short isQtyDecimal, short noDiscount, BigDecimal qtyBackordered, BigDecimal qtyCanceled, BigDecimal qtyInvoiced,
		BigDecimal qtyOrdered, BigDecimal qtyRefunded, BigDecimal qtyShipped, BigDecimal baseCost, BigDecimal price, BigDecimal basePrice, BigDecimal originalPrice, BigDecimal baseOriginalPrice, BigDecimal taxPercent, BigDecimal taxAmount, BigDecimal baseTaxAmount, BigDecimal taxInvoiced, BigDecimal baseTaxInvoiced, BigDecimal discountPercent, BigDecimal discountAmount, BigDecimal baseDiscountAmount, BigDecimal discountInvoiced, BigDecimal baseDiscountInvoiced, BigDecimal amountRefunded,
		BigDecimal baseAmountRefunded, BigDecimal rowTotal, BigDecimal baseRowTotal, BigDecimal rowInvoiced, BigDecimal baseRowInvoiced, BigDecimal rowWeight, BigDecimal baseTaxBeforeDiscount, BigDecimal taxBeforeDiscount, String extOrderItemId, Short lockedDoInvoice, Short lockedDoShip, BigDecimal priceInclTax, BigDecimal basePriceInclTax, BigDecimal rowTotalInclTax, BigDecimal baseRowTotalInclTax, BigDecimal hiddenTaxAmount, BigDecimal baseHiddenTaxAmount, BigDecimal hiddenTaxInvoiced,
		BigDecimal baseHiddenTaxInvoiced, BigDecimal hiddenTaxRefunded, BigDecimal baseHiddenTaxRefunded, int isNominal, BigDecimal taxCanceled, BigDecimal hiddenTaxCanceled, BigDecimal taxRefunded, BigDecimal baseTaxRefunded, BigDecimal discountRefunded, BigDecimal baseDiscountRefunded, Integer giftMessageId, Integer giftMessageAvailable, BigDecimal baseWeeeTaxAppliedAmount, BigDecimal baseWeeeTaxAppliedRowAmnt, BigDecimal weeeTaxAppliedAmount, BigDecimal weeeTaxAppliedRowAmount,
		String weeeTaxApplied, BigDecimal weeeTaxDisposition, BigDecimal weeeTaxRowDisposition, BigDecimal baseWeeeTaxDisposition, BigDecimal baseWeeeTaxRowDisposition, Set<SalesOrderTaxItem> salesOrderTaxItems, Set<DownloadableLinkPurchasedItem> downloadableLinkPurchasedItems) {
		this.salesFlatOrder = salesFlatOrder;
		this.coreStore = coreStore;
		this.parentItemId = parentItemId;
		this.quoteItemId = quoteItemId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.productId = productId;
		this.productType = productType;
		this.productOptions = productOptions;
		this.weight = weight;
		this.isVirtual = isVirtual;
		this.sku = sku;
		this.name = name;
		this.description = description;
		this.appliedRuleIds = appliedRuleIds;
		this.additionalData = additionalData;
		this.freeShipping = freeShipping;
		this.isQtyDecimal = isQtyDecimal;
		this.noDiscount = noDiscount;
		this.qtyBackordered = qtyBackordered;
		this.qtyCanceled = qtyCanceled;
		this.qtyInvoiced = qtyInvoiced;
		this.qtyOrdered = qtyOrdered;
		this.qtyRefunded = qtyRefunded;
		this.qtyShipped = qtyShipped;
		this.baseCost = baseCost;
		this.price = price;
		this.basePrice = basePrice;
		this.originalPrice = originalPrice;
		this.baseOriginalPrice = baseOriginalPrice;
		this.taxPercent = taxPercent;
		this.taxAmount = taxAmount;
		this.baseTaxAmount = baseTaxAmount;
		this.taxInvoiced = taxInvoiced;
		this.baseTaxInvoiced = baseTaxInvoiced;
		this.discountPercent = discountPercent;
		this.discountAmount = discountAmount;
		this.baseDiscountAmount = baseDiscountAmount;
		this.discountInvoiced = discountInvoiced;
		this.baseDiscountInvoiced = baseDiscountInvoiced;
		this.amountRefunded = amountRefunded;
		this.baseAmountRefunded = baseAmountRefunded;
		this.rowTotal = rowTotal;
		this.baseRowTotal = baseRowTotal;
		this.rowInvoiced = rowInvoiced;
		this.baseRowInvoiced = baseRowInvoiced;
		this.rowWeight = rowWeight;
		this.baseTaxBeforeDiscount = baseTaxBeforeDiscount;
		this.taxBeforeDiscount = taxBeforeDiscount;
		this.extOrderItemId = extOrderItemId;
		this.lockedDoInvoice = lockedDoInvoice;
		this.lockedDoShip = lockedDoShip;
		this.priceInclTax = priceInclTax;
		this.basePriceInclTax = basePriceInclTax;
		this.rowTotalInclTax = rowTotalInclTax;
		this.baseRowTotalInclTax = baseRowTotalInclTax;
		this.hiddenTaxAmount = hiddenTaxAmount;
		this.baseHiddenTaxAmount = baseHiddenTaxAmount;
		this.hiddenTaxInvoiced = hiddenTaxInvoiced;
		this.baseHiddenTaxInvoiced = baseHiddenTaxInvoiced;
		this.hiddenTaxRefunded = hiddenTaxRefunded;
		this.baseHiddenTaxRefunded = baseHiddenTaxRefunded;
		this.isNominal = isNominal;
		this.taxCanceled = taxCanceled;
		this.hiddenTaxCanceled = hiddenTaxCanceled;
		this.taxRefunded = taxRefunded;
		this.baseTaxRefunded = baseTaxRefunded;
		this.discountRefunded = discountRefunded;
		this.baseDiscountRefunded = baseDiscountRefunded;
		this.giftMessageId = giftMessageId;
		this.giftMessageAvailable = giftMessageAvailable;
		this.baseWeeeTaxAppliedAmount = baseWeeeTaxAppliedAmount;
		this.baseWeeeTaxAppliedRowAmnt = baseWeeeTaxAppliedRowAmnt;
		this.weeeTaxAppliedAmount = weeeTaxAppliedAmount;
		this.weeeTaxAppliedRowAmount = weeeTaxAppliedRowAmount;
		this.weeeTaxApplied = weeeTaxApplied;
		this.weeeTaxDisposition = weeeTaxDisposition;
		this.weeeTaxRowDisposition = weeeTaxRowDisposition;
		this.baseWeeeTaxDisposition = baseWeeeTaxDisposition;
		this.baseWeeeTaxRowDisposition = baseWeeeTaxRowDisposition;
		this.salesOrderTaxItems = salesOrderTaxItems;
		this.downloadableLinkPurchasedItems = downloadableLinkPurchasedItems;
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
	@JoinColumn(name = "order_id", nullable = false)
	public SalesFlatOrder getSalesFlatOrder() {
		return this.salesFlatOrder;
	}

	public void setSalesFlatOrder(SalesFlatOrder salesFlatOrder) {
		this.salesFlatOrder = salesFlatOrder;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	public CoreStore getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(CoreStore coreStore) {
		this.coreStore = coreStore;
	}

	@Column(name = "parent_item_id")
	public Integer getParentItemId() {
		return this.parentItemId;
	}

	public void setParentItemId(Integer parentItemId) {
		this.parentItemId = parentItemId;
	}

	@Column(name = "quote_item_id")
	public Integer getQuoteItemId() {
		return this.quoteItemId;
	}

	public void setQuoteItemId(Integer quoteItemId) {
		this.quoteItemId = quoteItemId;
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

	@Column(name = "product_id")
	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	@Column(name = "product_type")
	public String getProductType() {
		return this.productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	@Column(name = "product_options", length = 65535)
	public String getProductOptions() {
		return this.productOptions;
	}

	public void setProductOptions(String productOptions) {
		this.productOptions = productOptions;
	}

	@Column(name = "weight", precision = 12, scale = 4)
	public BigDecimal getWeight() {
		return this.weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
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

	@Column(name = "no_discount", nullable = false)
	public short getNoDiscount() {
		return this.noDiscount;
	}

	public void setNoDiscount(short noDiscount) {
		this.noDiscount = noDiscount;
	}

	@Column(name = "qty_backordered", precision = 12, scale = 4)
	public BigDecimal getQtyBackordered() {
		return this.qtyBackordered;
	}

	public void setQtyBackordered(BigDecimal qtyBackordered) {
		this.qtyBackordered = qtyBackordered;
	}

	@Column(name = "qty_canceled", precision = 12, scale = 4)
	public BigDecimal getQtyCanceled() {
		return this.qtyCanceled;
	}

	public void setQtyCanceled(BigDecimal qtyCanceled) {
		this.qtyCanceled = qtyCanceled;
	}

	@Column(name = "qty_invoiced", precision = 12, scale = 4)
	public BigDecimal getQtyInvoiced() {
		return this.qtyInvoiced;
	}

	public void setQtyInvoiced(BigDecimal qtyInvoiced) {
		this.qtyInvoiced = qtyInvoiced;
	}

	@Column(name = "qty_ordered", precision = 12, scale = 4)
	public BigDecimal getQtyOrdered() {
		return this.qtyOrdered;
	}

	public void setQtyOrdered(BigDecimal qtyOrdered) {
		this.qtyOrdered = qtyOrdered;
	}

	@Column(name = "qty_refunded", precision = 12, scale = 4)
	public BigDecimal getQtyRefunded() {
		return this.qtyRefunded;
	}

	public void setQtyRefunded(BigDecimal qtyRefunded) {
		this.qtyRefunded = qtyRefunded;
	}

	@Column(name = "qty_shipped", precision = 12, scale = 4)
	public BigDecimal getQtyShipped() {
		return this.qtyShipped;
	}

	public void setQtyShipped(BigDecimal qtyShipped) {
		this.qtyShipped = qtyShipped;
	}

	@Column(name = "base_cost", precision = 12, scale = 4)
	public BigDecimal getBaseCost() {
		return this.baseCost;
	}

	public void setBaseCost(BigDecimal baseCost) {
		this.baseCost = baseCost;
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

	@Column(name = "original_price", precision = 12, scale = 4)
	public BigDecimal getOriginalPrice() {
		return this.originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	@Column(name = "base_original_price", precision = 12, scale = 4)
	public BigDecimal getBaseOriginalPrice() {
		return this.baseOriginalPrice;
	}

	public void setBaseOriginalPrice(BigDecimal baseOriginalPrice) {
		this.baseOriginalPrice = baseOriginalPrice;
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

	@Column(name = "tax_invoiced", precision = 12, scale = 4)
	public BigDecimal getTaxInvoiced() {
		return this.taxInvoiced;
	}

	public void setTaxInvoiced(BigDecimal taxInvoiced) {
		this.taxInvoiced = taxInvoiced;
	}

	@Column(name = "base_tax_invoiced", precision = 12, scale = 4)
	public BigDecimal getBaseTaxInvoiced() {
		return this.baseTaxInvoiced;
	}

	public void setBaseTaxInvoiced(BigDecimal baseTaxInvoiced) {
		this.baseTaxInvoiced = baseTaxInvoiced;
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

	@Column(name = "discount_invoiced", precision = 12, scale = 4)
	public BigDecimal getDiscountInvoiced() {
		return this.discountInvoiced;
	}

	public void setDiscountInvoiced(BigDecimal discountInvoiced) {
		this.discountInvoiced = discountInvoiced;
	}

	@Column(name = "base_discount_invoiced", precision = 12, scale = 4)
	public BigDecimal getBaseDiscountInvoiced() {
		return this.baseDiscountInvoiced;
	}

	public void setBaseDiscountInvoiced(BigDecimal baseDiscountInvoiced) {
		this.baseDiscountInvoiced = baseDiscountInvoiced;
	}

	@Column(name = "amount_refunded", precision = 12, scale = 4)
	public BigDecimal getAmountRefunded() {
		return this.amountRefunded;
	}

	public void setAmountRefunded(BigDecimal amountRefunded) {
		this.amountRefunded = amountRefunded;
	}

	@Column(name = "base_amount_refunded", precision = 12, scale = 4)
	public BigDecimal getBaseAmountRefunded() {
		return this.baseAmountRefunded;
	}

	public void setBaseAmountRefunded(BigDecimal baseAmountRefunded) {
		this.baseAmountRefunded = baseAmountRefunded;
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

	@Column(name = "row_invoiced", nullable = false, precision = 12, scale = 4)
	public BigDecimal getRowInvoiced() {
		return this.rowInvoiced;
	}

	public void setRowInvoiced(BigDecimal rowInvoiced) {
		this.rowInvoiced = rowInvoiced;
	}

	@Column(name = "base_row_invoiced", nullable = false, precision = 12, scale = 4)
	public BigDecimal getBaseRowInvoiced() {
		return this.baseRowInvoiced;
	}

	public void setBaseRowInvoiced(BigDecimal baseRowInvoiced) {
		this.baseRowInvoiced = baseRowInvoiced;
	}

	@Column(name = "row_weight", precision = 12, scale = 4)
	public BigDecimal getRowWeight() {
		return this.rowWeight;
	}

	public void setRowWeight(BigDecimal rowWeight) {
		this.rowWeight = rowWeight;
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

	@Column(name = "ext_order_item_id")
	public String getExtOrderItemId() {
		return this.extOrderItemId;
	}

	public void setExtOrderItemId(String extOrderItemId) {
		this.extOrderItemId = extOrderItemId;
	}

	@Column(name = "locked_do_invoice")
	public Short getLockedDoInvoice() {
		return this.lockedDoInvoice;
	}

	public void setLockedDoInvoice(Short lockedDoInvoice) {
		this.lockedDoInvoice = lockedDoInvoice;
	}

	@Column(name = "locked_do_ship")
	public Short getLockedDoShip() {
		return this.lockedDoShip;
	}

	public void setLockedDoShip(Short lockedDoShip) {
		this.lockedDoShip = lockedDoShip;
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

	@Column(name = "hidden_tax_invoiced", precision = 12, scale = 4)
	public BigDecimal getHiddenTaxInvoiced() {
		return this.hiddenTaxInvoiced;
	}

	public void setHiddenTaxInvoiced(BigDecimal hiddenTaxInvoiced) {
		this.hiddenTaxInvoiced = hiddenTaxInvoiced;
	}

	@Column(name = "base_hidden_tax_invoiced", precision = 12, scale = 4)
	public BigDecimal getBaseHiddenTaxInvoiced() {
		return this.baseHiddenTaxInvoiced;
	}

	public void setBaseHiddenTaxInvoiced(BigDecimal baseHiddenTaxInvoiced) {
		this.baseHiddenTaxInvoiced = baseHiddenTaxInvoiced;
	}

	@Column(name = "hidden_tax_refunded", precision = 12, scale = 4)
	public BigDecimal getHiddenTaxRefunded() {
		return this.hiddenTaxRefunded;
	}

	public void setHiddenTaxRefunded(BigDecimal hiddenTaxRefunded) {
		this.hiddenTaxRefunded = hiddenTaxRefunded;
	}

	@Column(name = "base_hidden_tax_refunded", precision = 12, scale = 4)
	public BigDecimal getBaseHiddenTaxRefunded() {
		return this.baseHiddenTaxRefunded;
	}

	public void setBaseHiddenTaxRefunded(BigDecimal baseHiddenTaxRefunded) {
		this.baseHiddenTaxRefunded = baseHiddenTaxRefunded;
	}

	@Column(name = "is_nominal", nullable = false)
	public int getIsNominal() {
		return this.isNominal;
	}

	public void setIsNominal(int isNominal) {
		this.isNominal = isNominal;
	}

	@Column(name = "tax_canceled", precision = 12, scale = 4)
	public BigDecimal getTaxCanceled() {
		return this.taxCanceled;
	}

	public void setTaxCanceled(BigDecimal taxCanceled) {
		this.taxCanceled = taxCanceled;
	}

	@Column(name = "hidden_tax_canceled", precision = 12, scale = 4)
	public BigDecimal getHiddenTaxCanceled() {
		return this.hiddenTaxCanceled;
	}

	public void setHiddenTaxCanceled(BigDecimal hiddenTaxCanceled) {
		this.hiddenTaxCanceled = hiddenTaxCanceled;
	}

	@Column(name = "tax_refunded", precision = 12, scale = 4)
	public BigDecimal getTaxRefunded() {
		return this.taxRefunded;
	}

	public void setTaxRefunded(BigDecimal taxRefunded) {
		this.taxRefunded = taxRefunded;
	}

	@Column(name = "base_tax_refunded", precision = 12, scale = 4)
	public BigDecimal getBaseTaxRefunded() {
		return this.baseTaxRefunded;
	}

	public void setBaseTaxRefunded(BigDecimal baseTaxRefunded) {
		this.baseTaxRefunded = baseTaxRefunded;
	}

	@Column(name = "discount_refunded", precision = 12, scale = 4)
	public BigDecimal getDiscountRefunded() {
		return this.discountRefunded;
	}

	public void setDiscountRefunded(BigDecimal discountRefunded) {
		this.discountRefunded = discountRefunded;
	}

	@Column(name = "base_discount_refunded", precision = 12, scale = 4)
	public BigDecimal getBaseDiscountRefunded() {
		return this.baseDiscountRefunded;
	}

	public void setBaseDiscountRefunded(BigDecimal baseDiscountRefunded) {
		this.baseDiscountRefunded = baseDiscountRefunded;
	}

	@Column(name = "gift_message_id")
	public Integer getGiftMessageId() {
		return this.giftMessageId;
	}

	public void setGiftMessageId(Integer giftMessageId) {
		this.giftMessageId = giftMessageId;
	}

	@Column(name = "gift_message_available")
	public Integer getGiftMessageAvailable() {
		return this.giftMessageAvailable;
	}

	public void setGiftMessageAvailable(Integer giftMessageAvailable) {
		this.giftMessageAvailable = giftMessageAvailable;
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

	@Column(name = "weee_tax_applied", length = 65535)
	public String getWeeeTaxApplied() {
		return this.weeeTaxApplied;
	}

	public void setWeeeTaxApplied(String weeeTaxApplied) {
		this.weeeTaxApplied = weeeTaxApplied;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salesFlatOrderItem")
	public Set<SalesOrderTaxItem> getSalesOrderTaxItems() {
		return this.salesOrderTaxItems;
	}

	public void setSalesOrderTaxItems(Set<SalesOrderTaxItem> salesOrderTaxItems) {
		this.salesOrderTaxItems = salesOrderTaxItems;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salesFlatOrderItem")
	public Set<DownloadableLinkPurchasedItem> getDownloadableLinkPurchasedItems() {
		return this.downloadableLinkPurchasedItems;
	}

	public void setDownloadableLinkPurchasedItems(Set<DownloadableLinkPurchasedItem> downloadableLinkPurchasedItems) {
		this.downloadableLinkPurchasedItems = downloadableLinkPurchasedItems;
	}

}