package com.yeon.lang.value.locale;

import com.yeon.lang.AbstractYeonDao;
import com.yeon.lang.ResourceType;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
public class LocalValueDao extends AbstractYeonDao {
	public long insert(LocalValueType type, LocalValueEntity entity) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new InsertStatementCreator(type, entity), keyHolder);

		return keyHolder.getKey().longValue();
	}

	private static class InsertStatementCreator implements PreparedStatementCreator {
		LocalValueEntity entity;
		LocalValueType type;

		private InsertStatementCreator(LocalValueType type, LocalValueEntity entity) {
			super();
			this.type = type;
			this.entity = entity;
		}

		@Override
		public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
			String sql = "INSERT INTO " + type.getTableName() + "(resource_type, resource_id, locale, value_cont, last_updated) VALUES(?, ?, ?, ?, NOW())";

			PreparedStatement ps = con.prepareStatement(sql, new String[] { "id" });
			ps.setString(1, String.valueOf(entity.getResourceType().getCode()));
			ps.setString(2, entity.getResourceId());
			ps.setString(3, entity.getLocale());
			ps.setString(4, entity.getValue());

			return ps;
		}

	}

	public int update(LocalValueType type, long id, String value) {
		// TODO last_updated 업데이트 해야할 지 생각해볼 것.
		String sql = "UPDATE " + type.getTableName() + " SET value_cont=?, last_updated=NOW() WHERE id=?";

		return jdbcTemplate.update(sql, value, id);
	}

	public int delete(LocalValueType type, long id) {
		String query = "DELETE FROM " + type.getTableName() + " WHERE id=?";

		return jdbcTemplate.update(query, id);
	}

	public LocalValueEntity select(LocalValueType type, long id) {
		String sql = "SELECT id, resource_type, resource_id, locale, value_cont, last_updated FROM " + type.getTableName() + " WHERE id=?";

		return jdbcTemplate.query(sql, new Object[] { id }, new ResultSetExtractor<LocalValueEntity>() {
			@Override
			public LocalValueEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (!rs.next()) {
					return null;
				}

				LocalValueEntity entity = new LocalValueEntity();
				entity.setId(rs.getLong(1));
				entity.setResourceType(ResourceType.fromCode(rs.getString(2)));
				entity.setResourceId(rs.getString(3));
				entity.setLocale(rs.getString(4));
				entity.setValue(rs.getString(5));
				entity.setCreateDate(rs.getTimestamp(6));

				return entity;
			}
		});
	}

	public List<LocalValueEntity> selectAllByResourceId(LocalValueType type, String resourceId) {
		String sql = "SELECT id, resource_type, resource_id, locale, value_cont, last_updated FROM " + type.getTableName() + " WHERE resource_id=?";

		return selectAll(sql, resourceId);
	}

	public Page<LocalValueEntity> selectAllByPrefix(LocalValueType type, ResourceType resourceType, String valuePrefix, Pageable pageable) {
		String sql = "SELECT id, resource_type, resource_id, locale, value_cont, last_updated FROM " + type.getTableName() + " WHERE resource_type=? AND value_cont like ? LIMIT ?, ?";
		List<LocalValueEntity> localValues = selectAll(sql, String.valueOf(resourceType.getCode()), valuePrefix + "%", pageable.getOffset(), pageable.getPageSize());

		String countSql = "SELECT count(*) FROM " + type.getTableName() + " WHERE resource_type=? AND value_cont like ?";
		int total = jdbcTemplate.queryForInt(countSql, String.valueOf(resourceType.getCode()), valuePrefix + "%");

		return new PageImpl<LocalValueEntity>(localValues, pageable, total);
	}

	public List<LocalValueEntity> selectAllByValue(LocalValueType type, ResourceType resourceType, String locale, String value) {
		String sql = "SELECT id, resource_type, resource_id, locale, value_cont, last_updated FROM " + type.getTableName() + " WHERE resource_type=? AND locale=? AND value_cont=?";
		return selectAll(sql, String.valueOf(resourceType.getCode()), locale, value);
	}

	public List<LocalValueEntity> selectAllByLocale(LocalValueType type, String resourceId, String locale) {
		String sql = "SELECT id, resource_type, resource_id, locale, value_cont, last_updated FROM " + type.getTableName() + " WHERE resource_id=? AND locale=?";

		return selectAll(sql, resourceId, locale);
	}

	public LocalValueEntity selectByValue(LocalValueType type, String resourceId, String locale, String value) {
		String sql = "SELECT id, resource_type, resource_id, locale, value_cont, last_updated FROM " + type.getTableName() + " WHERE resource_id=? AND locale=? AND value_cont=?";

		return jdbcTemplate.query(sql, new Object[] { resourceId, locale, value }, new ResultSetExtractor<LocalValueEntity>() {
			@Override
			public LocalValueEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (!rs.next()) {
					return null;
				}

				LocalValueEntity entity = new LocalValueEntity();
				entity.setId(rs.getLong(1));
				entity.setResourceType(ResourceType.fromCode(rs.getString(2)));
				entity.setResourceId(rs.getString(3));
				entity.setLocale(rs.getString(4));
				entity.setValue(rs.getString(5));
				entity.setCreateDate(rs.getTimestamp(6));

				return entity;
			}
		});
	}

	private List<LocalValueEntity> selectAll(String sql, Object... args) {
		return jdbcTemplate.query(sql, args, new RowMapper<LocalValueEntity>() {
			@Override
			public LocalValueEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
				LocalValueEntity entity = new LocalValueEntity();
				entity.setId(rs.getLong(1));
				entity.setResourceType(ResourceType.fromCode(rs.getString(2)));
				entity.setResourceId(rs.getString(3));
				entity.setLocale(rs.getString(4));
				entity.setValue(rs.getString(5));
				entity.setCreateDate(rs.getTimestamp(6));

				return entity;
			}
		});
	}
}
