package com.pulsarang.mom.store.db;

import com.pulsarang.mom.store.EventStore;

public class BasicQuerySet implements QuerySet {

	@Override
	public String table() {
		return "CREATE TABLE IF NOT EXISTS queue (" + //
				" id BIGINT AUTO_INCREMENT PRIMARY KEY" + //
				" , status INT NOT NULL default " + EventStore.STATUS_WAIT + "" + //
				" , message VARCHAR2(65000) NOT NULL" + ")\n";
	}

	@Override
	public String count() {
		return "SELECT count(id) FROM queue WHERE status=?";
	}

	@Override
	public String insert() {
		return "INSERT INTO queue(message) VALUES(?)";
	}

	@Override
	public String updateStatus() {
		return "UPDATE queue SET status=? WHERE id=?";
	}

	@Override
	public String list() {
		return "SELECT id, status, message FROM queue WHERE status=? ORDER BY id ASC LIMIT ?";
	}

	@Override
	public String delete() {
		return "DELETE FROM queue WHERE id=?";
	}

	@Override
	public String deleteAll() {
		return "TRUNCATE TABLE queue";
	}
}
