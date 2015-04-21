package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
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
import javax.persistence.UniqueConstraint;

/**
 * ReportViewedProductAggregatedMonthly generated by hbm2java
 */
@Entity
@Table(name = "report_viewed_product_aggregated_monthly"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = {"period", "store_id", "product_id"}))
public class ReportViewedProductAggregatedMonthly implements java.io.Serializable {

	private Integer id;
	private CoreStore coreStore;
	private CatalogProductEntity catalogProductEntity;
	private Date period;
	private String productName;
	private BigDecimal productPrice;
	private int viewsNum;
	private short ratingPos;

	public ReportViewedProductAggregatedMonthly() {
	}

	public ReportViewedProductAggregatedMonthly(BigDecimal productPrice, int viewsNum, short ratingPos) {
		this.productPrice = productPrice;
		this.viewsNum = viewsNum;
		this.ratingPos = ratingPos;
	}

	public ReportViewedProductAggregatedMonthly(CoreStore coreStore, CatalogProductEntity catalogProductEntity, Date period, String productName, BigDecimal productPrice, int viewsNum, short ratingPos) {
		this.coreStore = coreStore;
		this.catalogProductEntity = catalogProductEntity;
		this.period = period;
		this.productName = productName;
		this.productPrice = productPrice;
		this.viewsNum = viewsNum;
		this.ratingPos = ratingPos;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	public CoreStore getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(CoreStore coreStore) {
		this.coreStore = coreStore;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	public CatalogProductEntity getCatalogProductEntity() {
		return this.catalogProductEntity;
	}

	public void setCatalogProductEntity(CatalogProductEntity catalogProductEntity) {
		this.catalogProductEntity = catalogProductEntity;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "period", length = 10)
	public Date getPeriod() {
		return this.period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}

	@Column(name = "product_name")
	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "product_price", nullable = false, precision = 12, scale = 4)
	public BigDecimal getProductPrice() {
		return this.productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	@Column(name = "views_num", nullable = false)
	public int getViewsNum() {
		return this.viewsNum;
	}

	public void setViewsNum(int viewsNum) {
		this.viewsNum = viewsNum;
	}

	@Column(name = "rating_pos", nullable = false)
	public short getRatingPos() {
		return this.ratingPos;
	}

	public void setRatingPos(short ratingPos) {
		this.ratingPos = ratingPos;
	}

}
