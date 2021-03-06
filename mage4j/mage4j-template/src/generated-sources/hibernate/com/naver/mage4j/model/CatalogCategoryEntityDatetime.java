package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * CatalogCategoryEntityDatetime generated by hbm2java
 */
@Entity
@Table(name = "catalog_category_entity_datetime"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = {"entity_type_id", "entity_id", "attribute_id", "store_id"}))
public class CatalogCategoryEntityDatetime implements java.io.Serializable {

	private Integer valueId;
	private CatalogCategoryEntity catalogCategoryEntity;
	private EavAttribute eavAttribute;
	private CoreStore coreStore;
	private short entityTypeId;
	private Date value;

	public CatalogCategoryEntityDatetime() {
	}

	public CatalogCategoryEntityDatetime(CatalogCategoryEntity catalogCategoryEntity, EavAttribute eavAttribute, CoreStore coreStore, short entityTypeId) {
		this.catalogCategoryEntity = catalogCategoryEntity;
		this.eavAttribute = eavAttribute;
		this.coreStore = coreStore;
		this.entityTypeId = entityTypeId;
	}

	public CatalogCategoryEntityDatetime(CatalogCategoryEntity catalogCategoryEntity, EavAttribute eavAttribute, CoreStore coreStore, short entityTypeId, Date value) {
		this.catalogCategoryEntity = catalogCategoryEntity;
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
	public CatalogCategoryEntity getCatalogCategoryEntity() {
		return this.catalogCategoryEntity;
	}

	public void setCatalogCategoryEntity(CatalogCategoryEntity catalogCategoryEntity) {
		this.catalogCategoryEntity = catalogCategoryEntity;
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
	public CoreStore getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(CoreStore coreStore) {
		this.coreStore = coreStore;
	}

	@Column(name = "entity_type_id", nullable = false)
	public short getEntityTypeId() {
		return this.entityTypeId;
	}

	public void setEntityTypeId(short entityTypeId) {
		this.entityTypeId = entityTypeId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "value", length = 19)
	public Date getValue() {
		return this.value;
	}

	public void setValue(Date value) {
		this.value = value;
	}

}
