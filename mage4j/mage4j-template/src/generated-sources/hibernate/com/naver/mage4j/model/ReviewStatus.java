package com.naver.mage4j.model;

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

/**
 * ReviewStatus generated by hbm2java
 */
@Entity
@Table(name = "review_status"
	, catalog = "magento")
public class ReviewStatus implements java.io.Serializable {

	private Short statusId;
	private String statusCode;
	private Set<Review> reviews = new HashSet<Review>(0);

	public ReviewStatus() {
	}

	public ReviewStatus(String statusCode) {
		this.statusCode = statusCode;
	}

	public ReviewStatus(String statusCode, Set<Review> reviews) {
		this.statusCode = statusCode;
		this.reviews = reviews;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "status_id", unique = true, nullable = false)
	public Short getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Short statusId) {
		this.statusId = statusId;
	}

	@Column(name = "status_code", nullable = false, length = 32)
	public String getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "reviewStatus")
	public Set<Review> getReviews() {
		return this.reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

}
