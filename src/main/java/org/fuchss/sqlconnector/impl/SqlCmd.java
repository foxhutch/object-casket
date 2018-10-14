package org.fuchss.sqlconnector.impl;

import java.util.List;
import java.util.Map;

import org.fuchss.sqlconnector.impl.object.SqlPrototypeImpl;
import org.fuchss.sqlconnector.port.SqlArg;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlCmd {

	static String insertValues(String table, List<String> columns) {
		String attr = "";
		String val = "";
		for (String column : columns) {
			attr += ((attr.equals("")) ? "\"" : ", \"") + column + "\"";
			val += ((val.equals("")) ? "" : ", ") + "?" + "";
		}
		if (attr.isEmpty()) {
			return "INSERT INTO \"" + table + "\" DEFAULT VALUES";
		} else {
			return "INSERT INTO \"" + table + "\" (" + attr + ") VALUES (" + val + ")";
		}
	}

	static String delete(String table, String column, SqlObject pkObj) {
		return "DELETE FROM \"" + table + "\" WHERE \"" + column + "\" = " + pkObj.toSqlString();
	}

	static String selectAll(String table) {
		return "SELECT * FROM \"" + table + "\"";
	}

	static String select(String table, String column, int pk) {
		return "SELECT * FROM \"" + table + "\" WHERE \"" + column + "\" = " + pk;
	}

	static String select(String table, String column, SqlObject pkObj) {
		return "SELECT * FROM \"" + table + "\" WHERE \"" + column + "\" = " + pkObj.toSqlString();
	}

	public static String select(String table, List<SqlArg> args) {
		String keyValuePairs = "";
		for (SqlArg column : args) {
			keyValuePairs += keyValuePairs.equals("") ? column.sqlClausePart() : column.sqlClausePart(" AND ");
		}
		return "SELECT * FROM \"" + table + "\" WHERE " + keyValuePairs;
	}

	public static String delete(String table, List<SqlArg> args) {
		String keyValuePairs = "";
		for (SqlArg column : args) {
			keyValuePairs += keyValuePairs.equals("") ? column.sqlClausePart() : column.sqlClausePart(" AND ");
		}
		return "DELETE FROM \"" + table + "\" WHERE " + keyValuePairs;
	}

	static String update(String table, List<SqlArg> args, String pkColumnName, int pk) {
		String keyValuePairs = "";
		for (SqlArg column : args) {
			keyValuePairs += (keyValuePairs.equals("")) ? column.sqlClausePart() : column.sqlClausePart(",");
		}

		return "UPDATE \"" + table + "\" SET " + keyValuePairs + " WHERE \"" + pkColumnName + "\" = " + pk;
	}

	static String update(String table, List<SqlArg> args, String pkColumnName, SqlObject pkObj) {
		String keyValuePairs = "";
		for (SqlArg column : args) {
			keyValuePairs += (keyValuePairs.equals("")) ? column.sqlClausePart() : column.sqlClausePart(",");
		}

		return "UPDATE \"" + table + "\" SET " + keyValuePairs + " WHERE \"" + pkColumnName + "\" = " + pkObj.toSqlString();
	}

	static String findTableNames(String masterTable) {
		return "SELECT name FROM " + masterTable + " WHERE type='table' ORDER BY name;";
	}

	static String createTable(String table, Map<String, SqlPrototypeImpl> cols) {
		String columnInfo = "";
		for (String column : cols.keySet()) {
			columnInfo += ((columnInfo.equals("")) ? "" : ",") + cols.get(column).toSqlSubString(column);
		}
		return "CREATE TABLE \"" + table + "\" (" + columnInfo + ")";
	}

	static String assignTable(String table) {
		return "PRAGMA table_info ('" + table + "')";
	}

	static String getLastAutoIncrementedPK(String table, String pkColumnName) {
		return "SELECT MAX(" + pkColumnName + ") FROM \"" + table + "\"";

	}

}
