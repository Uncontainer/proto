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
 * CatalogProductEntityMediaGallery generated by hbm2java
 */
@Entity
@Table(name = "catalog_product_entity_media_gallery"
	, catalog = "magento")
public class CatalogProductEntityMediaGallery implements java.io.Serializable {

	private Integer valueId;
	private CatalogProductEntity catalogProductEntity;
	private EavAttribute eavAttribute;
	private String value;
	private Set<CatalogProductEntityMediaGalleryValue> catalogProductEntityMediaGalleryValues = new HashSet<CatalogProductEntityMediaGalleryValue>(0);

	public CatalogProductEntityMediaGallery() {
	}

	public CatalogProductEntityMediaGallery(CatalogProductEntity catalogProductEntity, EavAttribute eavAttribute) {
		this.catalogProductEntity = catalogProductEntity;
		this.eavAttribute = eavAttribute;
	}

	public CatalogProductEntityMediaGallery(CatalogProductEntity catalogProductEntity, EavAttribute eavAttribute, String value, Set<CatalogProductEntityMediaGalleryValue> catalogProductEntityMediaGalleryValues) {
		this.catalogProductEntity = catalogProductEntity;
		this.eavAttribute = eavAttribute;
		this.value = value;
		this.catalogProductEntityMediaGalleryValues = catalogProductEntityMediaGalleryValues;
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
	@JoinColumn(name = "entity_id", nullable = false)
	public CatalogProductEntity getCatalogProductEntity() {
		return this.catalogProductEntity;
	}

	public void setCatalogProductEntity(CatalogProductEntity catalogProductEntity) {
		this.catalogProductEntity = catalogProductEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attribute_id", nullable = false)
	public EavAttribute getEavAttribute() {
		return this.eavAttribute;
	}

	public void setEavAttribute(EavAttribute eavAttribute) {
		this.eavAttribute = eavAttribute;
	}

	@Column(name = "value")
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "catalogProductEntityMediaGallery")
	public Set<CatalogProductEntityMediaGalleryValue> getCatalogProductEntityMediaGalleryValues() {
		return this.catalogProductEntityMediaGalleryValues;
	}

	public void setCatalogProductEntityMediaGalleryValues(Set<CatalogProductEntityMediaGalleryValue> catalogProductEntityMediaGalleryValues) {
		this.catalogProductEntityMediaGalleryValues = catalogProductEntityMediaGalleryValues;
	}

}