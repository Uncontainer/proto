package com.naver.mage4j.core.mage.core.model.resource.model;

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

import com.naver.mage4j.core.mage.core.model.resource.store.Store;

/**
 * EavEntityStore generated by hbm2java
 */
@Entity
@Table(name = "eav_entity_store"
	, catalog = "magento")
public class EavEntityStore implements java.io.Serializable {

	private Integer entityStoreId;
	private EavEntityType eavEntityType;
	private Store coreStore;
	private String incrementPrefix;
	private String incrementLastId;

	public EavEntityStore() {
	}

	public EavEntityStore(EavEntityType eavEntityType, Store coreStore) {
		this.eavEntityType = eavEntityType;
		this.coreStore = coreStore;
	}

	public EavEntityStore(EavEntityType eavEntityType, Store coreStore, String incrementPrefix, String incrementLastId) {
		this.eavEntityType = eavEntityType;
		this.coreStore = coreStore;
		this.incrementPrefix = incrementPrefix;
		this.incrementLastId = incrementLastId;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "entity_store_id", unique = true, nullable = false)
	public Integer getEntityStoreId() {
		return this.entityStoreId;
	}

	public void setEntityStoreId(Integer entityStoreId) {
		this.entityStoreId = entityStoreId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_type_id", nullable = false)
	public EavEntityType getEavEntityType() {
		return this.eavEntityType;
	}

	public void setEavEntityType(EavEntityType eavEntityType) {
		this.eavEntityType = eavEntityType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	public Store getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(Store coreStore) {
		this.coreStore = coreStore;
	}

	@Column(name = "increment_prefix", length = 20)
	public String getIncrementPrefix() {
		return this.incrementPrefix;
	}

	public void setIncrementPrefix(String incrementPrefix) {
		this.incrementPrefix = incrementPrefix;
	}

	@Column(name = "increment_last_id", length = 50)
	public String getIncrementLastId() {
		return this.incrementLastId;
	}

	public void setIncrementLastId(String incrementLastId) {
		this.incrementLastId = incrementLastId;
	}

}
