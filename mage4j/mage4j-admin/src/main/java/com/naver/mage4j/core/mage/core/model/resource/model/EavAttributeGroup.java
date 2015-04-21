package com.naver.mage4j.core.mage.core.model.resource.model;

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
 * EavAttributeGroup generated by hbm2java
 */
@Entity
@Table(name = "eav_attribute_group"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = {"attribute_set_id", "attribute_group_name"}))
public class EavAttributeGroup implements java.io.Serializable {

	private Short attributeGroupId;
	private EavAttributeSet eavAttributeSet;
	private String attributeGroupName;
	private short sortOrder;
	private Short defaultId;
	private Set<EavEntityAttribute> eavEntityAttributes = new HashSet<EavEntityAttribute>(0);

	public EavAttributeGroup() {
	}

	public EavAttributeGroup(EavAttributeSet eavAttributeSet, short sortOrder) {
		this.eavAttributeSet = eavAttributeSet;
		this.sortOrder = sortOrder;
	}

	public EavAttributeGroup(EavAttributeSet eavAttributeSet, String attributeGroupName, short sortOrder, Short defaultId, Set<EavEntityAttribute> eavEntityAttributes) {
		this.eavAttributeSet = eavAttributeSet;
		this.attributeGroupName = attributeGroupName;
		this.sortOrder = sortOrder;
		this.defaultId = defaultId;
		this.eavEntityAttributes = eavEntityAttributes;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "attribute_group_id", unique = true, nullable = false)
	public Short getAttributeGroupId() {
		return this.attributeGroupId;
	}

	public void setAttributeGroupId(Short attributeGroupId) {
		this.attributeGroupId = attributeGroupId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attribute_set_id", nullable = false)
	public EavAttributeSet getEavAttributeSet() {
		return this.eavAttributeSet;
	}

	public void setEavAttributeSet(EavAttributeSet eavAttributeSet) {
		this.eavAttributeSet = eavAttributeSet;
	}

	@Column(name = "attribute_group_name")
	public String getAttributeGroupName() {
		return this.attributeGroupName;
	}

	public void setAttributeGroupName(String attributeGroupName) {
		this.attributeGroupName = attributeGroupName;
	}

	@Column(name = "sort_order", nullable = false)
	public short getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(short sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Column(name = "default_id")
	public Short getDefaultId() {
		return this.defaultId;
	}

	public void setDefaultId(Short defaultId) {
		this.defaultId = defaultId;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttributeGroup")
	public Set<EavEntityAttribute> getEavEntityAttributes() {
		return this.eavEntityAttributes;
	}

	public void setEavEntityAttributes(Set<EavEntityAttribute> eavEntityAttributes) {
		this.eavEntityAttributes = eavEntityAttributes;
	}

}
