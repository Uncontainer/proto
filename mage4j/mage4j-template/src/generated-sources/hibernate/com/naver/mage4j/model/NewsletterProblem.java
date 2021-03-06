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
 * NewsletterProblem generated by hbm2java
 */
@Entity
@Table(name = "newsletter_problem"
	, catalog = "magento")
public class NewsletterProblem implements java.io.Serializable {

	private Integer problemId;
	private NewsletterQueue newsletterQueue;
	private NewsletterSubscriber newsletterSubscriber;
	private Integer problemErrorCode;
	private String problemErrorText;

	public NewsletterProblem() {
	}

	public NewsletterProblem(NewsletterQueue newsletterQueue) {
		this.newsletterQueue = newsletterQueue;
	}

	public NewsletterProblem(NewsletterQueue newsletterQueue, NewsletterSubscriber newsletterSubscriber, Integer problemErrorCode, String problemErrorText) {
		this.newsletterQueue = newsletterQueue;
		this.newsletterSubscriber = newsletterSubscriber;
		this.problemErrorCode = problemErrorCode;
		this.problemErrorText = problemErrorText;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "problem_id", unique = true, nullable = false)
	public Integer getProblemId() {
		return this.problemId;
	}

	public void setProblemId(Integer problemId) {
		this.problemId = problemId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "queue_id", nullable = false)
	public NewsletterQueue getNewsletterQueue() {
		return this.newsletterQueue;
	}

	public void setNewsletterQueue(NewsletterQueue newsletterQueue) {
		this.newsletterQueue = newsletterQueue;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subscriber_id")
	public NewsletterSubscriber getNewsletterSubscriber() {
		return this.newsletterSubscriber;
	}

	public void setNewsletterSubscriber(NewsletterSubscriber newsletterSubscriber) {
		this.newsletterSubscriber = newsletterSubscriber;
	}

	@Column(name = "problem_error_code")
	public Integer getProblemErrorCode() {
		return this.problemErrorCode;
	}

	public void setProblemErrorCode(Integer problemErrorCode) {
		this.problemErrorCode = problemErrorCode;
	}

	@Column(name = "problem_error_text", length = 200)
	public String getProblemErrorText() {
		return this.problemErrorText;
	}

	public void setProblemErrorText(String problemErrorText) {
		this.problemErrorText = problemErrorText;
	}

}
