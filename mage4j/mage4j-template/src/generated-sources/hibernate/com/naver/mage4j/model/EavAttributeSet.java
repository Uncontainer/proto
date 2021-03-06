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
import javax.persistence.UniqueConstraint;

/**
 * EavAttributeSet generated by hbm2java
 */
@Entity
@Table(name = "eav_attribute_set"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = {"entity_type_id", "attribute_set_name"}))
public class EavAttributeSet implements java.io.Serializable {

	private Short attributeSetId;
	private EavEntityType eavEntityType;
	private String attributeSetName;
	private short sortOrder;
	private Set<CatalogProductEntity> catalogProductEntities = new HashSet<CatalogProductEntity>(0);
	private Set<EavAttributeGroup> eavAttributeGroups = new HashSet<EavAttributeGroup>(0);

	public EavAttributeSet() {
	}

	public EavAttributeSet(EavEntityType eavEntityType, short sortOrder) {
		this.eavEntityType = eavEntityType;
		this.sortOrder = sortOrder;
	}

	public EavAttributeSet(EavEntityType eavEntityType, String attributeSetName, short sortOrder, Set<CatalogProductEntity> catalogProductEntities, Set<EavAttributeGroup> eavAttributeGroups) {
		this.eavEntityType = eavEntityType;
		this.attributeSetName = attributeSetName;
		this.sortOrder = sortOrder;
		this.catalogProductEntities = catalogProductEntities;
		this.eavAttributeGroups = eavAttributeGroups;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "attribute_set_id", unique = true, nullable = false)
	public Short getAttributeSetId() {
		return this.attributeSetId;
	}

	public void setAttributeSetId(Short attributeSetId) {
		this.attributeSetId = attributeSetId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_type_id", nullable = false)
	public EavEntityType getEavEntityType() {
		return this.eavEntityType;
	}

	public void setEavEntityType(EavEntityType eavEntityType) {
		this.eavEntityType = eavEntityType;
	}

	@Column(name = "attribute_set_name")
	public String getAttributeSetName() {
		return this.attributeSetName;
	}

	public void setAttributeSetName(String attributeSetName) {
		this.attributeSetName = attributeSetName;
	}

	@Column(name = "sort_order", nullable = false)
	public short getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(short sortOrder) {
		this.sortOrder = sortOrder;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttributeSet")
	public Set<CatalogProductEntity> getCatalogProductEntities() {
		return this.catalogProductEntities;
	}

	public void setCatalogProductEntities(Set<CatalogProductEntity> catalogProductEntities) {
		this.catalogProductEntities = catalogProductEntities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttributeSet")
	public Set<EavAttributeGroup> getEavAttributeGroups() {
		return this.eavAttributeGroups;
	}

	public void setEavAttributeGroups(Set<EavAttributeGroup> eavAttributeGroups) {
		this.eavAttributeGroups = eavAttributeGroups;
	}

}
