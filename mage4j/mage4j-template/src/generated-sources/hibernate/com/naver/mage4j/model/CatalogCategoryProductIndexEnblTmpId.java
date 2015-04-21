package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CatalogCategoryProductIndexEnblTmpId generated by hbm2java
 */
@Embeddable
public class CatalogCategoryProductIndexEnblTmpId implements java.io.Serializable {

	private int productId;
	private int visibility;

	public CatalogCategoryProductIndexEnblTmpId() {
	}

	public CatalogCategoryProductIndexEnblTmpId(int productId, int visibility) {
		this.productId = productId;
		this.visibility = visibility;
	}

	@Column(name = "product_id", nullable = false)
	public int getProductId() {
		return this.productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	@Column(name = "visibility", nullable = false)
	public int getVisibility() {
		return this.visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CatalogCategoryProductIndexEnblTmpId))
			return false;
		CatalogCategoryProductIndexEnblTmpId castOther = (CatalogCategoryProductIndexEnblTmpId)other;

		return (this.getProductId() == castOther.getProductId())
			&& (this.getVisibility() == castOther.getVisibility());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getProductId();
		result = 37 * result + this.getVisibility();
		return result;
	}

}