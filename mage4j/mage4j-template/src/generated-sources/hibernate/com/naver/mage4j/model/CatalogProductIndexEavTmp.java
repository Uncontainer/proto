package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CatalogProductIndexEavTmp generated by hbm2java
 */
@Entity
@Table(name = "catalog_product_index_eav_tmp"
	, catalog = "magento")
public class CatalogProductIndexEavTmp implements java.io.Serializable {

	private CatalogProductIndexEavTmpId id;

	public CatalogProductIndexEavTmp() {
	}

	public CatalogProductIndexEavTmp(CatalogProductIndexEavTmpId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "entityId", column = @Column(name = "entity_id", nullable = false)),
		@AttributeOverride(name = "attributeId", column = @Column(name = "attribute_id", nullable = false)),
		@AttributeOverride(name = "storeId", column = @Column(name = "store_id", nullable = false)),
		@AttributeOverride(name = "value", column = @Column(name = "value", nullable = false))})
	public CatalogProductIndexEavTmpId getId() {
		return this.id;
	}

	public void setId(CatalogProductIndexEavTmpId id) {
		this.id = id;
	}

}
