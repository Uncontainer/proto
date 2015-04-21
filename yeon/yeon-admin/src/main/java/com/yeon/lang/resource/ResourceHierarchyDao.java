package com.yeon.lang.resource;

import com.yeon.lang.AbstractYeonDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ResourceHierarchyDao extends AbstractYeonDao {
	public int insert(String id, String parentId, int order) {
		String sql = "INSERT INTO resource_hierarchy (id, parent_id, order_no, last_updated) VALUES(?, ?, ?, NOW())";
		return jdbcTemplate.update(sql, id, parentId, order);
	}

	public List<String> selectParentIds(String id) {
		String sql = "SELECT parent_id FROM resource_hierarchy WHERE id=? ORDER BY order_no ASC";

		return jdbcTemplate.query(sql, new Object[] { id }, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		});
	}
}
