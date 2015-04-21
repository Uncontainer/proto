package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CatalogProductIndexPriceBundleSelTmpId generated by hbm2java
 */
@Embeddable
public class CatalogProductIndexPriceBundleSelTmpId implements java.io.Serializable {

	private int entityId;
	private short customerGroupId;
	private short websiteId;
	private int optionId;
	private int selectionId;

	public CatalogProductIndexPriceBundleSelTmpId() {
	}

	public CatalogProductIndexPriceBundleSelTmpId(int entityId, short customerGroupId, short websiteId, int optionId, int selectionId) {
		this.entityId = entityId;
		this.customerGroupId = customerGroupId;
		this.websiteId = websiteId;
		this.optionId = optionId;
		this.selectionId = selectionId;
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

	@Column(name = "option_id", nullable = false)
	public int getOptionId() {
		return this.optionId;
	}

	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}

	@Column(name = "selection_id", nullable = false)
	public int getSelectionId() {
		return this.selectionId;
	}

	public void setSelectionId(int selectionId) {
		this.selectionId = selectionId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CatalogProductIndexPriceBundleSelTmpId))
			return false;
		CatalogProductIndexPriceBundleSelTmpId castOther = (CatalogProductIndexPriceBundleSelTmpId)other;

		return (this.getEntityId() == castOther.getEntityId())
			&& (this.getCustomerGroupId() == castOther.getCustomerGroupId())
			&& (this.getWebsiteId() == castOther.getWebsiteId())
			&& (this.getOptionId() == castOther.getOptionId())
			&& (this.getSelectionId() == castOther.getSelectionId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getEntityId();
		result = 37 * result + this.getCustomerGroupId();
		result = 37 * result + this.getWebsiteId();
		result = 37 * result + this.getOptionId();
		result = 37 * result + this.getSelectionId();
		return result;
	}

}
