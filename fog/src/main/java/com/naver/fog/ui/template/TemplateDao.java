package com.naver.fog.ui.template;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.stereotype.Component;

import com.naver.fog.AbstractResourceDao;

@Component
public class TemplateDao extends AbstractResourceDao<Template> {
	public TemplateDao() {
		super("nm_template");
	}

	@Override
	protected Template fromResultSet(ResultSet rs) throws SQLException {
		Template template = fillResource(rs, new Template());
		template.setTargetResourceId(rs.getLong("resource_id"));
		TemplateValueEncoder.decode(template, rs.getString("value"));

		return template;
	}

	@Override
	protected void insertChecked(Template template) {
		long contentId = insert("INSERT INTO nm_template SET name=?, description=?, resource_id=?, value=?, creator_id=?, create_ymdt=NOW()",
			new int[] {Types.VARCHAR, Types.VARCHAR, Types.BIGINT, Types.CLOB, Types.BIGINT},
			new Object[] {template.getName(), template.getDescription(), template.getTargetResourceId(), TemplateValueEncoder.encode(template), template.getCreatorId()});
		template.setId(contentId);
	}

	@Override
	protected int updateChecked(Template template) {
		return jdbcTemplate.update("UPDATE nm_template SET name=?, description=?, resource_id=?, value=? WHERE id=?"
			, template.getName(), template.getDescription(), template.getTargetResourceId(), TemplateValueEncoder.encode(template), template.getId());
	}
}
