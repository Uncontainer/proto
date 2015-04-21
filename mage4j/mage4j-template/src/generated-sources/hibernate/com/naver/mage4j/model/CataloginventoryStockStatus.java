package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
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
 * CataloginventoryStockStatus generated by hbm2java
 */
@Entity
@Table(name = "cataloginventory_stock_status"
	, catalog = "magento")
public class CataloginventoryStockStatus implements java.io.Serializable {

	private CataloginventoryStockStatusId id;
	private CoreWebsite coreWebsite;
	private CataloginventoryStock cataloginventoryStock;
	private CatalogProductEntity catalogProductEntity;
	private BigDecimal qty;
	private short stockStatus;

	public CataloginventoryStockStatus() {
	}

	public CataloginventoryStockStatus(CataloginventoryStockStatusId id, CoreWebsite coreWebsite, CataloginventoryStock cataloginventoryStock, CatalogProductEntity catalogProductEntity, BigDecimal qty, short stockStatus) {
		this.id = id;
		this.coreWebsite = coreWebsite;
		this.cataloginventoryStock = cataloginventoryStock;
		this.catalogProductEntity = catalogProductEntity;
		this.qty = qty;
		this.stockStatus = stockStatus;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "productId", column = @Column(name = "product_id", nullable = false)),
		@AttributeOverride(name = "websiteId", column = @Column(name = "website_id", nullable = false)),
		@AttributeOverride(name = "stockId", column = @Column(name = "stock_id", nullable = false))})
	public CataloginventoryStockStatusId getId() {
		return this.id;
	}

	public void setId(CataloginventoryStockStatusId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "website_id", nullable = false, insertable = false, updatable = false)
	public CoreWebsite getCoreWebsite() {
		return this.coreWebsite;
	}

	public void setCoreWebsite(CoreWebsite coreWebsite) {
		this.coreWebsite = coreWebsite;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_id", nullable = false, insertable = false, updatable = false)
	public CataloginventoryStock getCataloginventoryStock() {
		return this.cataloginventoryStock;
	}

	public void setCataloginventoryStock(CataloginventoryStock cataloginventoryStock) {
		this.cataloginventoryStock = cataloginventoryStock;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
	public CatalogProductEntity getCatalogProductEntity() {
		return this.catalogProductEntity;
	}

	public void setCatalogProductEntity(CatalogProductEntity catalogProductEntity) {
		this.catalogProductEntity = catalogProductEntity;
	}

	@Column(name = "qty", nullable = false, precision = 12, scale = 4)
	public BigDecimal getQty() {
		return this.qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	@Column(name = "stock_status", nullable = false)
	public short getStockStatus() {
		return this.stockStatus;
	}

	public void setStockStatus(short stockStatus) {
		this.stockStatus = stockStatus;
	}

}
