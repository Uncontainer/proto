package com.naver.fog.frame.hierarchy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.naver.fog.AbstractFogDao;

@Component
public class FrameHierarchyDao extends AbstractFogDao {
	public void insert(FrameHierarchy frameHierarchy) {
		jdbcTemplate.update("INSERT INTO nm_frame_hierarchy SET frame_id=?, parent_frame_id=?, creator_id=?, create_ymdt=NOW()"
			, new Object[] {frameHierarchy.getFrameId(), frameHierarchy.getParentFrameId(), frameHierarchy.getCreatorId()});
	}

	public FrameHierarchy select(long frameId, long parentFrameId) {
		return jdbcTemplate.query("SELECT * FROM nm_frame_hierarchy WHERE frame_id=? AND parent_frame_id=?"
			, new Object[] {frameId, parentFrameId}
			, new ResultSetExtractor<FrameHierarchy>() {
				public FrameHierarchy extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (!rs.next()) {
						return null;
					}

					return fromResultSet(rs);
				}
			});
	}

	private static FrameHierarchy fromResultSet(ResultSet rs) throws SQLException {
		FrameHierarchy frameHierarchy = new FrameHierarchy();
		frameHierarchy.setFrameId(rs.getLong("frame_id"));
		frameHierarchy.setParentFrameId(rs.getLong("parent_frame_id"));
		frameHierarchy.setCreatorId(rs.getLong("creator_id"));
		frameHierarchy.setCreatedDate(rs.getTimestamp("create_ymdt"));

		return frameHierarchy;
	}

	public List<Long> selectParentIds(long frameId) {
		return jdbcTemplate.query("SELECT parent_frame_id FROM nm_frame_hierarchy WHERE frame_id=?", new Object[] {frameId}, SINGLE_LONG_VALUE_ROW_MAPPER);
	}

	public int delete(long frameId, long parentFrameId) {
		return jdbcTemplate.update("DELETE FROM nm_frame_hierarchy WHERE frame_id=? AND parent_frame_id=?", new Object[] {frameId, parentFrameId});
	}

	public int deleteByFrame(long frameId) {
		return jdbcTemplate.update("DELETE FROM nm_frame_hierarchy WHERE frame_id=?", new Object[] {frameId});
	}
}
