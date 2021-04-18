package org.fuchss.sqlconnector.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.fuchss.sqlconnector.impl.object.SqlPrototypeImpl;
import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlArg;
import org.fuchss.sqlconnector.port.SqlDatabase;
import org.fuchss.sqlconnector.port.SqlObject;
import org.fuchss.sqlconnector.port.SqlPrototype;

public class SqlDatabaseImpl implements SqlDatabase {

	private Connection connection;
	private Semaphore beginTransaction = new Semaphore(1);
	private Semaphore endTransaction = new Semaphore(0);
	private Map<ResultSet, Statement> statementMap = new HashMap<>();

	public SqlDatabaseImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void beginTransaction() throws ConnectorException {
		SQLException exc = null;
		this.beginTransaction.acquireUninterruptibly();
		try {
			this.connection.setAutoCommit(false);
		} catch (SQLException e) {
			exc = e;
			this.beginTransaction.release();
		}
		if (exc != null) {
			ConnectorException.build(exc);
		}
		this.endTransaction.release();
	}

	@Override
	public void endTransaction() throws ConnectorException {
		if (!this.endTransaction.tryAcquire()) {
			DatabaseException.Error.NoTransaction.build();
		}
		try {
			this.connection.commit();
			this.beginTransaction.release();
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

	@Override
	public void rollback() throws ConnectorException {
		this.endTransaction.tryAcquire();
		this.beginTransaction.tryAcquire();
		try {
			this.connection.rollback();
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		} finally {
			this.beginTransaction.release();
		}

	}

	@Override
	public int newRow(String table, Map<String, SqlObject> values) throws ConnectorException {

		List<String> columnNames = new ArrayList<>();
		List<SqlObject> sqlValues = new ArrayList<>();
		values.forEach((k, v) -> SqlDatabaseImpl.add(columnNames, sqlValues, k, v));
		return this.create(table, columnNames, sqlValues);

	}

	private static void add(List<String> cols, List<SqlObject> vals, String colName, SqlObject sqlVal) {
		cols.add(colName);
		vals.add(sqlVal);
	}

	private int create(String table, List<String> columns, List<SqlObject> values) throws ConnectorException {
		try {
			int idx = 1;
			PreparedStatement prepStmt = this.connection.prepareStatement(SqlCmd.insertValues(table, columns));
			for (SqlObject val : values) {
				val.prepareStatement(idx++, prepStmt);
			}
			prepStmt.executeUpdate();
			ResultSet generatedKeys = this.connection.createStatement().executeQuery("SELECT last_insert_rowid()");
			return (generatedKeys.next()) ? generatedKeys.getInt(1) : 0;
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
		return 0;
	}

	@Override
	public void updateRow(String table, List<SqlArg> args, String pkColumnName, SqlObject pk) throws ConnectorException {
		try {
			PreparedStatement prepStmt = this.connection.prepareStatement(SqlCmd.update(table, args, pkColumnName));
			int idx = 1;
			for (SqlArg arg : args) {
				arg.getSqlObj().prepareStatement(idx++, prepStmt);
			}
			pk.prepareStatement(idx, prepStmt);
			prepStmt.executeUpdate();
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

	@Override
	public ResultSet query(SqlObject pk, String table, String column) throws ConnectorException {
		try {
			ResultSet resultSet = null;
			PreparedStatement stmt = this.connection.prepareStatement(SqlCmd.select(table, column));
			pk.prepareStatement(1, stmt);
			resultSet = stmt.executeQuery();
			return resultSet;
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
		return null;
	}

	@Override
	public void deleteRow(String table, String pkColumnName, SqlObject pk) throws ConnectorException {
		try {
			PreparedStatement stmt = this.connection.prepareStatement(SqlCmd.delete(table, pkColumnName));
			pk.prepareStatement(1, stmt);
			stmt.executeUpdate();
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

	/*
	 * public ResultSet query(SqlObject pk, String table, String column) throws
	 * ConnectorException { try { ResultSet resultSet = null; Statement stmt =
	 * this.connection.createStatement(); resultSet =
	 * stmt.executeQuery(SqlCmd.select(table, column, pk)); return resultSet; }
	 * catch (SQLException exc) { ConnectorException.build(exc); } return null; }
	 *
	 * @Override public void deleteRow(String table, String pkColumnName, SqlObject
	 * pk) throws ConnectorException { try { Statement stmt =
	 * this.connection.createStatement(); stmt.execute(SqlCmd.delete(table,
	 * pkColumnName, pk)); } catch (SQLException exc) {
	 * ConnectorException.build(exc); } }
	 */

	@Override
	public void deleteRows(String tableName, List<SqlArg> args) throws ConnectorException {
		try {
			PreparedStatement prepStmt = this.connection.prepareStatement(SqlCmd.delete(tableName, args));
			int idx = 1;
			for (SqlArg arg : args) {
				arg.getSqlObj().prepareStatement(idx++, prepStmt);
			}
			prepStmt.executeUpdate();
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}

	}

	@Override
	public ResultSet query(String tableName, List<SqlArg> args) throws ConnectorException {
		try {
			if (args == null) {
				return this.querydb(tableName);
			}
			return this.querydb(tableName, args);
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
		return null;
	}

	private ResultSet querydb(String tableName, List<SqlArg> args) throws SQLException, ConnectorException {
		PreparedStatement prepStmt = this.connection.prepareStatement(SqlCmd.select(tableName, args));
		int idx = 1;
		for (SqlArg arg : args) {
			arg.getSqlObj().prepareStatement(idx++, prepStmt);
		}
		return prepStmt.executeQuery();
	}

	private ResultSet querydb(String tableName) throws SQLException {
		ResultSet resultSet = null;
		Statement stmt = this.connection.createStatement();
		resultSet = stmt.executeQuery(SqlCmd.selectAll(tableName));
		return resultSet;
	}

	@Override
	public List<String> allTables() throws ConnectorException {
		try {
			List<String> tableNames = new ArrayList<>();
			DatabaseMetaData md = this.connection.getMetaData();
			ResultSet resultSet = md.getTables(null, null, "%", null);
			while (resultSet.next()) {
				String name = resultSet.getString(3);
				tableNames.add(name);
			}

			return tableNames;

		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
		return null;
	}

	@Override
	public void createTable(String tableName, Map<String, SqlPrototype> columns) throws ConnectorException {
		try {
			Map<String, SqlPrototypeImpl> columnsImpl = new HashMap<>();
			columns.forEach((name, prototype) -> columnsImpl.put(name, (SqlPrototypeImpl) prototype));
			List<SqlObject> defaultValues = new ArrayList<>();
			String statStr = SqlCmd.createTable(tableName, columnsImpl, defaultValues);
			if (defaultValues.isEmpty()) {
				Statement stmt = this.connection.createStatement();
				stmt.executeUpdate(statStr);
				stmt.close();
			} else {
				PreparedStatement prepStmt = this.connection.prepareStatement(statStr);
				int idx = 1;

				for (SqlObject val : defaultValues) {
					val.prepareStatement(idx++, prepStmt);
				}
				prepStmt.executeUpdate();
			}
			this.connection.commit();
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

	@Override
	public void assignTable(String tableName, Map<String, SqlPrototype> columns) throws ConnectorException {
		try {
			Map<String, SqlPrototypeImpl> columnsImpl = new HashMap<>();
			columns.forEach((name, prototype) -> columnsImpl.put(name, (SqlPrototypeImpl) prototype));
			Statement stmt = this.connection.createStatement();
			String statStr = SqlCmd.assignTable(tableName);
			stmt.execute(statStr);
			ResultSet resultSet = stmt.getResultSet();
			this.checkPrototypes(resultSet, columnsImpl, tableName);
			stmt.close();
			this.connection.commit();
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

	private void checkPrototypes(ResultSet resultSet, Map<String, SqlPrototypeImpl> columnsImpl, String tableName) throws SQLException, ConnectorException {
		Set<String> tableColumns = new HashSet<>();
		while (resultSet.next()) {
			String columnName = resultSet.getString(2);
			tableColumns.add(columnName);
			this.checkPrototype(columnsImpl.get(columnName), resultSet);
		}
		for (String prototypeColumn : columnsImpl.keySet()) {
			if (!tableColumns.contains(prototypeColumn)) {
				DatabaseException.Error.NoColumn.build(prototypeColumn, tableName);
			}
		}
	}

	private void checkPrototype(SqlPrototypeImpl prototype, ResultSet resultSet) throws SQLException, ConnectorException {
		if (prototype == null) {
			return;
		}
		SqlPrototypeValidatorImpl validator = new SqlPrototypeValidatorImpl(resultSet);
		prototype.validate(validator);
		if (!validator.isValid()) {
			DatabaseException.Error.InvalidPrototype.build(resultSet.getString(2));
		}
	}

	@Override
	public void closeStatement(ResultSet rs) throws ConnectorException {
		try {
			Statement stmt = this.statementMap.remove(rs);
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

	@Override
	public boolean wellformedTableName(String tableName) {
		if (tableName == null) {
			return false;
		}
		String acceptedName = tableName.replaceAll("[^\\x23\\x5F\\x30-\\x39\\x41-\\x5a\\x61-\\x7a]", ""); // # _ 0..9 A..Z a..z
		return tableName.equals(acceptedName);
	}

	@Override
	public char[] acceptedCharactersForTableName() {
		List<Character> acceptedChars = new ArrayList<>();
		acceptedChars.add((char) 0x23);
		acceptedChars.add((char) 0x5F);
		for (int i = 0x30; i <= 0x39; i++)
			acceptedChars.add((char) i);
		for (int i = 0x41; i <= 0x5a; i++)
			acceptedChars.add((char) i);
		for (int i = 0x61; i <= 0x7a; i++)
			acceptedChars.add((char) i);
		char[] res = new char[acceptedChars.size()];
		int count = 0;
		for (Character x : acceptedChars)
			res[count++] = x;
		return res;
	}

	@Override
	public ResultSet getMaxPK(String tableName, String pkColumnName) throws ConnectorException {
		try {
			ResultSet resultSet = null;
			Statement stmt = this.connection.createStatement();
			resultSet = stmt.executeQuery(SqlCmd.getLastAutoIncrementedPK(tableName, pkColumnName));
			return resultSet;
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
		return null;
	}

	private static class DatabaseException extends ConnectorException {

		private static final long serialVersionUID = 1L;

		private DatabaseException(Error error, String... arg) {
			super(error.format(arg));
		}

		static enum Error {

			NoColumn("No column %s found in table %s."), //
			NoTransaction("No transaction open"), //
			InvalidPrototype("Invalid prototype for column: %s");

			private String str;

			private Error(String str) {
				this.str = str;
			}

			private String format(String... arg) {
				Object[] oargs = arg;
				return String.format(this.str, oargs);
			}

			public void build(String... arg) throws ConnectorException {
				ConnectorException.build(new DatabaseException(this, arg));
			}

		}
	}

}
