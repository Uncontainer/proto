package com.naver.fog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public abstract class AbstractResourceDao<T extends Resource> extends AbstractFogDao {
	final String tableName;

	protected AbstractResourceDao(String tableName) {
		super();
		this.tableName = tableName;
	}

	protected final ResultSetExtractor<T> defaultResultSetExtractor = new ResultSetExtractor<T>() {
		public T extractData(ResultSet rs) throws SQLException, DataAccessException {
			if (!rs.next()) {
				return null;
			}

			return fromResultSet(rs);
		}
	};

	protected final RowMapper<T> defaultRowMapper = new RowMapper<T>() {
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {
			return fromResultSet(rs);
		}
	};

	public void insert(T resource) {
		if (resource.getId() != Resource.NULL_ID) {
			throw new IllegalArgumentException("Not Null resource ID");
		}

		insertChecked(resource);
	}

	protected long insert(String sql, int[] types, Object[] params) {
		SqlUpdate insert = new SqlUpdate(jdbcTemplate.getDataSource(), sql, types);
		insert.setReturnGeneratedKeys(true);
		insert.setGeneratedKeysColumnNames(new String[] {"id"});
		insert.compile();
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		insert.update(params, keyHolder);
		return keyHolder.getKey().longValue();
	}

	protected abstract void insertChecked(T resource);

	public int update(T resource) {
		if (resource == null) {
			return 0;
		}

		if (resource.getId() == Resource.NULL_ID) {
			throw new IllegalArgumentException("Null resource ID");
		}

		return updateChecked(resource);
	}

	protected abstract int updateChecked(T resource);

	public int deleteById(long id) {
		if (id == Resource.NULL_ID) {
			return 0;
		}

		return jdbcTemplate.update("DELETE FROM " + tableName + " WHERE id=?", id);
	}

	protected abstract T fromResultSet(ResultSet rs) throws SQLException;

	public T selectById(long id) {
		if (id == Resource.NULL_ID) {
			return null;
		}

		return jdbcTemplate.query("SELECT * FROM " + tableName + " WHERE id=?", new Object[] {id}, defaultResultSetExtractor);
	}

	public List<T> selectByIds(List<Long> ids) {
		return selectByIds("SELECT * FROM " + tableName, ids, defaultRowMapper);
	}

	public List<T> selectLatest(int count) {
		return jdbcTemplate.query("SELECT * FROM " + tableName + " ORDER BY id desc LIMIT ?", new Object[] {count}, defaultRowMapper);
	}

	public List<Long> selectIdsByNamePrefix(String namePrefix, int count) {
		return jdbcTemplate.query("SELECT id FROM " + tableName + " WHERE name LIKE ? LIMIT ?", new Object[] {namePrefix + "%", count}, SINGLE_LONG_VALUE_ROW_MAPPER);
	}

	protected static <T extends Resource> T fillResource(ResultSet rs, T resource) throws SQLException {
		resource.setId(rs.getLong("id"));
		resource.setName(rs.getString("name"));
		resource.setDescription(rs.getString("description"));
		resource.setCreatedDate(rs.getTimestamp("create_ymdt"));
		resource.setCreatorId(rs.getLong("creator_id"));

		return resource;
	}
}
