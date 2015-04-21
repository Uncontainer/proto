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

/**
 * TaxCalculationRateTitle generated by hbm2java
 */
@Entity
@Table(name = "tax_calculation_rate_title"
	, catalog = "magento")
public class TaxCalculationRateTitle implements java.io.Serializable {

	private Integer taxCalculationRateTitleId;
	private TaxCalculationRate taxCalculationRate;
	private CoreStore coreStore;
	private String value;

	public TaxCalculationRateTitle() {
	}

	public TaxCalculationRateTitle(TaxCalculationRate taxCalculationRate, CoreStore coreStore, String value) {
		this.taxCalculationRate = taxCalculationRate;
		this.coreStore = coreStore;
		this.value = value;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "tax_calculation_rate_title_id", unique = true, nullable = false)
	public Integer getTaxCalculationRateTitleId() {
		return this.taxCalculationRateTitleId;
	}

	public void setTaxCalculationRateTitleId(Integer taxCalculationRateTitleId) {
		this.taxCalculationRateTitleId = taxCalculationRateTitleId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_calculation_rate_id", nullable = false)
	public TaxCalculationRate getTaxCalculationRate() {
		return this.taxCalculationRate;
	}

	public void setTaxCalculationRate(TaxCalculationRate taxCalculationRate) {
		this.taxCalculationRate = taxCalculationRate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	public CoreStore getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(CoreStore coreStore) {
		this.coreStore = coreStore;
	}

	@Column(name = "value", nullable = false)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
