package com.pulsarang.mom.store.db;

public interface QuerySet {
	String table();

	String count();

	String insert();

	String updateStatus();

	String list();

	String delete();

	String deleteAll();
}
