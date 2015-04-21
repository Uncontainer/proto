package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
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

/**
 * EavEntityDecimal generated by hbm2java
 */
@Entity
@Table(name = "eav_entity_decimal"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = {"entity_id", "attribute_id", "store_id"}))
public class EavEntityDecimal implements java.io.Serializable {

	private Integer valueId;
	private EavEntity eavEntity;
	private EavEntityType eavEntityType;
	private CoreStore coreStore;
	private short attributeId;
	private BigDecimal value;

	public EavEntityDecimal() {
	}

	public EavEntityDecimal(EavEntity eavEntity, EavEntityType eavEntityType, CoreStore coreStore, short attributeId, BigDecimal value) {
		this.eavEntity = eavEntity;
		this.eavEntityType = eavEntityType;
		this.coreStore = coreStore;
		this.attributeId = attributeId;
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
	public EavEntity getEavEntity() {
		return this.eavEntity;
	}

	public void setEavEntity(EavEntity eavEntity) {
		this.eavEntity = eavEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_type_id", nullable = false)
	public EavEntityType getEavEntityType() {
		return this.eavEntityType;
	}

	public void setEavEntityType(EavEntityType eavEntityType) {
		this.eavEntityType = eavEntityType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	public CoreStore getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(CoreStore coreStore) {
		this.coreStore = coreStore;
	}

	@Column(name = "attribute_id", nullable = false)
	public short getAttributeId() {
		return this.attributeId;
	}

	public void setAttributeId(short attributeId) {
		this.attributeId = attributeId;
	}

	@Column(name = "value", nullable = false, precision = 12, scale = 4)
	public BigDecimal getValue() {
		return this.value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

}