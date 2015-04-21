package com.yeon.infra.remote.table;

import java.util.Collections;
import java.util.List;

public class Table {
	private final TableInfo tableInfo;
	private final String refreshUrl;
	private List<List<String>> rows;

	public Table(TableInfo tableInfo, String refreshUrl, List<List<String>> rows) {
		this.tableInfo = tableInfo;
		this.refreshUrl = refreshUrl;
		if (rows == null) {
			this.rows = Collections.emptyList();
		} else {
			this.rows = rows;
		}
	}

	public String getName() {
		return tableInfo.name;
	}

	public List<Link> getLinks() {
		return tableInfo.links;
	}

	public List<Column> getColumns() {
		return tableInfo.columns;
	}

	public String getRefreshUrl() {
		return refreshUrl;
	}

	public List<List<String>> getRows() {
		return rows;
	}
}
