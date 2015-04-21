package yeon.query;

public class AndCondition extends CompositeCondition {
	@Override
	public void buildSql(StringBuilder builder) {
		builder.append("(");
		conditions.get(0).buildSql(builder);
		for (int i = 1; i < conditions.size(); i++) {
			builder.append(") AND (");
			conditions.get(i).buildSql(builder);
		}
		builder.append(")");
	}
}
