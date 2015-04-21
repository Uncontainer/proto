package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * EavFormFieldsetLabelId generated by hbm2java
 */
@Embeddable
public class EavFormFieldsetLabelId implements java.io.Serializable {

	private short fieldsetId;
	private short storeId;

	public EavFormFieldsetLabelId() {
	}

	public EavFormFieldsetLabelId(short fieldsetId, short storeId) {
		this.fieldsetId = fieldsetId;
		this.storeId = storeId;
	}

	@Column(name = "fieldset_id", nullable = false)
	public short getFieldsetId() {
		return this.fieldsetId;
	}

	public void setFieldsetId(short fieldsetId) {
		this.fieldsetId = fieldsetId;
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
		if (!(other instanceof EavFormFieldsetLabelId))
			return false;
		EavFormFieldsetLabelId castOther = (EavFormFieldsetLabelId)other;

		return (this.getFieldsetId() == castOther.getFieldsetId())
			&& (this.getStoreId() == castOther.getStoreId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getFieldsetId();
		result = 37 * result + this.getStoreId();
		return result;
	}

}
