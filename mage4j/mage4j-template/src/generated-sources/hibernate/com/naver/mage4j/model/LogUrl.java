package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * LogUrl generated by hbm2java
 */
@Entity
@Table(name = "log_url"
	, catalog = "magento")
public class LogUrl implements java.io.Serializable {

	private LogUrlId id;

	public LogUrl() {
	}

	public LogUrl(LogUrlId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "urlId", column = @Column(name = "url_id", nullable = false)),
		@AttributeOverride(name = "visitorId", column = @Column(name = "visitor_id")),
		@AttributeOverride(name = "visitTime", column = @Column(name = "visit_time", nullable = false, length = 19))})
	public LogUrlId getId() {
		return this.id;
	}

	public void setId(LogUrlId id) {
		this.id = id;
	}

}
