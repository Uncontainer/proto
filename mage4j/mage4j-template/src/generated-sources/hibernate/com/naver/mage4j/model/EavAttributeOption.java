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
 * EavAttributeOption generated by hbm2java
 */
@Entity
@Table(name = "eav_attribute_option"
	, catalog = "magento")
public class EavAttributeOption implements java.io.Serializable {

	private Integer optionId;
	private EavAttribute eavAttribute;
	private short sortOrder;
	private Set<EavAttributeOptionValue> eavAttributeOptionValues = new HashSet<EavAttributeOptionValue>(0);

	public EavAttributeOption() {
	}

	public EavAttributeOption(EavAttribute eavAttribute, short sortOrder) {
		this.eavAttribute = eavAttribute;
		this.sortOrder = sortOrder;
	}

	public EavAttributeOption(EavAttribute eavAttribute, short sortOrder, Set<EavAttributeOptionValue> eavAttributeOptionValues) {
		this.eavAttribute = eavAttribute;
		this.sortOrder = sortOrder;
		this.eavAttributeOptionValues = eavAttributeOptionValues;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "option_id", unique = true, nullable = false)
	public Integer getOptionId() {
		return this.optionId;
	}

	public void setOptionId(Integer optionId) {
		this.optionId = optionId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attribute_id", nullable = false)
	public EavAttribute getEavAttribute() {
		return this.eavAttribute;
	}

	public void setEavAttribute(EavAttribute eavAttribute) {
		this.eavAttribute = eavAttribute;
	}

	@Column(name = "sort_order", nullable = false)
	public short getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(short sortOrder) {
		this.sortOrder = sortOrder;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttributeOption")
	public Set<EavAttributeOptionValue> getEavAttributeOptionValues() {
		return this.eavAttributeOptionValues;
	}

	public void setEavAttributeOptionValues(Set<EavAttributeOptionValue> eavAttributeOptionValues) {
		this.eavAttributeOptionValues = eavAttributeOptionValues;
	}

}
