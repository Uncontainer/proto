package com.naver.fog.frame.field;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.naver.fog.AbstractFogDao;

@Component
public class FrameFieldDao extends AbstractFogDao {
	public void insert(FrameField frameField) {
		jdbcTemplate.update("INSERT INTO nm_frame_field SET frame_id=?, field_id=?, creator_id=?, create_ymdt=NOW()"
			, new Object[] {frameField.getFrameId(), frameField.getFieldId(), frameField.getCreatorId()});
	}

	public FrameField select(long frameId, long fieldId) {
		return jdbcTemplate.query("SELECT * FROM nm_frame_field WHERE frame_id=? AND field_id=?", new Object[] {frameId, fieldId}, new ResultSetExtractor<FrameField>() {
			public FrameField extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (!rs.next()) {
					return null;
				}

				return fromResultSet(rs);
			}
		});
	}

	public List<FrameField> selectByFrame(long frameId) {
		return jdbcTemplate.query("SELECT * FROM nm_frame_field WHERE frame_id=?", new Object[] {frameId}, new RowMapper<FrameField>() {
			public FrameField mapRow(ResultSet rs, int rowNum) throws SQLException {
				return fromResultSet(rs);
			}
		});
	}

	private static FrameField fromResultSet(ResultSet rs) throws SQLException {
		FrameField frameField = new FrameField();
		frameField.setFrameId(rs.getLong("frame_id"));
		frameField.setFieldId(rs.getLong("field_id"));
		frameField.setCreatorId(rs.getLong("creator_id"));
		frameField.setCreatedDate(rs.getTimestamp("create_ymdt"));

		return frameField;
	}

	public List<Long> selectIdsByFrame(long frameId) {
		return jdbcTemplate.query("SELECT field_id FROM nm_frame_field WHERE frame_id=?", new Object[] {frameId}, SINGLE_LONG_VALUE_ROW_MAPPER);
	}

	public int delete(long frameId, long fieldId) {
		return jdbcTemplate.update("DELETE FROM nm_frame_field WHERE frame_id=? AND field_id=?", new Object[] {frameId, fieldId});
	}

	public int deleteByFrame(long frameId) {
		return jdbcTemplate.update("DELETE FROM nm_frame_field WHERE frame_id=?", new Object[] {frameId});
	}

	public List<Long> selectFrameIdsByFieldId(long fieldId, int count) {
		return jdbcTemplate.query("SELECT frame_id FROM nm_frame_field WHERE field_id=? LIMIT ?"
			, new Object[] {fieldId, count}
			, SINGLE_LONG_VALUE_ROW_MAPPER);
	}
}
