package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TagSummaryId generated by hbm2java
 */
@Embeddable
public class TagSummaryId implements java.io.Serializable {

	private int tagId;
	private short storeId;

	public TagSummaryId() {
	}

	public TagSummaryId(int tagId, short storeId) {
		this.tagId = tagId;
		this.storeId = storeId;
	}

	@Column(name = "tag_id", nullable = false)
	public int getTagId() {
		return this.tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
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
		if (!(other instanceof TagSummaryId))
			return false;
		TagSummaryId castOther = (TagSummaryId)other;

		return (this.getTagId() == castOther.getTagId())
			&& (this.getStoreId() == castOther.getStoreId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getTagId();
		result = 37 * result + this.getStoreId();
		return result;
	}

}
