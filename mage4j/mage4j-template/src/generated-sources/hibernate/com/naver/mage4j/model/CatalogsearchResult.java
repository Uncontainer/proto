package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * CatalogsearchResult generated by hbm2java
 */
@Entity
@Table(name = "catalogsearch_result"
	, catalog = "magento")
public class CatalogsearchResult implements java.io.Serializable {

	private CatalogsearchResultId id;
	private CatalogsearchQuery catalogsearchQuery;
	private CatalogProductEntity catalogProductEntity;
	private BigDecimal relevance;

	public CatalogsearchResult() {
	}

	public CatalogsearchResult(CatalogsearchResultId id, CatalogsearchQuery catalogsearchQuery, CatalogProductEntity catalogProductEntity, BigDecimal relevance) {
		this.id = id;
		this.catalogsearchQuery = catalogsearchQuery;
		this.catalogProductEntity = catalogProductEntity;
		this.relevance = relevance;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "queryId", column = @Column(name = "query_id", nullable = false)),
		@AttributeOverride(name = "productId", column = @Column(name = "product_id", nullable = false))})
	public CatalogsearchResultId getId() {
		return this.id;
	}

	public void setId(CatalogsearchResultId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "query_id", nullable = false, insertable = false, updatable = false)
	public CatalogsearchQuery getCatalogsearchQuery() {
		return this.catalogsearchQuery;
	}

	public void setCatalogsearchQuery(CatalogsearchQuery catalogsearchQuery) {
		this.catalogsearchQuery = catalogsearchQuery;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
	public CatalogProductEntity getCatalogProductEntity() {
		return this.catalogProductEntity;
	}

	public void setCatalogProductEntity(CatalogProductEntity catalogProductEntity) {
		this.catalogProductEntity = catalogProductEntity;
	}

	@Column(name = "relevance", nullable = false, precision = 20, scale = 4)
	public BigDecimal getRelevance() {
		return this.relevance;
	}

	public void setRelevance(BigDecimal relevance) {
		this.relevance = relevance;
	}

}
