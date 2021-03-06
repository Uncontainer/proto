package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CatalogCategoryProductIndexId generated by hbm2java
 */
@Embeddable
public class CatalogCategoryProductIndexId implements java.io.Serializable {

	private int categoryId;
	private int productId;
	private short storeId;

	public CatalogCategoryProductIndexId() {
	}

	public CatalogCategoryProductIndexId(int categoryId, int productId, short storeId) {
		this.categoryId = categoryId;
		this.productId = productId;
		this.storeId = storeId;
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

	@Column(name = "store_id", nullable = false)
	public short getStoreId() {
		return this.storeId;
	}

	public void setStoreId(short storeId) {
		this.storeId = storeId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CatalogCategoryProductIndexId))
			return false;
		CatalogCategoryProductIndexId castOther = (CatalogCategoryProductIndexId)other;

		return (this.getCategoryId() == castOther.getCategoryId())
			&& (this.getProductId() == castOther.getProductId())
			&& (this.getStoreId() == castOther.getStoreId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getCategoryId();
		result = 37 * result + this.getProductId();
		result = 37 * result + this.getStoreId();
		return result;
	}

}
