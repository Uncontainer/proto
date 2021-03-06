package com.naver.mage4j.core.mage.core.model.resource.model;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * DownloadableLinkPurchased generated by hbm2java
 */
@Entity
@Table(name = "downloadable_link_purchased"
	, catalog = "magento")
public class DownloadableLinkPurchased implements java.io.Serializable {

	private Integer purchasedId;
	private CustomerEntity customerEntity;
	private SalesFlatOrder salesFlatOrder;
	private String orderIncrementId;
	private int orderItemId;
	private Date createdAt;
	private Date updatedAt;
	private String productName;
	private String productSku;
	private String linkSectionTitle;
	private Set<DownloadableLinkPurchasedItem> downloadableLinkPurchasedItems = new HashSet<DownloadableLinkPurchasedItem>(0);

	public DownloadableLinkPurchased() {
	}

	public DownloadableLinkPurchased(int orderItemId, Date createdAt, Date updatedAt) {
		this.orderItemId = orderItemId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public DownloadableLinkPurchased(CustomerEntity customerEntity, SalesFlatOrder salesFlatOrder, String orderIncrementId, int orderItemId, Date createdAt, Date updatedAt, String productName, String productSku, String linkSectionTitle, Set<DownloadableLinkPurchasedItem> downloadableLinkPurchasedItems) {
		this.customerEntity = customerEntity;
		this.salesFlatOrder = salesFlatOrder;
		this.orderIncrementId = orderIncrementId;
		this.orderItemId = orderItemId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.productName = productName;
		this.productSku = productSku;
		this.linkSectionTitle = linkSectionTitle;
		this.downloadableLinkPurchasedItems = downloadableLinkPurchasedItems;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "purchased_id", unique = true, nullable = false)
	public Integer getPurchasedId() {
		return this.purchasedId;
	}

	public void setPurchasedId(Integer purchasedId) {
		this.purchasedId = purchasedId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	public CustomerEntity getCustomerEntity() {
		return this.customerEntity;
	}

	public void setCustomerEntity(CustomerEntity customerEntity) {
		this.customerEntity = customerEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	public SalesFlatOrder getSalesFlatOrder() {
		return this.salesFlatOrder;
	}

	public void setSalesFlatOrder(SalesFlatOrder salesFlatOrder) {
		this.salesFlatOrder = salesFlatOrder;
	}

	@Column(name = "order_increment_id", length = 50)
	public String getOrderIncrementId() {
		return this.orderIncrementId;
	}

	public void setOrderIncrementId(String orderIncrementId) {
		this.orderIncrementId = orderIncrementId;
	}

	@Column(name = "order_item_id", nullable = false)
	public int getOrderItemId() {
		return this.orderItemId;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
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
	@Column(name = "updated_at", nullable = false, length = 19)
	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Column(name = "product_name")
	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "product_sku")
	public String getProductSku() {
		return this.productSku;
	}

	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}

	@Column(name = "link_section_title")
	public String getLinkSectionTitle() {
		return this.linkSectionTitle;
	}

	public void setLinkSectionTitle(String linkSectionTitle) {
		this.linkSectionTitle = linkSectionTitle;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "downloadableLinkPurchased")
	public Set<DownloadableLinkPurchasedItem> getDownloadableLinkPurchasedItems() {
		return this.downloadableLinkPurchasedItems;
	}

	public void setDownloadableLinkPurchasedItems(Set<DownloadableLinkPurchasedItem> downloadableLinkPurchasedItems) {
		this.downloadableLinkPurchasedItems = downloadableLinkPurchasedItems;
	}

}
