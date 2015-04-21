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
 * CatalogCategoryProduct generated by hbm2java
 */
@Entity
@Table(name = "catalog_category_product"
	, catalog = "magento")
public class CatalogCategoryProduct implements java.io.Serializable {

	private CatalogCategoryProductId id;
	private CatalogCategoryEntity catalogCategoryEntity;
	private CatalogProductEntity catalogProductEntity;
	private int position;

	public CatalogCategoryProduct() {
	}

	public CatalogCategoryProduct(CatalogCategoryProductId id, CatalogCategoryEntity catalogCategoryEntity, CatalogProductEntity catalogProductEntity, int position) {
		this.id = id;
		this.catalogCategoryEntity = catalogCategoryEntity;
		this.catalogProductEntity = catalogProductEntity;
		this.position = position;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "categoryId", column = @Column(name = "category_id", nullable = false)),
		@AttributeOverride(name = "productId", column = @Column(name = "product_id", nullable = false))})
	public CatalogCategoryProductId getId() {
		return this.id;
	}

	public void setId(CatalogCategoryProductId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false, insertable = false, updatable = false)
	public CatalogCategoryEntity getCatalogCategoryEntity() {
		return this.catalogCategoryEntity;
	}

	public void setCatalogCategoryEntity(CatalogCategoryEntity catalogCategoryEntity) {
		this.catalogCategoryEntity = catalogCategoryEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
	public CatalogProductEntity getCatalogProductEntity() {
		return this.catalogProductEntity;
	}

	public void setCatalogProductEntity(CatalogProductEntity catalogProductEntity) {
		this.catalogProductEntity = catalogProductEntity;
	}

	@Column(name = "position", nullable = false)
	public int getPosition() {
		return this.position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}