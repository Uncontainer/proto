package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.naver.mage4j.core.mage.core.model.resource.website.Website;

/**
 * CatalogProductIndexWebsite generated by hbm2java
 */
@Entity
@Table(name = "catalog_product_index_website"
	, catalog = "magento")
public class CatalogProductIndexWebsite implements java.io.Serializable {

	private short websiteId;
	private Website coreWebsite;
	private Date websiteDate;
	private Float rate;

	public CatalogProductIndexWebsite() {
	}

	public CatalogProductIndexWebsite(Website coreWebsite) {
		this.coreWebsite = coreWebsite;
	}

	public CatalogProductIndexWebsite(Website coreWebsite, Date websiteDate, Float rate) {
		this.coreWebsite = coreWebsite;
		this.websiteDate = websiteDate;
		this.rate = rate;
	}

	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "coreWebsite"))
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "website_id", unique = true, nullable = false)
	public short getWebsiteId() {
		return this.websiteId;
	}

	public void setWebsiteId(short websiteId) {
		this.websiteId = websiteId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Website getCoreWebsite() {
		return this.coreWebsite;
	}

	public void setCoreWebsite(Website coreWebsite) {
		this.coreWebsite = coreWebsite;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "website_date", length = 10)
	public Date getWebsiteDate() {
		return this.websiteDate;
	}

	public void setWebsiteDate(Date websiteDate) {
		this.websiteDate = websiteDate;
	}

	@Column(name = "rate", precision = 12, scale = 0)
	public Float getRate() {
		return this.rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}

}
