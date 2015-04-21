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
 * RatingOptionVote generated by hbm2java
 */
@Entity
@Table(name = "rating_option_vote"
	, catalog = "magento")
public class RatingOptionVote implements java.io.Serializable {

	private Long voteId;
	private Review review;
	private RatingOption ratingOption;
	private String remoteIp;
	private long remoteIpLong;
	private Integer customerId;
	private long entityPkValue;
	private short ratingId;
	private short percent;
	private short value;

	public RatingOptionVote() {
	}

	public RatingOptionVote(RatingOption ratingOption, String remoteIp, long remoteIpLong, long entityPkValue, short ratingId, short percent, short value) {
		this.ratingOption = ratingOption;
		this.remoteIp = remoteIp;
		this.remoteIpLong = remoteIpLong;
		this.entityPkValue = entityPkValue;
		this.ratingId = ratingId;
		this.percent = percent;
		this.value = value;
	}

	public RatingOptionVote(Review review, RatingOption ratingOption, String remoteIp, long remoteIpLong, Integer customerId, long entityPkValue, short ratingId, short percent, short value) {
		this.review = review;
		this.ratingOption = ratingOption;
		this.remoteIp = remoteIp;
		this.remoteIpLong = remoteIpLong;
		this.customerId = customerId;
		this.entityPkValue = entityPkValue;
		this.ratingId = ratingId;
		this.percent = percent;
		this.value = value;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "vote_id", unique = true, nullable = false)
	public Long getVoteId() {
		return this.voteId;
	}

	public void setVoteId(Long voteId) {
		this.voteId = voteId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id")
	public Review getReview() {
		return this.review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "option_id", nullable = false)
	public RatingOption getRatingOption() {
		return this.ratingOption;
	}

	public void setRatingOption(RatingOption ratingOption) {
		this.ratingOption = ratingOption;
	}

	@Column(name = "remote_ip", nullable = false, length = 16)
	public String getRemoteIp() {
		return this.remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	@Column(name = "remote_ip_long", nullable = false)
	public long getRemoteIpLong() {
		return this.remoteIpLong;
	}

	public void setRemoteIpLong(long remoteIpLong) {
		this.remoteIpLong = remoteIpLong;
	}

	@Column(name = "customer_id")
	public Integer getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	@Column(name = "entity_pk_value", nullable = false)
	public long getEntityPkValue() {
		return this.entityPkValue;
	}

	public void setEntityPkValue(long entityPkValue) {
		this.entityPkValue = entityPkValue;
	}

	@Column(name = "rating_id", nullable = false)
	public short getRatingId() {
		return this.ratingId;
	}

	public void setRatingId(short ratingId) {
		this.ratingId = ratingId;
	}

	@Column(name = "percent", nullable = false)
	public short getPercent() {
		return this.percent;
	}

	public void setPercent(short percent) {
		this.percent = percent;
	}

	@Column(name = "value", nullable = false)
	public short getValue() {
		return this.value;
	}

	public void setValue(short value) {
		this.value = value;
	}

}
