package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.naver.mage4j.core.mage.core.model.resource.store.Store;

/**
 * WishlistItem generated by hbm2java
 */
@Entity
@Table(name = "wishlist_item"
	, catalog = "magento")
public class WishlistItem implements java.io.Serializable {

	private Integer wishlistItemId;
	private Wishlist wishlist;
	private Store coreStore;
	private CatalogProductEntity catalogProductEntity;
	private Date addedAt;
	private String description;
	private BigDecimal qty;
	private Set<WishlistItemOption> wishlistItemOptions = new HashSet<WishlistItemOption>(0);

	public WishlistItem() {
	}

	public WishlistItem(Wishlist wishlist, CatalogProductEntity catalogProductEntity, BigDecimal qty) {
		this.wishlist = wishlist;
		this.catalogProductEntity = catalogProductEntity;
		this.qty = qty;
	}

	public WishlistItem(Wishlist wishlist, Store coreStore, CatalogProductEntity catalogProductEntity, Date addedAt, String description, BigDecimal qty, Set<WishlistItemOption> wishlistItemOptions) {
		this.wishlist = wishlist;
		this.coreStore = coreStore;
		this.catalogProductEntity = catalogProductEntity;
		this.addedAt = addedAt;
		this.description = description;
		this.qty = qty;
		this.wishlistItemOptions = wishlistItemOptions;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "wishlist_item_id", unique = true, nullable = false)
	public Integer getWishlistItemId() {
		return this.wishlistItemId;
	}

	public void setWishlistItemId(Integer wishlistItemId) {
		this.wishlistItemId = wishlistItemId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wishlist_id", nullable = false)
	public Wishlist getWishlist() {
		return this.wishlist;
	}

	public void setWishlist(Wishlist wishlist) {
		this.wishlist = wishlist;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	public Store getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(Store coreStore) {
		this.coreStore = coreStore;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	public CatalogProductEntity getCatalogProductEntity() {
		return this.catalogProductEntity;
	}

	public void setCatalogProductEntity(CatalogProductEntity catalogProductEntity) {
		this.catalogProductEntity = catalogProductEntity;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "added_at", length = 19)
	public Date getAddedAt() {
		return this.addedAt;
	}

	public void setAddedAt(Date addedAt) {
		this.addedAt = addedAt;
	}

	@Column(name = "description", length = 65535, columnDefinition = "TEXT")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "qty", nullable = false, precision = 12, scale = 4)
	public BigDecimal getQty() {
		return this.qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "wishlistItem")
	public Set<WishlistItemOption> getWishlistItemOptions() {
		return this.wishlistItemOptions;
	}

	public void setWishlistItemOptions(Set<WishlistItemOption> wishlistItemOptions) {
		this.wishlistItemOptions = wishlistItemOptions;
	}

}
