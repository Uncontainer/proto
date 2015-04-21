package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CatalogProductIndexPriceOptIdxId generated by hbm2java
 */
@Embeddable
public class CatalogProductIndexPriceOptIdxId implements java.io.Serializable {

	private int entityId;
	private short customerGroupId;
	private short websiteId;

	public CatalogProductIndexPriceOptIdxId() {
	}

	public CatalogProductIndexPriceOptIdxId(int entityId, short customerGroupId, short websiteId) {
		this.entityId = entityId;
		this.customerGroupId = customerGroupId;
		this.websiteId = websiteId;
	}

	@Column(name = "entity_id", nullable = false)
	public int getEntityId() {
		return this.entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	@Column(name = "customer_group_id", nullable = false)
	public short getCustomerGroupId() {
		return this.customerGroupId;
	}

	public void setCustomerGroupId(short customerGroupId) {
		this.customerGroupId = customerGroupId;
	}

	@Column(name = "website_id", nullable = false)
	public short getWebsiteId() {
		return this.websiteId;
	}

	public void setWebsiteId(short websiteId) {
		this.websiteId = websiteId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CatalogProductIndexPriceOptIdxId))
			return false;
		CatalogProductIndexPriceOptIdxId castOther = (CatalogProductIndexPriceOptIdxId)other;

		return (this.getEntityId() == castOther.getEntityId())
			&& (this.getCustomerGroupId() == castOther.getCustomerGroupId())
			&& (this.getWebsiteId() == castOther.getWebsiteId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getEntityId();
		result = 37 * result + this.getCustomerGroupId();
		result = 37 * result + this.getWebsiteId();
		return result;
	}

}
