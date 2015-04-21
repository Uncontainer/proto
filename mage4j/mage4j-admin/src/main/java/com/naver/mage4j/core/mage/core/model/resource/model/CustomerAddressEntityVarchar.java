package com.naver.mage4j.core.mage.core.model.resource.model;

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

/**
 * CustomerAddressEntityVarchar generated by hbm2java
 */
@Entity
@Table(name = "customer_address_entity_varchar"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = {"entity_id", "attribute_id"}))
public class CustomerAddressEntityVarchar implements java.io.Serializable {

	private Integer valueId;
	private CustomerAddressEntity customerAddressEntity;
	private EavAttribute eavAttribute;
	private EavEntityType eavEntityType;
	private String value;

	public CustomerAddressEntityVarchar() {
	}

	public CustomerAddressEntityVarchar(CustomerAddressEntity customerAddressEntity, EavAttribute eavAttribute, EavEntityType eavEntityType) {
		this.customerAddressEntity = customerAddressEntity;
		this.eavAttribute = eavAttribute;
		this.eavEntityType = eavEntityType;
	}

	public CustomerAddressEntityVarchar(CustomerAddressEntity customerAddressEntity, EavAttribute eavAttribute, EavEntityType eavEntityType, String value) {
		this.customerAddressEntity = customerAddressEntity;
		this.eavAttribute = eavAttribute;
		this.eavEntityType = eavEntityType;
		this.value = value;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "value_id", unique = true, nullable = false)
	public Integer getValueId() {
		return this.valueId;
	}

	public void setValueId(Integer valueId) {
		this.valueId = valueId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_id", nullable = false)
	public CustomerAddressEntity getCustomerAddressEntity() {
		return this.customerAddressEntity;
	}

	public void setCustomerAddressEntity(CustomerAddressEntity customerAddressEntity) {
		this.customerAddressEntity = customerAddressEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attribute_id", nullable = false)
	public EavAttribute getEavAttribute() {
		return this.eavAttribute;
	}

	public void setEavAttribute(EavAttribute eavAttribute) {
		this.eavAttribute = eavAttribute;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_type_id", nullable = false)
	public EavEntityType getEavEntityType() {
		return this.eavEntityType;
	}

	public void setEavEntityType(EavEntityType eavEntityType) {
		this.eavEntityType = eavEntityType;
	}

	@Column(name = "value")
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
