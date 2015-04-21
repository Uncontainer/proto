package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CatalogCategoryProductIndexEnblTmp generated by hbm2java
 */
@Entity
@Table(name = "catalog_category_product_index_enbl_tmp"
	, catalog = "magento")
public class CatalogCategoryProductIndexEnblTmp implements java.io.Serializable {

	private CatalogCategoryProductIndexEnblTmpId id;

	public CatalogCategoryProductIndexEnblTmp() {
	}

	public CatalogCategoryProductIndexEnblTmp(CatalogCategoryProductIndexEnblTmpId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "productId", column = @Column(name = "product_id", nullable = false)),
		@AttributeOverride(name = "visibility", column = @Column(name = "visibility", nullable = false))})
	public CatalogCategoryProductIndexEnblTmpId getId() {
		return this.id;
	}

	public void setId(CatalogCategoryProductIndexEnblTmpId id) {
		this.id = id;
	}

}
