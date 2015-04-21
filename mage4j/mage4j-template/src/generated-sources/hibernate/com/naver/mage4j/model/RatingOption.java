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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * RatingOption generated by hbm2java
 */
@Entity
@Table(name = "rating_option"
	, catalog = "magento")
public class RatingOption implements java.io.Serializable {

	private Integer optionId;
	private Rating rating;
	private String code;
	private short value;
	private short position;
	private Set<RatingOptionVote> ratingOptionVotes = new HashSet<RatingOptionVote>(0);

	public RatingOption() {
	}

	public RatingOption(Rating rating, String code, short value, short position) {
		this.rating = rating;
		this.code = code;
		this.value = value;
		this.position = position;
	}

	public RatingOption(Rating rating, String code, short value, short position, Set<RatingOptionVote> ratingOptionVotes) {
		this.rating = rating;
		this.code = code;
		this.value = value;
		this.position = position;
		this.ratingOptionVotes = ratingOptionVotes;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "option_id", unique = true, nullable = false)
	public Integer getOptionId() {
		return this.optionId;
	}

	public void setOptionId(Integer optionId) {
		this.optionId = optionId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rating_id", nullable = false)
	public Rating getRating() {
		return this.rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	@Column(name = "code", nullable = false, length = 32)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "value", nullable = false)
	public short getValue() {
		return this.value;
	}

	public void setValue(short value) {
		this.value = value;
	}

	@Column(name = "position", nullable = false)
	public short getPosition() {
		return this.position;
	}

	public void setPosition(short position) {
		this.position = position;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ratingOption")
	public Set<RatingOptionVote> getRatingOptionVotes() {
		return this.ratingOptionVotes;
	}

	public void setRatingOptionVotes(Set<RatingOptionVote> ratingOptionVotes) {
		this.ratingOptionVotes = ratingOptionVotes;
	}

}
