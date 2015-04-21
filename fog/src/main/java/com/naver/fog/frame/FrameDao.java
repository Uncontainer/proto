package com.naver.fog.frame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.stereotype.Component;

import com.naver.fog.AbstractResourceDao;

@Component
public class FrameDao extends AbstractResourceDao<Frame> {
	public FrameDao() {
		super("nm_frame");
	}

	@Override
	protected Frame fromResultSet(ResultSet rs) throws SQLException {
		Frame frame = fillResource(rs, new Frame());
		frame.setDefaultTemplateId(rs.getLong("default_template_id"));

		return frame;
	}

	@Override
	protected void insertChecked(Frame frame) {
		long frameId = insert("INSERT INTO nm_frame SET name=?, description=?, default_template_id=?, creator_id=?, create_ymdt=NOW()",
			new int[] {Types.VARCHAR, Types.VARCHAR, Types.BIGINT, Types.BIGINT},
			new Object[] {frame.getName(), frame.getDescription(), frame.getDefaultTemplateId(), frame.getCreatorId()});
		frame.setId(frameId);
	}

	@Override
	protected int updateChecked(Frame frame) {
		return jdbcTemplate.update("UPDATE nm_frame SET name=?, description=?, default_template_id=? WHERE id=?"
			, frame.getName(), frame.getDescription(), frame.getDefaultTemplateId(), frame.getId());
	}
}
