package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * SalesFlatShipmentItem generated by hbm2java
 */
@Entity
@Table(name = "sales_flat_shipment_item"
	, catalog = "magento")
public class SalesFlatShipmentItem implements java.io.Serializable {

	private Integer entityId;
	private SalesFlatShipment salesFlatShipment;
	private BigDecimal rowTotal;
	private BigDecimal price;
	private BigDecimal weight;
	private BigDecimal qty;
	private Integer productId;
	private Integer orderItemId;
	private String additionalData;
	private String description;
	private String name;
	private String sku;

	public SalesFlatShipmentItem() {
	}

	public SalesFlatShipmentItem(SalesFlatShipment salesFlatShipment) {
		this.salesFlatShipment = salesFlatShipment;
	}

	public SalesFlatShipmentItem(SalesFlatShipment salesFlatShipment, BigDecimal rowTotal, BigDecimal price, BigDecimal weight, BigDecimal qty, Integer productId, Integer orderItemId, String additionalData, String description, String name, String sku) {
		this.salesFlatShipment = salesFlatShipment;
		this.rowTotal = rowTotal;
		this.price = price;
		this.weight = weight;
		this.qty = qty;
		this.productId = productId;
		this.orderItemId = orderItemId;
		this.additionalData = additionalData;
		this.description = description;
		this.name = name;
		this.sku = sku;
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
	@JoinColumn(name = "parent_id", nullable = false)
	public SalesFlatShipment getSalesFlatShipment() {
		return this.salesFlatShipment;
	}

	public void setSalesFlatShipment(SalesFlatShipment salesFlatShipment) {
		this.salesFlatShipment = salesFlatShipment;
	}

	@Column(name = "row_total", precision = 12, scale = 4)
	public BigDecimal getRowTotal() {
		return this.rowTotal;
	}

	public void setRowTotal(BigDecimal rowTotal) {
		this.rowTotal = rowTotal;
	}

	@Column(name = "price", precision = 12, scale = 4)
	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Column(name = "weight", precision = 12, scale = 4)
	public BigDecimal getWeight() {
		return this.weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	@Column(name = "qty", precision = 12, scale = 4)
	public BigDecimal getQty() {
		return this.qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	@Column(name = "product_id")
	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	@Column(name = "order_item_id")
	public Integer getOrderItemId() {
		return this.orderItemId;
	}

	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
	}

	@Column(name = "additional_data", length = 65535)
	public String getAdditionalData() {
		return this.additionalData;
	}

	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}

	@Column(name = "description", length = 65535)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "sku")
	public String getSku() {
		return this.sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

}
