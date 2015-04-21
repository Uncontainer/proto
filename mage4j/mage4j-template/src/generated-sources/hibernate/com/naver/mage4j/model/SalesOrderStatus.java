package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * SalesOrderStatus generated by hbm2java
 */
@Entity
@Table(name = "sales_order_status"
	, catalog = "magento")
public class SalesOrderStatus implements java.io.Serializable {

	private String status;
	private String label;
	private Set<SalesOrderStatusState> salesOrderStatusStates = new HashSet<SalesOrderStatusState>(0);
	private Set<SalesOrderStatusLabel> salesOrderStatusLabels = new HashSet<SalesOrderStatusLabel>(0);

	public SalesOrderStatus() {
	}

	public SalesOrderStatus(String status, String label) {
		this.status = status;
		this.label = label;
	}

	public SalesOrderStatus(String status, String label, Set<SalesOrderStatusState> salesOrderStatusStates, Set<SalesOrderStatusLabel> salesOrderStatusLabels) {
		this.status = status;
		this.label = label;
		this.salesOrderStatusStates = salesOrderStatusStates;
		this.salesOrderStatusLabels = salesOrderStatusLabels;
	}

	@Id
	@Column(name = "status", unique = true, nullable = false, length = 32)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "label", nullable = false, length = 128)
	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salesOrderStatus")
	public Set<SalesOrderStatusState> getSalesOrderStatusStates() {
		return this.salesOrderStatusStates;
	}

	public void setSalesOrderStatusStates(Set<SalesOrderStatusState> salesOrderStatusStates) {
		this.salesOrderStatusStates = salesOrderStatusStates;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salesOrderStatus")
	public Set<SalesOrderStatusLabel> getSalesOrderStatusLabels() {
		return this.salesOrderStatusLabels;
	}

	public void setSalesOrderStatusLabels(Set<SalesOrderStatusLabel> salesOrderStatusLabels) {
		this.salesOrderStatusLabels = salesOrderStatusLabels;
	}

}
