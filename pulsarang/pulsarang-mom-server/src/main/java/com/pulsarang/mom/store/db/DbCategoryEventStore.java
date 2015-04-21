package com.pulsarang.mom.store.db;

public abstract class DbCategoryEventStore extends DbEventStore {
	final String category;

	public DbCategoryEventStore(String name, String category) {
		super(name, new CategoryQuerySet(category));
		this.category = category;
	}
}
