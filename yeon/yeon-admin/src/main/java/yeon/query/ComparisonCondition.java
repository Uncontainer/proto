package yeon.query;

public class ComparisonCondition implements Condition {
	ResourcePointer lhs;
	ComparisonType op;
	String rhs;

	public ComparisonCondition(ComparisonType op, ResourcePointer lhs, String rhs) {
		super();
		this.op = op;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public void buildSql(StringBuilder builder) {
		builder.append("(property_id").append("=").append(lhs.getResourceId());
		builder.append(" AND value_cont ").append(op.getSqlOp()).append("'").append(op.getSqlOp()).append("')");
	}
}
