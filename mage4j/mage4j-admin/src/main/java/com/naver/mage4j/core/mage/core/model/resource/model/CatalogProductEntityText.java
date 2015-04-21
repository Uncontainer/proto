package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.naver.mage4j.core.mage.core.model.resource.store.Store;

/**
 * CatalogProductEntityText generated by hbm2java
 */
@Entity
@Table(name = "catalog_product_entity_text"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = {"entity_id", "attribute_id", "store_id"}))
public class CatalogProductEntityText implements java.io.Serializable {

	private Integer valueId;
	private CatalogProductEntity catalogProductEntity;
	private EavAttribute eavAttribute;
	private Store coreStore;
	private int entityTypeId;
	private String value;

	public CatalogProductEntityText() {
	}

	public CatalogProductEntityText(CatalogProductEntity catalogProductEntity, EavAttribute eavAttribute, Store coreStore, int entityTypeId) {
		this.catalogProductEntity = catalogProductEntity;
		this.eavAttribute = eavAttribute;
		this.coreStore = coreStore;
		this.entityTypeId = entityTypeId;
	}

	public CatalogProductEntityText(CatalogProductEntity catalogProductEntity, EavAttribute eavAttribute, Store coreStore, int entityTypeId, String value) {
		this.catalogProductEntity = catalogProductEntity;
		this.eavAttribute = eavAttribute;
		this.coreStore = coreStore;
		this.entityTypeId = entityTypeId;
		this.value = value;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "value_id", unique = true, nullable = false)
	public Integer getValueId() {
		return this.valueId;
	}

	public void setValueId(Integer valueId) {
		this.valueId = valueId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_id", nullable = false)
	public CatalogProductEntity getCatalogProductEntity() {
		return this.catalogProductEntity;
	}

	public void setCatalogProductEntity(CatalogProductEntity catalogProductEntity) {
		this.catalogProductEntity = catalogProductEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attribute_id", nullable = false)
	public EavAttribute getEavAttribute() {
		return this.eavAttribute;
	}

	public void setEavAttribute(EavAttribute eavAttribute) {
		this.eavAttribute = eavAttribute;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	public Store getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(Store coreStore) {
		this.coreStore = coreStore;
	}

	@Column(name = "entity_type_id", nullable = false)
	public int getEntityTypeId() {
		return this.entityTypeId;
	}

	public void setEntityTypeId(int entityTypeId) {
		this.entityTypeId = entityTypeId;
	}

	@Column(name = "value", length = 65535, columnDefinition = "TEXT")
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
