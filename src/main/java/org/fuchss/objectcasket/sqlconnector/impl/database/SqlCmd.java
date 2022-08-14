package org.fuchss.objectcasket.sqlconnector.impl.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlColumnSignatureImpl;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature.Flag;
import org.fuchss.objectcasket.sqlconnector.port.SqlDialect;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;

class SqlCmd {

	private SqlDialect dialect;

	SqlCmd(SqlDialect dialect) {
		this.dialect = dialect;
	}

	String createTable(String table, Map<String, SqlColumnSignatureImpl> cols) {
		List<ColumnDefinition> colDefs = cols.entrySet().stream().map(entry -> this.mkColumnDefinition(entry.getKey(), entry.getValue())).toList();
		StringBuilder columnInfo = new StringBuilder();
		for (ColumnDefinition colDef : colDefs)
			columnInfo.append(columnInfo.isEmpty() ? "" : ",").append(this.columnDefinition(colDef));
		return String.format("CREATE TABLE \"%s\" (%s)", table, columnInfo.toString());
	}

	private String columnDefinition(ColumnDefinition colDef) {
		StringBuilder columnDefinition = new StringBuilder();
		columnDefinition.append("\"").append(colDef.columnName()).append("\"");
		columnDefinition.append(" ").append(colDef.type());
		if (colDef.primaryKey())
			columnDefinition.append(" ").append(this.dialect.flagString(Flag.PRIMARY_KEY));
		if (colDef.autoIncrement())
			columnDefinition.append(" ").append(this.dialect.flagString(Flag.AUTOINCREMENT));
		if (colDef.notNull())
			columnDefinition.append(" ").append(this.dialect.flagString(Flag.NOT_NULL));
		if (colDef.defaultValue() != null) {
			if (colDef.type().equals(this.dialect.storageClassString(StorageClass.TEXT)) || colDef.type().equals(this.dialect.storageClassString(StorageClass.BLOB)))
				columnDefinition.append(String.format(" DEFAULT '%s'", colDef.defaultValue()));
			else
				columnDefinition.append(String.format(" DEFAULT %s", colDef.defaultValue()));
		}
		return columnDefinition.toString();
	}

	String dropTable(String table) {
		return String.format("DROP TABLE \"%s\"", table);
	}

	String alterTableDropColumn(String table, String column) {
		return String.format("ALTER TABLE \"%s\" DROP COLUMN \"%s\"", table, column);
	}

	String alterTableAddColumn(String table, String column, SqlColumnSignatureImpl colSig) {
		String columnDefinition = this.columnDefinition(this.mkColumnDefinition(column, colSig));
		return String.format("ALTER TABLE \"%s\" ADD COLUMN %s", table, columnDefinition);

	}

	String insertValues(String table, List<String> columnNames, String pkName, boolean isAutoIncrement) {
		List<String> columns = new ArrayList<>(columnNames);
		if (isAutoIncrement)
			columns.remove(pkName);
		String attr = this.columnNames(columns);
		String wildCards = this.wildCards(columns);
		return attr.isEmpty() ? String.format("INSERT INTO \"%s\" DEFAULT VALUES", table) : String.format("INSERT INTO \"%s\" (%s) VALUES (%s)", table, attr, wildCards);
	}

	private String wildCards(List<String> columnNames) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < columnNames.size(); i++) {
			str.append(str.isEmpty() ? "" : ", ").append("?");
		}
		return str.toString();
	}

	String select(String table, Set<String> columns, List<SqlArg> args, SqlArg.OP op) {
		String operand = this.dialect.operatorString(op);
		List<ColumnAndComperator> colAndComp = args.stream().map(arg -> new ColumnAndComperator(arg.columnName(), this.dialect.cmpString(arg.cmp()))).toList();
		return this.selectRowsStmt(table, columns, colAndComp, operand);
	}

	private String selectRowsStmt(String table, Set<String> columns, List<ColumnAndComperator> args, String operand) {
		StringBuilder whereClause = new StringBuilder();
		for (ColumnAndComperator arg : args) {
			String argString = String.format("\"%s\" %s ?", arg.columnName(), arg.comperator());
			whereClause.append(whereClause.isEmpty() ? "WHERE " : operand).append(argString);
		}
		if ((columns == null) || columns.isEmpty()) {
			return String.format("SELECT * FROM \"%s\" %s", table, whereClause.toString());
		}
		String columnNames = this.columnNames(columns);
		return String.format("SELECT %s FROM \"%s\" %s", columnNames, table, whereClause.toString());
	}

	private String columnNames(Collection<String> columns) {
		StringBuilder identifierList = new StringBuilder();
		for (String column : columns) {
			identifierList.append(identifierList.isEmpty() ? "\"" : ", \"").append(column).append("\"");
		}
		return identifierList.toString();
	}

	String update(String table, List<String> columnNames, String pkColumnName) {
		StringBuilder attr = new StringBuilder();
		for (String column : columnNames) {
			if (pkColumnName.equals(column))
				continue;
			attr.append(attr.isEmpty() ? String.format("\"%s\" = ?", column) : String.format(", \"%s\" = ?", column));
		}
		return String.format("UPDATE \"%s\" SET %s WHERE \"%s\" = ?", table, attr, pkColumnName);
	}

	String delete(String table, List<SqlArg> args, SqlArg.OP op) {
		String operand = this.dialect.operatorString(op);
		List<ColumnAndComperator> colAndComp = args.stream().map(arg -> new ColumnAndComperator(arg.columnName(), this.dialect.cmpString(arg.cmp()))).toList();
		StringBuilder whereClause = new StringBuilder();
		for (ColumnAndComperator cAc : colAndComp) {
			String argString = String.format("\"%s\" %s ?", cAc.columnName(), cAc.comperator());
			whereClause.append(whereClause.isEmpty() ? "WHERE " : operand).append(argString);
		}
		return String.format("DELETE FROM \"%s\" %s", table, whereClause.toString());
	}

	private ColumnDefinition mkColumnDefinition(String column, SqlColumnSignatureImpl colSig) {
		String type = this.dialect.storageClassString(colSig.getType());
		boolean primaryKey = colSig.isPrimaryKey();
		boolean autoIncrement = colSig.isAutoIncrementedPrimaryKey();
		boolean notNull = colSig.notNull();
		String defaultValue = (colSig.getDefaultValue().isNull() ? null : colSig.getDefaultValue().toString());
		return new ColumnDefinition(column, type, primaryKey, autoIncrement, notNull, defaultValue);
	}

	final record ColumnAndComperator(String columnName, String comperator) {
	}

	final record ColumnDefinition(String columnName, String type, boolean primaryKey, boolean autoIncrement, boolean notNull, String defaultValue) {
	}

	SqlValidator getValidator(ResultSet resultSet, String pkName) throws SQLException {
		return new SqlValidator(resultSet, pkName, this.dialect);
	}

}
