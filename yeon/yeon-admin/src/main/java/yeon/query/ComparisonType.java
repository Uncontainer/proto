package yeon.query;

public enum ComparisonType {
	EQ("="),
	NEQ("<>"),
	GTH(">"),
	GET(">="),
	LTH("<"),
	LET("<=");

	ComparisonType(String sqlOp) {
		this.sqlOp = sqlOp;
	}

	final String sqlOp;

	public String getSqlOp() {
		return sqlOp;
	}
}
