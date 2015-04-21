package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
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
 * SalesFlatShipmentTrack generated by hbm2java
 */
@Entity
@Table(name = "sales_flat_shipment_track"
	, catalog = "magento")
public class SalesFlatShipmentTrack implements java.io.Serializable {

	private Integer entityId;
	private SalesFlatShipment salesFlatShipment;
	private BigDecimal weight;
	private BigDecimal qty;
	private int orderId;
	private String trackNumber;
	private String description;
	private String title;
	private String carrierCode;
	private Date createdAt;
	private Date updatedAt;

	public SalesFlatShipmentTrack() {
	}

	public SalesFlatShipmentTrack(SalesFlatShipment salesFlatShipment, int orderId) {
		this.salesFlatShipment = salesFlatShipment;
		this.orderId = orderId;
	}

	public SalesFlatShipmentTrack(SalesFlatShipment salesFlatShipment, BigDecimal weight, BigDecimal qty, int orderId, String trackNumber, String description, String title, String carrierCode, Date createdAt, Date updatedAt) {
		this.salesFlatShipment = salesFlatShipment;
		this.weight = weight;
		this.qty = qty;
		this.orderId = orderId;
		this.trackNumber = trackNumber;
		this.description = description;
		this.title = title;
		this.carrierCode = carrierCode;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
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

	@Column(name = "weight", precision = 12, scale = 4)
	public BigDecimal getWeight() {
		return this.weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	@Column(name = "qty", precision = 12, scale = 4)
	public BigDecimal getQty() {
		return this.qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	@Column(name = "order_id", nullable = false)
	public int getOrderId() {
		return this.orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	@Column(name = "track_number", length = 65535)
	public String getTrackNumber() {
		return this.trackNumber;
	}

	public void setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
	}

	@Column(name = "description", length = 65535)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "title")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "carrier_code", length = 32)
	public String getCarrierCode() {
		return this.carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", length = 19)
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

}
