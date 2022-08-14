package org.fuchss.objectcasket.sqlconnector.impl.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.common.Util;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlColumnSignatureImpl;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlInteger;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlObj;
import org.fuchss.objectcasket.sqlconnector.impl.prepstat.PreCompiledCreate;
import org.fuchss.objectcasket.sqlconnector.port.PreCompiledStatement;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;
import org.fuchss.objectcasket.sqlconnector.port.TableAssignment;

abstract class SimpleDatabase extends AcidDatabase {

	protected boolean canCreateTable;
	protected ConfigurationImpl configImpl;

	protected SimpleDatabase(Connection connection, boolean canCreate, SqlObjectFatoryImpl objectFactory, SqlCmd sqlCmd) {
		super(connection, objectFactory, sqlCmd);
		this.canCreateTable = canCreate;
	}

	@Override
	public TableAssignment createTable(String tableName, Map<String, SqlColumnSignature> columns) throws CasketException {
		this.checkCreateTable(tableName, columns);
		this.preventTransaction();
		try {
			Map<String, SqlColumnSignatureImpl> columnsImpl = new HashMap<>();
			columns.forEach((name, prototype) -> columnsImpl.put(name, (SqlColumnSignatureImpl) prototype));
			String statStr = this.sqlCmd.createTable(tableName, columnsImpl);
			try (PreparedStatement prepStmt = this.connection.prepareStatement(statStr)) {
				prepStmt.executeUpdate();
			}
			this.connection.commit();
			TableAssignmentImpl tabImpl = new TableAssignmentImpl(tableName, columnsImpl);
			this.assignedTables.put(tabImpl, tabImpl);
			return tabImpl;
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		} finally {
			this.permitTransaction();
		}
	}

	@Override
	public TableAssignment createView(String tableName, Map<String, SqlColumnSignature> columns) throws CasketException {
		this.checkCreateView(tableName, columns);
		this.preventTransaction();

		Map<String, SqlColumnSignatureImpl> columnsImpl = new HashMap<>();
		columns.forEach((name, prototype) -> columnsImpl.put(name, (SqlColumnSignatureImpl) prototype));

		try {
			this.checkView(tableName, columnsImpl);
			this.connection.commit();
			TableAssignmentImpl tabImpl = new TableAssignmentImpl(tableName, columnsImpl);
			this.assignedTables.put(tabImpl, tabImpl);
			return tabImpl;
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		} finally {
			this.permitTransaction();
		}
	}

	@Override
	public TableAssignment adjustTable(String tableName, Map<String, SqlColumnSignature> columns) throws CasketException {
		this.checkCreateView(tableName, columns);
		this.preventTransaction();

		Map<String, SqlColumnSignatureImpl> columnsImpl = new HashMap<>();
		columns.forEach((name, prototype) -> columnsImpl.put(name, (SqlColumnSignatureImpl) prototype));

		try {
			if (this.isAssigned(tableName))
				throw CasketError.TABLE_IN_USE.build();

			Set<String> missingColumns = new HashSet<>();
			Set<String> obsoleteColumns = new HashSet<>();

			this.checkAssignment(tableName, columnsImpl, missingColumns, obsoleteColumns);

			this.dropAndAdd(tableName, columnsImpl, missingColumns, obsoleteColumns);
			TableAssignmentImpl tabImpl = new TableAssignmentImpl(tableName, columnsImpl);
			this.assignedTables.put(tabImpl, tabImpl);
			this.connection.commit();
			return tabImpl;
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		} finally {
			this.permitTransaction();
		}
	}

	private void checkAssignment(String tableName, Map<String, SqlColumnSignatureImpl> columnsImpl, Set<String> missingColumns, Set<String> obsoleteColumns) throws SQLException, CasketException {
		try {
			DatabaseMetaData metaData = this.connection.getMetaData();
			this.tableExists(metaData, tableName);
			String pkName = this.pkColumnName(metaData, tableName);
			int columnsLeft = this.checkAdjustment(metaData, tableName, pkName, columnsImpl, missingColumns, obsoleteColumns);
			if (columnsLeft < 2)
				throw CasketError.NOT_ENOUGH_COLUMNS.build();
		} catch (CasketException e) {
			this.connection.commit();
			throw e;
		}
	}

	private void dropAndAdd(String tableName, Map<String, SqlColumnSignatureImpl> columnsImpl, Set<String> missingColumns, Set<String> obsoleteColumns) throws CasketException {
		try {
			for (String column : obsoleteColumns) {
				try (Statement stmt = this.connection.createStatement()) {
					stmt.execute(this.sqlCmd.alterTableDropColumn(tableName, column));
				}
			}
			for (String column : missingColumns) {
				try (Statement stmt = this.connection.createStatement()) {
					stmt.execute(this.sqlCmd.alterTableAddColumn(tableName, column, columnsImpl.get(column)));
				}
			}
			this.connection.commit();
		} catch (SQLException e) {
			try {
				this.connection.rollback();
				throw CasketException.build(e);
			} catch (SQLException exc) {
				throw CasketException.build(exc);
			}
		}
	}

	@Override
	public void dropTable(String tableName) throws CasketException {
		if (!this.canCreateTable)
			throw CasketError.CREATION_OR_MODIFICATION_FAILED.build();
		this.preventTransaction();
		try {
			if (this.isAssigned(tableName))
				throw CasketError.TABLE_IN_USE.build();

			try (Statement stmt = this.connection.createStatement()) {
				stmt.execute(this.sqlCmd.dropTable(tableName));
			}
			this.connection.commit();
		} catch (SQLException e) {
			try {
				this.connection.rollback();
				throw CasketException.build(e);
			} catch (SQLException exc) {
				throw CasketException.build(exc);
			}
		} finally {
			this.permitTransaction();
		}
	}

	@Override
	public boolean tableExists(String tableName) throws CasketException {
		Util.objectsNotNull(tableName);
		this.preventTransaction();
		DatabaseMetaData metaData;
		try {
			metaData = this.connection.getMetaData();
			try (ResultSet resultSet = metaData.getTables(null, null, tableName, null)) {
				return resultSet.next();
			}
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		} finally {
			this.permitTransaction();
		}
	}

	private void checkCreateTable(String tableName, Map<String, SqlColumnSignature> columns) throws CasketException {
		if (!this.canCreateTable)
			throw CasketError.CREATION_OR_MODIFICATION_FAILED.build();
		if (columns.size() < 2)
			throw CasketError.NOT_ENOUGH_COLUMNS.build();
		this.checkCreateView(tableName, columns);
	}

	private void checkCreateView(String tableName, Map<String, SqlColumnSignature> columns) throws CasketException {
		Util.objectsNotNull(tableName, columns);
		if (!Util.isWellformed(tableName))
			throw CasketError.INVALID_NAME.build();
		this.checkColumns(columns.keySet());
		this.checkPK(columns.values());
	}

	@Override
	@SuppressWarnings("java:S2095")
	public PreCompiledStatement mkNewRowStmt(TableAssignment tabAssignment) throws CasketException {
		TableAssignmentImpl tabAssignImpl = this.assignedTables.get(tabAssignment);
		Util.objectsNotNull(tabAssignImpl);
		String tableName = tabAssignImpl.tableName();

		List<String> columnNames = new ArrayList<>(tabAssignImpl.getColSigMap().keySet());
		String pkName = tabAssignImpl.pkName();
		boolean isAutoIncrement = tabAssignImpl.getColSigMap().get(pkName).isAutoIncrementedPrimaryKey();
		try {
			PreparedStatement prepStmt = null;
			if (isAutoIncrement)
				prepStmt = this.connection.prepareStatement(this.sqlCmd.insertValues(tableName, columnNames, tabAssignImpl.pkName(), isAutoIncrement), Statement.RETURN_GENERATED_KEYS);
			else
				prepStmt = this.connection.prepareStatement(this.sqlCmd.insertValues(tableName, columnNames, tabAssignImpl.pkName(), isAutoIncrement));
			return new PreCompiledCreate(prepStmt, tableName, columnNames, tabAssignImpl.getColSigMap(), isAutoIncrement);
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		}
	}

	@Override
	public Map<String, SqlObject> newRow(PreCompiledStatement preStat, Map<String, SqlObject> values, Object obj) throws CasketException {
		Util.objectsNotNull(preStat, values);
		this.checkColumns(values.keySet());
		PreCompiledCreate preStatImpl = (PreCompiledCreate) preStat;

		this.checkVoucherAndAcquier(obj);

		try {
			String tableName = preStatImpl.tableName();
			String pkName = preStatImpl.pkName();
			preStatImpl.setValuesAndExecute(values);
			Map<String, SqlObject> result = new HashMap<>();
			result.put(pkName, values.get(pkName));
			this.transaction.add2Created(tableName, (SqlObj) (result.get(pkName)));
			return result;
		} finally {
			this.proceedTransaction();
		}
	}

	public Map<String, SqlObject> newRowOld(PreCompiledStatement preStat, Map<String, SqlObject> values, Object obj) throws CasketException {
		Util.objectsNotNull(preStat, values);
		this.checkColumns(values.keySet());
		PreCompiledCreate preStatImpl = (PreCompiledCreate) preStat;

		this.checkVoucherAndAcquier(obj);

		try {
			String tableName = preStatImpl.tableName();
			String pkName = preStatImpl.pkName();
			preStatImpl.setValuesAndExecute(values);
			Map<String, SqlObject> result = new HashMap<>();
			if (!preStatImpl.pkIsAutoincremented()) {
				result.put(pkName, values.get(pkName));
				this.transaction.add2Created(tableName, (SqlObj) (result.get(pkName)));
				return result;
			}
			result.put(pkName, this.autoIncrement());
			this.transaction.add2Created(tableName, (SqlObj) (result.get(pkName)));
			return result;
		} finally {
			this.proceedTransaction();
		}
	}

	protected void checkColumns(Set<String> columns) throws CasketException {
		if (columns.isEmpty())
			return;
		for (String columnName : columns) {
			if (!Util.isWellformed(columnName))
				throw CasketError.INVALID_NAME.build();
		}
	}

	protected void checkPK(Collection<SqlColumnSignature> prototyps) throws CasketException {
		int pk = 0;
		if (prototyps.contains(null))
			throw CasketError.INVALID_COLUMN_SIGNATURES.build();

		for (SqlColumnSignature proto : prototyps) {
			pk += (proto.isPrimaryKey() ? 1 : 0);
		}
		if (pk != 1)
			throw CasketError.MISSING_PK.build();
	}

	private SqlObject autoIncrement() throws CasketException {

		try (ResultSet generatedKeys = this.connection.createStatement().executeQuery("SELECT last_insert_rowid()")) {
			return SqlInteger.mkSqlObjectFromJava(generatedKeys.next() ? Long.valueOf(generatedKeys.getInt(1)) : 0L);
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		}
	}

	private int checkAdjustment(DatabaseMetaData metaData, String tableName, String pkName, Map<String, SqlColumnSignatureImpl> columnsImpl, Set<String> missingColumns, Set<String> obsoleteColumns) throws SQLException, CasketException {
		try (ResultSet resultSet = metaData.getColumns(null, null, tableName, null)) {
			Set<String> tableColumns = new HashSet<>();
			while (resultSet.next()) {
				SqlValidator validator = this.sqlCmd.getValidator(resultSet, pkName);
				String columnName = validator.getColumnName();
				tableColumns.add(columnName);
				SqlColumnSignatureImpl colImpl = columnsImpl.get(columnName);
				if (!validator.validate(columnsImpl.get(columnName)))
					return -1;
				if (colImpl == null)
					obsoleteColumns.add(columnName);

			}
			for (String prototypeColumn : columnsImpl.keySet()) {
				if (!tableColumns.contains(prototypeColumn)) {
					missingColumns.add(prototypeColumn);
				}
			}
			return (tableColumns.size() - obsoleteColumns.size()) + missingColumns.size();
		}
	}

	private boolean isAssigned(String tableName) {
		for (TableAssignment assignment : this.assignedTables.keySet()) {
			if (assignment.tableName().equals(tableName))
				return true;
		}
		return false;

	}

	private void checkView(String tableName, Map<String, SqlColumnSignatureImpl> columnsImpl) throws SQLException, CasketException {
		DatabaseMetaData metaData;
		try {
			metaData = this.connection.getMetaData();
			this.tableExists(metaData, tableName);
			String pkName = this.pkColumnName(metaData, tableName);
			this.checkPrototypes(metaData, tableName, pkName, columnsImpl);
		} catch (CasketException e) {
			this.connection.commit();
			throw e;
		}
	}

	private void tableExists(DatabaseMetaData metaData, String tableName) throws SQLException, CasketException {
		try (ResultSet resultSet = metaData.getTables(null, null, tableName, null)) {
			if (resultSet.next())
				return;
		}
		throw CasketError.MISSING_TABLE.build();
	}

	private String pkColumnName(DatabaseMetaData metaData, String tableName) throws CasketException, SQLException {
		List<String> pk = new ArrayList<>();
		try (ResultSet resultSet = metaData.getPrimaryKeys(null, null, tableName)) {
			while (resultSet.next())
				pk.add(resultSet.getString(4));
		}
		if (pk.size() != 1)
			throw CasketError.MISSING_PK.build();
		return pk.get(0);
	}

	private void checkPrototypes(DatabaseMetaData metaData, String tableName, String pkName, Map<String, SqlColumnSignatureImpl> columnsImpl) throws SQLException, CasketException {
		try (ResultSet resultSet = metaData.getColumns(null, null, tableName, null)) {
			Set<String> tableColumns = new HashSet<>();
			while (resultSet.next()) {
				SqlValidator validator = this.sqlCmd.getValidator(resultSet, pkName);
				String columnName = validator.getColumnName();
				tableColumns.add(columnName);
				if (!validator.validate(columnsImpl.get(columnName)))
					throw CasketError.WRONG_COLUMN_DEFINITION.build();
			}
			for (String prototypeColumn : columnsImpl.keySet()) {
				if (!tableColumns.contains(prototypeColumn)) {
					throw CasketError.WRONG_COLUMN_DEFINITION.build();
				}
			}
		}

	}

}
