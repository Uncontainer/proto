package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.naver.mage4j.core.mage.core.model.resource.store.Store;

/**
 * NewsletterQueue generated by hbm2java
 */
@Entity
@Table(name = "newsletter_queue"
	, catalog = "magento")
public class NewsletterQueue implements java.io.Serializable {

	private Integer queueId;
	private NewsletterTemplate newsletterTemplate;
	private Integer newsletterType;
	private String newsletterText;
	private String newsletterStyles;
	private String newsletterSubject;
	private String newsletterSenderName;
	private String newsletterSenderEmail;
	private int queueStatus;
	private Date queueStartAt;
	private Date queueFinishAt;
	private Set<Store> coreStores = new HashSet<Store>(0);
	private Set<NewsletterQueueLink> newsletterQueueLinks = new HashSet<NewsletterQueueLink>(0);
	private Set<NewsletterProblem> newsletterProblems = new HashSet<NewsletterProblem>(0);

	public NewsletterQueue() {
	}

	public NewsletterQueue(NewsletterTemplate newsletterTemplate, int queueStatus) {
		this.newsletterTemplate = newsletterTemplate;
		this.queueStatus = queueStatus;
	}

	public NewsletterQueue(NewsletterTemplate newsletterTemplate, Integer newsletterType, String newsletterText, String newsletterStyles, String newsletterSubject, String newsletterSenderName, String newsletterSenderEmail, int queueStatus, Date queueStartAt, Date queueFinishAt, Set<Store> coreStores, Set<NewsletterQueueLink> newsletterQueueLinks, Set<NewsletterProblem> newsletterProblems) {
		this.newsletterTemplate = newsletterTemplate;
		this.newsletterType = newsletterType;
		this.newsletterText = newsletterText;
		this.newsletterStyles = newsletterStyles;
		this.newsletterSubject = newsletterSubject;
		this.newsletterSenderName = newsletterSenderName;
		this.newsletterSenderEmail = newsletterSenderEmail;
		this.queueStatus = queueStatus;
		this.queueStartAt = queueStartAt;
		this.queueFinishAt = queueFinishAt;
		this.coreStores = coreStores;
		this.newsletterQueueLinks = newsletterQueueLinks;
		this.newsletterProblems = newsletterProblems;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "queue_id", unique = true, nullable = false)
	public Integer getQueueId() {
		return this.queueId;
	}

	public void setQueueId(Integer queueId) {
		this.queueId = queueId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "template_id", nullable = false)
	public NewsletterTemplate getNewsletterTemplate() {
		return this.newsletterTemplate;
	}

	public void setNewsletterTemplate(NewsletterTemplate newsletterTemplate) {
		this.newsletterTemplate = newsletterTemplate;
	}

	@Column(name = "newsletter_type")
	public Integer getNewsletterType() {
		return this.newsletterType;
	}

	public void setNewsletterType(Integer newsletterType) {
		this.newsletterType = newsletterType;
	}

	@Column(name = "newsletter_text", length = 65535, columnDefinition = "TEXT")
	public String getNewsletterText() {
		return this.newsletterText;
	}

	public void setNewsletterText(String newsletterText) {
		this.newsletterText = newsletterText;
	}

	@Column(name = "newsletter_styles", length = 65535, columnDefinition = "TEXT")
	public String getNewsletterStyles() {
		return this.newsletterStyles;
	}

	public void setNewsletterStyles(String newsletterStyles) {
		this.newsletterStyles = newsletterStyles;
	}

	@Column(name = "newsletter_subject", length = 200)
	public String getNewsletterSubject() {
		return this.newsletterSubject;
	}

	public void setNewsletterSubject(String newsletterSubject) {
		this.newsletterSubject = newsletterSubject;
	}

	@Column(name = "newsletter_sender_name", length = 200)
	public String getNewsletterSenderName() {
		return this.newsletterSenderName;
	}

	public void setNewsletterSenderName(String newsletterSenderName) {
		this.newsletterSenderName = newsletterSenderName;
	}

	@Column(name = "newsletter_sender_email", length = 200)
	public String getNewsletterSenderEmail() {
		return this.newsletterSenderEmail;
	}

	public void setNewsletterSenderEmail(String newsletterSenderEmail) {
		this.newsletterSenderEmail = newsletterSenderEmail;
	}

	@Column(name = "queue_status", nullable = false)
	public int getQueueStatus() {
		return this.queueStatus;
	}

	public void setQueueStatus(int queueStatus) {
		this.queueStatus = queueStatus;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "queue_start_at", length = 19)
	public Date getQueueStartAt() {
		return this.queueStartAt;
	}

	public void setQueueStartAt(Date queueStartAt) {
		this.queueStartAt = queueStartAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "queue_finish_at", length = 19)
	public Date getQueueFinishAt() {
		return this.queueFinishAt;
	}

	public void setQueueFinishAt(Date queueFinishAt) {
		this.queueFinishAt = queueFinishAt;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "newsletter_queue_store_link", catalog = "magento", joinColumns = {
		@JoinColumn(name = "queue_id", nullable = false, updatable = false)}, inverseJoinColumns = {
		@JoinColumn(name = "store_id", nullable = false, updatable = false)})
	public Set<Store> getCoreStores() {
		return this.coreStores;
	}

	public void setCoreStores(Set<Store> coreStores) {
		this.coreStores = coreStores;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "newsletterQueue")
	public Set<NewsletterQueueLink> getNewsletterQueueLinks() {
		return this.newsletterQueueLinks;
	}

	public void setNewsletterQueueLinks(Set<NewsletterQueueLink> newsletterQueueLinks) {
		this.newsletterQueueLinks = newsletterQueueLinks;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "newsletterQueue")
	public Set<NewsletterProblem> getNewsletterProblems() {
		return this.newsletterProblems;
	}

	public void setNewsletterProblems(Set<NewsletterProblem> newsletterProblems) {
		this.newsletterProblems = newsletterProblems;
	}

}
