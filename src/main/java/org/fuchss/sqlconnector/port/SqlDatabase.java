package org.fuchss.sqlconnector.port;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public interface SqlDatabase {

	void createTable(String tableName, Map<String, SqlPrototype> columns) throws ConnectorException;

	void assignTable(String tableName, Map<String, SqlPrototype> columns) throws ConnectorException;

	int newRow(String table, Map<String, SqlObject> values) throws ConnectorException;

	void updateRow(String table, List<SqlArg> args, String pkColumnName, SqlObject pk) throws ConnectorException;

	ResultSet query(String tableName, List<SqlArg> newArgs) throws ConnectorException;

	ResultSet query(SqlObject pk, String table, String column) throws ConnectorException;

	void deleteRow(String table, String pkColumnName, SqlObject pk) throws ConnectorException;

	//

	void deleteRows(String tableName, List<SqlArg> newArgs) throws ConnectorException;

	void updateRow(String table, List<SqlArg> args, String pkColumnName, int pk) throws ConnectorException;

	void beginTransaction() throws ConnectorException;

	void endTransaction() throws ConnectorException;

	void rollback() throws ConnectorException;

	ResultSet query(int pk, String table, String column) throws ConnectorException;

	void closeStatement(ResultSet rs) throws ConnectorException;

	// Managing the Database

	List<String> allTables() throws ConnectorException;

	boolean wellformedTableName(String tableName);

	ResultSet getMaxPK(String tableName, String pkColumnName) throws ConnectorException;

}
