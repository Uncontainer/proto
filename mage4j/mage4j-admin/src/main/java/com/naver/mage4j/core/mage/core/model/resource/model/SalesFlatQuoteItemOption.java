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

/**
 * SalesFlatQuoteItemOption generated by hbm2java
 */
@Entity
@Table(name = "sales_flat_quote_item_option"
	, catalog = "magento")
public class SalesFlatQuoteItemOption implements java.io.Serializable {

	private Integer optionId;
	private SalesFlatQuoteItem salesFlatQuoteItem;
	private int productId;
	private String code;
	private String value;

	public SalesFlatQuoteItemOption() {
	}

	public SalesFlatQuoteItemOption(SalesFlatQuoteItem salesFlatQuoteItem, int productId, String code) {
		this.salesFlatQuoteItem = salesFlatQuoteItem;
		this.productId = productId;
		this.code = code;
	}

	public SalesFlatQuoteItemOption(SalesFlatQuoteItem salesFlatQuoteItem, int productId, String code, String value) {
		this.salesFlatQuoteItem = salesFlatQuoteItem;
		this.productId = productId;
		this.code = code;
		this.value = value;
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
	@JoinColumn(name = "item_id", nullable = false)
	public SalesFlatQuoteItem getSalesFlatQuoteItem() {
		return this.salesFlatQuoteItem;
	}

	public void setSalesFlatQuoteItem(SalesFlatQuoteItem salesFlatQuoteItem) {
		this.salesFlatQuoteItem = salesFlatQuoteItem;
	}

	@Column(name = "product_id", nullable = false)
	public int getProductId() {
		return this.productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	@Column(name = "code", nullable = false)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "value", length = 65535, columnDefinition = "TEXT")
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
