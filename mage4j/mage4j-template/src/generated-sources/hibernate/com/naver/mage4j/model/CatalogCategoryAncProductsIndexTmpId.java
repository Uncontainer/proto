package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CatalogCategoryAncProductsIndexTmpId generated by hbm2java
 */
@Embeddable
public class CatalogCategoryAncProductsIndexTmpId implements java.io.Serializable {

	private int categoryId;
	private int productId;
	private Integer position;

	public CatalogCategoryAncProductsIndexTmpId() {
	}

	public CatalogCategoryAncProductsIndexTmpId(int categoryId, int productId) {
		this.categoryId = categoryId;
		this.productId = productId;
	}

	public CatalogCategoryAncProductsIndexTmpId(int categoryId, int productId, Integer position) {
		this.categoryId = categoryId;
		this.productId = productId;
		this.position = position;
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

	@Column(name = "position")
	public Integer getPosition() {
		return this.position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CatalogCategoryAncProductsIndexTmpId))
			return false;
		CatalogCategoryAncProductsIndexTmpId castOther = (CatalogCategoryAncProductsIndexTmpId)other;

		return (this.getCategoryId() == castOther.getCategoryId())
			&& (this.getProductId() == castOther.getProductId())
			&& ((this.getPosition() == castOther.getPosition()) || (this.getPosition() != null && castOther.getPosition() != null && this.getPosition().equals(castOther.getPosition())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getCategoryId();
		result = 37 * result + this.getProductId();
		result = 37 * result + (getPosition() == null ? 0 : this.getPosition().hashCode());
		return result;
	}

}
