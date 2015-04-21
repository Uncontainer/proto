package yeon.query;

import java.util.List;

/*
 * FROM : SELECT resource_id FROM class WHERE name=?
 * WHERE : (SELECT resource_id FROM triple WHERE resoruce_id IN ( ${FROM} ) AND property_id=? AND object_value=?)+
 * SELECT : SELECT resource_id, property_id, object_value FROM triple WHERE resource_id IN ( ${WHERE} ) AND property_id IN (?)
 * 
 */
public class Select implements Query {
	List<ResourcePointer> fields;
	ResourcePointer tableFilter;
	Condition whereClause;

	public Select(List<ResourcePointer> fields, ResourcePointer tableFilter, Condition whereClause) {
		super();
		this.fields = fields;
		this.tableFilter = tableFilter;
		this.whereClause = whereClause;
	}

	public String toSql() {
		StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append("SELECT subject_resource_id, property_id, object_value");
		sqlBuilder.append(" FROM triple");
		sqlBuilder.append(" WHERE property_id IN (");
		sqlBuilder.append("'").append(fields.get(0).getResourceId()).append("'");
		for (int i = 1; i < fields.size(); i++) {
			sqlBuilder.append(",'").append(fields.get(0).getResourceId()).append("'");
		}
		sqlBuilder.append(")");
		if (whereClause != null) {
			sqlBuilder.append(" AND subject_resource_id IN (");
			sqlBuilder.append("SELECT subject_resource_id FROM triple WHERE ");
			whereClause.buildSql(sqlBuilder);
			sqlBuilder.append(")");
		}

		if (tableFilter != null) {
			String domainClassId = tableFilter.getResourceId();

			sqlBuilder.append(" AND subject_resource_id IN (");
			sqlBuilder.append("SELECT id FROM resource WHERE type_class_id='").append(domainClassId).append("'");
			sqlBuilder.append(")");
		}

		sqlBuilder.append(" ORDER BY subject_resource_id ASC");

		return sqlBuilder.toString();
	}

	public List<ResourcePointer> getFields() {
		return fields;
	}

	public ResourcePointer getTableFilter() {
		return tableFilter;
	}

	public Condition getWhereClause() {
		return whereClause;
	}
}
