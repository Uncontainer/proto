package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CatalogProductIndexPriceOptAgrTmp generated by hbm2java
 */
@Entity
@Table(name = "catalog_product_index_price_opt_agr_tmp"
	, catalog = "magento")
public class CatalogProductIndexPriceOptAgrTmp implements java.io.Serializable {

	private CatalogProductIndexPriceOptAgrTmpId id;
	private BigDecimal minPrice;
	private BigDecimal maxPrice;
	private BigDecimal tierPrice;
	private BigDecimal groupPrice;

	public CatalogProductIndexPriceOptAgrTmp() {
	}

	public CatalogProductIndexPriceOptAgrTmp(CatalogProductIndexPriceOptAgrTmpId id) {
		this.id = id;
	}

	public CatalogProductIndexPriceOptAgrTmp(CatalogProductIndexPriceOptAgrTmpId id, BigDecimal minPrice, BigDecimal maxPrice, BigDecimal tierPrice, BigDecimal groupPrice) {
		this.id = id;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.tierPrice = tierPrice;
		this.groupPrice = groupPrice;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "entityId", column = @Column(name = "entity_id", nullable = false)),
		@AttributeOverride(name = "customerGroupId", column = @Column(name = "customer_group_id", nullable = false)),
		@AttributeOverride(name = "websiteId", column = @Column(name = "website_id", nullable = false)),
		@AttributeOverride(name = "optionId", column = @Column(name = "option_id", nullable = false))})
	public CatalogProductIndexPriceOptAgrTmpId getId() {
		return this.id;
	}

	public void setId(CatalogProductIndexPriceOptAgrTmpId id) {
		this.id = id;
	}

	@Column(name = "min_price", precision = 12, scale = 4)
	public BigDecimal getMinPrice() {
		return this.minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	@Column(name = "max_price", precision = 12, scale = 4)
	public BigDecimal getMaxPrice() {
		return this.maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	@Column(name = "tier_price", precision = 12, scale = 4)
	public BigDecimal getTierPrice() {
		return this.tierPrice;
	}

	public void setTierPrice(BigDecimal tierPrice) {
		this.tierPrice = tierPrice;
	}

	@Column(name = "group_price", precision = 12, scale = 4)
	public BigDecimal getGroupPrice() {
		return this.groupPrice;
	}

	public void setGroupPrice(BigDecimal groupPrice) {
		this.groupPrice = groupPrice;
	}

}
