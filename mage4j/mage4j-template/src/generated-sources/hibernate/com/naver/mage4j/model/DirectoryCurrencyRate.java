package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * DirectoryCurrencyRate generated by hbm2java
 */
@Entity
@Table(name = "directory_currency_rate"
	, catalog = "magento")
public class DirectoryCurrencyRate implements java.io.Serializable {

	private DirectoryCurrencyRateId id;
	private BigDecimal rate;

	public DirectoryCurrencyRate() {
	}

	public DirectoryCurrencyRate(DirectoryCurrencyRateId id, BigDecimal rate) {
		this.id = id;
		this.rate = rate;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "currencyFrom", column = @Column(name = "currency_from", nullable = false, length = 3)),
		@AttributeOverride(name = "currencyTo", column = @Column(name = "currency_to", nullable = false, length = 3))})
	public DirectoryCurrencyRateId getId() {
		return this.id;
	}

	public void setId(DirectoryCurrencyRateId id) {
		this.id = id;
	}

	@Column(name = "rate", nullable = false, precision = 24, scale = 12)
	public BigDecimal getRate() {
		return this.rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

}
