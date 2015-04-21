package com.naver.mage4j.core.mage.core.model.resource.layout;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import static javax.persistence.GenerationType.*;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.naver.mage4j.core.mage.core.model.resource.model.WidgetInstancePageLayout;

/**
 * CoreLayoutUpdate generated by hbm2java
 */
@Entity
@Table(name = "core_layout_update"
	, catalog = "magento")
public class CoreLayoutUpdate implements java.io.Serializable {

	private Integer layoutUpdateId;
	private String handle;
	private String xml;
	private short sortOrder;
	private Set<WidgetInstancePageLayout> widgetInstancePageLayouts = new HashSet<WidgetInstancePageLayout>(0);
	private Set<CoreLayoutLink> coreLayoutLinks = new HashSet<CoreLayoutLink>(0);

	private transient CoreLayoutUpdateHelper helper = new CoreLayoutUpdateHelper(this);

	public CoreLayoutUpdate() {
	}

	public CoreLayoutUpdate(short sortOrder) {
		this.sortOrder = sortOrder;
	}

	public CoreLayoutUpdate(String handle, String xml, short sortOrder, Set<WidgetInstancePageLayout> widgetInstancePageLayouts, Set<CoreLayoutLink> coreLayoutLinks) {
		this.handle = handle;
		this.xml = xml;
		this.sortOrder = sortOrder;
		this.widgetInstancePageLayouts = widgetInstancePageLayouts;
		this.coreLayoutLinks = coreLayoutLinks;
	}

	@Transient
	public CoreLayoutUpdateHelper getHelper() {
		return helper;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "layout_update_id", unique = true, nullable = false)
	public Integer getLayoutUpdateId() {
		return this.layoutUpdateId;
	}

	public void setLayoutUpdateId(Integer layoutUpdateId) {
		this.layoutUpdateId = layoutUpdateId;
	}

	@Column(name = "handle")
	public String getHandle() {
		return this.handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	@Column(name = "xml", length = 65535, columnDefinition = "TEXT")
	public String getXml() {
		return this.xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	@Column(name = "sort_order", nullable = false)
	public short getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(short sortOrder) {
		this.sortOrder = sortOrder;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreLayoutUpdate")
	public Set<WidgetInstancePageLayout> getWidgetInstancePageLayouts() {
		return this.widgetInstancePageLayouts;
	}

	public void setWidgetInstancePageLayouts(Set<WidgetInstancePageLayout> widgetInstancePageLayouts) {
		this.widgetInstancePageLayouts = widgetInstancePageLayouts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreLayoutUpdate")
	public Set<CoreLayoutLink> getCoreLayoutLinks() {
		return this.coreLayoutLinks;
	}

	public void setCoreLayoutLinks(Set<CoreLayoutLink> coreLayoutLinks) {
		this.coreLayoutLinks = coreLayoutLinks;
	}

}
