package com.yeon.lang.property;

import com.yeon.lang.AbstractYeonDao;
import com.yeon.lang.impl.MapProperty;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MapPropertyDao extends AbstractYeonDao {
	public MapProperty select(String propertyId) {
		String sql = "SELECT id, domain_class_id, range_class_id FROM property WHERE id=?";

		return jdbcTemplate.query(sql, new Object[] { propertyId }, new ResultSetExtractor<MapProperty>() {
			@Override
			public MapProperty extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (!rs.next()) {
					return null;
				}

				MapProperty property = new MapProperty();
				property.setId(rs.getString(1));
				property.setDomainClassId(rs.getString(2));
				property.setRangeClassId(rs.getString(3));

				return property;
			}
		});
	}

	public List<MapProperty> selectByClassId(String classId) {
		String sql = "SELECT id, domain_class_id, range_class_id FROM property WHERE domain_class_id=?";

		return jdbcTemplate.query(sql, new Object[] { classId }, new RowMapper<MapProperty>() {
			@Override
			public MapProperty mapRow(ResultSet rs, int rowNum) throws SQLException {
				MapProperty property = new MapProperty();
				property.setId(rs.getString(1));
				property.setDomainClassId(rs.getString(2));
				property.setRangeClassId(rs.getString(3));

				return property;
			}
		});
	}

	public int delete(String propertyId) {
		String sql = "DELETE FROM property WHERE id=?";

		return jdbcTemplate.update(sql, propertyId);
	}

	public void insert(final MapProperty property) {
		String sql = "INSERT INTO property(id, domain_class_id, range_class_id, last_updated) VALUES(?, ?, ?, NOW())";

		jdbcTemplate.update(sql, property.getId(), property.getDomainClassId(), property.getRangeClassId());
	}

}
