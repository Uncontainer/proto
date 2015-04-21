package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * RatingEntity generated by hbm2java
 */
@Entity
@Table(name = "rating_entity"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = "entity_code"))
public class RatingEntity implements java.io.Serializable {

	private Short entityId;
	private String entityCode;
	private Set<Rating> ratings = new HashSet<Rating>(0);

	public RatingEntity() {
	}

	public RatingEntity(String entityCode) {
		this.entityCode = entityCode;
	}

	public RatingEntity(String entityCode, Set<Rating> ratings) {
		this.entityCode = entityCode;
		this.ratings = ratings;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "entity_id", unique = true, nullable = false)
	public Short getEntityId() {
		return this.entityId;
	}

	public void setEntityId(Short entityId) {
		this.entityId = entityId;
	}

	@Column(name = "entity_code", unique = true, nullable = false, length = 64)
	public String getEntityCode() {
		return this.entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ratingEntity")
	public Set<Rating> getRatings() {
		return this.ratings;
	}

	public void setRatings(Set<Rating> ratings) {
		this.ratings = ratings;
	}

}
