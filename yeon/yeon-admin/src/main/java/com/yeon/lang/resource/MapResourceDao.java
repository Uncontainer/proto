package com.yeon.lang.resource;

import com.yeon.lang.AbstractYeonDao;
import com.yeon.lang.impl.MapResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MapResourceDao extends AbstractYeonDao {
	public int insert(MapResource resource) {
		String sql = "INSERT INTO resource(id, type_class_id, last_updated) VALUES(?, ?, NOW())";

		return jdbcTemplate.update(sql, resource.getId(), resource.getTypeClassId());
	}

	public MapResource select(String resourceId) {
		String sql = "SELECT id, type_class_id FROM resource WHERE id=?";

		return jdbcTemplate.query(sql, new Object[] { resourceId }, new ResultSetExtractor<MapResource>() {
			@Override
			public MapResource extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (!rs.next()) {
					return null;
				} else {
					MapResource resource = new MapResource(rs.getString(2));
					resource.setId(rs.getString(1));

					return resource;
				}
			}
		});
	}

	public int update(MapResource resource) {
		String sql = "UPDATE resource SET type_class_id=? WHERE id=?";

		return jdbcTemplate.update(sql, resource.getTypeClassId(), resource.getId());
	}

	public Page<MapResource> selectByTypeClassId(String resourceId, Pageable pageable) {
		String sql = "SELECT id, type_class_id FROM resource WHERE type_class_id=? LIMIT ?,?";
		List<MapResource> resources = jdbcTemplate.query(sql, new Object[] { resourceId, pageable.getOffset(), pageable.getPageSize() }, new RowMapper<MapResource>() {
			@Override
			public MapResource mapRow(ResultSet rs, int rowNum) throws SQLException {
				MapResource resource = new MapResource(rs.getString(2));
				resource.setId(rs.getString(1));

				return resource;
			}
		});

		String countQuery = "SELECT id, type_class_id FROM resource WHERE type_class_id=?";
		int total = jdbcTemplate.queryForInt(countQuery, resourceId);

		return new PageImpl<MapResource>(resources, pageable, total);
	}
}
