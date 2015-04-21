package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * CatalogEavAttribute generated by hbm2java
 */
@Entity
@Table(name = "catalog_eav_attribute"
	, catalog = "magento")
public class CatalogEavAttribute implements java.io.Serializable {

	private short attributeId;
	private EavAttribute eavAttribute;
	private String frontendInputRenderer;
	private short isGlobal;
	private short isVisible;
	private short isSearchable;
	private short isFilterable;
	private short isComparable;
	private short isVisibleOnFront;
	private short isHtmlAllowedOnFront;
	private short isUsedForPriceRules;
	private short isFilterableInSearch;
	private short usedInProductListing;
	private short usedForSortBy;
	private short isConfigurable;
	private String applyTo;
	private short isVisibleInAdvancedSearch;
	private int position;
	private short isWysiwygEnabled;
	private short isUsedForPromoRules;

	public CatalogEavAttribute() {
	}

	public CatalogEavAttribute(EavAttribute eavAttribute, short isGlobal, short isVisible, short isSearchable, short isFilterable, short isComparable, short isVisibleOnFront, short isHtmlAllowedOnFront, short isUsedForPriceRules, short isFilterableInSearch, short usedInProductListing, short usedForSortBy, short isConfigurable, short isVisibleInAdvancedSearch, int position, short isWysiwygEnabled, short isUsedForPromoRules) {
		this.eavAttribute = eavAttribute;
		this.isGlobal = isGlobal;
		this.isVisible = isVisible;
		this.isSearchable = isSearchable;
		this.isFilterable = isFilterable;
		this.isComparable = isComparable;
		this.isVisibleOnFront = isVisibleOnFront;
		this.isHtmlAllowedOnFront = isHtmlAllowedOnFront;
		this.isUsedForPriceRules = isUsedForPriceRules;
		this.isFilterableInSearch = isFilterableInSearch;
		this.usedInProductListing = usedInProductListing;
		this.usedForSortBy = usedForSortBy;
		this.isConfigurable = isConfigurable;
		this.isVisibleInAdvancedSearch = isVisibleInAdvancedSearch;
		this.position = position;
		this.isWysiwygEnabled = isWysiwygEnabled;
		this.isUsedForPromoRules = isUsedForPromoRules;
	}

	public CatalogEavAttribute(EavAttribute eavAttribute, String frontendInputRenderer, short isGlobal, short isVisible, short isSearchable, short isFilterable, short isComparable, short isVisibleOnFront, short isHtmlAllowedOnFront, short isUsedForPriceRules, short isFilterableInSearch, short usedInProductListing, short usedForSortBy, short isConfigurable, String applyTo, short isVisibleInAdvancedSearch, int position, short isWysiwygEnabled, short isUsedForPromoRules) {
		this.eavAttribute = eavAttribute;
		this.frontendInputRenderer = frontendInputRenderer;
		this.isGlobal = isGlobal;
		this.isVisible = isVisible;
		this.isSearchable = isSearchable;
		this.isFilterable = isFilterable;
		this.isComparable = isComparable;
		this.isVisibleOnFront = isVisibleOnFront;
		this.isHtmlAllowedOnFront = isHtmlAllowedOnFront;
		this.isUsedForPriceRules = isUsedForPriceRules;
		this.isFilterableInSearch = isFilterableInSearch;
		this.usedInProductListing = usedInProductListing;
		this.usedForSortBy = usedForSortBy;
		this.isConfigurable = isConfigurable;
		this.applyTo = applyTo;
		this.isVisibleInAdvancedSearch = isVisibleInAdvancedSearch;
		this.position = position;
		this.isWysiwygEnabled = isWysiwygEnabled;
		this.isUsedForPromoRules = isUsedForPromoRules;
	}

	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "eavAttribute"))
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "attribute_id", unique = true, nullable = false)
	public short getAttributeId() {
		return this.attributeId;
	}

	public void setAttributeId(short attributeId) {
		this.attributeId = attributeId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public EavAttribute getEavAttribute() {
		return this.eavAttribute;
	}

	public void setEavAttribute(EavAttribute eavAttribute) {
		this.eavAttribute = eavAttribute;
	}

	@Column(name = "frontend_input_renderer")
	public String getFrontendInputRenderer() {
		return this.frontendInputRenderer;
	}

	public void setFrontendInputRenderer(String frontendInputRenderer) {
		this.frontendInputRenderer = frontendInputRenderer;
	}

	@Column(name = "is_global", nullable = false)
	public short getIsGlobal() {
		return this.isGlobal;
	}

	public void setIsGlobal(short isGlobal) {
		this.isGlobal = isGlobal;
	}

	@Column(name = "is_visible", nullable = false)
	public short getIsVisible() {
		return this.isVisible;
	}

	public void setIsVisible(short isVisible) {
		this.isVisible = isVisible;
	}

	@Column(name = "is_searchable", nullable = false)
	public short getIsSearchable() {
		return this.isSearchable;
	}

	public void setIsSearchable(short isSearchable) {
		this.isSearchable = isSearchable;
	}

	@Column(name = "is_filterable", nullable = false)
	public short getIsFilterable() {
		return this.isFilterable;
	}

	public void setIsFilterable(short isFilterable) {
		this.isFilterable = isFilterable;
	}

	@Column(name = "is_comparable", nullable = false)
	public short getIsComparable() {
		return this.isComparable;
	}

	public void setIsComparable(short isComparable) {
		this.isComparable = isComparable;
	}

	@Column(name = "is_visible_on_front", nullable = false)
	public short getIsVisibleOnFront() {
		return this.isVisibleOnFront;
	}

	public void setIsVisibleOnFront(short isVisibleOnFront) {
		this.isVisibleOnFront = isVisibleOnFront;
	}

	@Column(name = "is_html_allowed_on_front", nullable = false)
	public short getIsHtmlAllowedOnFront() {
		return this.isHtmlAllowedOnFront;
	}

	public void setIsHtmlAllowedOnFront(short isHtmlAllowedOnFront) {
		this.isHtmlAllowedOnFront = isHtmlAllowedOnFront;
	}

	@Column(name = "is_used_for_price_rules", nullable = false)
	public short getIsUsedForPriceRules() {
		return this.isUsedForPriceRules;
	}

	public void setIsUsedForPriceRules(short isUsedForPriceRules) {
		this.isUsedForPriceRules = isUsedForPriceRules;
	}

	@Column(name = "is_filterable_in_search", nullable = false)
	public short getIsFilterableInSearch() {
		return this.isFilterableInSearch;
	}

	public void setIsFilterableInSearch(short isFilterableInSearch) {
		this.isFilterableInSearch = isFilterableInSearch;
	}

	@Column(name = "used_in_product_listing", nullable = false)
	public short getUsedInProductListing() {
		return this.usedInProductListing;
	}

	public void setUsedInProductListing(short usedInProductListing) {
		this.usedInProductListing = usedInProductListing;
	}

	@Column(name = "used_for_sort_by", nullable = false)
	public short getUsedForSortBy() {
		return this.usedForSortBy;
	}

	public void setUsedForSortBy(short usedForSortBy) {
		this.usedForSortBy = usedForSortBy;
	}

	@Column(name = "is_configurable", nullable = false)
	public short getIsConfigurable() {
		return this.isConfigurable;
	}

	public void setIsConfigurable(short isConfigurable) {
		this.isConfigurable = isConfigurable;
	}

	@Column(name = "apply_to")
	public String getApplyTo() {
		return this.applyTo;
	}

	public void setApplyTo(String applyTo) {
		this.applyTo = applyTo;
	}

	@Column(name = "is_visible_in_advanced_search", nullable = false)
	public short getIsVisibleInAdvancedSearch() {
		return this.isVisibleInAdvancedSearch;
	}

	public void setIsVisibleInAdvancedSearch(short isVisibleInAdvancedSearch) {
		this.isVisibleInAdvancedSearch = isVisibleInAdvancedSearch;
	}

	@Column(name = "position", nullable = false)
	public int getPosition() {
		return this.position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Column(name = "is_wysiwyg_enabled", nullable = false)
	public short getIsWysiwygEnabled() {
		return this.isWysiwygEnabled;
	}

	public void setIsWysiwygEnabled(short isWysiwygEnabled) {
		this.isWysiwygEnabled = isWysiwygEnabled;
	}

	@Column(name = "is_used_for_promo_rules", nullable = false)
	public short getIsUsedForPromoRules() {
		return this.isUsedForPromoRules;
	}

	public void setIsUsedForPromoRules(short isUsedForPromoRules) {
		this.isUsedForPromoRules = isUsedForPromoRules;
	}

}
