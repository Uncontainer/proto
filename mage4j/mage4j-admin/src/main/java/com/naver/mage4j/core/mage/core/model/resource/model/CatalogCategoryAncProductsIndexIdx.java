package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CatalogCategoryAncProductsIndexIdx generated by hbm2java
 */
@Entity
@Table(name = "catalog_category_anc_products_index_idx"
	, catalog = "magento")
public class CatalogCategoryAncProductsIndexIdx implements java.io.Serializable {

	private CatalogCategoryAncProductsIndexIdxId id;

	public CatalogCategoryAncProductsIndexIdx() {
	}

	public CatalogCategoryAncProductsIndexIdx(CatalogCategoryAncProductsIndexIdxId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "categoryId", column = @Column(name = "category_id", nullable = false)),
		@AttributeOverride(name = "productId", column = @Column(name = "product_id", nullable = false)),
		@AttributeOverride(name = "position", column = @Column(name = "position"))})
	public CatalogCategoryAncProductsIndexIdxId getId() {
		return this.id;
	}

	public void setId(CatalogCategoryAncProductsIndexIdxId id) {
		this.id = id;
	}

}
