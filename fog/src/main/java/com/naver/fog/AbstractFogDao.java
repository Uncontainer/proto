package com.naver.fog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public abstract class AbstractFogDao {
	@Autowired
	protected JdbcTemplate jdbcTemplate;

	protected <T extends Resource> List<T> selectByIds(String sql, List<Long> ids, RowMapper<T> rowMapper) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyList();
		}

		StringBuilder inClause = new StringBuilder(2 * ids.size());
		inClause.append("?");
		for (int i = 1; i < ids.size(); i++) {
			inClause.append(",?");
		}

		return jdbcTemplate.query(sql + " WHERE id IN (" + inClause + ")", ids.toArray(new Long[ids.size()]), rowMapper);
	}

	protected static final RowMapper<Long> SINGLE_LONG_VALUE_ROW_MAPPER = new RowMapper<Long>() {
		public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getLong(1);
		}
	};
}
