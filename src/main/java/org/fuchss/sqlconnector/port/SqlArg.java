package org.fuchss.sqlconnector.port;

public final class SqlArg {

	private String column;
	private CMP cmp;
	private SqlObject obj;

	private static String clause = "%s \"%s\" %s ?"; // and "column" < ?

	public SqlArg(String column, CMP cmp, SqlObject obj) {
		this.column = column;
		this.cmp = cmp;
		this.obj = obj;
	}

	public String sqlClausePart(String op) {

		return String.format(SqlArg.clause, op, this.column, this.cmp.getSymbol(), this.obj.toSqlString());

	}

	public String sqlClausePart() {

		return String.format(SqlArg.clause, "", this.column, this.cmp.getSymbol(), this.obj.toSqlString());

	}

	public SqlObject getSqlObj() {
		return this.obj;
	}

	public enum CMP {
		LESS("<"), GREATER(">"), EQUAL("="), LESSEQ("<="), GREATEREQ(">=");

		private final String symbol;

		private CMP(String symbol) {
			this.symbol = symbol;
		}

		public String getSymbol() {
			return this.symbol;
		}

	}

}
