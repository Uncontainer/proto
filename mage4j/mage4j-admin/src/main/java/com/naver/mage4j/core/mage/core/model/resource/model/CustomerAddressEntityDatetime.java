package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * CustomerAddressEntityDatetime generated by hbm2java
 */
@Entity
@Table(name = "customer_address_entity_datetime"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = {"entity_id", "attribute_id"}))
public class CustomerAddressEntityDatetime implements java.io.Serializable {

	private Integer valueId;
	private CustomerAddressEntity customerAddressEntity;
	private EavAttribute eavAttribute;
	private EavEntityType eavEntityType;
	private Date value;

	public CustomerAddressEntityDatetime() {
	}

	public CustomerAddressEntityDatetime(CustomerAddressEntity customerAddressEntity, EavAttribute eavAttribute, EavEntityType eavEntityType, Date value) {
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "value", nullable = false, length = 19)
	public Date getValue() {
		return this.value;
	}

	public void setValue(Date value) {
		this.value = value;
	}

}
