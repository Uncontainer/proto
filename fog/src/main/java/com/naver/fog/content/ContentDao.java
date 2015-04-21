package com.naver.fog.content;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.naver.fog.AbstractResourceDao;

@Component
public class ContentDao extends AbstractResourceDao<Content> {
	public ContentDao() {
		super("nm_content");
	}

	@Override
	protected Content fromResultSet(ResultSet rs) throws SQLException {
		Content content = fillResource(rs, new Content());
		content.setFrameId(rs.getLong("frame_id"));
		content.setValue(rs.getString("value"));

		return content;
	}

	@Override
	protected void insertChecked(Content content) {
		long contentId = insert("INSERT INTO nm_content SET name=?, description=?, frame_id=?, value=?, creator_id=?, create_ymdt=NOW()",
			new int[] {Types.VARCHAR, Types.VARCHAR, Types.BIGINT, Types.CLOB, Types.BIGINT},
			new Object[] {content.getName(), content.getDescription(), content.getFrameId(), content.getValue(), content.getCreatorId()});
		content.setId(contentId);
	}

	@Override
	protected int updateChecked(Content content) {
		return jdbcTemplate.update("UPDATE nm_content SET name=?, description=?, frame_id=?, value=? WHERE id=?"
			, content.getName(), content.getDescription(), content.getFrameId(), content.getValue(), content.getId());
	}

	public Page<Content> selectByFrame(long frameId, Pageable pageable) {
		List<Content> contents = jdbcTemplate.query("SELECT * FROM nm_content WHERE frame_id=? ORDER BY id desc LIMIT ?, ?"
			, new Object[] {frameId, pageable.getOffset(), pageable.getPageSize()}
			, defaultRowMapper);

		int totalCount = jdbcTemplate.queryForInt("SELECT count(*) FROM nm_content WHERE frame_id=?", new Object[] {frameId});

		return new PageImpl<Content>(contents, pageable, totalCount);
	}
}
