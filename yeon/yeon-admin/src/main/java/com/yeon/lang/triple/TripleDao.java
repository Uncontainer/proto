package com.yeon.lang.triple;

import com.yeon.lang.AbstractYeonDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class TripleDao extends AbstractYeonDao {
	public long insert(final Triple triple) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new InsertStatementCreator(triple), keyHolder);

		return keyHolder.getKey().longValue();
	}

	public Triple select(final long tripleId) {
		String sql = "SELECT subject_resource_id, property_id, object_value FROM triple WHERE id=?";
		return jdbcTemplate.query(sql, new Object[] { tripleId }, new ResultSetExtractor<Triple>() {
			@Override
			public Triple extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (!rs.next()) {
					return null;
				}

				Triple triple = new Triple();
				triple.setId(tripleId);
				triple.setSubject(rs.getString(1));
				triple.setPropertyId(rs.getString(2));
				triple.setObject(rs.getString(3));

				return triple;
			}
		});
	}

	public List<Triple> selectByResourceId(final String resourceId) {
		String sql = "SELECT id, property_id, object_value FROM triple WHERE subject_resource_id=?";
		return jdbcTemplate.query(sql, new Object[] { resourceId }, new RowMapper<Triple>() {
			@Override
			public Triple mapRow(ResultSet rs, int rowNum) throws SQLException {
				Triple triple = new Triple();
				triple.setPropertyId(resourceId);
				triple.setId(rs.getLong(1));
				triple.setSubject(rs.getString(2));
				triple.setObject(rs.getString(3));

				return triple;
			}
		});
	}

	private static class InsertStatementCreator implements PreparedStatementCreator {
		static final String sql = "INSERT INTO triple(subject_resource_id, property_id, object_value) VALUES(?, ?, ?)";

		Triple triple;

		private InsertStatementCreator(Triple triple) {
			super();
			this.triple = triple;
		}

		public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
			ps.setString(1, triple.getSubject());
			ps.setString(2, triple.getPropertyId());
			ps.setString(3, triple.getObject());
			return ps;
		}
	}

}
