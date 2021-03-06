package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * EavFormFieldsetLabel generated by hbm2java
 */
@Entity
@Table(name = "eav_form_fieldset_label"
	, catalog = "magento")
public class EavFormFieldsetLabel implements java.io.Serializable {

	private EavFormFieldsetLabelId id;
	private EavFormFieldset eavFormFieldset;
	private CoreStore coreStore;
	private String label;

	public EavFormFieldsetLabel() {
	}

	public EavFormFieldsetLabel(EavFormFieldsetLabelId id, EavFormFieldset eavFormFieldset, CoreStore coreStore, String label) {
		this.id = id;
		this.eavFormFieldset = eavFormFieldset;
		this.coreStore = coreStore;
		this.label = label;
	}

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "fieldsetId", column = @Column(name = "fieldset_id", nullable = false)),
		@AttributeOverride(name = "storeId", column = @Column(name = "store_id", nullable = false))})
	public EavFormFieldsetLabelId getId() {
		return this.id;
	}

	public void setId(EavFormFieldsetLabelId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fieldset_id", nullable = false, insertable = false, updatable = false)
	public EavFormFieldset getEavFormFieldset() {
		return this.eavFormFieldset;
	}

	public void setEavFormFieldset(EavFormFieldset eavFormFieldset) {
		this.eavFormFieldset = eavFormFieldset;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false, insertable = false, updatable = false)
	public CoreStore getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(CoreStore coreStore) {
		this.coreStore = coreStore;
	}

	@Column(name = "label", nullable = false)
	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
