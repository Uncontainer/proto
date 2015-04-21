package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * ReviewEntitySummary generated by hbm2java
 */
@Entity
@Table(name = "review_entity_summary"
	, catalog = "magento")
public class ReviewEntitySummary implements java.io.Serializable {

	private Long primaryId;
	private CoreStore coreStore;
	private long entityPkValue;
	private short entityType;
	private short reviewsCount;
	private short ratingSummary;

	public ReviewEntitySummary() {
	}

	public ReviewEntitySummary(CoreStore coreStore, long entityPkValue, short entityType, short reviewsCount, short ratingSummary) {
		this.coreStore = coreStore;
		this.entityPkValue = entityPkValue;
		this.entityType = entityType;
		this.reviewsCount = reviewsCount;
		this.ratingSummary = ratingSummary;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "primary_id", unique = true, nullable = false)
	public Long getPrimaryId() {
		return this.primaryId;
	}

	public void setPrimaryId(Long primaryId) {
		this.primaryId = primaryId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	public CoreStore getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(CoreStore coreStore) {
		this.coreStore = coreStore;
	}

	@Column(name = "entity_pk_value", nullable = false)
	public long getEntityPkValue() {
		return this.entityPkValue;
	}

	public void setEntityPkValue(long entityPkValue) {
		this.entityPkValue = entityPkValue;
	}

	@Column(name = "entity_type", nullable = false)
	public short getEntityType() {
		return this.entityType;
	}

	public void setEntityType(short entityType) {
		this.entityType = entityType;
	}

	@Column(name = "reviews_count", nullable = false)
	public short getReviewsCount() {
		return this.reviewsCount;
	}

	public void setReviewsCount(short reviewsCount) {
		this.reviewsCount = reviewsCount;
	}

	@Column(name = "rating_summary", nullable = false)
	public short getRatingSummary() {
		return this.ratingSummary;
	}

	public void setRatingSummary(short ratingSummary) {
		this.ratingSummary = ratingSummary;
	}

}
