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

import com.naver.mage4j.core.mage.core.model.resource.store.Store;

/**
 * CatalogProductOptionTypeTitle generated by hbm2java
 */
@Entity
@Table(name = "catalog_product_option_type_title"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = {"option_type_id", "store_id"}))
public class CatalogProductOptionTypeTitle implements java.io.Serializable {

	private Integer optionTypeTitleId;
	private CatalogProductOptionTypeValue catalogProductOptionTypeValue;
	private Store coreStore;
	private String title;

	public CatalogProductOptionTypeTitle() {
	}

	public CatalogProductOptionTypeTitle(CatalogProductOptionTypeValue catalogProductOptionTypeValue, Store coreStore) {
		this.catalogProductOptionTypeValue = catalogProductOptionTypeValue;
		this.coreStore = coreStore;
	}

	public CatalogProductOptionTypeTitle(CatalogProductOptionTypeValue catalogProductOptionTypeValue, Store coreStore, String title) {
		this.catalogProductOptionTypeValue = catalogProductOptionTypeValue;
		this.coreStore = coreStore;
		this.title = title;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "option_type_title_id", unique = true, nullable = false)
	public Integer getOptionTypeTitleId() {
		return this.optionTypeTitleId;
	}

	public void setOptionTypeTitleId(Integer optionTypeTitleId) {
		this.optionTypeTitleId = optionTypeTitleId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "option_type_id", nullable = false)
	public CatalogProductOptionTypeValue getCatalogProductOptionTypeValue() {
		return this.catalogProductOptionTypeValue;
	}

	public void setCatalogProductOptionTypeValue(CatalogProductOptionTypeValue catalogProductOptionTypeValue) {
		this.catalogProductOptionTypeValue = catalogProductOptionTypeValue;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	public Store getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(Store coreStore) {
		this.coreStore = coreStore;
	}

	@Column(name = "title")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}