package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CatalogCategoryProductIndexTmp generated by hbm2java
 */
@Entity
@Table(name = "catalog_category_product_index_tmp"
	, catalog = "magento")
public class CatalogCategoryProductIndexTmp implements java.io.Serializable {

	private CatalogCategoryProductIndexTmpId id;

	public CatalogCategoryProductIndexTmp() {
	}

	public CatalogCategoryProductIndexTmp(CatalogCategoryProductIndexTmpId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "categoryId", column = @Column(name = "category_id", nullable = false)),
		@AttributeOverride(name = "productId", column = @Column(name = "product_id", nullable = false)),
		@AttributeOverride(name = "position", column = @Column(name = "position", nullable = false)),
		@AttributeOverride(name = "isParent", column = @Column(name = "is_parent", nullable = false)),
		@AttributeOverride(name = "storeId", column = @Column(name = "store_id", nullable = false)),
		@AttributeOverride(name = "visibility", column = @Column(name = "visibility", nullable = false))})
	public CatalogCategoryProductIndexTmpId getId() {
		return this.id;
	}

	public void setId(CatalogCategoryProductIndexTmpId id) {
		this.id = id;
	}

}
