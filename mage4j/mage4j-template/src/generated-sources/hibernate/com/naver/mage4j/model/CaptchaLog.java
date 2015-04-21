package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CaptchaLog generated by hbm2java
 */
@Entity
@Table(name = "captcha_log"
	, catalog = "magento")
public class CaptchaLog implements java.io.Serializable {

	private CaptchaLogId id;
	private int count;
	private Date updatedAt;

	public CaptchaLog() {
	}

	public CaptchaLog(CaptchaLogId id, int count) {
		this.id = id;
		this.count = count;
	}

	public CaptchaLog(CaptchaLogId id, int count, Date updatedAt) {
		this.id = id;
		this.count = count;
		this.updatedAt = updatedAt;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "type", column = @Column(name = "type", nullable = false, length = 32)),
		@AttributeOverride(name = "value", column = @Column(name = "value", nullable = false, length = 32))})
	public CaptchaLogId getId() {
		return this.id;
	}

	public void setId(CaptchaLogId id) {
		this.id = id;
	}

	@Column(name = "count", nullable = false)
	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", length = 19)
	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

}