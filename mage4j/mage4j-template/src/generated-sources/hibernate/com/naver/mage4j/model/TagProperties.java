package com.naver.mage4j.model;

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

/**
 * TagProperties generated by hbm2java
 */
@Entity
@Table(name = "tag_properties"
	, catalog = "magento")
public class TagProperties implements java.io.Serializable {

	private TagPropertiesId id;
	private Tag tag;
	private CoreStore coreStore;
	private int basePopularity;

	public TagProperties() {
	}

	public TagProperties(TagPropertiesId id, Tag tag, CoreStore coreStore, int basePopularity) {
		this.id = id;
		this.tag = tag;
		this.coreStore = coreStore;
		this.basePopularity = basePopularity;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "tagId", column = @Column(name = "tag_id", nullable = false)),
		@AttributeOverride(name = "storeId", column = @Column(name = "store_id", nullable = false))})
	public TagPropertiesId getId() {
		return this.id;
	}

	public void setId(TagPropertiesId id) {
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
	public CoreStore getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(CoreStore coreStore) {
		this.coreStore = coreStore;
	}

	@Column(name = "base_popularity", nullable = false)
	public int getBasePopularity() {
		return this.basePopularity;
	}

	public void setBasePopularity(int basePopularity) {
		this.basePopularity = basePopularity;
	}

}