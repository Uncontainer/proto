package com.yeon.lang.clazz;

import com.yeon.lang.AbstractYeonDao;
import com.yeon.lang.impl.MapClass;
import org.springframework.stereotype.Component;

@Component
public class MapClassDao extends AbstractYeonDao {
	public int insert(MapClass clazz) {
		String sql = "INSERT INTO class(id, last_updated) VALUES(?, NOW())";

		return jdbcTemplate.update(sql, clazz.getId());
	}

	public MapClass select(String resourceId) {
		return new MapClass(resourceId);
		// String sql = "SELECT id FROM class WHERE id=?";
		// return jdbcTemplate.query(sql, new Object[] { resourceId }, new ResultSetExtractor<MapClass>() {
		// @Override
		// public MapClass extractData(ResultSet rs) throws SQLException, DataAccessException {
		// if (!rs.next()) {
		// return null;
		// } else {
		// MapClass resource = new MapClass();
		// resource.setId(rs.getString(1));
		//
		// return resource;
		// }
		// }
		// });
	}

	public int delete(String classId) {
		String sql = "DELETE FROM class WHERE id=?";

		return jdbcTemplate.update(sql, classId);
	}
}
