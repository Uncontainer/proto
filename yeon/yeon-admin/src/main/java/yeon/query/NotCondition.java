package yeon.query;

public class NotCondition implements Condition {
	Condition condition;

	public NotCondition(Condition condition) {
		super();
		this.condition = condition;
	}

	@Override
	public void buildSql(StringBuilder builder) {
		builder.append(" NOT (");
		condition.buildSql(builder);
		builder.append(")");
	}
}
