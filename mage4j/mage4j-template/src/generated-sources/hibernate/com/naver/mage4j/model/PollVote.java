package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PollVote generated by hbm2java
 */
@Entity
@Table(name = "poll_vote"
	, catalog = "magento")
public class PollVote implements java.io.Serializable {

	private Integer voteId;
	private PollAnswer pollAnswer;
	private int pollId;
	private Long ipAddress;
	private Integer customerId;
	private Date voteTime;

	public PollVote() {
	}

	public PollVote(PollAnswer pollAnswer, int pollId) {
		this.pollAnswer = pollAnswer;
		this.pollId = pollId;
	}

	public PollVote(PollAnswer pollAnswer, int pollId, Long ipAddress, Integer customerId, Date voteTime) {
		this.pollAnswer = pollAnswer;
		this.pollId = pollId;
		this.ipAddress = ipAddress;
		this.customerId = customerId;
		this.voteTime = voteTime;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "vote_id", unique = true, nullable = false)
	public Integer getVoteId() {
		return this.voteId;
	}

	public void setVoteId(Integer voteId) {
		this.voteId = voteId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poll_answer_id", nullable = false)
	public PollAnswer getPollAnswer() {
		return this.pollAnswer;
	}

	public void setPollAnswer(PollAnswer pollAnswer) {
		this.pollAnswer = pollAnswer;
	}

	@Column(name = "poll_id", nullable = false)
	public int getPollId() {
		return this.pollId;
	}

	public void setPollId(int pollId) {
		this.pollId = pollId;
	}

	@Column(name = "ip_address")
	public Long getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(Long ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Column(name = "customer_id")
	public Integer getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "vote_time", length = 19)
	public Date getVoteTime() {
		return this.voteTime;
	}

	public void setVoteTime(Date voteTime) {
		this.voteTime = voteTime;
	}

}
