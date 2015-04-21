package com.naver.mage4j.core.mage.core.model.resource.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * CataloginventoryStock generated by hbm2java
 */
@Entity
@Table(name = "cataloginventory_stock"
	, catalog = "magento")
public class CataloginventoryStock implements java.io.Serializable {

	private Short stockId;
	private String stockName;
	private Set<CataloginventoryStockStatus> cataloginventoryStockStatuses = new HashSet<CataloginventoryStockStatus>(0);
	private Set<CataloginventoryStockItem> cataloginventoryStockItems = new HashSet<CataloginventoryStockItem>(0);

	public CataloginventoryStock() {
	}

	public CataloginventoryStock(String stockName, Set<CataloginventoryStockStatus> cataloginventoryStockStatuses, Set<CataloginventoryStockItem> cataloginventoryStockItems) {
		this.stockName = stockName;
		this.cataloginventoryStockStatuses = cataloginventoryStockStatuses;
		this.cataloginventoryStockItems = cataloginventoryStockItems;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "stock_id", unique = true, nullable = false)
	public Short getStockId() {
		return this.stockId;
	}

	public void setStockId(Short stockId) {
		this.stockId = stockId;
	}

	@Column(name = "stock_name")
	public String getStockName() {
		return this.stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cataloginventoryStock")
	public Set<CataloginventoryStockStatus> getCataloginventoryStockStatuses() {
		return this.cataloginventoryStockStatuses;
	}

	public void setCataloginventoryStockStatuses(Set<CataloginventoryStockStatus> cataloginventoryStockStatuses) {
		this.cataloginventoryStockStatuses = cataloginventoryStockStatuses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cataloginventoryStock")
	public Set<CataloginventoryStockItem> getCataloginventoryStockItems() {
		return this.cataloginventoryStockItems;
	}

	public void setCataloginventoryStockItems(Set<CataloginventoryStockItem> cataloginventoryStockItems) {
		this.cataloginventoryStockItems = cataloginventoryStockItems;
	}

}
