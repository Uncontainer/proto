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
 * CatalogProductEntityTierPrice generated by hbm2java
 */
@Entity
@Table(name = "catalog_product_entity_tier_price"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = {"entity_id", "all_groups", "customer_group_id", "qty", "website_id"}))
public class CatalogProductEntityTierPrice implements java.io.Serializable {

	private Integer valueId;
	private CoreWebsite coreWebsite;
	private CatalogProductEntity catalogProductEntity;
	private CustomerGroup customerGroup;
	private short allGroups;
	private BigDecimal qty;
	private BigDecimal value;

	public CatalogProductEntityTierPrice() {
	}

	public CatalogProductEntityTierPrice(CoreWebsite coreWebsite, CatalogProductEntity catalogProductEntity, CustomerGroup customerGroup, short allGroups, BigDecimal qty, BigDecimal value) {
		this.coreWebsite = coreWebsite;
		this.catalogProductEntity = catalogProductEntity;
		this.customerGroup = customerGroup;
		this.allGroups = allGroups;
		this.qty = qty;
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
	@JoinColumn(name = "website_id", nullable = false)
	public CoreWebsite getCoreWebsite() {
		return this.coreWebsite;
	}

	public void setCoreWebsite(CoreWebsite coreWebsite) {
		this.coreWebsite = coreWebsite;
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
	@JoinColumn(name = "customer_group_id", nullable = false)
	public CustomerGroup getCustomerGroup() {
		return this.customerGroup;
	}

	public void setCustomerGroup(CustomerGroup customerGroup) {
		this.customerGroup = customerGroup;
	}

	@Column(name = "all_groups", nullable = false)
	public short getAllGroups() {
		return this.allGroups;
	}

	public void setAllGroups(short allGroups) {
		this.allGroups = allGroups;
	}

	@Column(name = "qty", nullable = false, precision = 12, scale = 4)
	public BigDecimal getQty() {
		return this.qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	@Column(name = "value", nullable = false, precision = 12, scale = 4)
	public BigDecimal getValue() {
		return this.value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

}
