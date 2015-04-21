package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SalesruleProductAttributeId generated by hbm2java
 */
@Embeddable
public class SalesruleProductAttributeId implements java.io.Serializable {

	private int ruleId;
	private short websiteId;
	private short customerGroupId;
	private short attributeId;

	public SalesruleProductAttributeId() {
	}

	public SalesruleProductAttributeId(int ruleId, short websiteId, short customerGroupId, short attributeId) {
		this.ruleId = ruleId;
		this.websiteId = websiteId;
		this.customerGroupId = customerGroupId;
		this.attributeId = attributeId;
	}

	@Column(name = "rule_id", nullable = false)
	public int getRuleId() {
		return this.ruleId;
	}

	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}

	@Column(name = "website_id", nullable = false)
	public short getWebsiteId() {
		return this.websiteId;
	}

	public void setWebsiteId(short websiteId) {
		this.websiteId = websiteId;
	}

	@Column(name = "customer_group_id", nullable = false)
	public short getCustomerGroupId() {
		return this.customerGroupId;
	}

	public void setCustomerGroupId(short customerGroupId) {
		this.customerGroupId = customerGroupId;
	}

	@Column(name = "attribute_id", nullable = false)
	public short getAttributeId() {
		return this.attributeId;
	}

	public void setAttributeId(short attributeId) {
		this.attributeId = attributeId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SalesruleProductAttributeId))
			return false;
		SalesruleProductAttributeId castOther = (SalesruleProductAttributeId)other;

		return (this.getRuleId() == castOther.getRuleId())
			&& (this.getWebsiteId() == castOther.getWebsiteId())
			&& (this.getCustomerGroupId() == castOther.getCustomerGroupId())
			&& (this.getAttributeId() == castOther.getAttributeId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getRuleId();
		result = 37 * result + this.getWebsiteId();
		result = 37 * result + this.getCustomerGroupId();
		result = 37 * result + this.getAttributeId();
		return result;
	}

}
