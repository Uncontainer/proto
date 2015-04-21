package com.yeon.infra.util;

import com.yeon.lang.AbstractYeonDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SerialGenerator extends AbstractYeonDao {
	public static final long MIN_CUSTOM_SERIAL = (long) Integer.MAX_VALUE + 1;

	public long current(String name) {
		return jdbcTemplate.query("SELECT srl_no FROM cmp_serial WHERE srl_nm=?", new Object[] { name },
				new ResultSetExtractor<Long>() {
					@Override
					public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
						if (rs.next()) {
							return rs.getLong(1);
						} else {
							return MIN_CUSTOM_SERIAL;
						}
					}
				});
	}

	public long next(String name, int amount) {
		if (amount < 1 || amount > 10000) {
			throw new IllegalArgumentException("Expect between 1 and 10000.");
		}

		long serial = nextSerial(name, amount);
		if (serial == 0) {
			insertSerial(name);
			serial = nextSerial(name, amount);
			if (serial == 0) {
				throw new RuntimeException("Fail to reserve serial.");
			}
		}

		return serial - amount;
	}

	private long nextSerial(String name, int amount) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int updateCount = jdbcTemplate.update(new UpdateStatementCreator(name, amount), keyHolder);
		if (updateCount > 1) {
			return keyHolder.getKey().longValue();
		} else {
			return 0L;
		}
	}

	private void insertSerial(String name) {
		jdbcTemplate.update("INSERT INTO cmp_serial(srl_nm, srl_no) VALUES (?, ?)", name, MIN_CUSTOM_SERIAL);
	}

	private static class UpdateStatementCreator implements PreparedStatementCreator {
		static final String sql = "UPDATE cmp_serial SET srl_no=LAST_INSERT_ID(srl_no+?) WHERE srl_nm=?";

		String name;
		int amount;

		private UpdateStatementCreator(String name, int amount) {
			super();
			this.name = name;
			this.amount = amount;
		}

		public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] { "srl_no" });
			ps.setInt(1, amount);
			ps.setString(2, name);
			return ps;
		}
	}
}
