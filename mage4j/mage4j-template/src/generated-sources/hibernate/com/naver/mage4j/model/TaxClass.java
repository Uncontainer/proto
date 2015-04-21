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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * TaxClass generated by hbm2java
 */
@Entity
@Table(name = "tax_class"
	, catalog = "magento")
public class TaxClass implements java.io.Serializable {

	private Short classId;
	private String className;
	private String classType;
	private Set<TaxCalculation> taxCalculationsForProductTaxClassId = new HashSet<TaxCalculation>(0);
	private Set<TaxCalculation> taxCalculationsForCustomerTaxClassId = new HashSet<TaxCalculation>(0);

	public TaxClass() {
	}

	public TaxClass(String className, String classType) {
		this.className = className;
		this.classType = classType;
	}

	public TaxClass(String className, String classType, Set<TaxCalculation> taxCalculationsForProductTaxClassId, Set<TaxCalculation> taxCalculationsForCustomerTaxClassId) {
		this.className = className;
		this.classType = classType;
		this.taxCalculationsForProductTaxClassId = taxCalculationsForProductTaxClassId;
		this.taxCalculationsForCustomerTaxClassId = taxCalculationsForCustomerTaxClassId;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "class_id", unique = true, nullable = false)
	public Short getClassId() {
		return this.classId;
	}

	public void setClassId(Short classId) {
		this.classId = classId;
	}

	@Column(name = "class_name", nullable = false)
	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Column(name = "class_type", nullable = false, length = 8)
	public String getClassType() {
		return this.classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taxClassByProductTaxClassId")
	public Set<TaxCalculation> getTaxCalculationsForProductTaxClassId() {
		return this.taxCalculationsForProductTaxClassId;
	}

	public void setTaxCalculationsForProductTaxClassId(Set<TaxCalculation> taxCalculationsForProductTaxClassId) {
		this.taxCalculationsForProductTaxClassId = taxCalculationsForProductTaxClassId;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taxClassByCustomerTaxClassId")
	public Set<TaxCalculation> getTaxCalculationsForCustomerTaxClassId() {
		return this.taxCalculationsForCustomerTaxClassId;
	}

	public void setTaxCalculationsForCustomerTaxClassId(Set<TaxCalculation> taxCalculationsForCustomerTaxClassId) {
		this.taxCalculationsForCustomerTaxClassId = taxCalculationsForCustomerTaxClassId;
	}

}
