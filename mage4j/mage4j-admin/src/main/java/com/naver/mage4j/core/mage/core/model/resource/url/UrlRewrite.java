package com.naver.mage4j.core.mage.core.model.resource.url;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import static javax.persistence.GenerationType.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.naver.mage4j.core.mage.core.model.resource.model.CatalogCategoryEntity;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogProductEntity;
import com.naver.mage4j.core.mage.core.model.resource.store.Store;

/**
 * CoreUrlRewrite generated by hbm2java
 */
@Entity
@Table(name = "core_url_rewrite"
	, catalog = "magento"
	, uniqueConstraints = {@UniqueConstraint(columnNames = {"id_path", "is_system", "store_id"}), @UniqueConstraint(columnNames = {"request_path", "store_id"})})
public class UrlRewrite implements java.io.Serializable {
	private Integer urlRewriteId;
	private CatalogCategoryEntity catalogCategoryEntity;
	private Store store;
	private CatalogProductEntity catalogProductEntity;
	private String idPath;
	private String requestPath;
	private String targetPath;
	private Short isSystem;
	private String options;
	private String description;

	private transient final UrlRewriteHelper helper = new UrlRewriteHelper(this);

	public UrlRewrite() {
	}

	public UrlRewrite(Store store) {
		this.store = store;
	}

	public UrlRewrite(UrlRewrite other) {
		this.catalogCategoryEntity = other.catalogCategoryEntity;
		this.store = other.store;
		this.catalogProductEntity = other.catalogProductEntity;
		this.idPath = other.idPath;
		this.requestPath = other.requestPath;
		this.targetPath = other.targetPath;
		this.isSystem = other.isSystem;
		this.options = other.options;
		this.description = other.description;
	}

	@Transient
	public UrlRewriteHelper getHelper() {
		return helper;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "url_rewrite_id", unique = true, nullable = false)
	public Integer getUrlRewriteId() {
		return this.urlRewriteId;
	}

	public void setUrlRewriteId(Integer urlRewriteId) {
		this.urlRewriteId = urlRewriteId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	public CatalogCategoryEntity getCatalogCategoryEntity() {
		return this.catalogCategoryEntity;
	}

	public void setCatalogCategoryEntity(CatalogCategoryEntity catalogCategoryEntity) {
		this.catalogCategoryEntity = catalogCategoryEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	public Store getStore() {
		return this.store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	public CatalogProductEntity getCatalogProductEntity() {
		return this.catalogProductEntity;
	}

	public void setCatalogProductEntity(CatalogProductEntity catalogProductEntity) {
		this.catalogProductEntity = catalogProductEntity;
	}

	@Column(name = "id_path")
	public String getIdPath() {
		return this.idPath;
	}

	public void setIdPath(String idPath) {
		this.idPath = idPath;
	}

	@Column(name = "request_path")
	public String getRequestPath() {
		return this.requestPath;
	}

	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}

	@Column(name = "target_path")
	public String getTargetPath() {
		return this.targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	@Column(name = "is_system")
	public Short getIsSystem() {
		return this.isSystem;
	}

	public void setIsSystem(Short isSystem) {
		this.isSystem = isSystem;
	}

	@Column(name = "options")
	public String getOptions() {
		return this.options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
