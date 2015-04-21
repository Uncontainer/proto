package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CatalogProductIndexEavDecimalTmp generated by hbm2java
 */
@Entity
@Table(name = "catalog_product_index_eav_decimal_tmp"
	, catalog = "magento")
public class CatalogProductIndexEavDecimalTmp implements java.io.Serializable {

	private CatalogProductIndexEavDecimalTmpId id;
	private BigDecimal value;

	public CatalogProductIndexEavDecimalTmp() {
	}

	public CatalogProductIndexEavDecimalTmp(CatalogProductIndexEavDecimalTmpId id, BigDecimal value) {
		this.id = id;
		this.value = value;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "entityId", column = @Column(name = "entity_id", nullable = false)),
		@AttributeOverride(name = "attributeId", column = @Column(name = "attribute_id", nullable = false)),
		@AttributeOverride(name = "storeId", column = @Column(name = "store_id", nullable = false))})
	public CatalogProductIndexEavDecimalTmpId getId() {
		return this.id;
	}

	public void setId(CatalogProductIndexEavDecimalTmpId id) {
		this.id = id;
	}

	@Column(name = "value", nullable = false, precision = 12, scale = 4)
	public BigDecimal getValue() {
		return this.value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

}
