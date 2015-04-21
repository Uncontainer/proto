package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CatalogProductIndexEavDecimalIdx generated by hbm2java
 */
@Entity
@Table(name = "catalog_product_index_eav_decimal_idx"
	, catalog = "magento")
public class CatalogProductIndexEavDecimalIdx implements java.io.Serializable {

	private CatalogProductIndexEavDecimalIdxId id;

	public CatalogProductIndexEavDecimalIdx() {
	}

	public CatalogProductIndexEavDecimalIdx(CatalogProductIndexEavDecimalIdxId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "entityId", column = @Column(name = "entity_id", nullable = false)),
		@AttributeOverride(name = "attributeId", column = @Column(name = "attribute_id", nullable = false)),
		@AttributeOverride(name = "storeId", column = @Column(name = "store_id", nullable = false)),
		@AttributeOverride(name = "value", column = @Column(name = "value", nullable = false, precision = 12, scale = 4))})
	public CatalogProductIndexEavDecimalIdxId getId() {
		return this.id;
	}

	public void setId(CatalogProductIndexEavDecimalIdxId id) {
		this.id = id;
	}

}
