package com.pulsarang.mom.store.db.h2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.pulsarang.mom.store.db.BasicQuerySet;
import com.pulsarang.mom.store.db.CategoryQuerySet;
import com.pulsarang.mom.store.db.DbEventStore;
import com.pulsarang.mom.store.db.QuerySet;

public class H2EventStore extends DbEventStore {
	String url;

	public H2EventStore(String name) {
		this(name, new BasicQuerySet());
	}

	public H2EventStore(String name, String category) {
		this(name, new CategoryQuerySet(category));
	}

	protected H2EventStore(String name, QuerySet querySet) {
		super(name, querySet);

		url = "jdbc:h2:~/.nmp/mom/db/" + name;
	}

	@Override
	protected Connection createConnection() {
		Connection conn;
		try {
			conn = DriverManager.getConnection(url, "sa", "");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return conn;
	}

	@Override
	protected void init() {
		try {
			Class.forName("org.h2.Driver");
			Connection conn = createConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(querySet.table());
			stmt.close();
			conn.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
