package com.pulsarang.infra.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.stereotype.Component;

@Component
public class SerialGenerator {
	@Resource(name = "dataSourceForLock")
	private DataSource dataSource;

	public long current(String name) throws SQLException {
		Connection connection = dataSource.getConnection();
		try {
			Statement stmt = connection.createStatement();
			try {
				long currentSerial;
				ResultSet rs = stmt.executeQuery(String.format("SELECT srl_no FROM cmp_serial WHERE srl_nm='%s'", name));
				try {
					if (!rs.next()) {
						currentSerial = 0;
					} else {
						currentSerial = rs.getLong(1);
					}
				} finally {
					rs.close();
				}

				return currentSerial;
			} finally {
				stmt.close();
			}
		} finally {
			connection.close();
		}
	}

	public long next(String name, int amount) throws SQLException {
		if (amount < 1 || amount > 10000) {
			throw new IllegalArgumentException("Expect between 1 and 10000.");
		}

		Connection connection = dataSource.getConnection();
		try {
			Statement stmt = connection.createStatement();

			try {
				long serial = nextSerial(stmt, name, amount);
				if (serial == 0) {
					insertSerial(stmt, name);
					serial = nextSerial(stmt, name, amount);
				}

				if (serial == 0) {
					return 0;
				}

				return serial - amount;
			} finally {
				stmt.close();
			}

		} finally {
			connection.close();
		}
	}

	private long nextSerial(Statement stmt, String name, int amount) throws SQLException {
		stmt.executeUpdate(String.format("UPDATE cmp_serial SET srl_no=LAST_INSERT_ID(srl_no+%d) WHERE srl_nm='%s'", amount, name));
		ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
		try {
			if (!rs.next()) {
				return 0;
			}

			return rs.getLong(1);
		} finally {
			rs.close();
		}
	}

	private void insertSerial(Statement stmt, String name) throws SQLException {
		stmt.executeUpdate(String.format("INSERT INTO cmp_serial(srl_nm, srl_no) VALUES ('%s', 1)", name));
	}
}
