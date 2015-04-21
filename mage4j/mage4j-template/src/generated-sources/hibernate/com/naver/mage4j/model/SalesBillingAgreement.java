package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SalesBillingAgreement generated by hbm2java
 */
@Entity
@Table(name = "sales_billing_agreement"
	, catalog = "magento")
public class SalesBillingAgreement implements java.io.Serializable {

	private Integer agreementId;
	private CustomerEntity customerEntity;
	private CoreStore coreStore;
	private String methodCode;
	private String referenceId;
	private String status;
	private Date createdAt;
	private Date updatedAt;
	private String agreementLabel;
	private Set<SalesFlatOrder> salesFlatOrders = new HashSet<SalesFlatOrder>(0);

	public SalesBillingAgreement() {
	}

	public SalesBillingAgreement(CustomerEntity customerEntity, String methodCode, String referenceId, String status, Date createdAt) {
		this.customerEntity = customerEntity;
		this.methodCode = methodCode;
		this.referenceId = referenceId;
		this.status = status;
		this.createdAt = createdAt;
	}

	public SalesBillingAgreement(CustomerEntity customerEntity, CoreStore coreStore, String methodCode, String referenceId, String status, Date createdAt, Date updatedAt, String agreementLabel, Set<SalesFlatOrder> salesFlatOrders) {
		this.customerEntity = customerEntity;
		this.coreStore = coreStore;
		this.methodCode = methodCode;
		this.referenceId = referenceId;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.agreementLabel = agreementLabel;
		this.salesFlatOrders = salesFlatOrders;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "agreement_id", unique = true, nullable = false)
	public Integer getAgreementId() {
		return this.agreementId;
	}

	public void setAgreementId(Integer agreementId) {
		this.agreementId = agreementId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	public CustomerEntity getCustomerEntity() {
		return this.customerEntity;
	}

	public void setCustomerEntity(CustomerEntity customerEntity) {
		this.customerEntity = customerEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	public CoreStore getCoreStore() {
		return this.coreStore;
	}

	public void setCoreStore(CoreStore coreStore) {
		this.coreStore = coreStore;
	}

	@Column(name = "method_code", nullable = false, length = 32)
	public String getMethodCode() {
		return this.methodCode;
	}

	public void setMethodCode(String methodCode) {
		this.methodCode = methodCode;
	}

	@Column(name = "reference_id", nullable = false, length = 32)
	public String getReferenceId() {
		return this.referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	@Column(name = "status", nullable = false, length = 20)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false, length = 19)
	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", length = 19)
	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Column(name = "agreement_label")
	public String getAgreementLabel() {
		return this.agreementLabel;
	}

	public void setAgreementLabel(String agreementLabel) {
		this.agreementLabel = agreementLabel;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sales_billing_agreement_order", catalog = "magento", joinColumns = {
		@JoinColumn(name = "agreement_id", nullable = false, updatable = false)}, inverseJoinColumns = {
		@JoinColumn(name = "order_id", nullable = false, updatable = false)})
	public Set<SalesFlatOrder> getSalesFlatOrders() {
		return this.salesFlatOrders;
	}

	public void setSalesFlatOrders(Set<SalesFlatOrder> salesFlatOrders) {
		this.salesFlatOrders = salesFlatOrders;
	}

}
