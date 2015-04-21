package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CatalogProductIndexEavIdx generated by hbm2java
 */
@Entity
@Table(name = "catalog_product_index_eav_idx"
	, catalog = "magento")
public class CatalogProductIndexEavIdx implements java.io.Serializable {

	private CatalogProductIndexEavIdxId id;

	public CatalogProductIndexEavIdx() {
	}

	public CatalogProductIndexEavIdx(CatalogProductIndexEavIdxId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "entityId", column = @Column(name = "entity_id", nullable = false)),
		@AttributeOverride(name = "attributeId", column = @Column(name = "attribute_id", nullable = false)),
		@AttributeOverride(name = "storeId", column = @Column(name = "store_id", nullable = false)),
		@AttributeOverride(name = "value", column = @Column(name = "value", nullable = false))})
	public CatalogProductIndexEavIdxId getId() {
		return this.id;
	}

	public void setId(CatalogProductIndexEavIdxId id) {
		this.id = id;
	}

}
