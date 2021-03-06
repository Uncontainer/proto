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
 * CatalogProductLinkAttribute generated by hbm2java
 */
@Entity
@Table(name = "catalog_product_link_attribute"
	, catalog = "magento")
public class CatalogProductLinkAttribute implements java.io.Serializable {

	private Short productLinkAttributeId;
	private CatalogProductLinkType catalogProductLinkType;
	private String productLinkAttributeCode;
	private String dataType;
	private Set<CatalogProductLinkAttributeVarchar> catalogProductLinkAttributeVarchars = new HashSet<CatalogProductLinkAttributeVarchar>(0);
	private Set<CatalogProductLinkAttributeDecimal> catalogProductLinkAttributeDecimals = new HashSet<CatalogProductLinkAttributeDecimal>(0);
	private Set<CatalogProductLinkAttributeInt> catalogProductLinkAttributeInts = new HashSet<CatalogProductLinkAttributeInt>(0);

	public CatalogProductLinkAttribute() {
	}

	public CatalogProductLinkAttribute(CatalogProductLinkType catalogProductLinkType) {
		this.catalogProductLinkType = catalogProductLinkType;
	}

	public CatalogProductLinkAttribute(CatalogProductLinkType catalogProductLinkType, String productLinkAttributeCode, String dataType, Set<CatalogProductLinkAttributeVarchar> catalogProductLinkAttributeVarchars, Set<CatalogProductLinkAttributeDecimal> catalogProductLinkAttributeDecimals, Set<CatalogProductLinkAttributeInt> catalogProductLinkAttributeInts) {
		this.catalogProductLinkType = catalogProductLinkType;
		this.productLinkAttributeCode = productLinkAttributeCode;
		this.dataType = dataType;
		this.catalogProductLinkAttributeVarchars = catalogProductLinkAttributeVarchars;
		this.catalogProductLinkAttributeDecimals = catalogProductLinkAttributeDecimals;
		this.catalogProductLinkAttributeInts = catalogProductLinkAttributeInts;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "product_link_attribute_id", unique = true, nullable = false)
	public Short getProductLinkAttributeId() {
		return this.productLinkAttributeId;
	}

	public void setProductLinkAttributeId(Short productLinkAttributeId) {
		this.productLinkAttributeId = productLinkAttributeId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "link_type_id", nullable = false)
	public CatalogProductLinkType getCatalogProductLinkType() {
		return this.catalogProductLinkType;
	}

	public void setCatalogProductLinkType(CatalogProductLinkType catalogProductLinkType) {
		this.catalogProductLinkType = catalogProductLinkType;
	}

	@Column(name = "product_link_attribute_code", length = 32)
	public String getProductLinkAttributeCode() {
		return this.productLinkAttributeCode;
	}

	public void setProductLinkAttributeCode(String productLinkAttributeCode) {
		this.productLinkAttributeCode = productLinkAttributeCode;
	}

	@Column(name = "data_type", length = 32)
	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "catalogProductLinkAttribute")
	public Set<CatalogProductLinkAttributeVarchar> getCatalogProductLinkAttributeVarchars() {
		return this.catalogProductLinkAttributeVarchars;
	}

	public void setCatalogProductLinkAttributeVarchars(Set<CatalogProductLinkAttributeVarchar> catalogProductLinkAttributeVarchars) {
		this.catalogProductLinkAttributeVarchars = catalogProductLinkAttributeVarchars;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "catalogProductLinkAttribute")
	public Set<CatalogProductLinkAttributeDecimal> getCatalogProductLinkAttributeDecimals() {
		return this.catalogProductLinkAttributeDecimals;
	}

	public void setCatalogProductLinkAttributeDecimals(Set<CatalogProductLinkAttributeDecimal> catalogProductLinkAttributeDecimals) {
		this.catalogProductLinkAttributeDecimals = catalogProductLinkAttributeDecimals;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "catalogProductLinkAttribute")
	public Set<CatalogProductLinkAttributeInt> getCatalogProductLinkAttributeInts() {
		return this.catalogProductLinkAttributeInts;
	}

	public void setCatalogProductLinkAttributeInts(Set<CatalogProductLinkAttributeInt> catalogProductLinkAttributeInts) {
		this.catalogProductLinkAttributeInts = catalogProductLinkAttributeInts;
	}

}
