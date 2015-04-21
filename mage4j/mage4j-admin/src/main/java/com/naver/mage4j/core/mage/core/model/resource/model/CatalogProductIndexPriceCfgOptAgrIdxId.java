package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CatalogProductIndexPriceCfgOptAgrIdxId generated by hbm2java
 */
@Embeddable
public class CatalogProductIndexPriceCfgOptAgrIdxId implements java.io.Serializable {

	private int parentId;
	private int childId;
	private short customerGroupId;
	private short websiteId;

	public CatalogProductIndexPriceCfgOptAgrIdxId() {
	}

	public CatalogProductIndexPriceCfgOptAgrIdxId(int parentId, int childId, short customerGroupId, short websiteId) {
		this.parentId = parentId;
		this.childId = childId;
		this.customerGroupId = customerGroupId;
		this.websiteId = websiteId;
	}

	@Column(name = "parent_id", nullable = false)
	public int getParentId() {
		return this.parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	@Column(name = "child_id", nullable = false)
	public int getChildId() {
		return this.childId;
	}

	public void setChildId(int childId) {
		this.childId = childId;
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
		if (!(other instanceof CatalogProductIndexPriceCfgOptAgrIdxId))
			return false;
		CatalogProductIndexPriceCfgOptAgrIdxId castOther = (CatalogProductIndexPriceCfgOptAgrIdxId)other;

		return (this.getParentId() == castOther.getParentId())
			&& (this.getChildId() == castOther.getChildId())
			&& (this.getCustomerGroupId() == castOther.getCustomerGroupId())
			&& (this.getWebsiteId() == castOther.getWebsiteId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getParentId();
		result = 37 * result + this.getChildId();
		result = 37 * result + this.getCustomerGroupId();
		result = 37 * result + this.getWebsiteId();
		return result;
	}

}
