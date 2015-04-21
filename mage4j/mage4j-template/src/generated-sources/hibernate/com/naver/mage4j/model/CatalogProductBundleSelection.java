package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
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
 * CatalogProductBundleSelection generated by hbm2java
 */
@Entity
@Table(name = "catalog_product_bundle_selection"
	, catalog = "magento")
public class CatalogProductBundleSelection implements java.io.Serializable {

	private Integer selectionId;
	private CatalogProductBundleOption catalogProductBundleOption;
	private CatalogProductEntity catalogProductEntity;
	private int parentProductId;
	private int position;
	private short isDefault;
	private short selectionPriceType;
	private BigDecimal selectionPriceValue;
	private BigDecimal selectionQty;
	private short selectionCanChangeQty;
	private Set<CatalogProductBundleSelectionPrice> catalogProductBundleSelectionPrices = new HashSet<CatalogProductBundleSelectionPrice>(0);

	public CatalogProductBundleSelection() {
	}

	public CatalogProductBundleSelection(CatalogProductBundleOption catalogProductBundleOption, CatalogProductEntity catalogProductEntity, int parentProductId, int position, short isDefault, short selectionPriceType, BigDecimal selectionPriceValue, short selectionCanChangeQty) {
		this.catalogProductBundleOption = catalogProductBundleOption;
		this.catalogProductEntity = catalogProductEntity;
		this.parentProductId = parentProductId;
		this.position = position;
		this.isDefault = isDefault;
		this.selectionPriceType = selectionPriceType;
		this.selectionPriceValue = selectionPriceValue;
		this.selectionCanChangeQty = selectionCanChangeQty;
	}

	public CatalogProductBundleSelection(CatalogProductBundleOption catalogProductBundleOption, CatalogProductEntity catalogProductEntity, int parentProductId, int position, short isDefault, short selectionPriceType, BigDecimal selectionPriceValue, BigDecimal selectionQty, short selectionCanChangeQty, Set<CatalogProductBundleSelectionPrice> catalogProductBundleSelectionPrices) {
		this.catalogProductBundleOption = catalogProductBundleOption;
		this.catalogProductEntity = catalogProductEntity;
		this.parentProductId = parentProductId;
		this.position = position;
		this.isDefault = isDefault;
		this.selectionPriceType = selectionPriceType;
		this.selectionPriceValue = selectionPriceValue;
		this.selectionQty = selectionQty;
		this.selectionCanChangeQty = selectionCanChangeQty;
		this.catalogProductBundleSelectionPrices = catalogProductBundleSelectionPrices;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "selection_id", unique = true, nullable = false)
	public Integer getSelectionId() {
		return this.selectionId;
	}

	public void setSelectionId(Integer selectionId) {
		this.selectionId = selectionId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "option_id", nullable = false)
	public CatalogProductBundleOption getCatalogProductBundleOption() {
		return this.catalogProductBundleOption;
	}

	public void setCatalogProductBundleOption(CatalogProductBundleOption catalogProductBundleOption) {
		this.catalogProductBundleOption = catalogProductBundleOption;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	public CatalogProductEntity getCatalogProductEntity() {
		return this.catalogProductEntity;
	}

	public void setCatalogProductEntity(CatalogProductEntity catalogProductEntity) {
		this.catalogProductEntity = catalogProductEntity;
	}

	@Column(name = "parent_product_id", nullable = false)
	public int getParentProductId() {
		return this.parentProductId;
	}

	public void setParentProductId(int parentProductId) {
		this.parentProductId = parentProductId;
	}

	@Column(name = "position", nullable = false)
	public int getPosition() {
		return this.position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Column(name = "is_default", nullable = false)
	public short getIsDefault() {
		return this.isDefault;
	}

	public void setIsDefault(short isDefault) {
		this.isDefault = isDefault;
	}

	@Column(name = "selection_price_type", nullable = false)
	public short getSelectionPriceType() {
		return this.selectionPriceType;
	}

	public void setSelectionPriceType(short selectionPriceType) {
		this.selectionPriceType = selectionPriceType;
	}

	@Column(name = "selection_price_value", nullable = false, precision = 12, scale = 4)
	public BigDecimal getSelectionPriceValue() {
		return this.selectionPriceValue;
	}

	public void setSelectionPriceValue(BigDecimal selectionPriceValue) {
		this.selectionPriceValue = selectionPriceValue;
	}

	@Column(name = "selection_qty", precision = 12, scale = 4)
	public BigDecimal getSelectionQty() {
		return this.selectionQty;
	}

	public void setSelectionQty(BigDecimal selectionQty) {
		this.selectionQty = selectionQty;
	}

	@Column(name = "selection_can_change_qty", nullable = false)
	public short getSelectionCanChangeQty() {
		return this.selectionCanChangeQty;
	}

	public void setSelectionCanChangeQty(short selectionCanChangeQty) {
		this.selectionCanChangeQty = selectionCanChangeQty;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "catalogProductBundleSelection")
	public Set<CatalogProductBundleSelectionPrice> getCatalogProductBundleSelectionPrices() {
		return this.catalogProductBundleSelectionPrices;
	}

	public void setCatalogProductBundleSelectionPrices(Set<CatalogProductBundleSelectionPrice> catalogProductBundleSelectionPrices) {
		this.catalogProductBundleSelectionPrices = catalogProductBundleSelectionPrices;
	}

}