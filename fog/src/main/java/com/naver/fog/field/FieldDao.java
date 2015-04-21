package com.naver.fog.field;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import com.naver.fog.ui.JacksonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.naver.fog.AbstractResourceDao;

@Component
public class FieldDao extends AbstractResourceDao<Field> {
	public FieldDao() {
		super("nm_field");
	}

	@Override
	protected Field fromResultSet(ResultSet rs) throws SQLException {
		FieldType fieldType = FieldType.fromId(rs.getInt("field_type"));
		Field field = fieldType == FieldType.FRAME ? new FramedField() : new Field();

		fillResource(rs, field);
		field.setFieldType(fieldType);
		setOptionJson(field, rs.getString("options"));
		field.setDefaultTemplateId(rs.getLong("default_template_id"));

		return field;
	}

	@Override
	protected void insertChecked(Field field) {
		long fieldId = insert("INSERT INTO nm_field SET name=?, description=?, field_type=?, options=?, default_template_id=?, creator_id=?, create_ymdt=NOW()",
			new int[] {Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.BIGINT, Types.BIGINT},
			new Object[] {field.getName(), field.getDescription(), field.getFieldType().getId(), getOptionJson(field), field.getDefaultTemplateId(), field.getCreatorId()});

		field.setId(fieldId);
	}

	@Override
	protected int updateChecked(Field field) {
		return jdbcTemplate.update("UPDATE nm_field SET name=?, description=?, field_type=?, options=?, default_template_id=? WHERE id=?"
			, field.getName(), field.getDescription(), field.getFieldType().getId(), getOptionJson(field), field.getDefaultTemplateId(), field.getId());
	}

	private void setOptionJson(Field field, String json) {
		if (StringUtils.isNotBlank(json)) {
			field.setOptions(JacksonUtil.toObject(json, Map.class));
		}
	}

	private String getOptionJson(Field field) {
		Map<String, Object> options = field.getOptions();
		if (MapUtils.isEmpty(options)) {
			return null;
		} else {
			return JacksonUtil.toJson(options);
		}
	}
}
