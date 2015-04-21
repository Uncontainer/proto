package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CatalogProductIndexEavTmpId generated by hbm2java
 */
@Embeddable
public class CatalogProductIndexEavTmpId implements java.io.Serializable {

	private int entityId;
	private short attributeId;
	private short storeId;
	private int value;

	public CatalogProductIndexEavTmpId() {
	}

	public CatalogProductIndexEavTmpId(int entityId, short attributeId, short storeId, int value) {
		this.entityId = entityId;
		this.attributeId = attributeId;
		this.storeId = storeId;
		this.value = value;
	}

	@Column(name = "entity_id", nullable = false)
	public int getEntityId() {
		return this.entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	@Column(name = "attribute_id", nullable = false)
	public short getAttributeId() {
		return this.attributeId;
	}

	public void setAttributeId(short attributeId) {
		this.attributeId = attributeId;
	}

	@Column(name = "store_id", nullable = false)
	public short getStoreId() {
		return this.storeId;
	}

	public void setStoreId(short storeId) {
		this.storeId = storeId;
	}

	@Column(name = "value", nullable = false)
	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CatalogProductIndexEavTmpId))
			return false;
		CatalogProductIndexEavTmpId castOther = (CatalogProductIndexEavTmpId)other;

		return (this.getEntityId() == castOther.getEntityId())
			&& (this.getAttributeId() == castOther.getAttributeId())
			&& (this.getStoreId() == castOther.getStoreId())
			&& (this.getValue() == castOther.getValue());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getEntityId();
		result = 37 * result + this.getAttributeId();
		result = 37 * result + this.getStoreId();
		result = 37 * result + this.getValue();
		return result;
	}

}
