package com.naver.mage4j.core.mage.core.model.resource.model;

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
 * CatalogProductLinkAttributeInt generated by hbm2java
 */
@Entity
@Table(name = "catalog_product_link_attribute_int"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = {"product_link_attribute_id", "link_id"}))
public class CatalogProductLinkAttributeInt implements java.io.Serializable {

	private Integer valueId;
	private CatalogProductLinkAttribute catalogProductLinkAttribute;
	private CatalogProductLink catalogProductLink;
	private int value;

	public CatalogProductLinkAttributeInt() {
	}

	public CatalogProductLinkAttributeInt(CatalogProductLink catalogProductLink, int value) {
		this.catalogProductLink = catalogProductLink;
		this.value = value;
	}

	public CatalogProductLinkAttributeInt(CatalogProductLinkAttribute catalogProductLinkAttribute, CatalogProductLink catalogProductLink, int value) {
		this.catalogProductLinkAttribute = catalogProductLinkAttribute;
		this.catalogProductLink = catalogProductLink;
		this.value = value;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "value_id", unique = true, nullable = false)
	public Integer getValueId() {
		return this.valueId;
	}

	public void setValueId(Integer valueId) {
		this.valueId = valueId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_link_attribute_id")
	public CatalogProductLinkAttribute getCatalogProductLinkAttribute() {
		return this.catalogProductLinkAttribute;
	}

	public void setCatalogProductLinkAttribute(CatalogProductLinkAttribute catalogProductLinkAttribute) {
		this.catalogProductLinkAttribute = catalogProductLinkAttribute;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "link_id", nullable = false)
	public CatalogProductLink getCatalogProductLink() {
		return this.catalogProductLink;
	}

	public void setCatalogProductLink(CatalogProductLink catalogProductLink) {
		this.catalogProductLink = catalogProductLink;
	}

	@Column(name = "value", nullable = false)
	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
