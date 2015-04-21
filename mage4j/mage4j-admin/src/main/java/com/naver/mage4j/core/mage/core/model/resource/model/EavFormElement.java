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
 * EavFormElement generated by hbm2java
 */
@Entity
@Table(name = "eav_form_element"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = {"type_id", "attribute_id"}))
public class EavFormElement implements java.io.Serializable {

	private Integer elementId;
	private EavFormType eavFormType;
	private EavAttribute eavAttribute;
	private EavFormFieldset eavFormFieldset;
	private int sortOrder;

	public EavFormElement() {
	}

	public EavFormElement(EavFormType eavFormType, EavAttribute eavAttribute, int sortOrder) {
		this.eavFormType = eavFormType;
		this.eavAttribute = eavAttribute;
		this.sortOrder = sortOrder;
	}

	public EavFormElement(EavFormType eavFormType, EavAttribute eavAttribute, EavFormFieldset eavFormFieldset, int sortOrder) {
		this.eavFormType = eavFormType;
		this.eavAttribute = eavAttribute;
		this.eavFormFieldset = eavFormFieldset;
		this.sortOrder = sortOrder;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "element_id", unique = true, nullable = false)
	public Integer getElementId() {
		return this.elementId;
	}

	public void setElementId(Integer elementId) {
		this.elementId = elementId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id", nullable = false)
	public EavFormType getEavFormType() {
		return this.eavFormType;
	}

	public void setEavFormType(EavFormType eavFormType) {
		this.eavFormType = eavFormType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attribute_id", nullable = false)
	public EavAttribute getEavAttribute() {
		return this.eavAttribute;
	}

	public void setEavAttribute(EavAttribute eavAttribute) {
		this.eavAttribute = eavAttribute;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fieldset_id")
	public EavFormFieldset getEavFormFieldset() {
		return this.eavFormFieldset;
	}

	public void setEavFormFieldset(EavFormFieldset eavFormFieldset) {
		this.eavFormFieldset = eavFormFieldset;
	}

	@Column(name = "sort_order", nullable = false)
	public int getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

}
