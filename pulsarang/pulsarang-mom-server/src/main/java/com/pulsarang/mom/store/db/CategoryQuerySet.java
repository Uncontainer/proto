package com.pulsarang.mom.store.db;

import com.pulsarang.mom.store.EventStore;

public class CategoryQuerySet implements QuerySet {

	final String category;

	final String tableCreateQuery;
	final String countQuery;
	final String insertQuery;
	final String updateQuery;
	final String listQuery;
	final String deleteQuery;
	final String deleteAllQuery;

	public CategoryQuerySet(String category) {
		this.category = category;

		this.tableCreateQuery = "CREATE TABLE IF NOT EXISTS queue (" + //
				" id BIGINT AUTO_INCREMENT PRIMARY KEY, " + //
				" category VARCHAR2(50) NOT NULL, " + //
				" status INT NOT NULL default " + EventStore.STATUS_WAIT + ", " + //
				" message VARCHAR2(20000) NOT NULL" + ")\n";

		this.countQuery = String.format("SELECT count(id) FROM queue WHERE status=? AND category='%s'", category);
		this.insertQuery = String.format("INSERT INTO queue(category, message) VALUES('%s', ?)", category);
		this.updateQuery = String.format("UPDATE queue SET status=? WHERE id=? AND category='%s'", category);
		this.listQuery = String.format("SELECT id, status, message FROM queue WHERE status=? AND category='%s' ORDER BY id ASC LIMIT ?", category);
		this.deleteQuery = String.format("DELETE FROM queue WHERE id=? AND category='%s'", category);
		this.deleteAllQuery = String.format("DELETE FROM queue WHERE category='%s'", category);
	}

	@Override
	public String table() {
		return tableCreateQuery;
	}

	@Override
	public String count() {
		return countQuery;
	}

	@Override
	public String insert() {
		return insertQuery;

	}

	@Override
	public String updateStatus() {
		return updateQuery;
	}

	@Override
	public String list() {
		return listQuery;
	}

	@Override
	public String delete() {
		return deleteQuery;
	}

	@Override
	public String deleteAll() {
		return deleteAllQuery;
	}

}
