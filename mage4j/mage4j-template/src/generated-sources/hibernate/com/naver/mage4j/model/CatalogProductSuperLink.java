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
import javax.persistence.UniqueConstraint;

/**
 * CatalogProductSuperLink generated by hbm2java
 */
@Entity
@Table(name = "catalog_product_super_link"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "parent_id"}))
public class CatalogProductSuperLink implements java.io.Serializable {

	private Integer linkId;
	private CatalogProductEntity catalogProductEntityByParentId;
	private CatalogProductEntity catalogProductEntityByProductId;

	public CatalogProductSuperLink() {
	}

	public CatalogProductSuperLink(CatalogProductEntity catalogProductEntityByParentId, CatalogProductEntity catalogProductEntityByProductId) {
		this.catalogProductEntityByParentId = catalogProductEntityByParentId;
		this.catalogProductEntityByProductId = catalogProductEntityByProductId;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "link_id", unique = true, nullable = false)
	public Integer getLinkId() {
		return this.linkId;
	}

	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", nullable = false)
	public CatalogProductEntity getCatalogProductEntityByParentId() {
		return this.catalogProductEntityByParentId;
	}

	public void setCatalogProductEntityByParentId(CatalogProductEntity catalogProductEntityByParentId) {
		this.catalogProductEntityByParentId = catalogProductEntityByParentId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	public CatalogProductEntity getCatalogProductEntityByProductId() {
		return this.catalogProductEntityByProductId;
	}

	public void setCatalogProductEntityByProductId(CatalogProductEntity catalogProductEntityByProductId) {
		this.catalogProductEntityByProductId = catalogProductEntityByProductId;
	}

}
