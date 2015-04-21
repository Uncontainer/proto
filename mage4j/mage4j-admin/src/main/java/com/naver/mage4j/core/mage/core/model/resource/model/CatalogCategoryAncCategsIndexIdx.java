package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CatalogCategoryAncCategsIndexIdx generated by hbm2java
 */
@Entity
@Table(name = "catalog_category_anc_categs_index_idx"
	, catalog = "magento")
public class CatalogCategoryAncCategsIndexIdx implements java.io.Serializable {

	private CatalogCategoryAncCategsIndexIdxId id;

	public CatalogCategoryAncCategsIndexIdx() {
	}

	public CatalogCategoryAncCategsIndexIdx(CatalogCategoryAncCategsIndexIdxId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "categoryId", column = @Column(name = "category_id", nullable = false)),
		@AttributeOverride(name = "path", column = @Column(name = "path"))})
	public CatalogCategoryAncCategsIndexIdxId getId() {
		return this.id;
	}

	public void setId(CatalogCategoryAncCategsIndexIdxId id) {
		this.id = id;
	}

}