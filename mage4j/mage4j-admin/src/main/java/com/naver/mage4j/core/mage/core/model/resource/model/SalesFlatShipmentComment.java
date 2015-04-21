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

/**
 * SalesFlatShipmentComment generated by hbm2java
 */
@Entity
@Table(name = "sales_flat_shipment_comment"
	, catalog = "magento")
public class SalesFlatShipmentComment implements java.io.Serializable {

	private Integer entityId;
	private SalesFlatShipment salesFlatShipment;
	private Integer isCustomerNotified;
	private short isVisibleOnFront;
	private String comment;
	private Date createdAt;

	public SalesFlatShipmentComment() {
	}

	public SalesFlatShipmentComment(SalesFlatShipment salesFlatShipment, short isVisibleOnFront) {
		this.salesFlatShipment = salesFlatShipment;
		this.isVisibleOnFront = isVisibleOnFront;
	}

	public SalesFlatShipmentComment(SalesFlatShipment salesFlatShipment, Integer isCustomerNotified, short isVisibleOnFront, String comment, Date createdAt) {
		this.salesFlatShipment = salesFlatShipment;
		this.isCustomerNotified = isCustomerNotified;
		this.isVisibleOnFront = isVisibleOnFront;
		this.comment = comment;
		this.createdAt = createdAt;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "entity_id", unique = true, nullable = false)
	public Integer getEntityId() {
		return this.entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", nullable = false)
	public SalesFlatShipment getSalesFlatShipment() {
		return this.salesFlatShipment;
	}

	public void setSalesFlatShipment(SalesFlatShipment salesFlatShipment) {
		this.salesFlatShipment = salesFlatShipment;
	}

	@Column(name = "is_customer_notified")
	public Integer getIsCustomerNotified() {
		return this.isCustomerNotified;
	}

	public void setIsCustomerNotified(Integer isCustomerNotified) {
		this.isCustomerNotified = isCustomerNotified;
	}

	@Column(name = "is_visible_on_front", nullable = false)
	public short getIsVisibleOnFront() {
		return this.isVisibleOnFront;
	}

	public void setIsVisibleOnFront(short isVisibleOnFront) {
		this.isVisibleOnFront = isVisibleOnFront;
	}

	@Column(name = "comment", length = 65535, columnDefinition = "TEXT")
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", length = 19)
	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}