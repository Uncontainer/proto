package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CatalogProductIndexPriceBundleOptTmp generated by hbm2java
 */
@Entity
@Table(name = "catalog_product_index_price_bundle_opt_tmp"
	, catalog = "magento")
public class CatalogProductIndexPriceBundleOptTmp implements java.io.Serializable {

	private CatalogProductIndexPriceBundleOptTmpId id;
	private BigDecimal minPrice;
	private BigDecimal altPrice;
	private BigDecimal maxPrice;
	private BigDecimal tierPrice;
	private BigDecimal altTierPrice;
	private BigDecimal groupPrice;
	private BigDecimal altGroupPrice;

	public CatalogProductIndexPriceBundleOptTmp() {
	}

	public CatalogProductIndexPriceBundleOptTmp(CatalogProductIndexPriceBundleOptTmpId id) {
		this.id = id;
	}

	public CatalogProductIndexPriceBundleOptTmp(CatalogProductIndexPriceBundleOptTmpId id, BigDecimal minPrice, BigDecimal altPrice, BigDecimal maxPrice, BigDecimal tierPrice, BigDecimal altTierPrice, BigDecimal groupPrice, BigDecimal altGroupPrice) {
		this.id = id;
		this.minPrice = minPrice;
		this.altPrice = altPrice;
		this.maxPrice = maxPrice;
		this.tierPrice = tierPrice;
		this.altTierPrice = altTierPrice;
		this.groupPrice = groupPrice;
		this.altGroupPrice = altGroupPrice;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "entityId", column = @Column(name = "entity_id", nullable = false)),
		@AttributeOverride(name = "customerGroupId", column = @Column(name = "customer_group_id", nullable = false)),
		@AttributeOverride(name = "websiteId", column = @Column(name = "website_id", nullable = false)),
		@AttributeOverride(name = "optionId", column = @Column(name = "option_id", nullable = false))})
	public CatalogProductIndexPriceBundleOptTmpId getId() {
		return this.id;
	}

	public void setId(CatalogProductIndexPriceBundleOptTmpId id) {
		this.id = id;
	}

	@Column(name = "min_price", precision = 12, scale = 4)
	public BigDecimal getMinPrice() {
		return this.minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	@Column(name = "alt_price", precision = 12, scale = 4)
	public BigDecimal getAltPrice() {
		return this.altPrice;
	}

	public void setAltPrice(BigDecimal altPrice) {
		this.altPrice = altPrice;
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

	@Column(name = "alt_tier_price", precision = 12, scale = 4)
	public BigDecimal getAltTierPrice() {
		return this.altTierPrice;
	}

	public void setAltTierPrice(BigDecimal altTierPrice) {
		this.altTierPrice = altTierPrice;
	}

	@Column(name = "group_price", precision = 12, scale = 4)
	public BigDecimal getGroupPrice() {
		return this.groupPrice;
	}

	public void setGroupPrice(BigDecimal groupPrice) {
		this.groupPrice = groupPrice;
	}

	@Column(name = "alt_group_price", precision = 12, scale = 4)
	public BigDecimal getAltGroupPrice() {
		return this.altGroupPrice;
	}

	public void setAltGroupPrice(BigDecimal altGroupPrice) {
		this.altGroupPrice = altGroupPrice;
	}

}
