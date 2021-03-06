package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CatalogProductIndexEavDecimalId generated by hbm2java
 */
@Embeddable
public class CatalogProductIndexEavDecimalId implements java.io.Serializable {

	private int entityId;
	private short attributeId;
	private short storeId;

	public CatalogProductIndexEavDecimalId() {
	}

	public CatalogProductIndexEavDecimalId(int entityId, short attributeId, short storeId) {
		this.entityId = entityId;
		this.attributeId = attributeId;
		this.storeId = storeId;
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

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CatalogProductIndexEavDecimalId))
			return false;
		CatalogProductIndexEavDecimalId castOther = (CatalogProductIndexEavDecimalId)other;

		return (this.getEntityId() == castOther.getEntityId())
			&& (this.getAttributeId() == castOther.getAttributeId())
			&& (this.getStoreId() == castOther.getStoreId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getEntityId();
		result = 37 * result + this.getAttributeId();
		result = 37 * result + this.getStoreId();
		return result;
	}

}
