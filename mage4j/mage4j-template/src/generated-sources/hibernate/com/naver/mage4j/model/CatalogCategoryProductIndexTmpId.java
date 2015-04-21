package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CatalogCategoryProductIndexTmpId generated by hbm2java
 */
@Embeddable
public class CatalogCategoryProductIndexTmpId implements java.io.Serializable {

	private int categoryId;
	private int productId;
	private int position;
	private short isParent;
	private short storeId;
	private short visibility;

	public CatalogCategoryProductIndexTmpId() {
	}

	public CatalogCategoryProductIndexTmpId(int categoryId, int productId, int position, short isParent, short storeId, short visibility) {
		this.categoryId = categoryId;
		this.productId = productId;
		this.position = position;
		this.isParent = isParent;
		this.storeId = storeId;
		this.visibility = visibility;
	}

	@Column(name = "category_id", nullable = false)
	public int getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	@Column(name = "product_id", nullable = false)
	public int getProductId() {
		return this.productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	@Column(name = "position", nullable = false)
	public int getPosition() {
		return this.position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Column(name = "is_parent", nullable = false)
	public short getIsParent() {
		return this.isParent;
	}

	public void setIsParent(short isParent) {
		this.isParent = isParent;
	}

	@Column(name = "store_id", nullable = false)
	public short getStoreId() {
		return this.storeId;
	}

	public void setStoreId(short storeId) {
		this.storeId = storeId;
	}

	@Column(name = "visibility", nullable = false)
	public short getVisibility() {
		return this.visibility;
	}

	public void setVisibility(short visibility) {
		this.visibility = visibility;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CatalogCategoryProductIndexTmpId))
			return false;
		CatalogCategoryProductIndexTmpId castOther = (CatalogCategoryProductIndexTmpId)other;

		return (this.getCategoryId() == castOther.getCategoryId())
			&& (this.getProductId() == castOther.getProductId())
			&& (this.getPosition() == castOther.getPosition())
			&& (this.getIsParent() == castOther.getIsParent())
			&& (this.getStoreId() == castOther.getStoreId())
			&& (this.getVisibility() == castOther.getVisibility());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getCategoryId();
		result = 37 * result + this.getProductId();
		result = 37 * result + this.getPosition();
		result = 37 * result + this.getIsParent();
		result = 37 * result + this.getStoreId();
		result = 37 * result + this.getVisibility();
		return result;
	}

}
