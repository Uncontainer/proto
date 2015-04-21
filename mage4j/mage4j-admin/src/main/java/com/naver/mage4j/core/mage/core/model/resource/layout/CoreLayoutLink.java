package com.naver.mage4j.core.mage.core.model.resource.layout;

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
 * CoreLayoutLink generated by hbm2java
 */
@Entity
@Table(name = "core_layout_link"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = {"store_id", "package", "theme", "layout_update_id"}))
public class CoreLayoutLink implements java.io.Serializable {

	private Integer layoutLinkId;
	private CoreLayoutUpdate coreLayoutUpdate;
	private Store coreStore;
	private String area;
	private String package_;
	private String theme;

	public CoreLayoutLink() {
	}

	public CoreLayoutLink(CoreLayoutUpdate coreLayoutUpdate, Store coreStore) {
		this.coreLayoutUpdate = coreLayoutUpdate;
		this.coreStore = coreStore;
	}

	public CoreLayoutLink(CoreLayoutUpdate coreLayoutUpdate, Store coreStore, String area, String package_, String theme) {
		this.coreLayoutUpdate = coreLayoutUpdate;
		this.coreStore = coreStore;
		this.area = area;
		this.package_ = package_;
		this.theme = theme;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "layout_link_id", unique = true, nullable = false)
	public Integer getLayoutLinkId() {
		return this.layoutLinkId;
	}

	public void setLayoutLinkId(Integer layoutLinkId) {
		this.layoutLinkId = layoutLinkId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "layout_update_id", nullable = false)
	public CoreLayoutUpdate getCoreLayoutUpdate() {
		return this.coreLayoutUpdate;
	}

	public void setCoreLayoutUpdate(CoreLayoutUpdate coreLayoutUpdate) {
		this.coreLayoutUpdate = coreLayoutUpdate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	public Store getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(Store coreStore) {
		this.coreStore = coreStore;
	}

	@Column(name = "area", length = 64)
	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Column(name = "package", length = 64)
	public String getPackage_() {
		return this.package_;
	}

	public void setPackage_(String package_) {
		this.package_ = package_;
	}

	@Column(name = "theme", length = 64)
	public String getTheme() {
		return this.theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

}
