package com.pulsarang.infra.remote.table;

import java.util.ArrayList;
import java.util.List;

public class TableInfo {
	String name;
	List<Link> links;
	List<Column> columns;

	public TableInfo(String name) {
		this.name = name;
		this.columns = new ArrayList<Column>();
		this.links = new ArrayList<Link>();
	}

	public String getName() {
		return name;
	}

	public List<Link> getLinks() {
		return links;
	}

	public List<Column> getColumns() {
		return columns;
	}
}
