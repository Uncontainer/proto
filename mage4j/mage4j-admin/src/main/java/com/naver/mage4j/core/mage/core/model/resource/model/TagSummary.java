package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.naver.mage4j.core.mage.core.model.resource.store.Store;

/**
 * TagSummary generated by hbm2java
 */
@Entity
@Table(name = "tag_summary"
	, catalog = "magento")
public class TagSummary implements java.io.Serializable {

	private TagSummaryId id;
	private Tag tag;
	private Store coreStore;
	private int customers;
	private int products;
	private int uses;
	private int historicalUses;
	private int popularity;
	private int basePopularity;

	public TagSummary() {
	}

	public TagSummary(TagSummaryId id, Tag tag, Store coreStore, int customers, int products, int uses, int historicalUses, int popularity, int basePopularity) {
		this.id = id;
		this.tag = tag;
		this.coreStore = coreStore;
		this.customers = customers;
		this.products = products;
		this.uses = uses;
		this.historicalUses = historicalUses;
		this.popularity = popularity;
		this.basePopularity = basePopularity;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "tagId", column = @Column(name = "tag_id", nullable = false)),
		@AttributeOverride(name = "storeId", column = @Column(name = "store_id", nullable = false))})
	public TagSummaryId getId() {
		return this.id;
	}

	public void setId(TagSummaryId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tag_id", nullable = false, insertable = false, updatable = false)
	public Tag getTag() {
		return this.tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false, insertable = false, updatable = false)
	public Store getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(Store coreStore) {
		this.coreStore = coreStore;
	}

	@Column(name = "customers", nullable = false)
	public int getCustomers() {
		return this.customers;
	}

	public void setCustomers(int customers) {
		this.customers = customers;
	}

	@Column(name = "products", nullable = false)
	public int getProducts() {
		return this.products;
	}

	public void setProducts(int products) {
		this.products = products;
	}

	@Column(name = "uses", nullable = false)
	public int getUses() {
		return this.uses;
	}

	public void setUses(int uses) {
		this.uses = uses;
	}

	@Column(name = "historical_uses", nullable = false)
	public int getHistoricalUses() {
		return this.historicalUses;
	}

	public void setHistoricalUses(int historicalUses) {
		this.historicalUses = historicalUses;
	}

	@Column(name = "popularity", nullable = false)
	public int getPopularity() {
		return this.popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	@Column(name = "base_popularity", nullable = false)
	public int getBasePopularity() {
		return this.basePopularity;
	}

	public void setBasePopularity(int basePopularity) {
		this.basePopularity = basePopularity;
	}

}